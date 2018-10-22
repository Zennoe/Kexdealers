package loaders;

import java.io.IOError;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL14C;
import org.lwjgl.opengl.GL21C;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.stb.STBImage;

import com.mokiat.data.front.parser.MTLColor;
import com.mokiat.data.front.parser.MTLMaterial;

import textures.Material;
import textures.MultiTexture;
import textures.Texture;
import utility.MathUtil;

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
			GL11C.glDeleteTextures(id);
		}
	}
	
	private ArrayList<Integer> textures = new ArrayList<>();
	
	private Texture2D defaultDiffuse;
	private Texture2D defaultAmbient;
	private Texture2D defaultNormal;
	private Texture2D defaultSpecular;
	private Texture2D defaultSpecularExponent;
	private Texture2D defaultDissolve;
	
	public MaterialLoader() {
		defaultDiffuse = generateOnePixelTexture2D((byte) 0xce, (byte) 0x1e, (byte) 0xff);
		defaultAmbient = generateOnePixelTexture2D((byte) 0, (byte) 0, (byte) 0);
		defaultNormal = generateOnePixelTexture2D((byte) 0x80, (byte) 0x80, (byte) 0xff);
		defaultSpecular = generateOnePixelTexture2D((byte) 0x00, (byte) 0x00, (byte) 0x00);
		defaultSpecularExponent = generateOnePixelTexture2D((byte) 0x00, (byte) 0x00, (byte) 0x00);
		defaultDissolve = generateOnePixelTexture2D((byte) 0x00, (byte) 0x00, (byte) 0x00);
	}
	
	public Material loadMaterial(MTLMaterial material, String normal_filename) {
		String name = material.getName();
		// Colors
		MTLColor color = material.getAmbientColor();
		Vector3f ambient = new Vector3f(color.r, color.g, color.b);
		color = material.getDiffuseColor();
		Vector3f diffuse = new Vector3f(color.r, color.g, color.b);
		color = material.getSpecularColor();
		Vector3f specular = new Vector3f(color.r, color.g, color.b);
		color = material.getTransmissionColor();
		Vector3f transmission = new Vector3f(color.r, color.g, color.b);
		// Textures
		int diffuseID;
		try {
			diffuseID = loadTexture(material.getDiffuseTexture(), true);
		} catch (IOException e) {
			diffuseID = defaultDiffuse.getID();
			e.printStackTrace();
		}
		int ambientID;
		try {
			ambientID = loadTexture(material.getAmbientTexture(), false);
		} catch (IOException e) {
			ambientID = defaultAmbient.getID();
			e.printStackTrace();
		}
		int normalID;
		try {
			normalID = loadTexture(normal_filename, false);
		} catch (IOException e) {
			normalID = defaultNormal.getID();
			e.printStackTrace();
		}
		int specularID;
		try {
			specularID = loadTexture(material.getSpecularTexture(), false);
		} catch (IOException e) {
			specularID = defaultSpecular.getID();
			e.printStackTrace();
		}
		int specularExponentID;
		try {
			specularExponentID = loadTexture(material.getSpecularExponentTexture(), false);
		} catch (IOException e) {
			specularExponentID = defaultSpecularExponent.getID();
			e.printStackTrace();
		}
		int dissolveID;
		try {
			dissolveID = loadTexture(material.getDissolveTexture(), false);
		} catch (IOException e) {
			dissolveID = defaultDissolve.getID();
			e.printStackTrace();
		}
		// Parameters
		float dissolve = material.getDissolve();
		float specularExponent = material.getSpecularExponent();
		
		return new Material(name, ambient, diffuse, specular, transmission,
				ambientID, normalID, diffuseID, specularID, specularExponentID, dissolveID,
				dissolve, specularExponent);
	}
	
	public Material loadMaterial(String filename, float shininess){
		Texture2D diffuse;
		try {
			diffuse = loadTexture2D(filename +"_diffuse", true);
		} catch (IOException e) {
			diffuse = defaultDiffuse;
			e.printStackTrace();
		}
		Texture2D specular;
		try {
			specular = loadTexture2D(filename +"_specular", false);
		} catch (IOException e) {
			specular = defaultSpecular;
			e.printStackTrace();
		}
		return new Material(diffuse.getID(), specular.getID(), shininess, diffuse.getWidth(), diffuse.getHeight());
	}
	
	public MultiTexture loadMultiTexture(String defTexture, String rTex, String gTex, String bTex,
			String defTextureN, String rTexN, String gTexN, String bTexN){
		Texture2D def;
		Texture2D r;
		Texture2D g;
		Texture2D b;
		try {
			def = loadTexture2D(defTexture, true);
			r = loadTexture2D(rTex, true);
			g = loadTexture2D(gTex, true);
			b = loadTexture2D(bTex, true);
		} catch (IOException e) {
			def = r = g = b = defaultDiffuse;
			e.printStackTrace();
		}
		// Normal Maps
		Texture2D def_n;
		Texture2D r_n;
		Texture2D g_n;
		Texture2D b_n;
	try {
			def_n = loadTexture2D(defTextureN, true);
			r_n = loadTexture2D(rTexN, true);
			g_n = loadTexture2D(gTexN, true);
			b_n = loadTexture2D(bTexN, true);
		} catch (IOException e) {
			def_n = r_n = g_n = b_n = defaultNormal;
			e.printStackTrace();
		}
		return new MultiTexture(def.getID(), r.getID(), g.getID(), b.getID(),
				def_n.getID(), r_n.getID(), g_n.getID(), b_n.getID());
	}
	
	public Texture loadBlendMap(String filename){
		try {
			return new Texture(loadTexture2D(filename, false).getID());
		} catch (IOException e) {
			e.printStackTrace();
			return new Texture(defaultDiffuse.getID());
		}
	}
	
	private Texture2D loadTexture2D(String filename, boolean gammaCorrected) throws IOException{
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load("res/" +filename +".png", width, height, comp, 4);
		if(data == null){
			throw new IOException(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11C.glGenTextures();
		// textures.add(textureID);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
		// Set texture filtering
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		if(gammaCorrected){
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL21C.GL_SRGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}else{
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}
		// Generate Mipmaps
		GL30C.glGenerateMipmap(GL11C.GL_TEXTURE_2D);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR_MIPMAP_LINEAR);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL14C.GL_TEXTURE_LOD_BIAS, -1.0f);
		// Anisotropic Filtering - Availability not checked. May not be supported.
		float amount = Math.min(4f, GL11C.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		// Un-bind Texture
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return new Texture2D(textureID, widthInt, heightInt);
	}
	
	private int loadTexture(String filename, boolean gammaCorrected) throws IOException{
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load(filename +".png", width, height, comp, 4);
		if(data == null){
			throw new IOException(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11C.glGenTextures();
		// textures.add(textureID);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
		// Set texture filtering
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		if(gammaCorrected){
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL21C.GL_SRGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}else{
			GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGB, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		}
		// Generate Mipmaps
		GL30C.glGenerateMipmap(GL11C.GL_TEXTURE_2D);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR_MIPMAP_LINEAR);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL14C.GL_TEXTURE_LOD_BIAS, -1.0f);
		// Anisotropic Filtering - Availability not checked. May not be supported.
		float amount = Math.min(4f, GL11C.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		// Un-bind Texture
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return textureID;
	}
	
	private Texture2D generateOnePixelTexture2D(byte r, byte g, byte b) {
		int textureID = GL11C.glGenTextures();
		
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL11C.GL_REPEAT);
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL11C.GL_REPEAT);
		// create buffer and write to texture
		byte[] byteA = { r, g, b };
		ByteBuffer data = ByteBuffer.wrap(byteA);
		data.flip();
		GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGB, 1, 1, 0, GL11C.GL_RGB, GL11C.GL_UNSIGNED_BYTE, data);
		
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return new Texture2D(textureID, 1, 1);
	}
	
	public void cleanUp(){
		for(int texture : textures){
			GL11C.glDeleteTextures(texture);
		}
	}
	
}
