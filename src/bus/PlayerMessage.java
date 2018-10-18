package bus;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class PlayerMessage extends Message {
	
	private final Operation op;
	private final Vector2f vec;

	public PlayerMessage(Operation op) {
		this.op = op;
		this.vec = new Vector2f();
	}

	public PlayerMessage(Operation op, Vector2f vec) {
		this.op = op;
		this.vec = vec;
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
	
	public Vector2fc getVector() {
		return vec;
	}

}
