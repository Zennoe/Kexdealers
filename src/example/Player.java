package example;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.PhysicsComponent;
import ecs.Transformable;
import terrain.Terrain;

public class Player {
	
	private EntityController entityController;
	
	private Vector3f moveVec = new Vector3f();
	
	//-- player params
	private final float walkSpeed = 32.5f;
	private final Vector3f jumpForce = new Vector3f(0, 90_000.0f, 0);
	private final Vector3f cameraOffset = new Vector3f(0.0f, 10.0f, 0.0f);
	private final float cameraTurnSpeed = 5.0f;
	/*
	private final Vector3f cameraFPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	private final Vector3f cameraTPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	*/
	
	//-- state tracking
	/*
	// cameraDirective indicates how the camera is supposed to be updated
	// 0: don't update	1: follow player (FP)	2: look at player
	// 3: follow player (TP)
	private int cameraDirective = 1;
	*/
	// TODO suggestion: rename FPPCameraComponent to CameraComponent
	
	public Player(EntityController entityController){
		this.entityController = entityController;
	}
	
	public void update(int eID, float delta, Terrain terrain){
		Transformable transformable = entityController.getTransformable(eID);
		PhysicsComponent physics = entityController.getPhysicsComponent(eID);
		
		//-- Poll input
		float yaw = (float) Math.toRadians(Display.getMouseX() * cameraTurnSpeed * delta);
		float pitch = (float) Math.toRadians(Display.getMouseY() * cameraTurnSpeed * delta);
		Vector2f inputDir = pollMoveInputDir();
		boolean jumping = (Display.pressedKeys[GLFW.GLFW_KEY_SPACE]);

		//-- Update player velocity and rotation
		transformable.rotateRadians(0.0f, -yaw, 0.0f);
		
		if (physics.isOnGround()) {
			// player is on ground
			physics.setOnGround(true);
			if (jumping) {
				physics.applyForce("jump", jumpForce);
			}
			moveVec.x = inputDir.x;
			moveVec.z = inputDir.y;
			moveVec.mul(walkSpeed);
			moveVec.rotate(transformable.getRotation());
		} else {
			// player is mid air
			physics.setOnGround(false);
			physics.removeForce("jump");
		}
		
		// update velocity position
		physics.setVelocity(new Vector3f(moveVec.x,
				physics.getVelocity().y(), moveVec.z));
	
		//-- Camera update
		FPPCameraComponent camera = entityController.getFPPCameraComponent(eID);
		camera.rotateYaw(yaw);
		camera.rotatePitch(pitch);
		Vector3f newCamPos = new Vector3f(transformable.getPosition());
		newCamPos.add(cameraOffset, newCamPos);
		//newCamPos.y = 0.0f;
		camera.setPosition(newCamPos);
		camera.changeFOV((float) (100.0f * Display.getMouseScroll() * delta));
	}
	
	private Vector2f pollMoveInputDir() {
		// returns a normalised direction vector pointing representing player 2D move input.
		// Input is polled from AWSD input.
		Vector2f inputDirVec = new Vector2f(); // forward: +y, left: +x
		
		if(Display.pressedKeys[GLFW.GLFW_KEY_W]){
			inputDirVec.y += 1;
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_S]){
			inputDirVec.y -= 1;
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_A]){
			inputDirVec.x += 1;
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_D]){
			inputDirVec.x -= 1;
		}
		
		if (Float.compare(inputDirVec.length(), 0) != 0) {
			inputDirVec.normalize();
		}
			
		return inputDirVec;
	}
}
