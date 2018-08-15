package render;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*; // allows us to create windows
import static org.lwjgl.opengl.GL11.*; // gives us access to things like "GL_TRUE" which we'll need 
import static org.lwjgl.system.MemoryUtil.*; // allows us to use 'NULL' in our code, note this is slightly different from java's 'null'

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Display {

	public long window = 0;

	private int width;
	private int height;

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	private boolean isInitialised = false;

	public boolean isInitialised() {
		return isInitialised;
	}

	public Display(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void create() {
		// Set error output
		GLFWErrorCallback.createPrint(System.err).set();
		// Initialize GLFW
		if (glfwInit() != true) {
			throw new RuntimeException("GLFW initialization failed!");
		}
		// Set window properties
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// Create window
		window = glfwCreateWindow(width, height, "Game Loop", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Could not create Window");
		}
		glfwSetWindowPos(window, 1, 20);
		glfwShowWindow(window);
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, w, h);
		width = w.get(0);
		height = h.get(0);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		isInitialised = true;
	}

	public void submitFrame() {
		if (!isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		glfwSwapBuffers(window);
	}

	public void destroy() {
		if (!isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
