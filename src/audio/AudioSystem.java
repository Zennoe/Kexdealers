package audio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTExponentDistance;
import org.lwjgl.openal.EXTLinearDistance;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import ecs.AudioSourceComponent;
import ecs.EntityController;
import example.FPPCamera;
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
        AL10.alDistanceModel(EXTLinearDistance.AL_LINEAR_DISTANCE_CLAMPED);
	}
	
	public void run(FPPCamera fppcamera) {
		// update listener's position
		Vector3f lis_pos = fppcamera.getPosition();
		AL10.alListener3f(AL10.AL_POSITION, lis_pos.x, lis_pos.y, lis_pos.z);
		
		// update listener's orientation
		Vector3f lis_dir_forward = (new Matrix4f(fppcamera.getViewMatrix())).invert()
													.transformDirection(new Vector3f(0,0,-1));
		Vector3f lis_dir_up = (new Vector3f(0,1,0));
		float[] lis_dir = { lis_dir_forward.x, lis_dir_forward.y, lis_dir_forward.z,
							lis_dir_up.x, lis_dir_up.y, lis_dir_up.z };
		AL10.alListenerfv(AL10.AL_ORIENTATION, lis_dir);
		
		// update listener's velocity
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0); // TODO IMPLEMENTATION
		
		
		for (AudioSourceComponent comp : entityController.getAudioSourceComponents()) {
			if (comp.getAl_id() < 0) {
				// component was previously not initialised! fixing it now
				comp.setAl_id(AL10.alGenSources());
				System.out.println("AudioSourceComp of entity " + comp.getEID() + 
									" was initialised with AL_ID " + comp.getAl_id());
				comp.setCmd_start(true); // REMOVE ME I'm a hack
			}
			
			
			// if no playing resource has been set; skip entity
			if (comp.getAl_resource_name() == null) {
				comp.setAl_resource_name("disappointment_02"); // REMOVE ME
				continue;
			}
			
			// change source's state if requested by user
			if (comp.isCmd_start()) {
				comp.setCmd_start(false);
				
				// reset source
				AL10.alSourceRewind(comp.getAl_id());
				AL10.alSourcei(comp.getAl_id(), AL10.AL_BUFFER, sound_buffers.get(comp.getAl_resource_name()).getBufferID());
				
				// update attributes
				AL10.alSourcef(comp.getAl_id(), AL10.AL_GAIN, comp.getAl_gain());
				AL10.alSourcef(comp.getAl_id(), AL10.AL_PITCH, comp.getAl_pitch());
				AL10.alSourcei(comp.getAl_id(), AL10.AL_LOOPING, comp.isAl_looping() ? AL10.AL_TRUE : AL10.AL_FALSE);
				AL10.alSourcef(comp.getAl_id(), AL10.AL_REFERENCE_DISTANCE, comp.getAl_reference_distance());
				AL10.alSourcef(comp.getAl_id(), AL10.AL_ROLLOFF_FACTOR, comp.getAl_rolloff_factor());
				AL10.alSourcef(comp.getAl_id(), AL10.AL_MAX_DISTANCE, comp.getAl_max_distance());
				
				// let's play source
				AL10.alSourcePlay(comp.getAl_id());
			}
			if (comp.isCmd_stop()) {
				comp.setCmd_stop(false);
				
				AL10.alSourceStop(comp.getAl_id());
			}
			
			// if the audio is playing, update its attributes
			if (AL10.alGetSourcei(comp.getAl_id(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
				
				Vector3f al_pos = entityController.getTransformable(comp.getEID()).getPosition();
				AL10.alSource3f(comp.getAl_id(), AL10.AL_POSITION, al_pos.x, al_pos.y, al_pos.z);
				AL10.alSource3f(comp.getAl_id(), AL10.AL_DIRECTION, 0, 0, 0); // TODO IMPLEMENTATION
				AL10.alSource3f(comp.getAl_id(), AL10.AL_VELOCITY, 0, 0, 0); // TODO IMPLEMENTATION
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
		}
		sound_buffers.clear();
		
		// properly quit OpenAL
		ALC10.alcMakeContextCurrent(al_context);
		ALC10.alcDestroyContext(al_context);
		ALC10.alcCloseDevice(al_device);
	}
}
