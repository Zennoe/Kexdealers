package textures;

import org.lwjgl.opengl.GL11;

public class Texture2D {
	
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
