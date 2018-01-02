package textures;

import org.lwjgl.opengl.GL11;

public class Texture {
	
	private int id;
	
	private int width;
	private int height;
	
	private int columns;
	private int rows;
	
	public Texture(int textureID, int width, int height){
		this.id = textureID;
		this.width = width;
		this.height = height;
		
		this.columns = 1;
		this.rows = 1;
	}
	
	public Texture(int textureID, int width, int height, int columns, int rows){
		this.id = textureID;
		this.width = width;
		this.height = height;
		
		this.columns = columns;
		this.rows = rows;
	}

	public int getID(){
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void delete() {
		GL11.glDeleteTextures(id);
	}
	
}
