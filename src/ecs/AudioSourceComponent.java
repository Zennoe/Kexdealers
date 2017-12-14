package ecs;

public class AudioSourceComponent extends Component{
	
	private int al_id = -1;
	private String al_resource_name = null;

	private float al_gain = 1.0f;
	private float al_pitch = 1.0f;
	private float al_rolloff_factor = 1.0f;
	private float al_reference_distance = 5.0f;
	private float al_max_distance = 100.0f;
	private boolean al_looping = true;
	private boolean cmd_start = false;
	private boolean cmd_stop = false;
	
	// ecs
	private int eID;
	private String assetName = "default";
	
	public AudioSourceComponent(int eID, String assetName) {
		this.eID = eID;
		this.assetName = assetName;
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

	public float getAl_gain() {
		return al_gain;
	}

	public void setAl_gain(float al_gain) {
		this.al_gain = al_gain;
	}

	public float getAl_pitch() {
		return al_pitch;
	}

	public void setAl_pitch(float al_pitch) {
		this.al_pitch = al_pitch;
	}

	public boolean isAl_looping() {
		return al_looping;
	}

	public void setAl_looping(boolean al_looping) {
		this.al_looping = al_looping;
	}

	public int getAl_id() {
		return al_id;
	}

	public void setAl_id(int al_id) {
		this.al_id = al_id;
	}

	public String getAl_resource_name() {
		return al_resource_name;
	}

	public void setAl_resource_name(String al_resource_name) {
		this.al_resource_name = al_resource_name;
	}

	public float getAl_rolloff_factor() {
		return al_rolloff_factor;
	}

	public void setAl_rolloff_factor(float al_rolloff_factor) {
		this.al_rolloff_factor = al_rolloff_factor;
	}

	public float getAl_reference_distance() {
		return al_reference_distance;
	}

	public void setAl_reference_distance(float al_reference_distance) {
		this.al_reference_distance = al_reference_distance;
	}

	public float getAl_max_distance() {
		return al_max_distance;
	}

	public void setAl_max_distance(float al_max_distance) {
		this.al_max_distance = al_max_distance;
	}

	public boolean isCmd_start() {
		return cmd_start;
	}

	public void setCmd_start(boolean cmd_start) {
		this.cmd_start = cmd_start;
	}

	public boolean isCmd_stop() {
		return cmd_stop;
	}

	public void setCmd_stop(boolean cmd_stop) {
		this.cmd_stop = cmd_stop;
	}
}
