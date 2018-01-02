package loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import textures.Material;
import textures.MultiTexture;
import textures.Texture;

public class MaterialLoader {

	private ArrayList<Integer> textures = new ArrayList<>();
	
	public Material loadMaterial(String filename, float shininess){
		Material newMaterial = new Material();
		newMaterial.setTexture(loadTexture(filename +"_diffuse", true));
		newMaterial.setSpecularMap(loadTexture(filename +"_specular", false));
		return newMaterial;
	}
	
	public MultiTexture loadMultiTexture(String defTexture, String rTex, String gTex, String bTex,
			String defTextureN, String rTexN, String gTexN, String bTexN){
		Texture def = loadTexture(defTexture, true);
		Texture r = loadTexture(rTex, true);
		Texture g = loadTexture(gTex, true);
		Texture b = loadTexture(bTex, true);
		// Normal Maps
		Texture def_n = loadTexture(defTextureN, false);
		Texture r_n = loadTexture(rTexN, false);
		Texture g_n = loadTexture(gTexN, false);
		Texture b_n = loadTexture(bTexN, false);
		return new MultiTexture(def.getID(), r.getID(), g.getID(), b.getID(),
				def_n.getID(), r_n.getID(), g_n.getID(), b_n.getID());
	}
	
	public Texture loadBlendMap(String filename){
		return loadTexture(filename, false);
	}
	
	private Texture loadTexture(String filename, boolean gammaCorrected){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load("res/" +filename +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11.glGenTextures();
		textures.add(textureID);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		// Set texture filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		if(gammaCorrected){
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL21.GL_SRGB, widthInt, heightInt, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		}else{
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, widthInt, heightInt, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		}
		// Generate Mipmaps
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.0f);
		// Anisotropic Filtering - Availability not checked. May not be supported.
		float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		// Un-bind Texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return new Texture(textureID, widthInt, heightInt);
	}
	
	public void cleanUp(){
		for(int texture : textures){
			GL11.glDeleteTextures(texture);
		}
	}
}
