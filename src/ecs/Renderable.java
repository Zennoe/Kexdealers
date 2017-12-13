package ecs;

public class Renderable  extends Component{
	
	private int eID;
	
	private String assetName = "default";
	
	public Renderable(int eID){
		this.eID = eID;
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
	
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
}
