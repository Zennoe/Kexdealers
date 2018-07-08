package bus;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public class RenderSysMessage extends Message {
	
	private final Operation op;
	
	private final Vector3fc posBegin, posEnd, colour;
	private final double time;
	
	public RenderSysMessage(Operation op) {
		this.op = op;
		posBegin = posEnd = colour = new Vector3f();
		time = 0.0d;
	}
	
	public RenderSysMessage(Operation op, Vector3fc lineBegin, Vector3fc lineEnd, Vector3fc lineColour, double lifeTime) {
		this.op = op;
		posBegin = lineBegin;
		posEnd = lineEnd;
		colour = lineColour;
		time = lifeTime;
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

	public Vector3fc getPosBegin() {
		return posBegin;
	}

	public Vector3fc getPosEnd() {
		return posEnd;
	}

	public Vector3fc getColour() {
		return colour;
	}

	public double getTime() {
		return time;
	}
}
