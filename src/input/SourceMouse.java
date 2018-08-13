package input;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import render.Display;

public class SourceMouse implements InputSourceI {

	private boolean dointeract = false;
	private Vector2f prevMousePos = new Vector2f();

	private final Display display;
	private final MessageBus bus;

	public SourceMouse(Display disp, MessageBus bus) {
		this.display = disp;
		this.bus = bus;
	}

	@Override
	public boolean closeGame() {
		return false;
	}

	@Override
	public Vector2f pollMoveDirection() {
		return new Vector2f();
	}

	@Override
	public boolean doJump() {
		return false;
	}

	@Override
	public Vector2f pollLookMove() {
		DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(display.window, b1, b2);
		
		Vector2f ret = new Vector2f((float) b1.get(0), (float) b2.get(0));
		ret.sub(prevMousePos);
		ret.mul(new Vector2f(1, -1));
		
		prevMousePos.set((float) b1.get(0), (float) b2.get(0));
		
		return ret;
	}

	@Override
	public boolean doInteract() {
		int glfwResult = GLFW.glfwGetMouseButton(display.window, GLFW.GLFW_MOUSE_BUTTON_1);

		// dointeract is used to ensure that interact can be only triggered once per
		// buttonpress

		if (!dointeract && glfwResult >= 1) {
			// key was just pressed
			dointeract = true;
			return true;
		} else if (dointeract && glfwResult <= 0) {
			// key was just released
			dointeract = false;
		}
		// else: key is held down or not pressed

		return false;
	}

	@Override
	public boolean isActivatingGravitySuck() {
		return false;
	}

	@Override
	public boolean doTeleport() {
		return false;
	}

	@Override
	public boolean toggleWireframe() {
		return false;
	}

}
