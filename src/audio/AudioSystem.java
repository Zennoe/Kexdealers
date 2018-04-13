package audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import ecs.AudioSourceComponent;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import example.ResourceLoader;

public class AudioSystem {
	
	private EntityController entityController;
	
	private ResourceLoader resourceLoader;
	
	// OpenAL context
	private long alDevice;
	private long alContext;

	public AudioSystem(EntityController entityController, ResourceLoader resourceLoader) {
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
		
		// Initialize OpenAL
		alDevice = ALC10.alcOpenDevice((ByteBuffer) null);
		if(alDevice == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to open audio device");
		}
		
		ALCCapabilities deviceCaps = ALC.createCapabilities(alDevice);
		if(!deviceCaps.OpenALC10) {
			ALC10.alcCloseDevice(alDevice); // close previously opened alDevice
			throw new IllegalStateException("Failed to laod audio capabilities");
		}
		
		String defaultDeviceSpecifier = ALC10.alcGetString(MemoryUtil.NULL, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        if(defaultDeviceSpecifier != null) {
        	System.out.println("Audio output device: " + defaultDeviceSpecifier);
        } else {
        	throw new AssertionError("Default Audio Device Specifier == null");
        }
        
        alContext = ALC10.alcCreateContext(alDevice, (IntBuffer) null);
        EXTThreadLocalContext.alcSetThreadContext(alContext);
        
        AL.createCapabilities(deviceCaps);
        
        AL10.alDistanceModel(EXTLinearDistance.AL_LINEAR_DISTANCE_CLAMPED);
        // Doppler-Effect is ignored for now.
        AL10.alListener3f(AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
	}
	
	public void run(int listenerEID) {
		FPPCameraComponent camera = entityController.getFPPCameraComponent(listenerEID);
		// Update listener position
		Vector3f listenerPosition = camera.getPosition();
		AL10.alListener3f(AL10.AL_POSITION, listenerPosition.x, listenerPosition.y, listenerPosition.z);
		// Update listener orientation
		Vector3f listenerFWD = new Matrix4f(camera.getViewMatrix())
				.invert()
				.transformDirection(new Vector3f(0.0f, 0.0f, -1.0f));
		Vector3f listenerUP = new Vector3f(0.0f, 1.0f, 0.0f); // Imprecise? Camera has more than 1 DOF.
		float[] ListenerOrientation = {listenerFWD.x, listenerFWD.y, listenerFWD.z,
				listenerUP.x, listenerUP.y, listenerUP.z};
		AL10.alListenerfv(AL10.AL_ORIENTATION, ListenerOrientation);
		
		for(AudioSourceComponent comp : entityController.getAudioSourceComponents()) {
			
			if(AL10.alGetSourcei(comp.getSourceID(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
				Vector3f pos = entityController.getTransformable(comp.getEID()).getPosition();
				AL10.alSource3f(comp.getSourceID(), AL10.AL_POSITION, pos.x, pos.y, pos.z);
				AL10.alSource3f(comp.getSourceID(), AL10.AL_DIRECTION, 0, 0, 0); // TODO AL_DIRECTION
			}
		}
		
	}
	
	public void playEntitySound(int eID) {
		AudioSourceComponent comp = entityController.getAudioSourceComponent(eID);
		
		AL10.alGetError();
		
		AL10.alSourcePlay(comp.getSourceID());
		
		int err = AL10.alGetError();
		if(err != AL10.AL_NO_ERROR) {
			System.out.println("oh no " +err);
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
	
	public void attachAudioSource(int eID, String assetName, 
			float gain, float pitch, float refDist, float rollOff, float maxDist, boolean looping) {
		entityController.addAudioSourceComponent(eID)
			.setAudioSourceFileName(assetName)
			.setGain(gain)
			.setPitch(pitch)
			.setReferenceDistance(refDist)
			.setRolloffFactor(rollOff)
			.setMaxDistance(maxDist)
			.setLooping(looping);
		resourceLoader.loadSound(assetName);
		
	}
	
	public void detachAudioSource(int eID) {
		stopEntitySound(eID);
		String assetName = entityController.getAudioSourceComponent(eID).getAudioSourceFileName();
		resourceLoader.unloadSound(assetName);
		entityController.removeAudioSourceComponent(eID);
	}
	
	public void cleanUp() {
		// Delete all sound buffers
		
		// Close OpenAL
		ALC10.alcMakeContextCurrent(alContext);
		ALC10.alcDestroyContext(alContext);
		ALC10.alcCloseDevice(alDevice);
	}
	
}
