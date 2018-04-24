package ecs;

import java.util.HashMap;

import org.joml.Vector3f;

public class PhysicsComponent extends Component {

	private int eID;

	private Vector3f velocity = new Vector3f();
	private Vector3f acceleration = new Vector3f();
	private float weight = 0.0f;
	private HashMap<String, Vector3f> listOfAppliedForces = new HashMap<>();

	private boolean isAffectedByPhysics = true;
	private boolean isAffectedByGravity = false;
	private boolean isOnGround = false; // TODO replace with proper collision system

	public PhysicsComponent(int eID) {
		this.eID = eID;
	}

	@Override
	public int getEID() {
		return eID;
	}

	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public PhysicsComponent setVelocity(Vector3f velocity) {
		this.velocity.set(velocity);
		return this;
	}

	public Vector3f getAcceleration() {
		return acceleration;
	}

	public PhysicsComponent setAcceleration(Vector3f acceleration) {
		this.acceleration.set(acceleration);
		return this;
	}

	public PhysicsComponent resetVelocity() {
		velocity.set(0,0,0);
		return this;
	}
	
	public PhysicsComponent resetAcceleration() {
		acceleration.set(0,0,0);
		return this;
	}
	
	public float getWeight() {
		return weight;
	}

	public PhysicsComponent setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	public Iterable<Vector3f> getAppliedForces() {
		return listOfAppliedForces.values();
	}
	
	public Vector3f getAppliedForce(String forceName) {
		return listOfAppliedForces.get(forceName);
	}

	public Vector3f removeForce(String forceName) {
		return listOfAppliedForces.remove(forceName);
	}
	
	public PhysicsComponent applyForce(String forceName, Vector3f force) {
		listOfAppliedForces.put(forceName, force);
		return this;
	}
	
	public PhysicsComponent updateForce(String forceName, Vector3f force) {
		listOfAppliedForces.get(forceName).set(force);
		return this;
	}
	
	public PhysicsComponent increaseForce(String forceName, Vector3f forceAdditum) {
		listOfAppliedForces.get(forceName).add(forceAdditum);
		return this;
	}

	public void setForce(String forceName, Vector3f force) {
		listOfAppliedForces.get(forceName).set(force);
	}

public boolean isAffectedByPhysics() {
		return isAffectedByPhysics;
	}

	public PhysicsComponent setAffectedByPhysics(boolean isAffectedByPhysics) {
		this.isAffectedByPhysics = isAffectedByPhysics;
		return this;
	}

	public boolean isAffectedByGravity() {
		return isAffectedByGravity;
	}

	public PhysicsComponent setAffectedByGravity(boolean isAffectedByGravity) {
		this.isAffectedByGravity = isAffectedByGravity;
		return this;
	}

	public boolean isOnGround() {
		return isOnGround;
	}

	public PhysicsComponent setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
		return this;
	}

}
