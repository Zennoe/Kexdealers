package bus;

public abstract class BusMessage {
	
	protected boolean complete = false;
	
	public abstract void setComplete();
	public abstract boolean isComplete();
}
