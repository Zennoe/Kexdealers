package textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class MultiTexture {
	
	private int defaultID;
	private int rID;
	private int gID;
	private int bID;
	
	public MultiTexture(int defaultID, int rID, int gID, int bID){
		this.defaultID = defaultID;
		this.rID = rID;
		this.gID = gID;
		this.bID = bID;
	}
	
	public void bind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, defaultID);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, rID);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gID);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bID);
	}
	
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public int getDefaultID(){
		return defaultID;
	}
	
	public int getRID(){
		return rID;
	}
	
	public int getGID(){
		return gID;
	}
	
	public int getBID(){
		return bID;
	}
}
