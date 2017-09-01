package example;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*; // allows us to create windows
import static org.lwjgl.opengl.GL11.*; // gives us access to things like "GL_TRUE" which we'll need 
import static org.lwjgl.system.MemoryUtil.*; // allows us to use 'NULL' in our code, note this is slightly different from java's 'null'

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Display {

	public static long window = NULL;
	
	public static int width;
	public static int height;
	
	// Lazy way to make inputs globally accessible
	public static boolean[] pressedKeys = new boolean[1024];
	public static boolean[] pressedMouseButtons = new boolean[8];
	public static boolean shiftMod = false;
	public static boolean ctrlMod = false;
	public static boolean altMod = false;
	
	private static double mouseScroll = 0;
	private static double mouseX = 0;
	private static double mouseY = 0;
	private static double oldMouseX = 0;
	private static double oldMouseY = 0;
	
	public Display(int width, int height){
		Display.width = width;
		Display.height = height;
	}
	
	public void create(){
		// Set error output
		GLFWErrorCallback.createPrint(System.err).set();
		// Initialize GLFW
		if(glfwInit() != true){
			System.err.println("GLFW initialization failed!");
		}
		// Set OpenGL version
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	    // Hide window after creation
	    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	    // Disable window resizing
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		// Create window
		window = glfwCreateWindow(width, height, "Game Loop", NULL, NULL);
		if(window == NULL){
			System.err.println("Could not create Window");
		}
		// Set input callback methods for keyboard/mouse input etc
		setInputCallbacks();
		// Set window position - account for border width
		glfwSetWindowPos(window, 1, 20);
		// Set GLFW context. Vital for program to work.
		glfwMakeContextCurrent(window);
		// Enable V-Sync as FPS Limiter
		glfwSwapInterval(1);
		// Show created window
		glfwShowWindow(window);
		// Bad stuff happening without this
		GL.createCapabilities();
		// Define viewport
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, w, h);
		int width = w.get(0);
		int height = h.get(0);
		glViewport(0, 0, width, height);
		
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	
	public void destroy(){
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void setInputCallbacks(){
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if(action == GLFW_PRESS){
				pressedKeys[key] = true;
			}else if(action == GLFW_RELEASE){
				pressedKeys[key] = false;
			}
			if(mods == GLFW_MOD_SHIFT){
				shiftMod = true;
			}
			if(mods == GLFW_MOD_CONTROL){
				ctrlMod = true;
			}
			if(mods == GLFW_MOD_ALT){
				altMod = true;
			}
			
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if(action == GLFW_PRESS){
				pressedMouseButtons[button] = true;
			}else if(action == GLFW_RELEASE){
				pressedMouseButtons[button] = false;
			}
		});
		
		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			mouseScroll = yoffset;
		});
		
		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			mouseX = xpos;
			mouseY = ypos;
		});
	}
	
	public static double getMouseScroll(){
		double sc = mouseScroll;
		mouseScroll = 0;
		return sc;
	}
	
	public static double getMouseX(){
		double mx = mouseX - oldMouseX;
		oldMouseX = mouseX;
		return mx;
	}
	
	public static double getMouseY(){
		double my = mouseY - oldMouseY;
		oldMouseY = mouseY;
		return my;
	}

}

