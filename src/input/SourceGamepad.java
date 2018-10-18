package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import java.nio.ByteBuffer;

import bus.MessageBus;
import render.Display;

public class SourceGamepad implements InputSourceI {

	private final Display disp;
	private final MessageBus bus;

	public final int glfwId;
	public final String name;
	private GLFWGamepadState gstate;

	// state
	private boolean connectionClosed = false;
	public boolean sourceConnectionClosed() {
		if (!GLFW.glfwJoystickIsGamepad(this.glfwId)) {
			connectionClosed = true;
			System.out.println("Gamepad " + name + " was disconnected.");
		}
		return connectionClosed;
	}

	public SourceGamepad(Display disp, MessageBus bus, int glfwJoypadId) {
		if (!disp.isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		this.disp = disp;
		this.bus = bus;

		this.glfwId = glfwJoypadId;
		if (GLFW.glfwJoystickIsGamepad(this.glfwId)) {
			this.name = GLFW.glfwGetGamepadName(this.glfwId);
			System.out.println("Gamepad " + name + " was added.");
		} else {
			this.name = null;
			connectionClosed = true;
		}
	}
	
	private void updateGState() {
		gstate = new GLFWGamepadState(ByteBuffer.allocateDirect(GLFWGamepadState.SIZEOF));
		GLFW.glfwGetGamepadState(glfwId, gstate);
	}
	
	private void checkIfClosed() {
		if (connectionClosed) {
			throw new IllegalStateException("Gamepad " + name + " has been disconnected!");
		}
	}
	
	@Override
	public boolean closeGame() {
		checkIfClosed();
		updateGState();
		return gstate.buttons(GLFW.GLFW_GAMEPAD_BUTTON_START) == GLFW.GLFW_PRESS;
	}
}
