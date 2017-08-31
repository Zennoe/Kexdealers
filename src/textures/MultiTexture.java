package textures;

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
