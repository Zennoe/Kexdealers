package loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import textures.Texture;

public class TextureLoader {
	
	public Texture createEmptyTexture(int width, int height, int pixelFormat) {
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, pixelFormat, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		System.out.println(GL11.glGetError());
		return new Texture(id, width, height);
	}
	
	public Texture loadTexture(String fileName, boolean mipMapped, float lodBias, int anisotropicFiltering) {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load("res/" +fileName +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +fileName);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		// Set texture filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// Write buffer to buffer bound to GL_TEXTURE_2D
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, widthInt, heightInt, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		// Generate MipMaps
		if(mipMapped) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, lodBias); // -1 was used as default before
		}
		// Anisotropic Filtering - Availability not checked. May not be supported.
		if(anisotropicFiltering > 0) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		}
		// Un-bind Texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return new Texture(textureID, widthInt, heightInt);
	}
	
	// TODO: loadTextureAtlas
	public Texture loadTextureAtlas(String fileName, int columns, int rows) {
		return null;
	}
}
