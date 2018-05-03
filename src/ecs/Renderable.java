package ecs;

public class Renderable extends Component {
	
	private int eID;
	
	private String assetName = "default";
	
	public Renderable(int eID){
		this.eID = eID;
	}
	
	@Override
	public int getEID(){
		return eID;
	}
	
	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	@Override
	public Renderable clone() {
		Renderable deepCopy = new Renderable(this.eID)
				.setAssetName(this.assetName);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Renderable<").append(eID).append(">");
		s.append("(");
		s.append(" RES: ").append(assetName);
		s.append(" )");
		return s.toString();
	}
	
	public String getAssetName(){
		return assetName;
	}
	
	public Renderable setAssetName(String assetName) {
		this.assetName = assetName;
		return this;
	}
	
}
