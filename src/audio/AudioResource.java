package audio;

import org.lwjgl.openal.AL10;

public class AudioResource {
	
	private final int bufferID;
	
	public AudioResource(String fileName) {
		bufferID = AL10.alGenBuffers();
		
		WaveData waveFile = WaveData.create(fileName);
		AL10.alBufferData(bufferID, waveFile.getAudioFormat(), waveFile.getData(), waveFile.getSampleRate());
		waveFile.dispose();
	}
	
	public int getBufferID() {
		return bufferID;
	}
	
	public void destroy() {
		AL10.alDeleteBuffers(bufferID);
	}
}
