package ecs;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformable {
	
	private int eID;
	
	private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	
	private float rotX = 0.0f;
	private float rotY = 0.0f;
	private float rotZ = 0.0f;
	
	private float scale = 1.0f;
	
	private Matrix4f transformation = new Matrix4f();
	
	private Vector3f direction = new Vector3f(1.0f, 0.0f, 1.0f);

	public Transformable(int eID){
		this.eID = eID;
		
		transformation = new Matrix4f()
			.translate(position)
			.rotateX((float) Math.toRadians(rotX))
			.rotateY((float) Math.toRadians(rotY))
			.rotateZ((float) Math.toRadians(rotZ))
			.scale(scale);
		
	}
	
	public int getEID(){
		return eID;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Transformable setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	public float getRotX() {
		return rotX;
	}

	public Transformable setRotX(float rotX) {
		this.rotX = rotX;
		return this;
	}

	public float getRotY() {
		return rotY;
	}

	public Transformable setRotY(float rotY) {
		this.rotY = rotY;
		return this;
	}

	public float getRotZ() {
		return rotZ;
	}

	public Transformable setRotZ(float rotZ) {
		this.rotZ = rotZ;
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
				.rotateX((float) Math.toRadians(rotX))
				.rotateY((float) Math.toRadians(rotY))
				.rotateZ((float) Math.toRadians(rotZ))
				.scale(scale);
	}
	
	public void increasePosition(float x, float y, float z){
		position.add(x, y, z);
	}
	
	public void increasePosition(Vector3f vec){
		position.add(vec);
	}
	
	public void rotate(float angleX, float angleY, float angleZ){
		rotX += angleX;
		rotY += angleY;
		rotZ += angleZ;
	}
	
	public Vector3f getDirectionVector(){
		direction.set(
			(float) Math.sin(Math.toRadians(rotY)),
			0.0f,
			(float) Math.cos(Math.toRadians(rotY))
		);
		return direction.normalize();
	}
}
