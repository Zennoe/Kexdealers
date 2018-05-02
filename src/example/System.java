package example;

import bus.MessageBus;
import ecs.EntityController;

public abstract class System {
	
	protected MessageBus messageBus;
	protected EntityController entityController;
	
	protected System(MessageBus messageBus, EntityController entityController) {
		this.messageBus = messageBus;
		this.entityController = entityController;
	}
	
	public abstract void run();
	public abstract void update();
	public abstract void cleanUp();
	
}
