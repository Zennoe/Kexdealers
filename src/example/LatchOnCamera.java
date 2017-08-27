package example;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ecs.Transformable;
import ecs.EntityController;
import example.Display;


public class LatchOnCamera {
	
	private EntityController entityController;
	// Input parameters for the projection matrix
	private float FOV = 70.0f;
	private float nearPlane = 0.01f;
	private float farPlane = 1000.0f;
	private float aspectRatio = Display.width / Display.height;
	// Input parameters for the view matrix
	private Vector3f cameraPosition = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f negCamPos = new Vector3f(0.0f, 0.0f, 0.0f);
	@SuppressWarnings("unused")
	private float pitch, yaw, roll;
	
	private float distanceFromTarget = 30.0f;
	private float verticalOffset = 8.0f;
	
	private float renderDistance = 512.0f;
	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;	
	
	public LatchOnCamera(EntityController entityController){
		this.entityController = entityController;

		// Calculate projection matrix
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f().setPerspective((float) Math.toRadians(FOV), aspectRatio, nearPlane, farPlane);
	}
	
	public Matrix4f getViewMatrix(){
		viewMatrix
			.identity()
			.rotateX((float) Math.toRadians(pitch))
			.rotateY((float) Math.toRadians(yaw))
			.translate(cameraPosition.negate(negCamPos));
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public Vector3f getPosition(){
		return cameraPosition;
	}
	
	public float getRenderDistance(){
		return renderDistance;
	}

	public void lookAt(int eID){
		Transformable transformable = entityController.getTransformable(eID);
		// Calculate updated relative position in here
		float horizDist = (float) (distanceFromTarget * Math.cos(Math.toRadians(pitch)));
		float vertiDist = (float) (distanceFromTarget * Math.sin(Math.toRadians(pitch)));
		cameraPosition.y = transformable.getPosition().y +  vertiDist +verticalOffset;
		
		float theta =  transformable.getRotY();
		float offsetX = (float) (horizDist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDist * Math.cos(Math.toRadians(theta)));
		cameraPosition.x = transformable.getPosition().x - offsetX;
		cameraPosition.z = transformable.getPosition().z - offsetZ;
		
		yaw = (180 - theta) % 360;
		
		
	}
	
	public void reactToInput(double mouseScroll, double mouseVerti){
		distanceFromTarget -= (float) (mouseScroll * 0.5f);
		pitch += (float) (mouseVerti * 0.07f);
		if(pitch > 89){
			pitch = 89;
		}else if(pitch < -89){
			pitch = -89;
		}
	}
	
}
