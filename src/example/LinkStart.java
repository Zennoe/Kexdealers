package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import audio.AudioSystem;
import bus.MessageBus;
import bus.Operation;
import ecs.AudioSourceComponent;
import ecs.EntityController;
import ecs.PhysicsComponent;
import physics.PhysicsSystem;
import render.RenderSystem;


/*
 *  We're gonna lose FeelsGoodMan :clap:
 */
public class LinkStart implements Runnable{

	private Thread gameloop = null;
	private boolean running = false;
	
	// online == false -> run in local mode
	private boolean online = false;
	
	private static int targetFPS = 120;
	public static double timeDelta = 1 / targetFPS;
	
	private long tickCounter = 0;
	
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
		
		// Message Bus
		MessageBus messageBus = MessageBus.getInstance();

		// Systems
		RenderSystem renderSystem = new RenderSystem(messageBus, entityController);
		AudioSystem audioSystem = new AudioSystem(messageBus, entityController);
		NetworkSystem networkSystem = new NetworkSystem();
		TeleportationSystem teleportationSystem = new TeleportationSystem(messageBus, entityController);
		PhysicsSystem physicsSystem = new PhysicsSystem(messageBus, entityController);
		
		
		// Local mode: Load a local instance
		// Online mode: Connect to a server and request an instance from there.
		//				Should the connection fail, fall back to local mode.
		InstanceLoader instanceLoader = new InstanceLoader(entityController, graphicsLoader, audioLoader, renderSystem, audioSystem);
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
		Player player = new Player(entityController);
		
		entityController.getPhysicsComponent(6).applyForce("force", new Vector3f(150.0f,1.0f,12.5f));
		entityController.getPhysicsComponent(10).applyForce("wooosh", new Vector3f(0,5,0));
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			if(online) {
				//networkSystem.sendPlayerData(playerID);
			}
			
			if(Display.pressedKeys[GLFW.GLFW_KEY_O]){
				messageBus.messageTeleportationSys(Operation.SYS_TELEPORTATION_TARGETCOORDS, playerID, new Vector3f(450.0f, 25.0f, 350.0f));
			}
			
			player.update(playerID, (float)timeDelta, resourceLoader.getTerrain());
			// Teleport
			teleportationSystem.run();
			// Gravity
			//gravity(entityController, resourceLoader);
			// Physics
			physicsSystem.run(timeDelta, resourceLoader.getTerrain());
			if (Display.pressedKeys[GLFW.GLFW_KEY_N]) {
				entityController.getPhysicsComponent(6).increaseForce("force", new Vector3f(-50.0f * (float) timeDelta,0 ,0));
			}
			if (Display.pressedKeys[GLFW.GLFW_KEY_M]) {
				entityController.getPhysicsComponent(6).increaseForce("force", new Vector3f(50.0f * (float) timeDelta,0 ,0));
			}
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
			
			if(Display.pressedKeys[GLFW.GLFW_KEY_L]) {
				messageBus.messageRenderSys(bus.Operation.SYS_RENDER_WIREFRAME_ON);
			} else if(Display.pressedKeys[GLFW.GLFW_KEY_N]) {
				messageBus.messageRenderSys(bus.Operation.SYS_RENDER_WIREFRAME_OFF);
			}
			
			// Sky box
			resourceLoader.getSkybox().updateRotation((float)timeDelta);
			
			// Audio
			audioSystem.run();
			
			// Render
			renderSystem.run();
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			
			if (tickCounter % (targetFPS*2) == 0) {
				System.out.println((Math.floor(1000 / timeDelta)) / 1000 + " FPS");
			}
			
			tickCounter++;
		}
		
		audioSystem.cleanUp();
		
		display.destroy();
		if(online) {
			networkSystem.disconnectFromServer();
		}
	}
	
}
