package textures;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11C;

public class Material {

	public static final Vector3f DEFAULT_COLOR = new Vector3f(0.5f, 0.0f, 0.5f);
	private String name;
	// Colors
	private Vector3f ambient = DEFAULT_COLOR;
	private Vector3f diffuse = DEFAULT_COLOR;
	private Vector3f specular = DEFAULT_COLOR;
	private Vector3f transmission = DEFAULT_COLOR;
	// Textures
	private int ambientID;
	private int diffuseID;
	private int specularID;
	private int specularExponentID;
	private int dissolveID;
	// Parameters
	private float dissolve;
	private float specularExponent;
	
	public Material(String name, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f transmission,
			int ambientID, int diffuseID, int specularID, int specularExponentID, int dissolveID, float dissolve,
			float specularExponent) {
		this.name = name;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.transmission = transmission;
		this.ambientID = ambientID;
		this.diffuseID = diffuseID;
		this.specularID = specularID;
		this.specularExponentID = specularExponentID;
		this.dissolveID = dissolveID;
		this.dissolve = dissolve;
		this.specularExponent = specularExponent;
	}

	// old constructor
	public Material(int diffuseID, int specularID, float shininess, int width, int height) {
		this.diffuseID = diffuseID;
		this.specularID = specularID;
	}
	
	public String getName() {
		return name;
	}

	public Vector3f getAmbient() {
		return ambient;
	}

	public Vector3f getDiffuse() {
		return diffuse;
	}

	public Vector3f getSpecular() {
		return specular;
	}

	public Vector3f getTransmission() {
		return transmission;
	}

	public int getAmbientID() {
		return ambientID;
	}

	public int getDiffuseID() {
		return diffuseID;
	}

	public int getSpecularID() {
		return specularID;
	}

	public int getSpecularExponentID() {
		return specularExponentID;
	}

	public int getDissolveID() {
		return dissolveID;
	}

	public float getDissolve() {
		return dissolve;
	}

	public float getSpecularExponent() {
		return specularExponent;
	}

	public void delete(){
		GL11C.glDeleteTextures(ambientID);
		GL11C.glDeleteTextures(diffuseID);
		GL11C.glDeleteTextures(specularID);
		GL11C.glDeleteTextures(specularExponentID);
		GL11C.glDeleteTextures(dissolveID);
	}
	
}
