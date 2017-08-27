package textures;

import org.lwjgl.opengl.GL11;

public class Material {

	private int diffuseID;
	private int specularID;
	private float shininess;
	private int width;
	private int height;
	
	public Material(int diffuseID, int specularID, float shininess, int width, int height){
		this.diffuseID = diffuseID;
		this.specularID = specularID;
		this.shininess = shininess;
		this.width = width;
		this.height = height;
	}
	
	public int getDiffuseID(){
		return diffuseID;
	}
	
	public int getSpecularID(){
		return specularID;
	}
	
	public float getShininess(){
		return shininess;
	}
	
	public void delete(){
		GL11.glDeleteTextures(diffuseID);
		GL11.glDeleteTextures(specularID);
	}
	
}
