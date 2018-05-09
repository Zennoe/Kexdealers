package bus;

public class NetworkSysMessage extends BusMessage {

	private final Operation op;
	private final Object content;
	
	public NetworkSysMessage(Operation op, Object content) {
		this.op = op;
		this.content = content;
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
	
	public Object getContent() {
		return content;
	}

}
