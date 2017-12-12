package audio;

import ecs.EntityController;
import example.ResourceLoader;

public class AudioSystem {
	
	private EntityController entityController;
	
	private ResourceLoader resourceLoader;

	public AudioSystem(EntityController entityController, ResourceLoader resourceLoader) {
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
	}
	
	public void run() {
		// do stuff
	}
	
	// add and remove audio related components only through this system
	
}
