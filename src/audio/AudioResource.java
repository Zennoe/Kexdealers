package audio;

import java.util.Iterator;

import org.lwjgl.openal.AL10;

public class AudioResource {

	private final String fileName;
	private final float lengthSecs;
	private final int bufferID;

	public AudioResource(String fileName) {
		this.fileName = fileName;

		bufferID = AL10.alGenBuffers();

		WaveData waveFile = WaveData.create(fileName);
		AL10.alBufferData(bufferID, waveFile.getAudioFormat(), waveFile.getData(), waveFile.getSampleRate());
		waveFile.dispose();

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while creating buffer of " + fileName + ": " + err);
		}
		
		lengthSecs = calculateBufferSize();
	}

	public int getBufferID() {
		return bufferID;
	}
	
	public float getLengthSecs() {
		return lengthSecs;
	}

	public void destroy(Iterator<Integer> allSourceIDs) {
		while (allSourceIDs.hasNext()) {
			int currSource = allSourceIDs.next().intValue();
			if (AL10.alGetSourcei(currSource, AL10.AL_BUFFER) == bufferID) {
				AL10.alSourcei(currSource, AL10.AL_BUFFER, AL10.AL_NONE);
			}
		}

		AL10.alDeleteBuffers(bufferID);

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err.println("Audio: error while deleting buffer of " + fileName + ": " + err);
		}
	}

	private float calculateBufferSize() {
		float size = AL10.alGetBufferi(bufferID, AL10.AL_SIZE);
		float bits = AL10.alGetBufferi(bufferID, AL10.AL_BITS);
		float channels = AL10.alGetBufferi(bufferID, AL10.AL_CHANNELS);
		float frequency = AL10.alGetBufferi(bufferID, AL10.AL_FREQUENCY);

		int err = AL10.alGetError();
		if (err != AL10.AL_NO_ERROR) {
			System.err
					.println("Audio: error while calculating buffer length " + bufferID + " " + fileName + ": " + err);
			return -1;
		} else {
			return size / channels / (bits / 8) / frequency;
		}
	}
}
