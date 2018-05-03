package ecs;

public class PlayerControllerComponent extends Component{

	private int eID;
	
	public PlayerControllerComponent(int eID) {
		this.eID = eID;
	}
	
	@Override
	public int getEID() {
		return eID;
	}

	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}

	@Override
	public PlayerControllerComponent clone() {
		PlayerControllerComponent deepCopy = new PlayerControllerComponent(this.eID);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("PlayerControllerComponent<").append(eID).append(">");
		s.append("(");
		s.append(" )");
		return s.toString();
	}
}
