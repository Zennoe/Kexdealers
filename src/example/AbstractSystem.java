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
	
	public abstract void run();
	public abstract void update();
	public abstract void cleanUp();
	
}
