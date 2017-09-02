package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.HashSet;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import ecs.EntityController;
import ecs.Transformable;
import render.RenderSystem;
import terrain.Terrain;



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
		
		// Window creation
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
		
		// Load a nice sky
		resourceLoader.loadSkybox(1200.0f, "night");
		
		// Load a terrain
		String[] drgb = {"mud", "path", "grass3", "flowerGrass"};
		resourceLoader.loadTerrain(256, 32, "heightMap256", drgb, "blendMap256");
		
		// Entities
		int treeID = entityController.allocEID();
		renderSystem.materialize(treeID, "lowPolyTree");
		
		int playerID = entityController.allocEID();
		renderSystem.materialize(playerID, "player");
		
		FPPCamera fppCamera = new FPPCamera();
		Player player = new Player(fppCamera, entityController, playerID);
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			player.update((float)timeDelta);
			gravity(entityController, resourceLoader);
			resourceLoader.getSkybox().updateRotation((float)timeDelta);
			// Render
			renderSystem.run(fppCamera, sun);
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		display.destroy();
	}
	
	private void gravity(EntityController entityController, ResourceLoader resourceLoader){
		
		HashSet<Transformable> transformables = entityController.getTransformables();
		Terrain terrain = resourceLoader.getTerrain();
		for(Transformable transformable : transformables){
			Vector3f correctedPos = transformable.getPosition();
			correctedPos.set(correctedPos.x, terrain.getHeightAtPoint(correctedPos.x, correctedPos.z), correctedPos.z);
			transformable.setPosition(correctedPos);
		}
	}
	
}
