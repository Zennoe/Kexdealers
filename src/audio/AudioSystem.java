package audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
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
	private long alDevice;
	private long alContext;

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
			throw new IllegalStateException("Failed to laod audio capabilities");
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
		// Initialization done
		
		audioLoader = new AudioLoader();
	}

	public void run() {
		// control update rate here

		// update :)
		update();
		
		// cleanUp on program exit
		//cleanUp();
	}
	
	public void update() {
		super.timeMarkStart();
		
		// Assumes that there is only one FPP camera component so the first one found is used.
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
	
		for (AudioSourceComponent comp : entityController.getAudioSourceComponents()) {
			if (AL10.alGetSourcei(comp.getSourceID(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
				Vector3fc pos = entityController.getTransformable(comp.getEID()).getPosition();
				Vector3fc dir = entityController.getTransformable(comp.getEID()).getDirectionVector();
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
		}
		
		super.timeMarkEnd();
	}
	
	public void cleanUp() {
		// Delete all sound buffers

		// Close OpenAL
		ALC10.alcMakeContextCurrent(alContext);
		ALC10.alcDestroyContext(alContext);
		ALC10.alcCloseDevice(alDevice);
	}
	
	public void loadBlueprint(ArrayList<String> blueprint) {
		// - AudioSourceComponent
		ArrayList<String> audioSourceComponentData = 
				BlueprintLoader.getAllLinesWith("AUDIOSOURCECOMPONENT", blueprint);
		String[] frags = null;
		for(String dataSet : audioSourceComponentData) {
			try {
				int eID = BlueprintLoader.extractEID(dataSet);
				if (eID < 0) {
					continue;
				}
				
				frags = BlueprintLoader.getDataFragments(dataSet);
				attachAudioSource(eID, frags[0], 
						Float.valueOf(frags[1]), 
						Float.valueOf(frags[2]), 
						Float.valueOf(frags[3]), 
						Float.valueOf(frags[4]), 
						Float.valueOf(frags[5]), 
						Boolean.valueOf(frags[6]));
			} catch (NullPointerException|IllegalArgumentException e) {
				System.err.println("Couldn't parse line of AudiosourceComponent.");
			}
		}
	}
	
	public void playEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);

		AL10.alGetError();

		AL10.alSourcePlay(comp.getSourceID());

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.out.println("oh no " + err);
		}

	}

	public void pauseEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);
		AL10.alSourcePause(comp.getSourceID());
	}

	public void stopEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);
		AL10.alSourceStop(comp.getSourceID());
		AL10.alSourceRewind(comp.getSourceID());
	}

	public void attachAudioSource(int eID, String assetName, float gain, float pitch, float refDist, float rollOff,
			float maxDist, boolean looping) {
		entityController.addAudioSourceComponent(eID)
			.setAudioSourceFileName(assetName)
			.setGain(gain).setPitch(pitch)
			.setReferenceDistance(refDist).setRolloffFactor(rollOff)
			.setMaxDistance(maxDist)
			.setLooping(looping);
		audioLoader.loadSound(assetName);

	}

	public void detachAudioSource(int eID) {
		stopEntitySound(eID);
		String assetName = entityController.getAudioSourceComponent(eID).getAudioSourceFileName();
		audioLoader.unloadSound(assetName);
		entityController.removeAudioSourceComponent(eID);
	}

}
