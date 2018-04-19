package ecs;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class Transformable extends Component{
	
	private int eID;
	
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	
	private Quaternionf rotation = new Quaternionf().identity();
	
	private float scale = 1.0f;
	
	// vecs and mats for getters. are to be updated by their respective
	// getters
	private Matrix4f transformation = new Matrix4f();
	private Vector3f dirVec = new Vector3f();
	
	public Transformable(int eID){
		this.eID = eID;
		
		transformation = new Matrix4f().identity();
	}
	
	public int getEID(){
		return eID;
	}
	
	public void setEID(int eID) {
		this.eID = eID;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Transformable setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	public float getRotX() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).x);
	}

	public Transformable setRotX(float rotX) {
		rotation.rotateX((float) Math.toRadians(rotX));
		return this;
	}

	public float getRotY() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).y);
	}

	public Transformable setRotY(float rotY) {
		rotation.rotateY((float) Math.toRadians(rotY));
		return this;
	}

	public float getRotZ() {
		Vector3f euler = new Vector3f();
		return (float) Math.toRadians(rotation.getEulerAnglesXYZ(euler).z);
	}

	public Transformable setRotZ(float rotZ) {
		rotation.rotateZ((float) Math.toRadians(rotZ));
		return this;
	}

	public float getScale() {
		return scale;
	}

	public Transformable setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	// The M in MVP :ok_hand:
	public Matrix4f getTransformation(){
		return transformation
				.identity()
				.translate(position)
				.rotate(rotation)
				.scale(scale);
	}
	
	public void increasePosition(float x, float y, float z){
		position.add(x, y, z);
	}
	
	public void increasePosition(Vector3f vec){
		position.add(vec);
	}
	
	public void rotateRadians(float angleX, float angleY, float angleZ){
		rotation.rotateLocal(angleX, angleY, angleZ);
	}
	
	public void rotate(float angleX, float angleY, float angleZ){
		rotateRadians((float) Math.toRadians(angleX),
						(float) Math.toRadians(angleY), 
						(float) Math.toRadians(angleZ));
	}
	
	public Vector3f getEulerRotation() {
		return rotation.getEulerAnglesXYZ(new Vector3f());
	}
	
	public Quaternionf getRotation() {
		return new Quaternionf(rotation);
	}
	
	public Vector3f getDirectionVector(){
		dirVec.set(0,0,1);
		dirVec.rotate(rotation);
		
		return dirVec;
	}
}
