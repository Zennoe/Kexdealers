package textures;

import org.joml.Vector4f;

public class Material {
	
	public static final Vector4f DEFAULT_COLOR = new Vector4f(0.5f, 0.0f, 0.5f, 1.0f);
	
	private Vector4f ambient;
	private Vector4f diffuse;
	private Vector4f specular;
	
	private float reflectance;
	
	private Texture texture;
	private Texture specularMap;
	private Texture normalMap;
	
	public Material() {
		this.ambient = DEFAULT_COLOR;
		this.diffuse = DEFAULT_COLOR;
		this.specular = DEFAULT_COLOR;
		
		this.reflectance = 0;
		
		this.texture = null;
	}
	
	public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, float reflectance) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		
		this.reflectance = reflectance;
	}

	public Vector4f getAmbient() {
		return ambient;
	}

	public void setAmbient(Vector4f ambient) {
		this.ambient = ambient;
	}

	public Vector4f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector4f diffuse) {
		this.diffuse = diffuse;
	}

	public Vector4f getSpecular() {
		return specular;
	}

	public void setSpecular(Vector4f specular) {
		this.specular = specular;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getSpecularMap() {
		return specularMap;
	}
	
	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}
	
	public Texture getNormalMap() {
		return normalMap;
	}
	
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}
	
}
