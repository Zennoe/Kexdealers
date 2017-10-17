package ecs;

import java.util.ArrayList;

public class Entity {
	
	private int eID;
	
	private ArrayList<String> componentTypes = new ArrayList<>();
	private ArrayList<Component> components = new ArrayList<>();
	
	public Entity(int eID) {
		this.eID = eID;
	}
	
	public void addComponent(String type, Component component) {
		componentTypes.add(type);
		components.add(component);
	}
	
	public int getEID() {
		return eID;
	}
	
	public ArrayList<String> getComposition(){
		return componentTypes;
	}
	
	public Component getComponentOfType(String type) {
		return components.get(componentTypes.indexOf(type));
	}
}
