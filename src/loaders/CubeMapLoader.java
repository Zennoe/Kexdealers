package loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

public class CubeMapLoader {
	
	public int loadCubeMap(String[] rltBoBaF){
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		
		int textureID = GL11.glGenTextures();
		//GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		// Set texture filtering
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		for(int i = 0; i < rltBoBaF.length; i++){
			ByteBuffer data = stbImageLoad(rltBoBaF[0], width, height, comp);
			widthInt = width.get();
			heightInt = height.get();
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, widthInt, heightInt, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
			
			width.clear();
			height.clear();
			comp.clear();
		}
		
		// Un-bind Texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return textureID;
	}
	
	private ByteBuffer stbImageLoad(String filename, IntBuffer width, IntBuffer height, IntBuffer comp){
		ByteBuffer data = STBImage.stbi_load("res/" +filename +".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() +" -> " +filename);
		}
		return data;
	}
}
