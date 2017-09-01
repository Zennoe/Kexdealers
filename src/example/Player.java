package example;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.Transformable;

public class Player {
	
	private FPPCamera camera;
	private Vector3f cameraOffset = new Vector3f(0.0f, 15.0f, 0.0f);
	
	private EntityController entityController;
	
	private int eID;
	
	public Player(FPPCamera camera, EntityController entityController, int eID){
		this.camera = camera;
		this.entityController = entityController;
		this.eID = eID;
	}
	
	public void update(float delta){
		Transformable transformable = entityController.getTransformable(eID);
		
		// Rotation
		float yaw = (float) Math.toRadians(Display.getMouseX() * 5.0f * delta);
		float pitch = (float) Math.toRadians(Display.getMouseY() * 5.0f * delta);
		transformable.rotate(0.0f, (float) Math.toDegrees(-yaw), 0.0f);
		//cameraOffset.rotateY((float) Math.toDegrees(yaw));
		//System.out.println(transformable.getRotY());

		// Translation
		// Move the player around horizontally.
		Vector3f scaledDirVec = transformable.getDirectionVector().mul(delta * 20.0f);
		if(Display.pressedKeys[GLFW.GLFW_KEY_W]){
			transformable.increasePosition(scaledDirVec);
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_S]){
			transformable.increasePosition(scaledDirVec.negate());
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_A]){
			transformable.increasePosition(scaledDirVec.z, scaledDirVec.y, -scaledDirVec.x);
		}
		if(Display.pressedKeys[GLFW.GLFW_KEY_D]){
			transformable.increasePosition(-scaledDirVec.z, scaledDirVec.y, scaledDirVec.x);
		}
		
		// Camera update
		camera.rotateYaw(yaw);
		camera.rotatePitch(pitch);
		Vector3f newCamPos = new Vector3f(transformable.getPosition());
		newCamPos.add(cameraOffset, newCamPos);
		//newCamPos.y = 0.0f;
		camera.setPosition(newCamPos);
		
		camera.changeFOV((float) (100.0f * Display.getMouseScroll() * delta));
		
	}
}
