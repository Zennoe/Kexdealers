package audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class WaveData {
	
	private final int audioFormat;
	private final int sampleRate;
	private final int totalBytes;
	private final int bytesPerFrame;
	private final ByteBuffer data;
	
	private final AudioInputStream audioStream;
	private final byte[] dataArray;
	
	private WaveData(AudioInputStream audioStream) throws UnsupportedAudioFileException	{
		this.audioStream = audioStream;
		
		AudioFormat af = audioStream.getFormat();
		audioFormat = getOpenAlFormat(af.getChannels(), af.getSampleSizeInBits());
		
		this.sampleRate = (int) af.getSampleRate();
		this.bytesPerFrame = af.getFrameSize();
		this.totalBytes = (int) (audioStream.getFrameLength() * bytesPerFrame);
		this.data = BufferUtils.createByteBuffer(totalBytes);
		this.dataArray = new byte[totalBytes];
		
		loadData();
	}
	
	public void dispose() {
		data.clear();
	}
	
	private ByteBuffer loadData() { // TODO AudioStream might not be needed after this is called - fixed?
		try {
			int bytesRead = audioStream.read(dataArray, 0, totalBytes);
			
			data.clear();
			data.put(dataArray, 0, bytesRead);
			data.flip();
			
			audioStream.close();
		} catch (IOException x) {
			System.err.println("Couldn't read bytes from audio stream");
			x.printStackTrace();
		}
		
		return data;
	}
	
	public static WaveData create(String fileName) {
		try {//(BufferedInputStream stream = new BufferedInputStream(Class.class.getResourceAsStream(fileName)))
			File file = new File(fileName);
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			return new WaveData(audioStream);
			
		} catch (FileNotFoundException x) {
			System.err.println("Couldn't find audio file: " +fileName);
			x.printStackTrace();
			
		} catch (IOException x) {
			x.printStackTrace();
			
		} catch (UnsupportedAudioFileException x) {
			x.printStackTrace();
		}
		// ugh
		return null;
	}
	
	private static int getOpenAlFormat(int channels, int bitsPerSample) throws UnsupportedAudioFileException {
		if(channels == 1) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		} else if (channels == 2) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
		} else {
			throw new UnsupportedAudioFileException("File has more than 2 channels!");
		}
	}
	
	public int getAudioFormat() {
		return audioFormat;
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	
	public ByteBuffer getData()	{
		return data;
	}
}

