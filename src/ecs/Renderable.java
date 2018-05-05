package ecs;

public class Renderable extends Component {
	
	private int eID;
	
	private String assetName = "default";
	
	// unused right now
	private String modelName = "default";
	private String materialName = "default";
	
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
				.setAssetName(this.assetName)
				.setModelName(this.modelName)
				.setMaterialName(this.materialName);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Renderable<").append(eID).append(">");
		s.append("(");
		s.append(" RES: ").append(assetName);
		s.append(" 3D: ").append(modelName);
		s.append(" MAT: ").append(materialName);
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
	
	public String getModelName() {
		return modelName;
	}
	
	public Renderable setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public Renderable setMaterialName(String materialName) {
		this.materialName = materialName;
		return this;
	}
	
}
