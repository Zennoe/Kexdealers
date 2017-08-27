package ecs;

import org.joml.Vector3f;

public class PointLightComponent {

	private Vector3f position;
	private Vector3f ambient;
	private Vector3f diffuse;
	private Vector3f specular;
	private Vector3f attenuation;
	
	public PointLightComponent(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f attenuation){
		this.position = position;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.attenuation = attenuation;
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

