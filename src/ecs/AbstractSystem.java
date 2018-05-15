package ecs;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import bus.MessageBus;

public abstract class AbstractSystem {
	
	protected final MessageBus messageBus;
	protected final EntityController entityController;
	
	protected int tickRate = 60;
	private double tickTime = 1.0f / tickRate;
	private double tickBegin;
	
	protected AbstractSystem(MessageBus messageBus, EntityController entityController) {
		this.messageBus = messageBus;
		this.entityController = entityController;
	}
	
	/*
	 * System thread's run() method. Runs when Thread.start() is called.
	 * Has a loop that runs according to it's own tick rate and calls update() each tick.
	 * As such the run() method is mostly for tick timing.
	 */
	public abstract void run();
	/*
	 * The update() method is called each tick from the run() method and is where each system's
	 * operations are executed in.
	 */
	protected abstract void update();
	/*
	 * The cleanUo() method is supposed to be called before this system's run() method exits and the thread terminates.
	 */
	protected abstract void cleanUp();
	/*
	 * Loads data from a blueprint file into the ECS
	 */
	public abstract void loadBlueprint(ArrayList<String> blueprint);
	
	// MUST be called at the beginning of update()
	protected void timeMarkStart() {
		tickBegin = GLFW.glfwGetTime();
	}
	
	// MUST be called at the end of update()
	protected void timeMarkEnd() {
		tickTime = (GLFW.glfwGetTime() - tickBegin);
	}
	
	protected double getDeltaTime() {
		return Math.min(tickTime, 1.0f / tickRate);
	}
}
