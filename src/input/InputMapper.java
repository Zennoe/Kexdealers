package input;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import bus.Operation;
import render.Display;

public class InputMapper {
	private Display disp;
	private MessageBus bus;
	
	// action state tracking
	private boolean wireframeMode = false;
	
	private List<InputSourceI> inputSources = new ArrayList<>();

	public InputMapper(Display disp, MessageBus bus) {
		this.disp = disp;
		this.bus = bus;
		// TODO detect if future input libs present
		
		inputSources.add(new SourceKeyboard(disp, bus));
		inputSources.add(new SourceMouse(disp, bus));
	}

	public void pollEvents() {
		// check for devices
		GLFW.glfwPollEvents();


		for (InputSourceI is : inputSources) {
			handleActions(is);
		}
	}

	private void handleActions(InputSourceI is) {
		// system
		if (is.closeGame()) {
			GLFW.glfwSetWindowShouldClose(disp.window, true);
		}
		
		// movement
		bus.messagePlayer(Operation.PLAYER_MOVE, is.pollMoveDirection());
		
		if (is.doJump()) {
			bus.messagePlayer(Operation.PLAYER_JUMP);
		}

		// look
		bus.messagePlayer(Operation.PLAYER_LOOK, is.pollLookMove());

		// action
		if (is.doInteract()) { // interacting with world
			bus.messagePlayer(Operation.PLAYER_INTERACT);
		}
		
		// debug
		if (is.isActivatingGravitySuck()) {
			// TODO
		}
		if (is.doTeleport()) {
			bus.messageTeleportationSys(Operation.SYS_TELEPORTATION_TARGETCOORDS, 0, new Vector3f(450.0f, 25.0f, 350.0f));
		}
		if (is.toggleWireframe()) {
			wireframeMode = !wireframeMode;
			
			if (wireframeMode) {
				bus.messageRenderSys(Operation.SYS_RENDER_WIREFRAME_ON);
			} else {
				bus.messageRenderSys(Operation.SYS_RENDER_WIREFRAME_OFF);
			}
		}

	}
}
