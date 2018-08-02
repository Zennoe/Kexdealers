package example;

import org.joml.Vector3f;
import ecs.Transformable;

public class Teleportation {

	private final String name;
	private final Vector3f destination;

	private final Vector3f triggerLocation;
	private final float triggerRadius;

	public Teleportation(String name, Vector3f destination, Vector3f triggerLocation, float triggerRadius) {
		this.name = name;
		this.destination = destination;
		this.triggerLocation = triggerLocation;
		this.triggerRadius = triggerRadius;
	}

	public boolean checkTrigger(Transformable transformable) {
		return (triggerRadius * triggerRadius) >= transformable.getPosition().distanceSquared(triggerLocation);
	}

	public String getName() {
		return name;
	}

	public Vector3f getDestination() {
		Vector3f _destination = new Vector3f(destination);
		return _destination;
	}
	
	@Override
	public String toString() {
		return String.format("Name: %s Destination %s TriggerLocation: %s TriggerRadius: %f",
				name, destination.toString(), triggerLocation.toString(), triggerRadius);
	}

}