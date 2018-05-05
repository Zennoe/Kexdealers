package physics;

import org.joml.Vector3f;

import bus.MessageBus;
import ecs.EntityController;
import ecs.PhysicsComponent;
import ecs.Transformable;
import example.AbstractSystem;
import terrain.Terrain;

public class PhysicsSystem extends AbstractSystem {

	private static final Vector3f G_FORCE = new Vector3f(0, -98.1f, 0);

	public PhysicsSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}

	public void run(double timeDelta, Terrain terrain) {
		// process all physics components
		for (PhysicsComponent currentComp : entityController.getPhysicsComponents()) {
			Transformable currTrans = entityController.getTransformable(currentComp.getEID());
			// TODO Implement a proper collision system.
			// collision check using terrain height
			float currHeight = terrain.getHeightAtPoint(currTrans.getPosition().x(), currTrans.getPosition().z());
			if (Float.compare(currHeight, currTrans.getPosition().y()) > 0) {
				currentComp.setOnGround(true);
				Vector3f pos = new Vector3f();
				currTrans.getPosition().add(0.0f, currHeight, 0.0f, pos);
				Vector3f vel = new Vector3f(currentComp.getVelocity());
				vel.setComponent(1, 0.0f);
				currTrans.setPosition(pos);
				currentComp.setVelocity(vel);
			} else {
				currentComp.setOnGround(false);
			}
			
			if (currentComp.isAffectedByPhysics()) {
				// update gravity
				if (currentComp.isAffectedByGravity()) {
					currentComp.applyForce("gravity", (new Vector3f(G_FORCE)).mul(currentComp.getWeight()));
				} else {
					currentComp.removeForce("gravity");
				}
				
				// update acceleration
				Vector3f resultingForce = new Vector3f();
				for (Vector3f currForce : currentComp.getAppliedForces()) {
					resultingForce.add(currForce);
				}
				if (currentComp.isOnGround()) {
					// simulate friction (too simple.)
					// TODO rewrite this.
					final float frictionFactor = 0.2f;
					Vector3f frictionForce = new Vector3f(currentComp.getVelocity());
					frictionForce.y = 0;
					frictionForce.mul(-1.0f/frictionFactor);
					resultingForce.add(frictionForce);
				}
				Vector3f newAccel = resultingForce.div(currentComp.getWeight());
				currentComp.setAcceleration(newAccel);

				// update velocity
				Vector3f newVeloc = (new Vector3f(currentComp.getVelocity())).add(newAccel.mul((float) timeDelta));
				currentComp.setVelocity(newVeloc);

				// update position
				currTrans.increasePosition(newVeloc.mul((float) timeDelta));
			}
		}
	}
	
	public void update() {
		
	}
	
	public void cleanUp() {
		
	}
}
