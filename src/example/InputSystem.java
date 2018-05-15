package example;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import bus.Operation;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.PhysicsComponent;

public class InputSystem extends AbstractSystem {

	public InputSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}
	
	@Override
	public void run() {
		update();
	}

	@Override
	public void update() {
		super.timeMarkStart();
		
		if (Display.pressedKeys[GLFW.GLFW_KEY_B]) {
			entityController.getPhysicsComponent(6).setForce("force", new Vector3f(0,0,0));
		}
		
		if(Display.pressedKeys[GLFW.GLFW_KEY_0]) {
			Vector3f attrPos = new Vector3f(400.0f, 100.0f, 400.0f);
			for(PhysicsComponent pc : entityController.getPhysicsComponents()) {
				Vector3f attrForce = new Vector3f(); 
				entityController.getTransformable(pc.getEID()).getPosition().sub(attrPos, attrForce).mul(-40.0f);
				pc.applyForce("attractor", attrForce);
			}
			
		}
		
		if(Display.pressedKeys[GLFW.GLFW_KEY_O]){
			messageBus.messageTeleportationSys(Operation.SYS_TELEPORTATION_TARGETCOORDS, 0, new Vector3f(450.0f, 25.0f, 350.0f));
		}
		
		
		if(Display.pressedKeys[GLFW.GLFW_KEY_L]) {
			messageBus.messageRenderSys(Operation.SYS_RENDER_WIREFRAME_ON);
		} else if(Display.pressedKeys[GLFW.GLFW_KEY_N]) {
			messageBus.messageRenderSys(Operation.SYS_RENDER_WIREFRAME_OFF);
		}
		
		if (Display.pressedKeys[GLFW.GLFW_KEY_R]) {
			messageBus.messageRenderSys(Operation.SYS_RENDER_DEBUGLINES_ADDNEWLINE, 
					entityController.getTransformable(0).getPosition(), entityController.getTransformable(10).getPosition(),
					new Vector3f(0,1,0), 0);
		}
		
		super.timeMarkEnd();
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO Auto-generated method stub

	}

}
