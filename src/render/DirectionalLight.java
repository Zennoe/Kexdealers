package render;

import org.joml.Vector3f;

public class DirectionalLight {
	
	private Vector3f direction = new Vector3f(0.0f, -1.0f, 0.0f);
	private Vector3f ambient = new Vector3f(1.0f, 1.0f, 1.0f);
	private Vector3f diffuse = new Vector3f(1.0f, 1.0f, 1.0f);
	private Vector3f specular = new Vector3f(1.0f, 1.0f, 1.0f);
	
	public Vector3f getDirection(){
		return direction;
	}
	
	public DirectionalLight setDirection(Vector3f direction){
		this.direction = direction;
		return this;
	}

	public Vector3f getAmbient(){
		return ambient;
	}

	public DirectionalLight setAmbient(Vector3f ambient){
		this.ambient = ambient;
		return this;
	}

	public Vector3f getDiffuse(){
		return diffuse;
	}

	public DirectionalLight setDiffuse(Vector3f diffuse){
		this.diffuse = diffuse;
		return this;
	}

	public Vector3f getSpecular(){
		return specular;
	}

	public DirectionalLight setSpecular(Vector3f specular){
		this.specular = specular;
		return this;
	}
	
}
