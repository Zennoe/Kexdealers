package example;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FPPCamera {
	
	private Vector3f cameraPosition = new Vector3f();
	private float yaw = 180.0f;
	private float pitch = 0.0f;
	private Quaternionf viewDir = new Quaternionf();
	
	private float FOV = 70.0f;
	private float nearPlane = 0.01f;
	private float farPlane = 1000.0f;
	private float aspectRatio = Display.width / Display.height;
	
	private Vector3f negCamPos = new Vector3f();
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;	
	
	public FPPCamera(){
		
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
	}
	
	public Matrix4f getViewMatrix(){
		viewMatrix
			.identity()
			.rotate(viewDir)
			//.rotateY((float) Math.toRadians(yaw))
			//.rotateX((float) Math.toRadians(pitch))
			.translate(cameraPosition.negate(negCamPos));
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix(){
		projectionMatrix
			.identity()
			.setPerspective((float) Math.toRadians(FOV), aspectRatio * 2, nearPlane, farPlane);
		return projectionMatrix;
	}
	
	public Vector3f getPosition(){
		return cameraPosition;
	}
	
	public void setPosition(Vector3f pos){
		cameraPosition = pos;
	}
	
	public void rotate(float angleX, float angleY, float angleZ){
		viewDir.rotateLocal(angleX, angleY, angleZ);
	}
	
	public void rotateYaw(float angle){
		yaw += angle;
	}
	
	public void rotatePitch(float angle){
		pitch += angle;
	}
	
	public void changeFOV(float fov){
		FOV -= fov;
	}
	
}
