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
	
	// online == false -> run in local mode
	private boolean online = false;
	
	private static int targetFPS = 120;
	public static double timeDelta = 1000 / targetFPS;
	
	public static void main(String[] args){
		LinkStart link = new LinkStart();
		link.start();
		
	}
	
	public void start(){
		running = true;
		gameloop = new Thread(this, "game_loop");
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
		NetworkSystem networkSystem = new NetworkSystem(entityController);
		
		// Local mode: Load a local instance
		// Online mode: Connect to a server and request an instance from there.
		//				Should the connection fail, fall back to local mode.
		InstanceLoader instanceLoader = new InstanceLoader(entityController, resourceLoader, renderSystem);
		if(online) {		
			online = networkSystem.connectToServer("localhost", 2222, "kekzdealer");
			if(!online) {
				System.out.println("Connection to server failed. Falling back to offline-mode");
			}else {
				// Run NetworkSystem on a new thread so reading data from server can happen asynchronously
				Thread netSysThread = new Thread(networkSystem, "network_system");
				netSysThread.start();
				networkSystem.loadInstanceFromServer(instanceLoader);
				// THIS IS ONLY FOR TESTING SO THERE IS SOME DATA UNTIL ABOVE METHOD IS DONE
				instanceLoader.loadInstanceFromLocal("./res/floatingTestingIsland.txt");
			}
		}
		if(!online) {
			instanceLoader.loadInstanceFromLocal("./res/floatingTestingIsland.txt");
		}
		
		int playerID = 0; //look into file to choose the correct one :S
		FPPCamera fppCamera = new FPPCamera();
		Player player = new Player(fppCamera, entityController, playerID);
		
		
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			if(online) {
				networkSystem.sendPlayerData(playerID);
			}
			
			player.update((float)timeDelta);
			gravity(entityController, resourceLoader);
			resourceLoader.getSkybox().updateRotation((float)timeDelta);
			
			// Render
			renderSystem.run(fppCamera);
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		display.destroy();
		if(online) {
			networkSystem.disconnectFromServer();
		}
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
