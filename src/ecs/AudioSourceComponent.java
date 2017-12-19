package ecs;

public class AudioSourceComponent extends Component{
	
	private int eID;
	
	private String assetName = "default";
	
	private int sourceID;
	
	private float gain = 1.0f;
	private float pitch = 1.0f;
	
	private float referenceDistance = 10.0f;
	private float rolloffFactor = 1.0f;
	private float maxDistance = 50.0f;
	
	private boolean isLooping = false;
	
	public AudioSourceComponent(int eID) {
		this.eID = eID;
	}

	public int getEID() {
		return eID;
	}

	public void setEID(int eID) {
		this.eID = eID;
	}

	public String getAudioSourceFileName() {
		return assetName;
	}

	public void setAudioSourceFileName(String assetName) {
		this.assetName = assetName;
	}

	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}

	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		this.gain = gain;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getReferenceDistance() {
		return referenceDistance;
	}

	public void setReferenceDistance(float referenceDistance) {
		this.referenceDistance = referenceDistance;
	}

	public float getRolloffFactor() {
		return rolloffFactor;
	}

	public void setRolloffFactor(float rolloffFactor) {
		this.rolloffFactor = rolloffFactor;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

}
