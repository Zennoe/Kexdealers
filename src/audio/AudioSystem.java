package audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import ecs.EntityController;
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
		if(deviceCaps.OpenALC10) {
			throw new IllegalStateException("Failed to open audio device -> Failed to load capabilities");
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
		// Update listener position
		Vector3f listenerPosition = entityController.getFPPCameraComponent(listenerEID).getPosition();
		AL10.alListener3f(AL10.AL_POSITION, listenerPosition.x, listenerPosition.y, listenerPosition.z);
		// Update listener orientation
		Vector3f listenerFWD;
		Vector3f listenerUP;
	}
	
	// add and remove audio related components only through this system
	
}
