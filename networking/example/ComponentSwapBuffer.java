package example;

import java.util.HashSet;
import java.util.Set;

import ecs.Component;

public class ComponentSwapBuffer {

	private Set<Component> buffer0;
	private Set<Component> buffer1;
	
	private Object lock0;
	private Object lock1;
	
	private Set<Component> NetworkSide;
	private Set<Component> ClientSide;
	
	public ComponentSwapBuffer() {
		buffer0 = new HashSet<>();
		buffer1 = new HashSet<>();
		
		
	}
}
