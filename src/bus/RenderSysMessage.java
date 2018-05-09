package bus;

public class RenderSysMessage extends BusMessage {
	
	private final Operation op;
	
	public RenderSysMessage(Operation op) {
		this.op = op;
	}
	
	@Override
	public void setComplete() {
		super.complete = true;
	}
	
	@Override
	public boolean isComplete() {
		return super.complete;
	}
	
	public Operation getOP() {
		return op;
	}
}
