package ecs;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FPPCameraComponent extends Component{
	
	private int eID;
	
	private Vector3f cameraPosition = new Vector3f();
	private Quaternionf viewDir = new Quaternionf();
	// viewDir.rotateY((float) Math.toRadians(180));
	
	// Make settable if video options ever come around
	private float FOV = 70.0f;
	private float nearPlane = 0.001f;
	private float farPlane = 3000.0f;
	private float aspectRatio = 1920 / 1080;
	
	private Vector3f negCamPos = new Vector3f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	
	public FPPCameraComponent(int eID) {
		this.eID = eID;
	}
	
	public int getEID() {
		return eID;
	}

	public void setEID(int eID) {
		this.eID = eID;
	}
	
	public Matrix4f getViewMatrix(){
		viewMatrix
			.identity()
			.rotate(viewDir)
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
		viewDir.rotateY(angle);
	}
	
	public void rotatePitch(float angle){
		viewDir.rotateLocalX(angle);
	}
	
	public void changeFOV(float fov){
		FOV -= fov;
	}

}
