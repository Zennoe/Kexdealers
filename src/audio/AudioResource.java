package audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.AL10;

public class AudioResource {
	
	private final int buffer_id;
	
	public AudioResource(String file) throws UnsupportedAudioFileException, IOException {
		// create new buffer
		buffer_id = AL10.alGenBuffers();
		
		// load file and store in buffer
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer_id, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}
	
	public int getBufferID() { return buffer_id; }
	
	public void dtor() {
		AL10.alDeleteBuffers(buffer_id);
	}
}
