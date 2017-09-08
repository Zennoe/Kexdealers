package textures;

public class MultiTexture {
	
	private int defaultID;
	private int rID;
	private int gID;
	private int bID;
	
	private int defaultID_normal;
	private int rID_normal;
	private int gID_normal;
	private int bID_normal;
	
	public MultiTexture(int defaultID, int rID, int gID, int bID,
			int defaultID_normal, int rID_normal, int gID_normal, int bID_normal){
		this.defaultID = defaultID;
		this.rID = rID;
		this.gID = gID;
		this.bID = bID;
		
		this.defaultID_normal = defaultID_normal;
		this.rID_normal = rID_normal;
		this.gID_normal = gID_normal;
		this.bID_normal = bID_normal;
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
	
	public int getNormalDefaultID(){
		return defaultID_normal;
	}
	
	public int getNormalRID(){
		return rID_normal;
	}
	
	public int getNormalGID(){
		return gID_normal;
	}
	
	public int getNormalBID(){
		return bID_normal;
	}
}
