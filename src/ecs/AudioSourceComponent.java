package ecs;

public class AudioSourceComponent extends Component{
	
	private int eID;
	
	private String assetName = "default";
	
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

}
