package bus;

public class CharacterSheetMessage extends Message {
	
	private final Operation op;
	private final Object content;
	
	public CharacterSheetMessage(Operation op, Object content) {
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
