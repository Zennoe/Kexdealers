package textures;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11C;

public class Material {

	public static final Vector4f DEFAULT_COLOR = new Vector4f(0.5f, 0.0f, 0.5f, 1.0f);
	
	private Vector4f ambient = DEFAULT_COLOR;
	private Vector4f diffuse = DEFAULT_COLOR;
	private Vector4f specular = DEFAULT_COLOR;
	
	private int diffuseID;
	private int specularID;
	private float shininess;
	private int width;
	private int height;
	
	public Material(Vector4f ambient, Vector4f diffuse, Vector4f specular, float shininess) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}
	
	// old constructor
	public Material(int diffuseID, int specularID, float shininess, int width, int height) {
		this.diffuseID = diffuseID;
		this.specularID = specularID;
		this.shininess = shininess;
		this.width = width;
		this.height = height;
	}

	public int getDiffuseID(){
		return diffuseID;
	}

	public Material setDiffuseID(int diffuseID) {
		this.diffuseID = diffuseID;
		return this;
	}
	
	public int getSpecularID(){
		return specularID;
	}
	
	public Material setSpecularID(int specularID) {
		this.specularID = specularID;
		return this;
	}
	
	public float getShininess(){
		return shininess;
	}
	
	public Material setShininess(float shininess) {
		this.shininess = shininess;
		return this;
	}
	
	public void delete(){
		GL11C.glDeleteTextures(diffuseID);
		GL11C.glDeleteTextures(specularID);
	}
	
}
