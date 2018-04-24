package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import java.util.HashSet;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;

import audio.AudioSystem;
import bus.MessageBus;
import bus.Operation;
import ecs.EntityController;
import ecs.Transformable;
import physics.PhysicsSystem;
import render.RenderSystem;
import terrain.Terrain;


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
		AudioSystem audioSystem = new AudioSystem(entityController, resourceLoader);
		NetworkSystem networkSystem = new NetworkSystem(entityController);
		TeleportationSystem teleportationSystem = new TeleportationSystem(entityController);
		PhysicsSystem physicsSystem = new PhysicsSystem(entityController);
		
		// Message Bus
		MessageBus messageBus = MessageBus.getInstance();
		
		// Local mode: Load a local instance
		// Online mode: Connect to a server and request an instance from there.
		//				Should the connection fail, fall back to local mode.
		InstanceLoader instanceLoader = new InstanceLoader(entityController, resourceLoader, renderSystem, audioSystem);
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
		
		
		int iid = resourceLoader.getSound("music").getSourceID();
		ecs.AudioSourceComponent asc = entityController.getAudioSourceComponent(8);
		
		asc.setSourceID(iid);
		// update attributes
		AL10.alSourcef(asc.getSourceID(), AL10.AL_GAIN, asc.getGain());
		AL10.alSourcef(asc.getSourceID(), AL10.AL_PITCH, asc.getPitch());
		AL10.alSourcei(asc.getSourceID(), AL10.AL_LOOPING, asc.isLooping() ? AL10.AL_TRUE : AL10.AL_FALSE);
		AL10.alSourcef(asc.getSourceID(), AL10.AL_REFERENCE_DISTANCE, asc.getReferenceDistance());
		AL10.alSourcef(asc.getSourceID(), AL10.AL_ROLLOFF_FACTOR, asc.getRolloffFactor());
		AL10.alSourcef(asc.getSourceID(), AL10.AL_MAX_DISTANCE, asc.getMaxDistance());
		audioSystem.playEntitySound(8);
		System.out.println(AL10.AL_PLAYING == AL10.alGetSourcei(iid, AL10.AL_SOURCE_STATE));
		
		entityController.getPhysicsComponent(6).applyForce("force", new Vector3f(150.0f,1.0f,12.5f));
		entityController.getPhysicsComponent(10).applyForce("wooosh", new Vector3f(0,5,0));
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			if(online) {
				networkSystem.sendPlayerData(playerID);
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
			
			// Sky box
			resourceLoader.getSkybox().updateRotation((float)timeDelta);
			
			// Audio
			audioSystem.run(playerID);
			
			// Render
			renderSystem.run(playerID);
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		audioSystem.cleanUp();
		
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
			System.out.printf("EID: " + transformable.getEID() + " y: %.10f", correctedPos.y);
			System.out.println();
			transformable.setPosition(correctedPos);
		}
		System.exit(0);
	}
	
}
