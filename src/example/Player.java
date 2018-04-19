package example;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;
import terrain.Terrain;

public class Player {
	
	private EntityController entityController;
	
	private Vector3f playerPrevPos = new Vector3f();
	private Vector3f moveDirVec = new Vector3f();
	private Vector3f previousMoveDir = new Vector3f();
	private float playerSpeedY = 0.0f, playerAccelY = 0.0f;
	
	//-- player params
	private final float walkSpeed = 35.0f, midAirSpeed = 10.0f;
	private final float jumpInitSpeed = 40.0f;
	private final float gravity = -7.0f;
	private final float playerWeight = 12.0f;
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
		
		playerAccelY = gravity * playerWeight;
		
		//-- Poll input
		float yaw = (float) Math.toRadians(Display.getMouseX() * cameraTurnSpeed * delta);
		float pitch = (float) Math.toRadians(Display.getMouseY() * cameraTurnSpeed * delta);
		Vector2f inputDir = pollMoveInputDir();
		boolean jumping = (Display.pressedKeys[GLFW.GLFW_KEY_SPACE]);

		//-- Update player position and rotation
		
		transformable.rotateRadians(0.0f, -yaw, 0.0f);
		
		Vector3f transPos = transformable.getPosition();
		if (transPos.y <= terrain.getHeightAtPoint(transPos.x, transPos.z)) {
			// player is on ground
			moveDirVec.x = inputDir.x;
			moveDirVec.z = inputDir.y;
			moveDirVec.mul(walkSpeed * delta);
			moveDirVec.rotate(transformable.getRotation());
			previousMoveDir.set(moveDirVec);
			
			playerSpeedY = (jumping) ? jumpInitSpeed : 0;
			// clamp player to ground
			transPos.y = terrain.getHeightAtPoint(transPos.x, transPos.z) - 0.01f;
			
		} else {
			// player is mid air
			moveDirVec.x = previousMoveDir.x;
			moveDirVec.z = previousMoveDir.z;
			
			playerSpeedY += playerAccelY * delta;
		}
		
		moveDirVec.y = playerSpeedY * delta;
		
		// update player position
		playerPrevPos.set(transformable.getPosition());
		transformable.increasePosition(moveDirVec);
	
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
