package bus;

import org.joml.Vector3f;

public class TeleportationSysMessage extends BusMessage{

	private final Operation op;
	private final int targetEID;
	private final Vector3f destination;
	
	public TeleportationSysMessage(Operation op, int targetEID, Vector3f destination) {
		this.op = op;
		this.targetEID = targetEID;
		this.destination = destination;
	}
	
	@Override
	protected void setComplete() {
		super.complete = true;
	}
	
	@Override
	protected boolean isComplete() {
		return super.complete;
	}

	public Operation getOP() {
		return op;
	}
	
	public int getTargetEID() {
		return targetEID;
	}
	
	public Vector3f getDestination() {
		return destination;
	}
}
