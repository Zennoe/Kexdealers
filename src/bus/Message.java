package bus;

public abstract class Message {
	
	protected boolean complete = false;
	
	//private Systems recipient;
	//private int behaviorID;
	//private Object[] args;
	
	public abstract void setComplete();
	public abstract boolean isComplete();
}
