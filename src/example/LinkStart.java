package example;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import org.lwjgl.glfw.GLFW;



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
		
		
		// < The Loop >
		double frameBegin;
		while(running){
			frameBegin = GLFW.glfwGetTime();
			
			// Update
			GLFW.glfwPollEvents();
			
			if(GLFW.glfwWindowShouldClose(Display.window)){
				running = false;
			}
			
			timeDelta = glfwGetTime() - frameBegin;
			// FPS: System.out.println((int) (Math.floor(1000 / timeDelta)) / 1000);
		}
		
		display.destroy();
	}
}
