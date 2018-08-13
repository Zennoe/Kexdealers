package ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EntityController {

	private final HashMap<Integer, ArrayList<String>> entities = new HashMap<>();

	private final HashMap<Integer, Transformable> transformable = new HashMap<>();
	private final HashMap<Integer, Renderable> renderable = new HashMap<>();
	private final HashMap<Integer, PointLightComponent> pointLightComponent = new HashMap<>();
	private final HashMap<Integer, AudioSourceComponent> audioSourceComponent = new HashMap<>();
	private final HashMap<Integer, FPPCameraComponent> fppCameraComponent = new HashMap<>();
	private final HashMap<Integer, PlayerControllerComponent> playerControllerComponent = new HashMap<>();
	private final HashMap<Integer, PhysicsComponent> physicsComponent = new HashMap<>();
	
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
	
	public void directAllocEID(int eID) {
		// Initialize the entity 
		// Transformable must always exist for each entity.
		ArrayList<String> comps = new ArrayList<String>();
		comps.add("transformable");
		entities.put(eID, comps);
		addTransformable(eID);
	}
	// --- ADDERS ---
	
	public Transformable addTransformable(int eID){// TODO: Make not required
		entities.get(eID).add("transformable");
		Transformable comp = new Transformable(eID);
		transformable.put(eID, comp);
		return comp;
	}
	
	public Renderable addRenderable(int eID){
		entities.get(eID).add("renderable");
		Renderable comp = new Renderable(eID);
		renderable.put(eID, comp);
		return comp;
	}
	
	public PointLightComponent addPointLightComponent(int eID){
		entities.get(eID).add("pointlightcomponent");
		PointLightComponent comp = new PointLightComponent(eID);
		pointLightComponent.put(eID, comp);
		return comp;
	}
	
	public AudioSourceComponent addAudioSourceComponent(int eID) {
		entities.get(eID).add("audiosourcecomponent");
		AudioSourceComponent comp = new AudioSourceComponent(eID);
		audioSourceComponent.put(eID, comp);
		return comp;
	}
	
	public FPPCameraComponent addFPPCameraComponent(int eID) {
		entities.get(eID).add("fppcameracomponent");
		FPPCameraComponent comp = new FPPCameraComponent(eID);
		fppCameraComponent.put(eID, comp);
		return comp;
	}
	
	public PlayerControllerComponent addPlayerControllerComponent(int eID) {
		entities.get(eID).add("playercontrollercomponent");
		PlayerControllerComponent comp = new PlayerControllerComponent(eID);
		playerControllerComponent.put(eID, comp);
		return comp;
	}
	
	public PhysicsComponent addPhysicsComponent(int eID) {
		entities.get(eID).add("physicscomponent");
		PhysicsComponent comp = new PhysicsComponent(eID);
		physicsComponent.put(eID, comp);
		return comp;
	}
	
	public void addComponentOfType(int eID, String type, Component component) {
		component.setEID(eID);
		switch (type){
			case "transformable": transformable.put(eID, (Transformable) component); break;
			case "renderable": renderable.put(eID, (Renderable) component); break;
			case "pointlightcomponent": pointLightComponent.put(eID, (PointLightComponent) component); break;
			case "audiosourcecomponent": audioSourceComponent.put(eID, (AudioSourceComponent) component); break;
			case "fppcameracomponent": fppCameraComponent.put(eID, (FPPCameraComponent) component); break;
			case "playercontrollercomponent": playerControllerComponent.put(eID, (PlayerControllerComponent) component); break;
			case "physicscomponent": physicsComponent.put(eID, (PhysicsComponent) component); break;
			default: System.err.println("Failed to add component of type " + type + " to entity " + eID + "! Unknown type!");
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
	
	public AudioSourceComponent removeAudioSourceComponent(int eID) {
		entities.get(eID).remove("audiosourcecomponent");
		return audioSourceComponent.remove(eID);
	}
	
	public FPPCameraComponent removeFPPCameraComponent(int eID) {
		entities.get(eID).remove("fppcameracomponent");
		return fppCameraComponent.remove(eID);
	}
	
	public PlayerControllerComponent removePlayerControllerComponent(int eID) {
		entities.get(eID).remove("playercontrollercomponent");
		return playerControllerComponent.remove(eID);
	}
	
	public PhysicsComponent removePhysicsComponent(int eID) {
		entities.get(eID).remove("physicscomponent");
		return physicsComponent.remove(eID);
	}
	
	public Component removeComponentOfType(int eID, String type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case "transformable": return removeTransformable(eID);
				case "renderable": return removeRenderable(eID);
				case "pointlightcomponent": return removePointLightComponent(eID);
				case "audiosourcecomponent": return removeAudioSourceComponent(eID);
				case "fppcameracomponent": return removeFPPCameraComponent(eID);
				case "playercontrollercomponent": return removePlayerControllerComponent(eID);
				case "physicscomponent": return removePhysicsComponent(eID);
				default: System.err.println("Failed to remove component of type " + type + " from entity " + eID + "! Unknown type!"); return null;
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
	
	public AudioSourceComponent getAudioSourceComponent(int eID) {
		return audioSourceComponent.get(eID);
	}
	
	public HashSet<AudioSourceComponent> getAudioSourceComponents(){
		return new HashSet<AudioSourceComponent>(audioSourceComponent.values());
	}
	
	public FPPCameraComponent getFPPCameraComponent(int eID) {
		return fppCameraComponent.get(eID);
	}
	
	public HashSet<FPPCameraComponent> getFPPCameraComponents(){
		return new HashSet<FPPCameraComponent>(fppCameraComponent.values());
	}
	
	public PlayerControllerComponent getPlayerControllerComponent(int eID) {
		return playerControllerComponent.get(eID);
	}
	
	public HashSet<PlayerControllerComponent> getPlayerControllerComponents(){
		return new HashSet<PlayerControllerComponent>(playerControllerComponent.values());
	}
	public PhysicsComponent getPhysicsComponent(int eID) {
		return physicsComponent.get(eID);
	}
	
	public HashSet<PhysicsComponent> getPhysicsComponents(){
		return new HashSet<PhysicsComponent>(physicsComponent.values());
	}
	
	public Component getComponentOfType(int eID, String type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case "transformable": return getTransformable(eID);
				case "renderable": return getRenderable(eID);
				case "pointlightcomponent": return getPointLightComponent(eID);
				case "audiosourcecomponent": return getAudioSourceComponent(eID);
				case "fppcameracomponent": return getFPPCameraComponent(eID);
				case "playercontrollercomponent": return getPlayerControllerComponent(eID);
				case "physicscomponent": return getPhysicsComponent(eID);
				default: System.err.println("Failed to get component of type " + type + " from entity " + eID + "! Unknown type!"); return null;
			}
		}
	}
	
	// --- QUERY ---
	
	public boolean hasComponent(int eID, String type){
		return entities.get(eID).contains(type);
	}
	
	public ArrayList<String> getComponentsFor(int eID) {
		return entities.get(eID);
	}
	
	public boolean isEntity(int eID) {
		return entities.containsKey(eID);
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
	
	public Entity copyEntity(int eID) {
		ArrayList<String> componentTypes = entities.get(eID);
		Entity entity = new Entity(eID);
		for(int i = 0; i < componentTypes.size(); i++) {
			entity.addComponent(
					componentTypes.get(i), 
					getComponentOfType(eID, componentTypes.get(i)));
		}
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
