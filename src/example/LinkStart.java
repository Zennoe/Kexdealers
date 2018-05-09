package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import audio.AudioSystem;
import bus.MessageBus;
import bus.NetworkSysMessage;
import bus.Operation;
import ecs.AbstractSystem;
import ecs.AudioSourceComponent;
import ecs.EntityController;
import ecs.PhysicsComponent;
import loaders.BlueprintLoader;
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
	
	private static int targetFPS = 60;
	public static double timeDelta = 1 / targetFPS;
	
	private long tickCounter = 0;
	
	// list of all systems
	private final HashMap<String, AbstractSystem> systems = new HashMap<>();
	
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
		systems.put("RenderSystem", new RenderSystem(messageBus, entityController));
		systems.put("AudioSystem", new AudioSystem(messageBus, entityController));
		systems.put("TeleportationSystem", new TeleportationSystem(messageBus, entityController));
		systems.put("PhysicsSystem", new PhysicsSystem(messageBus, entityController));
		systems.put("NetworkSystem", new NetworkSystem(messageBus, entityController));
		
		// Local mode: Load a local instance
		// Online mode: Connect to a server and request an instance from there.
		//				Should the connection fail, fall back to local mode.
		if(online) {		
			NetworkSysMessage message = 
					(NetworkSysMessage) messageBus.messageNetworkSys(
							Operation.SYS_NETWORK_CONNECT, new String("localhost:2222"));
			float connectTimeoutBegin = (float) GLFW.glfwGetTime();
			float connectTimeoutRemaining = 3000; // milliseconds
			while((!message.isComplete()) || (connectTimeoutRemaining > 0)) {
				if(message.isComplete()) {
					connectTimeoutRemaining = 0;
					online = true;
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException x) {
					x.printStackTrace();
					System.err.println("Got interrupted while waiting for NetworkSystem to connect to server");
				}
				connectTimeoutRemaining -= (float) (GLFW.glfwGetTime() - connectTimeoutBegin);
				connectTimeoutBegin = (float) GLFW.glfwGetTime();
			}
			if(!online) {
				System.out.println("Connection to server failed. Falling back to offline-mode");
			}else {
				// Run NetworkSystem on a new thread so reading data from server can happen asynchronously
				Thread netSysThread = new Thread(networkSystem, "network_system");
				netSysThread.start();
				ArrayList<String> blueprint = systems.get("NetworkSystem").loadInstanceFromServer(); // hmm
				// Entities
				String entityIDs = BlueprintLoader.getLineWith("ENTITIES", blueprint);
				for(String frag : BlueprintLoader.getDataFragments(entityIDs)){
					entityController.directAllocEID(Integer.valueOf(frag));
				} 
				// - Transformable
				ArrayList<String> transformableData = BlueprintLoader.getAllLinesWith("TRANSFORMABLE", blueprint);
				String[] frags = null;
				for(String dataSet : transformableData){
					int eID = BlueprintLoader.extractEID(dataSet);
					frags = BlueprintLoader.getDataFragments(dataSet);
					Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
					float rotX = Float.valueOf(frags[3]);
					float rotY = Float.valueOf(frags[4]);
					float rotZ = Float.valueOf(frags[5]);
					float scale = Float.valueOf(frags[6]);
					entityController.getTransformable(eID)
						.setPosition(position)
						.setRotX(rotX)
						.setRotY(rotY)
						.setRotZ(rotZ)
						.setScale(scale);
				}
				// Components are loaded by their system
				for(AbstractSystem system : systems.values()) {
					system.loadBlueprint(blueprint);
				}
			}
		}
		if(!online) {
			ArrayList<String> blueprint = BlueprintLoader.loadFromFile("./res/floatingTestingIsland.txt");
			// Entities
			String entityIDs = BlueprintLoader.getLineWith("ENTITIES", blueprint);
			for(String frag : BlueprintLoader.getDataFragments(entityIDs)){
				entityController.directAllocEID(Integer.valueOf(frag));
			} 
			// - Transformable
			ArrayList<String> transformableData = BlueprintLoader.getAllLinesWith("TRANSFORMABLE", blueprint);
			String[] frags = null;
			for(String dataSet : transformableData){
				int eID = BlueprintLoader.extractEID(dataSet);
				frags = BlueprintLoader.getDataFragments(dataSet);
				Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
				float rotX = Float.valueOf(frags[3]);
				float rotY = Float.valueOf(frags[4]);
				float rotZ = Float.valueOf(frags[5]);
				float scale = Float.valueOf(frags[6]);
				entityController.getTransformable(eID)
					.setPosition(position)
					.setRotX(rotX)
					.setRotY(rotY)
					.setRotZ(rotZ)
					.setScale(scale);
			}
			// Components are loaded by their system
			for(AbstractSystem system : systems.values()) {
				system.loadBlueprint(blueprint);
			}
		}
		
		int playerID = 0; //look into file to choose the correct one :S
		Player player = new Player(entityController);
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			if(online) {
				//networkSystem.sendPlayerData(playerID);
			}
			
			player.update(playerID, (float)timeDelta, resourceLoader.getTerrain());
			// Teleport
			systems.get("TeleportationSystem").run();
			// Gravity
			//gravity(entityController, resourceLoader);
			// Physics
			systems.get("PhysicsSystem").run(timeDelta, resourceLoader.getTerrain());
			
			// Input
			systems.get("InputSystem").run();
			
			// Audio
			systems.get("AudioSystem").run();
			
			// Render
			systems.get("RenderSystem").run();
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			
			if (tickCounter % (targetFPS*2) == 0) {
				System.out.println((Math.floor(1000 / timeDelta)) / 1000 + " FPS");
			}
			tickCounter++;
		}
		
		display.destroy();
		if(online) {
			messageBus.messageNetworkSys(bus.Operation.SYS_NETWORK_DISCONNECT, null);
		}
	}
	
}
