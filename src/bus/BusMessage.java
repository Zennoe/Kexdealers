package bus;

public abstract class BusMessage {
	
	protected boolean complete = false;
	
	protected abstract void setComplete();
	protected abstract boolean isComplete();
}
