package ecs;

import org.joml.Vector3f;

public class AudioSourceComponent extends Component{
	
	private int al_id = -1;
	private String al_resource_name = null;

	private float al_gain = 1.0f;
	private float al_pitch = 1.0f;
	private Vector3f al_position = new Vector3f(0,0,0);
	private Vector3f al_direction = new Vector3f(0,0,0);
	private Vector3f al_velocity = new Vector3f(0,0,0);
	private boolean al_looping = true;
	
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

	public Vector3f getAl_position() {
		return al_position;
	}

	public void setAl_position(Vector3f al_position) {
		this.al_position = al_position;
	}

	public Vector3f getAl_direction() {
		return al_direction;
	}

	public void setAl_direction(Vector3f al_direction) {
		this.al_direction = al_direction;
	}

	public Vector3f getAl_velocity() {
		return al_velocity;
	}

	public void setAl_velocity(Vector3f al_velocity) {
		this.al_velocity = al_velocity;
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
}
