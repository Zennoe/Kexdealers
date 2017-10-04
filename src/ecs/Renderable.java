package ecs;

public class Renderable  extends Component{
	
	private int eID;
	
	private String assetName = "default";
	
	public Renderable(int eID, String assetName){
		this.eID = eID;
		this.assetName = assetName;
	}
	
	public int getEID(){
		return eID;
	}
	
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	public String getAssetName(){
		return assetName;
	}
	
}
