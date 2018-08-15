package example;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import bus.PlayerMessage;
import bus.Systems;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.PhysicsComponent;
import ecs.Transformable;

public class Player {

	private MessageBus bus;
	private EntityController entityController;

	private Vector3f moveVec = new Vector3f();

	// -- player params
	private final float walkSpeed = 32.5f;
	private final Vector3f jumpForce = new Vector3f(0, 90_000.0f, 0);
	private final Vector3f cameraOffset = new Vector3f(0.0f, 10.0f, 0.0f);
	/*
	 * private final Vector3f cameraFPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	 * private final Vector3f cameraTPoffset = new Vector3f(0.0f, 10.0f, 0.0f);
	 */

	// -- state tracking
	/*
	 * cameraDirective indicates how the camera is supposed to be updated 0: don't
	 * update 1: follow player (FP) 2: look at player 3: follow player (TP) private
	 * int cameraDirective = 1;
	 */
	// TODO suggestion: rename FPPCameraComponent to CameraComponent

	public Player(MessageBus bus, EntityController entityController) {
		this.bus = bus;
		this.entityController = entityController;
	}

	public void update(int eID, double delta) {
		Vector2f inputMoveDir = new Vector2f();
		Vector2f inputLookDir = new Vector2f();
		boolean inputJump = false;
		boolean inputInteract = false;

		// process message bus
		PlayerMessage message;
		while((message = (PlayerMessage) bus.getNextMessage(Systems.PLAYER)) != null) {
			switch(message.getOP()) {
			case PLAYER_JUMP:
				inputJump = true;
				break;
			case PLAYER_INTERACT:
				inputInteract = true;
				break;
			case PLAYER_MOVE:
				inputMoveDir.add(message.getVector());
				break;
			case PLAYER_LOOK:
				inputLookDir.add(message.getVector());
				break;
			default: System.err.println("Player operation not implemented");
			}
		}
		
		Transformable transformable = entityController.getTransformable(eID);
		// TODO uncomment original once physics system runs again
		// PhysicsComponent physics = entityController.getPhysicsComponent(eID);
		// --workaround BEGIN
		PhysicsComponent physics = new PhysicsComponent(eID);
		physics.setOnGround(true);
		// -- workaround END

		// -- Poll input
		// ensure that input move doesn't exceed maximum move speed
		if (Float.compare(inputMoveDir.length(), 1) > 1) {
			inputMoveDir.normalize(1);
		}
		if (Float.compare(inputMoveDir.length(), -1) < -1) {
			inputMoveDir.normalize(-1);
		}
		Vector2f lookRot = new Vector2f(inputLookDir);
		lookRot.mul((float) delta);
		
		// TODO remove me
		if (inputInteract) {
			System.out.println("Player interacted");
		}

		// -- Update player velocity and rotation
		transformable.rotateRadians(0.0f, -lookRot.x(), 0.0f);

		if (physics.isOnGround()) {
			// player is on ground
			physics.setOnGround(true);
			if (inputJump) {
				physics.applyForce("jump", jumpForce);
			}
			moveVec.x = inputMoveDir.x;
			moveVec.z = inputMoveDir.y;
			moveVec.mul(walkSpeed);
			moveVec.rotate(transformable.getRotation());
		} else {
			// player is mid air
			physics.setOnGround(false);
			physics.removeForce("jump");
		}

		// update velocity position
		physics.setVelocity(new Vector3f(moveVec.x, physics.getVelocity().y(), moveVec.z));

		// -- Camera update
		FPPCameraComponent camera = entityController.getFPPCameraComponent(eID);
		if (camera != null) {
			camera.rotateYaw(lookRot.x());
			camera.rotatePitch(-lookRot.y());
			Vector3f newCamPos = new Vector3f(transformable.getPosition());
			newCamPos.add(cameraOffset, newCamPos);
			// newCamPos.y = 0.0f;
			camera.setPosition(newCamPos);
		}
	}
}
