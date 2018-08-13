package input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import render.Display;

public class SourceKeyboard implements InputSourceI {

	private boolean dojump = false;
	private boolean dointeract = false;
	private boolean doteleport = false;
	private boolean togglewireframe = false;

	private final Display display;
	private final MessageBus bus;

	public SourceKeyboard(Display disp, MessageBus bus) {
		this.display = disp;
		this.bus = bus;
	}

	@Override
	public Vector2f pollMoveDirection() {
		Vector2f ret = new Vector2f();
		
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_A) > 0) {
			ret.x += -1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_W) > 0) {
			ret.y += 1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_S) > 0) {
			ret.y += -1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_D) > 0) {
			ret.x += 1;
		}
		
		if (Float.compare(ret.length(), 0) != 0) {
			ret.normalize();
		}
		
		return ret;
	}

	@Override
	public boolean doJump() {
		int glfwResult = GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_SPACE);

		// dojump is used to ensure that jump can be only triggered once per keypress

		if (!dojump && glfwResult >= 1) {
			// key was just pressed
			dojump = true;
			return true;
		} else if (dojump && glfwResult <= 0) {
			// key was just released
			dojump = false;
		}
		// else: key is held down or not pressed

		return false;
	}

	@Override
	public Vector2f pollLookMove() {
		Vector2f ret = new Vector2f();
		
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_LEFT) > 0) {
			ret.x += -1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_UP) > 0) {
			ret.y += 1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_DOWN) > 0) {
			ret.y += -1;
		}
		if (GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_RIGHT) > 0) {
			ret.x += 1;
		}
		
		if (Float.compare(ret.length(), 0) != 0) {
			ret.normalize(5);
		}
		
		return ret;
	}

	@Override
	public boolean doInteract() {
		int glfwResult = GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_E);
		glfwResult += GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_F);

		// dointeract is used to ensure that interact can be only triggered once per
		// keypress

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
		return GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_0) > 0;
	}

	@Override
	public boolean doTeleport() {
		int glfwResult = GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_T);

		// doteleport is used to ensure that teleport can be only triggered once per keypress

		if (!doteleport && glfwResult >= 1) {
			// key was just pressed
			doteleport = true;
			return true;
		} else if (doteleport && glfwResult <= 0) {
			// key was just released
			doteleport = false;
		}
		// else: key is held down or not pressed

		return false;
	}

	@Override
	public boolean toggleWireframe() {
			int glfwResult = GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_V);

		// togglewireframe is used to ensure that wireframe can be only toggled once per keypress

		if (!togglewireframe && glfwResult >= 1) {
			// key was just pressed
			togglewireframe = true;
			return true;
		} else if (togglewireframe && glfwResult <= 0) {
			// key was just released
			togglewireframe = false;
		}
		// else: key is held down or not pressed

		return false;
	}

	@Override
	public boolean closeGame() {
		return GLFW.glfwGetKey(display.window, GLFW.GLFW_KEY_ESCAPE) > 0;
	}

}
