package ecs;

public class FPPCameraComponent extends Component{
	
	private int eID;
	
	public FPPCameraComponent(int eID) {
		this.eID = eID;
	}
	
	public int getEID() {
		return eID;
	}

	public void setEID(int eID) {
		this.eID = eID;
	}

}
