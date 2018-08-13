package input;

import org.joml.Vector2f;

public interface InputSourceI {

	//-- actions
	
	// system
	public boolean closeGame();
	
	// movement
	public Vector2f pollMoveDirection(); // negative x: left, positive y: forward
	public boolean doJump();
	
	// look
	public Vector2f pollLookMove(); // negative x: left, positive y: up
	
	// action
	public boolean doInteract(); // interacting with world
	
	// debug
	public boolean isActivatingGravitySuck();
	public boolean doTeleport();
	public boolean toggleWireframe();
}
