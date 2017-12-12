package audio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import ecs.AudioSourceComponent;
import ecs.EntityController;
import example.ResourceLoader;

public class AudioSystem {
	
	// ECS
	private EntityController entityController;
	private ResourceLoader resourceLoader;
	
	// OpenAL context
	private long al_device;
	private long al_context;
	
	// user objects
	private HashMap<String, AudioResource> sound_buffers = new HashMap<>();
	private HashMap<String, AudioSource> audio_players = new HashMap<>();

	public AudioSystem(EntityController entityController, ResourceLoader resourceLoader) {
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
		
		// init OpenAL
		al_device = ALC10.alcOpenDevice((ByteBuffer)null);
		if (al_device == MemoryUtil.NULL)
			throw new IllegalStateException("Failed to open the default device.");
		
		ALCCapabilities deviceCaps = ALC.createCapabilities(al_device);
		assert(deviceCaps.OpenALC10);
        System.out.println("OpenALC10: " + deviceCaps.OpenALC10);
        
        String defaultDeviceSpecifier = ALC10.alcGetString(MemoryUtil.NULL, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        assert(defaultDeviceSpecifier != null);
        System.out.println("Output device: " + defaultDeviceSpecifier);
        
        long context = ALC10.alcCreateContext(al_device, (IntBuffer)null);
        EXTThreadLocalContext.alcSetThreadContext(context);
        
        AL.createCapabilities(deviceCaps);
	}
	
	public void run() {
		
		for (AudioSourceComponent comp : entityController.getAudioSourceComponents()) {
			
			if (comp.getAl_id() < 0) {
				// component was not initialised! fixing it now
				comp.setAl_id(AL10.alGenSources());
			}
			
			if (comp.getAl_resource_name() == null) {
				// no playing resource set. skip component
				continue;
			}
			
			if (AL10.alGetSourcei(comp.getAl_id(), AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
				// reset source
				AL10.alSourceRewind(comp.getAl_id());
				AL10.alSourcei(comp.getAl_id(), AL10.AL_BUFFER, sound_buffers.get(comp.getAl_resource_name()).getBufferID());
				
				// update params
				AL10.alSourcef(comp.getAl_id(), AL10.AL_GAIN, comp.getAl_gain());
				AL10.alSourcef(comp.getAl_id(), AL10.AL_PITCH, comp.getAl_pitch());
				AL10.alSource3f(comp.getAl_id(), AL10.AL_POSITION, comp.getAl_position().x, comp.getAl_position().y, comp.getAl_position().z);
				AL10.alSource3f(comp.getAl_id(), AL10.AL_DIRECTION, comp.getAl_direction().x, comp.getAl_direction().y, comp.getAl_position().z);
				AL10.alSource3f(comp.getAl_id(), AL10.AL_VELOCITY, comp.getAl_velocity().x, comp.getAl_velocity().y, comp.getAl_velocity().z);
				AL10.alSourcei(comp.getAl_id(), AL10.AL_LOOPING, comp.isAl_looping() ? AL10.AL_TRUE : AL10.AL_FALSE);
				
				// play source
				AL10.alSourcePlay(comp.getAl_id());
			}
		}
	}
	
	public void loadSoundFile(String resource_name, String filename) throws UnsupportedAudioFileException, IOException {
		AudioResource resource = new AudioResource(filename);
		sound_buffers.put(resource_name, resource);
	}
	
	public void cleanUp() {
		// close all sound buffers
		for (String bu : sound_buffers.keySet()) {
			sound_buffers.get(bu).dtor();
			sound_buffers.remove(bu);
		}
		
		// properly quit OpenAL
		ALC10.alcMakeContextCurrent(al_context);
		ALC10.alcDestroyContext(al_context);
		ALC10.alcCloseDevice(al_device);
	}
}
