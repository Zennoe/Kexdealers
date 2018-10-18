package loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL12C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.stb.STBImage;

public class CubeMapLoader {
	
	public int loadCubeMap(String[] rltBoBaF){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		
		int textureID = GL11C.glGenTextures();
		//GL13C.glActiveTexture(GL13C.GL_TEXTURE0);
		GL11C.glBindTexture(GL13C.GL_TEXTURE_CUBE_MAP, textureID);
		GL11C.glTexParameteri(GL13C.GL_TEXTURE_CUBE_MAP, GL11C.GL_TEXTURE_WRAP_S, GL12C.GL_CLAMP_TO_EDGE);
		GL11C.glTexParameteri(GL13C.GL_TEXTURE_CUBE_MAP, GL11C.GL_TEXTURE_WRAP_T, GL12C.GL_CLAMP_TO_EDGE);
		// Set texture filtering
		GL11C.glTexParameteri(GL13C.GL_TEXTURE_CUBE_MAP, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR);
		GL11C.glTexParameteri(GL13C.GL_TEXTURE_CUBE_MAP, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR);
		for(int i = 0; i < rltBoBaF.length; i++){
			ByteBuffer data = stbImageLoad(rltBoBaF[i], width, height, comp);
			widthInt = width.get();
			heightInt = height.get();
			GL11C.glTexImage2D(GL13C.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11C.GL_RGBA, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
			
			width.clear();
			height.clear();
			comp.clear();
		}
		
		// Un-bind Texture
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		return textureID;
	}
	
	private ByteBuffer stbImageLoad(String filename, IntBuffer width, IntBuffer height, IntBuffer comp){
		ByteBuffer data = STBImage.stbi_load("res/skybox/" +filename +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		return data;
	}
}
