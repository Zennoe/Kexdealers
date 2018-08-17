package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.joml.Vector3f;

import bus.MessageBus;
import bus.Systems;
import bus.TeleportationSysMessage;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;
import loaders.BlueprintLoader;

public class TeleportationSystem extends AbstractSystem {

	private HashMap<String, Teleportation> teleportations;

	public TeleportationSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);

		teleportations = new HashMap<>();
	}

	@Override
	public void run() {
		// control update rate here

		// update :)
		update();

		// cleanUp();
	}

	@Override
	protected void update() {
		super.timeMarkStart();

		// work message queue
		TeleportationSysMessage message;
		while ((message = (TeleportationSysMessage) messageBus.getNextMessage(Systems.TELEPORTATION_SYSTEM)) != null) {
			switch (message.getOP()) {
			case SYS_TELEPORTATION_TARGETCOORDS:
				teleportTo(message.getTargetEID(), message.getDestination());
				break;
			default:
				System.err.println("Teleportation operation not implemented");
			}
		}
		// for all player entities,
		// check all teleportations

		for (FPPCameraComponent player : entityController.getFPPCameraComponents()) {
			for (Teleportation tp : teleportations.values()) {
				Transformable transformable = entityController.getTransformable(player.getEID());
				if (tp.checkTrigger(transformable)) {
					teleportTo(player.getEID(), tp.getDestination());
				}
			}
		}

		super.timeMarkEnd();
	}

	@Override
	protected void cleanUp() {

	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		ArrayList<String> teleLines = BlueprintLoader.getAllLinesWith("TELEPORTATION", blueprint);

		for (String teleLine : teleLines) {
			try {
				// extract data and add teleportation
				String[] frags = BlueprintLoader.getDataFragments(teleLine);
				teleportations.put(frags[0], new Teleportation(frags[0], // name
						new Vector3f(Float.valueOf(frags[1]), Float.valueOf(frags[2]), Float.valueOf(frags[3])), // destination
						new Vector3f(Float.valueOf(frags[4]), Float.valueOf(frags[5]), Float.valueOf(frags[6])), // triggerLocation
						Float.valueOf(frags[7]))); // triggerradius

			} catch (NullPointerException | IndexOutOfBoundsException e) {
				System.err.println("Teleportations: couldn't load teleportation. Too few arguments.");
			} catch (IllegalArgumentException e) {
				System.err.printf("Teleportations: couldn't load teleportation. %s%n", e.toString());
			}
		}
		
		//System.out.println(teleportations.toString());
	}

	public Set<String> getAllTeleportations() {
		return teleportations.keySet();
	}

	private void teleportTo(int targetEID, Vector3f destination) {
		// > fancy effects <
		// wheeeeeee~~~
		entityController.getTransformable(targetEID).setPosition(destination);
		if (entityController.getPhysicsComponent(targetEID) != null) {
			entityController.getPhysicsComponent(targetEID).resetVelocity();
		}
	}
}