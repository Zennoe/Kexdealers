package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import render.LatchOnCamera;
import render.RenderSystem;



public class LinkStart implements Runnable{

	private Thread gameloop = null;
	private boolean running = false;
	
	private static int targetFPS = 120;
	public static double timeDelta = 1000 / targetFPS;
	
	public static void main(String[] args){
		LinkStart link = new LinkStart();
		link.start();
		
	}
	
	public void start(){
		running = true;
		gameloop = new Thread(this, "Game Loop");
		gameloop.start();
	}
	
	@Override
	public void run(){
		
		// "Visuals"
		Display display = new Display(1920, 1080);//1280, 720
		display.create();
		
		// Managers
		EntityController entityController = new EntityController();
		ResourceLoader resourceLoader = new ResourceLoader();
		
		// Systems
		RenderSystem renderSystem = new RenderSystem(entityController, resourceLoader);
		
		// Load a sun
		Vector3f dayAmbient = new Vector3f(0.529f, 0.807f, 0.95f).mul(0.1f);
		Vector3f dayDiffuse = new Vector3f(0.529f, 0.807f, 0.95f).mul(1.5f);
		Vector3f daySpecular = new Vector3f(0.529f, 0.807f, 0.95f).mul(1.9f);
		DirectionalLight sun = new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.3f), dayAmbient, dayDiffuse, daySpecular);
		
		// Test model
		int testEntity = entityController.allocEID();
		renderSystem.materialize(testEntity, "player");
		ControllerModule controller = new ControllerModule(testEntity);
		
		// Camera to look at test model
		LatchOnCamera camera = new LatchOnCamera(entityController);
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			controller.run(camera, timeDelta);
			
			// Render
			renderSystem.run(camera, sun);
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		display.destroy();
	}
}
