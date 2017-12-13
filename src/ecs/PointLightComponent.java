package ecs;

import org.joml.Vector3f;

public class PointLightComponent extends Component{

	private int eID;
	
	private Vector3f position = new Vector3f();
	private Vector3f ambient = new Vector3f();
	private Vector3f diffuse = new Vector3f();
	private Vector3f specular = new Vector3f();
	private Vector3f attenuation = new Vector3f();
	
	public PointLightComponent(int eID){
		this.eID = eID;
	}
	
	public int getEID(){
		return eID;
	}
	
	public void setEID(int eID) {
		this.eID = eID;
	}

	public Vector3f getPosition(){
		return position;
	}

	public PointLightComponent setPosition(Vector3f position){
		this.position = position;
		return this;
	}

	public Vector3f getAmbient(){
		return ambient;
	}

	public PointLightComponent setAmbient(Vector3f ambient){
		this.ambient = ambient;
		return this;
	}

	public Vector3f getDiffuse(){
		return diffuse;
	}

	public PointLightComponent setDiffuse(Vector3f diffuse){
		this.diffuse = diffuse;
		return this;
	}

	public Vector3f getSpecular(){
		return specular;
	}

	public PointLightComponent setSpecular(Vector3f specular){
		this.specular = specular;
		return this;
	}

	public Vector3f getAttenuation(){
		return attenuation;
	}

	public PointLightComponent setAttenuation(Vector3f attenuation){
		this.attenuation = attenuation;
		return this;
	}
	
	
}

