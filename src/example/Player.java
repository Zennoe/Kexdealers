package example;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;

public class Player {
	
	private Vector3f cameraOffset = new Vector3f(0.0f, 10.0f, 0.0f);
	
	private EntityController entityController;
	
	private float walkSpeed = 30.0f, midAirSpeed = 10.0f;
	private float jumpInitAccel = 50.0f;
	
	public Player(EntityController entityController){
		this.entityController = entityController;
	}
	
	public void update(int eID, float delta){
		Transformable transformable = entityController.getTransformable(eID);
		// Rotation
		float yaw = (float) Math.toRadians(Display.getMouseX() * 5.0f * delta);
		float pitch = (float) Math.toRadians(Display.getMouseY() * 5.0f * delta);
		transformable.rotate(0.0f, (float) Math.toDegrees(-yaw), 0.0f);

		// Translation
		// Move the player around horizontally.
		
		Vector2f inputDir = pollInputDir();
		
		Vector3f moveDirVec = new Vector3f();
		moveDirVec.x = inputDir.x;
		moveDirVec.z = inputDir.y;
		moveDirVec.y = 0.0f;
		moveDirVec.mul(walkSpeed * delta);
		moveDirVec.rotate(transformable.getRotation());
		
		transformable.increasePosition(moveDirVec);
	
		// Camera update
		FPPCameraComponent camera = entityController.getFPPCameraComponent(eID);
		camera.rotateYaw(yaw);
		camera.rotatePitch(pitch);
		Vector3f newCamPos = new Vector3f(transformable.getPosition());
		newCamPos.add(cameraOffset, newCamPos);
		//newCamPos.y = 0.0f;
		camera.setPosition(newCamPos);
		camera.changeFOV((float) (100.0f * Display.getMouseScroll() * delta));
	}
	
	private Vector2f pollInputDir() {
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
