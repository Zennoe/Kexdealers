package audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import bus.MessageBus;
import ecs.AbstractSystem;
import ecs.AudioSourceComponent;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.PhysicsComponent;
import loaders.AudioLoader;
import loaders.BlueprintLoader;

public class AudioSystem extends AbstractSystem {

	private final AudioLoader audioLoader;

	// OpenAL context
	private final int ALSOURCESCOUNT = 16;
	private long alDevice;
	private long alContext;
	private final List<Integer> alSourceIDs = new ArrayList<>();

	public AudioSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);

		// Initialize OpenAL
		alDevice = ALC10.alcOpenDevice((ByteBuffer) null);
		if (alDevice == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to open audio device");
		}

		ALCCapabilities deviceCaps = ALC.createCapabilities(alDevice);
		if (!deviceCaps.OpenALC10) {
			ALC10.alcCloseDevice(alDevice); // close previously opened alDevice
			throw new IllegalStateException("Failed to load audio capabilities");
		}

		String defaultDeviceSpecifier = ALC10.alcGetString(MemoryUtil.NULL, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		if (defaultDeviceSpecifier == null) {
			throw new AssertionError("Default Audio Device Specifier == null");
		}

		alContext = ALC10.alcCreateContext(alDevice, (IntBuffer) null);
		EXTThreadLocalContext.alcSetThreadContext(alContext);

		AL.createCapabilities(deviceCaps);

		AL10.alDistanceModel(EXTLinearDistance.AL_LINEAR_DISTANCE_CLAMPED);
		AL10.alListener3f(AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);

		// generate source channels
		for (int i = 0; i < ALSOURCESCOUNT; i++) {
			alSourceIDs.add(AL10.alGenSources());
			if (alSourceIDs.get(alSourceIDs.size() - 1) == AL10.AL_NONE) {
				throw new IllegalStateException("Failed to create OpenAL source");
			}
		}

		// Initialization done
		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			throw new IllegalStateException("OpenAL error " + err);
		}

		audioLoader = new AudioLoader(alSourceIDs);
	}

	@Override
	public void run() {
		// control update rate here

		// update :)
		update();

		// cleanUp on program exit
		// cleanUp();
	}

	@Override
	public void update() {
		super.timeMarkStart();

		// TODO implement message bus

		// Assumes that there is only one FPP camera component so the first one found is
		// used.
		Set<FPPCameraComponent> fppCamComps = entityController.getFPPCameraComponents();
		FPPCameraComponent camera = fppCamComps.iterator().next();
		int listenerEID = camera.getEID();
		// Update listener position
		Vector3f listenerPosition = camera.getPosition();
		AL10.alListener3f(AL10.AL_POSITION, listenerPosition.x, listenerPosition.y, listenerPosition.z);
		// Update listener orientation
		Vector3f listenerFWD = new Matrix4f(camera.getViewMatrix()).invert()
				.transformDirection(new Vector3f(0.0f, 0.0f, -1.0f));
		Vector3f listenerUP = new Vector3f(0.0f, 1.0f, 0.0f); // Imprecise? Camera has more than 1 DOF.
		float[] ListenerOrientation = { listenerFWD.x, listenerFWD.y, listenerFWD.z, listenerUP.x, listenerUP.y,
				listenerUP.z };
		AL10.alListenerfv(AL10.AL_ORIENTATION, ListenerOrientation);
		// Update listener velocity
		PhysicsComponent listenPhysComp = entityController.getPhysicsComponent(listenerEID);
		if (listenPhysComp != null && listenPhysComp.isAffectedByPhysics()) {
			Vector3fc vel = listenPhysComp.getVelocity();
			AL10.alListener3f(AL10.AL_VELOCITY, vel.x(), vel.y(), vel.z());
		} else {
			AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
		}

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while updating listener: " + err);
		}

		for (AudioSourceComponent comp : entityController.getAudioSourceComponents()) {
			updateEntitySound(comp.getEID());
		}

		super.timeMarkEnd();
	}

	@Override
	public void cleanUp() {
		// Delete all sound buffers
		Iterator<AudioSourceComponent> it = entityController.getAudioSourceComponents().iterator();
		while (it.hasNext()) {
			AudioSourceComponent comp = it.next();
			String assetName = comp.getAudioSourceFileName();
			entityController.getAudioSourceComponents().remove(comp);
			audioLoader.unloadSound(assetName);
		}
		// Delete all sound sources
		for (Integer currSource: alSourceIDs) {
			AL10.alSourceStop(currSource.intValue());
			AL10.alSourcei(currSource.intValue(), AL10.AL_BUFFER, AL10.AL_NONE);
			AL10.alDeleteSources(currSource.intValue());
		}

		// Shutdown OpenAL
		ALC10.alcMakeContextCurrent(alContext);
		ALC10.alcDestroyContext(alContext);
		ALC10.alcCloseDevice(alDevice);
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// - AudioSourceComponent
		ArrayList<String> audioSourceComponentData = BlueprintLoader.getAllLinesWith("AUDIOSOURCECOMPONENT", blueprint);
		String[] frags = null;
		for (String dataSet : audioSourceComponentData) {
			int eID = -1;

			try {
				eID = BlueprintLoader.extractEID(dataSet);
				if (!entityController.isEntity(eID)) {
					System.err.printf("Audio: couldn't load component. %d is not a valid eID.%n", eID);
					continue;
				}

				// extract data and add component
				frags = BlueprintLoader.getDataFragments(dataSet);
				attachAudioSource(eID, frags[0], Float.valueOf(frags[1]), Float.valueOf(frags[2]),
						Float.valueOf(frags[3]), Float.valueOf(frags[4]), Float.valueOf(frags[5]));

			} catch (IndexOutOfBoundsException e) {
				System.err.printf("Audio: couldn't load component for entity %d. Too few arguments.%n", eID);
			} catch (IllegalArgumentException e) {
				System.err.printf("Audio: couldn't load component for entity %d. %s%n", eID, e.toString());
			}
		}
	}

	public void playEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);

		// find free source
		comp.setSourceID(AL10.AL_NONE);
		for (int currSource : alSourceIDs) {
			if (AL10.alGetSourcei(currSource, AL10.AL_BUFFER) == AL10.AL_NONE
					|| AL10.alGetSourcei(currSource, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED
					|| AL10.alGetSourcei(currSource, AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED) {

				comp.setSourceID(currSource);
			}
		}

		if (comp.getSourceID() != AL10.AL_NONE) {
			// check if valid start position
			if (Float.compare(comp.getStartPos(), 0) >= 0 || Float.compare(comp.getStartPos(),
					audioLoader.getSound(comp.getAudioSourceFileName()).getLengthSecs()) <= 0) {

				AL11.alSourcef(comp.getSourceID(), AL11.AL_SEC_OFFSET, comp.getStartPos());
				AL10.alSourcePlay(comp.getSourceID());
			}

		} else {
			System.out.println("Audio Warning: no free sources available to start new playback");
		}

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while playing " + eID + ": " + err);
		}

	}

	public void stopEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);
		if (AL10.alGetSourcei(comp.getSourceID(), AL10.AL_BUFFER) == audioLoader.getSound(comp.getAudioSourceFileName())
				.getBufferID()) {
			AL10.alSourceStop(comp.getSourceID());
			AL10.alSourceRewind(comp.getSourceID());
			AL10.alSourcei(comp.getSourceID(), AL10.AL_BUFFER, AL10.AL_NONE);
		}

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while playing " + eID + ": " + err);
		}
	}

	public void updateEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);

		if (AL10.alGetSourcei(comp.getSourceID(), AL10.AL_BUFFER) == audioLoader.getSound(comp.getAudioSourceFileName())
				.getBufferID() && AL10.alGetSourcei(comp.getSourceID(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
			Vector3fc pos = entityController.getTransformable(comp.getEID()).getPosition();
			Vector3fc dir = entityController.getTransformable(comp.getEID()).getDirectionVector();
			AL10.alSourcef(comp.getSourceID(), AL10.AL_GAIN, comp.getGain());
			AL10.alSourcef(comp.getSourceID(), AL10.AL_PITCH, comp.getPitch());
			AL10.alSourcef(comp.getSourceID(), AL10.AL_REFERENCE_DISTANCE, comp.getReferenceDistance());
			AL10.alSourcef(comp.getSourceID(), AL10.AL_ROLLOFF_FACTOR, comp.getRolloffFactor());
			AL10.alSourcef(comp.getSourceID(), AL10.AL_MAX_DISTANCE, comp.getMaxDistance());
			AL10.alSource3f(comp.getSourceID(), AL10.AL_POSITION, pos.x(), pos.y(), pos.z());
			AL10.alSource3f(comp.getSourceID(), AL10.AL_DIRECTION, dir.x(), dir.y(), dir.z());
			// set velocity if available
			PhysicsComponent physComp = entityController.getPhysicsComponent(comp.getEID());
			if (physComp != null && physComp.isAffectedByPhysics()) {
				Vector3fc vel = physComp.getVelocity();
				AL10.alSource3f(comp.getSourceID(), AL10.AL_VELOCITY, vel.x(), vel.y(), vel.z());
			} else {
				AL10.alSource3f(comp.getSourceID(), AL10.AL_VELOCITY, 0, 0, 0);
			}
		}

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while playing " + eID + ": " + err);
		}
	}

	public void attachAudioSource(int eID, String assetName, float gain, float pitch, float refDist, float rollOff,
			float maxDist) {
		audioLoader.loadSound(assetName);
		entityController.addAudioSourceComponent(eID).setAudioSourceFileName(assetName).setGain(gain).setPitch(pitch)
				.setReferenceDistance(refDist).setRolloffFactor(rollOff).setMaxDistance(maxDist);
	}

	public void detachAudioSource(int eID) {
		stopEntitySound(eID);
		String assetName = entityController.getAudioSourceComponent(eID).getAudioSourceFileName();
		audioLoader.unloadSound(assetName);
		entityController.removeAudioSourceComponent(eID);
	}

}
