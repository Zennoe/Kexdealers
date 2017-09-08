package ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.joml.Vector3f;

import ecs.PointLightComponent;

public class EntityController {

	private HashMap<Integer, ArrayList<String>> entities = new HashMap<>();

	private HashMap<Integer, Renderable> renderable = new HashMap<>();
	private HashMap<Integer, Transformable> transformable = new HashMap<>();
	private HashMap<Integer, PointLightComponent> pointLightComponent = new HashMap<>();
	
	// --- dereference ALL EC data ---
	
	public void wipeComplete(){
		entities.clear();
		
		renderable.clear();
		transformable.clear();
		pointLightComponent.clear();
	}
	
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
	
	public boolean directAllocEID(int eID){
		if(entities.get(eID) != null){
			return false;
		}else{
			// Initialize the entity 
			// Transformable must always exist for each entity.
			ArrayList<String> comps = new ArrayList<String>();
			comps.add("transformable");
			entities.put(eID, comps);
			addTransformable(eID);
			return true;
		}
	}
	
	public void freeEID(int eID){
		entities.get(eID).clear();
		entities.put(eID, null);
	}
	
	// --- ADDERS ---
	
	public Renderable addRenderable(int eID, String assetName){
		entities.get(eID).add("renderable");
		Renderable comp = new Renderable(eID, assetName);
		renderable.put(eID, comp);
		return comp;
	}
	
	public Transformable addTransformable(int eID){// Should exist anyways.
		entities.get(eID).add("transformable");
		Transformable comp = new Transformable(eID);
		transformable.put(eID, comp);
		return comp;
	}
	
	public PointLightComponent addPointLightComponent(int eID, Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, Vector3f attenuation){
		entities.get(eID).add("pointlightcomponent");
		PointLightComponent comp = new PointLightComponent(eID, position, ambient, diffuse, specular, attenuation);
		pointLightComponent.put(eID, comp);
		return comp;
	}
	
	// --- REMOVERS ---
	
	public void removeRenderable(int eID){
		entities.get(eID).remove("renderable");
		renderable.remove(eID);
	}
	
	public void removeTransformable(int eID){// Will probably crash the engine
		entities.get(eID).remove("transformable");
		transformable.remove(eID);
	}
	
	public void removePointLightComponent(int eID){
		entities.get(eID).remove("pointlightcomponent");
		pointLightComponent.remove(eID);
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
	
	// --- QUERY ---
	
	public boolean hasComponent(int eID, String type){
		return entities.get(eID).contains(type);
	}
}
