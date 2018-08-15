package input;

import org.joml.Vector2f;

public interface InputSourceI {

	//-- actions
	
	// system
	default boolean closeGame() { return false; }
	
	// movement
	default Vector2f pollMoveDirection() { // negative x: left, positive y: forward. clamped to [-1,1]
		return new Vector2f();
	}
	default public boolean doJump() { return false; }
	
	// look
	default Vector2f pollLookMove() { // negative x: left, positive y: up
		return new Vector2f();
	}
	default float getLookSensitivity() { return 1; }
	
	// action
	default boolean doInteract() {return false; } // interacting with world
	
	// debug
	default boolean isActivatingGravitySuck() { return false; }
	default boolean doTeleport() { return false; }
	default boolean toggleWireframe() { return false; }
}
