package example;

import bus.MessageBus;
import ecs.EntityController;

public abstract class AbstractSystem {
	
	protected MessageBus messageBus;
	protected EntityController entityController;
	
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
	public abstract void update();
	/*
	 * The cleanUo() method is supposed to be called before this system's run() method exits and the thread terminates.
	 */
	public abstract void cleanUp();
	
}
