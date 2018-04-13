package audio;

import org.lwjgl.openal.AL10;

public class AudioResource {
	
	private final int bufferID;
	private final int sourceID;
	
	public AudioResource(String fileName) {
		bufferID = AL10.alGenBuffers();
		
		WaveData waveFile = WaveData.create(fileName);
		AL10.alBufferData(bufferID, waveFile.getAudioFormat(), waveFile.getData(), waveFile.getSampleRate());
		waveFile.dispose();
		
		sourceID = AL10.alGenSources();
	}
	
	public int getBufferID() {
		return bufferID;
	}
	
	public int getSourceID() {
		return sourceID;
	}
	
	public void destroy() {
		AL10.alDeleteSources(sourceID);
		AL10.alDeleteBuffers(bufferID);
	}
}
