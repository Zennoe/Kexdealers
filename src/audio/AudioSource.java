package audio;

import java.nio.FloatBuffer;

import org.lwjgl.openal.AL10;

public class AudioSource {
	private int sourceID;
	
	public AudioSource() {
		sourceID = AL10.alGenSources();
		
		// set default properties
		setGain(1.0f);
		setPitch(1.0f);
		setPosition(0.0f, 0.0f, 0.0f);
		setDirection(0.0f, 0.0f, 0.0f);
		setLooping(true);
	}
	
	public void play(int buffer_id, boolean looping) {
		setLooping(looping);
		play(buffer_id);
	}
	public void play(int buffer_id) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer_id);
		AL10.alSourcePlay(sourceID);
	}
	
	public float getGain() {
		return AL10.alGetSourcef(sourceID, AL10.AL_GAIN);
	}
	public void setGain(float i) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, i);
	}
	public float getPitch() {
		return AL10.alGetSourcef(sourceID, AL10.AL_PITCH);
	}
	public void setPitch(float i) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, i);
	}
	public void getPosition(FloatBuffer v1, FloatBuffer v2, FloatBuffer v3) {
		AL10.alGetSource3f(sourceID, AL10.AL_POSITION, v1, v2, v3);
	}
	public void setPosition(float v1, float v2, float v3) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, v1, v2, v3);
	}
	public void getDirection(FloatBuffer v1, FloatBuffer v2, FloatBuffer v3) {
		AL10.alGetSource3f(sourceID, AL10.AL_DIRECTION, v1, v2, v3);
	}
	public void setDirection(float v1, float v2, float v3) {
		AL10.alSource3f(sourceID, AL10.AL_DIRECTION, v1, v2, v3);
	}
	public void getVelocity(FloatBuffer v1, FloatBuffer v2, FloatBuffer v3) {
		AL10.alGetSource3f(sourceID, AL10.AL_VELOCITY, v1, v2, v3);
	}
	public void setVelocity(float v1, float v2, float v3) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, v1, v2, v3);
	}
	public boolean getLooping() {
		return AL10.alGetSourcei(sourceID, AL10.AL_LOOPING) == AL10.AL_TRUE;
	}
	public void setLooping(boolean state) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, state ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
}