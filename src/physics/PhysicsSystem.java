package physics;

import org.joml.Vector3f;
import ecs.EntityController;
import ecs.PhysicsComponent;
import ecs.Transformable;
import terrain.Terrain;

public class PhysicsSystem {

	private EntityController entityController;

	// TODO move me to somewhere else
	private final Vector3f gravitationalAccel = new Vector3f(0, -9.8f, 0);

	public PhysicsSystem(EntityController entityController) {
		this.entityController = entityController;
	}

	public void run(double timeDelta, Terrain terrain) {
		// process all physics components
		for (PhysicsComponent currentComp : entityController.getPhysicsComponents()) {
			Transformable currTrans = entityController.getTransformable(currentComp.getEID());
			// TODO Implement a proper collision system.
			// collision check using terrain height
			float currHeight = terrain.getHeightAtPoint(currTrans.getPosition().x, currTrans.getPosition().z);
			if (Float.compare(currHeight, currTrans.getPosition().y) > 0) {
				currentComp.setOnGround(true);
				Vector3f pos = currTrans.getPosition();
				Vector3f vel = currentComp.getVelocity();
				pos.y = currHeight;
				vel.y = 0.0f;
				currTrans.setPosition(pos);
				currentComp.setVelocity(vel);
			} else {
				currentComp.setOnGround(false);
			}
			
			if (currentComp.isAffectedByPhysics()) {
				// update gravity
				if (currentComp.isAffectedByGravity()) {
					currentComp.applyForce("gravity", (new Vector3f(gravitationalAccel)).mul(currentComp.getWeight()));
				} else {
					currentComp.removeForce("gravity");
				}
				
				// update acceleration
				Vector3f resultingForce = new Vector3f();
				for (Vector3f currForce : currentComp.getAppliedForces()) {
					resultingForce.add(currForce);
				}
				Vector3f newAccel = resultingForce.div(currentComp.getWeight());
				currentComp.setAcceleration(newAccel);

				// update velocity
				Vector3f newVeloc = (new Vector3f(currentComp.getVelocity())).add(newAccel.mul((float) timeDelta));
				if (currentComp.isOnGround()) {
					// simulate friction (too simple.)
					// TODO rewrite this.
					final float frictionFactor = 0.99f;
					newVeloc.mul(frictionFactor);
				}
				currentComp.setVelocity(newVeloc);

				// update position
				currTrans.increasePosition(newVeloc.mul((float) timeDelta));
			}
		}
	}
}
