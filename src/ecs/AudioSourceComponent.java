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

	public AudioSourceComponent setAudioSourceFileName(String assetName) {
		this.assetName = assetName;
		return this;
	}

	public int getSourceID() {
		return sourceID;
	}

	public AudioSourceComponent setSourceID(int sourceID) {
		this.sourceID = sourceID;
		return this;
	}

	public float getGain() {
		return gain;
	}

	public AudioSourceComponent setGain(float gain) {
		this.gain = gain;
		return this;
	}

	public float getPitch() {
		return pitch;
	}

	public AudioSourceComponent setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public float getReferenceDistance() {
		return referenceDistance;
	}

	public AudioSourceComponent setReferenceDistance(float referenceDistance) {
		this.referenceDistance = referenceDistance;
		return this;
	}

	public float getRolloffFactor() {
		return rolloffFactor;
	}

	public AudioSourceComponent setRolloffFactor(float rolloffFactor) {
		this.rolloffFactor = rolloffFactor;
		return this;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public AudioSourceComponent setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
		return this;
	}

	public boolean isLooping() {
		return isLooping;
	}

	public AudioSourceComponent setLooping(boolean isLooping) {
		this.isLooping = isLooping;
		return this;
	}

}
