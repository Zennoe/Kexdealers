package ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.joml.Vector3f;

public class EntityController {

	private HashMap<Integer, ArrayList<String>> entities = new HashMap<>();

	private HashMap<Integer, Transformable> transformable = new HashMap<>();
	private HashMap<Integer, Renderable> renderable = new HashMap<>();
	private HashMap<Integer, PointLightComponent> pointLightComponent = new HashMap<>();
	
	
	
	// --- eID de-/allocation ---
	
	public int allocEID(){
		int i = 0;
		while(entities.get(i) != null){
			i++;
		}
		// Initialize the entity 
		// Transformable must always exist for each entity.
		ArrayList<String> comps = new ArrayList<String>();
		comps.add("transformable");
		entities.put(i, comps);
		addTransformable(i);
		return i;
	}
	
	public void freeEID(int eID){
		entities.get(eID).clear();
		entities.put(eID, null);
	}
	
	// --- ADDERS ---
	
	public Transformable addTransformable(int eID){// Should exist anyways.
		entities.get(eID).add("transformable");
		Transformable comp = new Transformable(eID);
		transformable.put(eID, comp);
		return comp;
	}
	
	public Renderable addRenderable(int eID, String assetName){
		entities.get(eID).add("renderable");
		Renderable comp = new Renderable(eID, assetName);
		renderable.put(eID, comp);
		return comp;
	}
	
	public PointLightComponent addPointLightComponent(int eID, Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f attenuation){
		entities.get(eID).add("pointlightcomponent");
		PointLightComponent comp = new PointLightComponent(eID, position, ambient, diffuse, specular, attenuation);
		pointLightComponent.put(eID, comp);
		return comp;
	}
	
	public void addComponentOfType(int eID, String type, Component component) {
		component.setEID(eID);
		switch (type){
			case "transformable": transformable.put(eID, (Transformable) component); break;
			case "renderable": renderable.put(eID, (Renderable) component); break;
			case "pointlightcomponent": pointLightComponent.put(eID, (PointLightComponent) component); break;
		}
	}
	
	// --- REMOVERS ---
	
	public Transformable removeTransformable(int eID){// Will probably crash the engine
		entities.get(eID).remove("transformable");
		return transformable.remove(eID);
	}
	
	public Renderable removeRenderable(int eID){
		entities.get(eID).remove("renderable");
		return renderable.remove(eID);
	}
	
	public PointLightComponent removePointLightComponent(int eID){
		entities.get(eID).remove("pointlightcomponent");
		return pointLightComponent.remove(eID);
	}
	
	public Component removeComponentOfType(int eID, String type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case "transformable": return removeTransformable(eID);
				case "renderable": return removeRenderable(eID);
				case "pointlightcomponent": return removePointLightComponent(eID);
				default: return null;
			}
		}
	}
	
	// --- GETTERS ---
	
	public Transformable getTransformable(int eID){
		return transformable.get(eID);
	}
	
	public HashSet<Transformable> getTransformables(){
		return new HashSet<Transformable>(transformable.values());
	}
	
	public Renderable getRenderable(int eID){
		return renderable.get(eID);
	}
	
	public HashSet<Renderable> getRenderables(){
		return new HashSet<Renderable>(renderable.values());
	}
	
	public PointLightComponent getPointLightComponent(int eID){
		return pointLightComponent.get(eID);
	}
	
	public HashSet<PointLightComponent> getPointLightComponents(){
		return new HashSet<PointLightComponent>(pointLightComponent.values());
	}
	
	public Component getComponentOfType(int eID, String type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case "transformable": return getTransformable(eID);
				case "renderable": return getRenderable(eID);
				case "pointlightcomponent": return getPointLightComponent(eID);
				default: return null;
			}
		}
	}
	
	// --- QUERY ---
	
	public boolean hasComponent(int eID, String type){
		return entities.get(eID).contains(type);
	}
	
	// --- EXCHANGE ---
	
	public Entity emitEntity(int eID) {
		// copy and delete components
		ArrayList<String> componentTypes = entities.get(eID);
		Entity entity = new Entity(eID);
		for(int i = 0; i < componentTypes.size(); i++) {
			entity.addComponent(
					componentTypes.get(i), 
					removeComponentOfType(eID, componentTypes.get(i)));
		}
		// free eID
		freeEID(eID);
		return entity;
	}
	
	public void integrateEntity(Entity entity) {
		int newEID = allocEID();
		ArrayList<String> componentTypes = entity.getComposition();
		for(int i = 0; i < componentTypes.size(); i++) {
			addComponentOfType(
					newEID, 
					componentTypes.get(i), 
					entity.getComponentOfType(componentTypes.get(i)));
		}
	}
	
}
