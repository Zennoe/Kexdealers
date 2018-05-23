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
	
	private class Texture2D{
		private int id;
		private int width;
		private int height;
		
		public Texture2D(int id, int width, int height){
			this.id = id;
			this.width = width;
			this.height = height;
		}

		public int getID(){
			return id;
		}

		public int getWidth(){
			return width;
		}

		public int getHeight(){
			return height;
		}
		
		public void delete(){
			GL11.glDeleteTextures(id);
		}
	}
	
	private ArrayList<Integer> textures = new ArrayList<>();
	
	public Material loadMaterial(String filename, float shininess){
		Texture2D diffuse = loadTexture2D(filename +"_diffuse", true);
		Texture2D specular = loadTexture2D(filename +"_specular", false);
		return new Material(diffuse.getID(), specular.getID(), shininess, diffuse.getWidth(), diffuse.getHeight());
	}
	
	public MultiTexture loadMultiTexture(String defTexture, String rTex, String gTex, String bTex,
			String defTextureN, String rTexN, String gTexN, String bTexN){
		Texture2D def = loadTexture2D(defTexture, true);
		Texture2D r = loadTexture2D(rTex, true);
		Texture2D g = loadTexture2D(gTex, true);
		Texture2D b = loadTexture2D(bTex, true);
		// Normal Maps
		Texture2D def_n = loadTexture2D(defTextureN, false);
		Texture2D r_n = loadTexture2D(rTexN, false);
		Texture2D g_n = loadTexture2D(gTexN, false);
		Texture2D b_n = loadTexture2D(bTexN, false);
		return new MultiTexture(def.getID(), r.getID(), g.getID(), b.getID(),
				def_n.getID(), r_n.getID(), g_n.getID(), b_n.getID());
	}
	
	public Texture loadBlendMap(String filename){
		return new Texture(loadTexture2D(filename, false).getID());
	}
	
	private Texture2D loadTexture2D(String filename, boolean gammaCorrected){
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
		
		return new Texture2D(textureID, widthInt, heightInt);
	}
	
	public void cleanUp(){
		for(int texture : textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
}
