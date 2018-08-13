package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import bus.MessageBus;
import bus.Systems;
import bus.RenderSysMessage;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;
import loaders.BlueprintLoader;
import loaders.GraphicsLoader;
import skybox.SkyboxRenderer;
import terrain.TerrainRenderer;
import ui.LineRenderer;

public class RenderSystem extends AbstractSystem {

	private final GraphicsLoader graphicsLoader;
	
	private final EntityRenderer entityRenderer;
	private final TerrainRenderer terrainRenderer;
	private final SkyboxRenderer skyboxRenderer;
	private final LineRenderer lineRenderer;
	
	private HashMap<String, HashSet<Transformable>> entitiesToRender = new HashMap<>(); // All the currently active transforms for one asset
	
	// state tracking
	private boolean drawDebugLines = false;
	
	/*
	 * TODO: Make separate ResourceLoaders for separate types of resources. 
	 * Let the loaders needed for each system be created in their own code.
	 */
	public RenderSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
		
		// Back-face culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		// Automatic Gamma-correction
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
		
		graphicsLoader = new GraphicsLoader();
		
		entityRenderer = new EntityRenderer();
		terrainRenderer = new TerrainRenderer();
		skyboxRenderer = new SkyboxRenderer();
		lineRenderer = new LineRenderer();
	}
	
	public void run() {
		// control update rate here
		
		// update :)
		update();
		
		// cleanUp on program exit
		// cleanUp();
	}
	
	public void update() {
		super.timeMarkStart();
		// message queue
		RenderSysMessage message;
		while((message = (RenderSysMessage) messageBus.getNextMessage(Systems.RENDER_SYSTEM)) != null) {
			switch(message.getOP()) {
			case SYS_RENDER_WIREFRAME_ON: GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				break;
			case SYS_RENDER_WIREFRAME_OFF: GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				break;
			case SYS_RENDER_DEBUGLINES_ON: drawDebugLines = true;
				break;
			case SYS_RENDER_DEBUGLINES_OFF: drawDebugLines = false; lineRenderer.clearLines();
				break;
			case SYS_RENDER_DEBUGLINES_ADDNEWLINE:
				lineRenderer.addLine(message.getPosBegin(), message.getPosEnd(), message.getColour(), message.getTime());
				break;
			default: System.err.println("Render operation not implemented");
			}
		}
		
		// update sky box rotation
		graphicsLoader.getSkybox().updateRotation((float)super.getDeltaTime());
		
		// render scene
		// Assumes that there is only one FPP camera component so the first one found is used.
		Set<FPPCameraComponent> fppCamComps = entityController.getFPPCameraComponents();
		FPPCameraComponent fppCamComp = fppCamComps.iterator().next();
		renderScene(fppCamComp);
		
		super.timeMarkEnd();
	}
	
	public void cleanUp() {
		// ???
	}
	
	public void loadBlueprint(ArrayList<String> blueprint) {
		
		// - Renderable
		String[] frags = null;
		// Skybox
		String skyboxData = BlueprintLoader.getLineWith("SKYBOX", blueprint);
		frags = BlueprintLoader.getDataFragments(skyboxData);
		graphicsLoader.loadSkybox(
				Float.valueOf(frags[0]),
				frags[1]);
		// Terrain
		String terrainData = BlueprintLoader.getLineWith("TERRAIN", blueprint);
		frags = BlueprintLoader.getDataFragments(terrainData);
		String[] drgb = {frags[4], frags[5], frags[6], frags[7]};
		graphicsLoader.loadTerrain(
				Integer.valueOf(frags[0]), 
				Integer.valueOf(frags[1]), 
				frags[2], 
				drgb, 
				frags[3]);
		// Sun
		String sunData = BlueprintLoader.getLineWith("SUN", blueprint);
		frags = BlueprintLoader.getDataFragments(sunData);
		Vector3f directionInverse = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
		Vector3f DL_ambient = new Vector3f(Float.valueOf(frags[3]), Float.valueOf(frags[4]), Float.valueOf(frags[5]));
		Vector3f DL_diffuse = new Vector3f(Float.valueOf(frags[6]), Float.valueOf(frags[7]), Float.valueOf(frags[8]));
		Vector3f DL_specular = new Vector3f(Float.valueOf(frags[9]), Float.valueOf(frags[10]), Float.valueOf(frags[11]));
		graphicsLoader.loadSun(
				directionInverse, 
				DL_ambient, DL_diffuse, DL_specular);
		ArrayList<String> renderableData = BlueprintLoader.getAllLinesWith("RENDERABLE", blueprint);
		for(String dataSet : renderableData){
			int eID = BlueprintLoader.extractEID(dataSet);
			frags = BlueprintLoader.getDataFragments(dataSet);
			materialize(eID, frags[0]);
		}
		// - PointLightComponent
		ArrayList<String> pointLightComponentData = BlueprintLoader.getAllLinesWith("POINTLIGHTCOMPONENT", blueprint);
		for(String dataSet : pointLightComponentData){
			int eID = BlueprintLoader.extractEID(dataSet);
			frags = BlueprintLoader.getDataFragments(dataSet);
			Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
			Vector3f ambient = new Vector3f(Float.valueOf(frags[3]), Float.valueOf(frags[4]), Float.valueOf(frags[5]));
			Vector3f diffuse = new Vector3f(Float.valueOf(frags[6]), Float.valueOf(frags[7]), Float.valueOf(frags[8]));
			Vector3f specular = new Vector3f(Float.valueOf(frags[9]), Float.valueOf(frags[10]), Float.valueOf(frags[11]));
			entityController.addPointLightComponent(eID)
				.setPosition(position)
				.setAmbient(ambient)
				.setDiffuse(diffuse)
				.setSpecular(specular)
				.setRadius(Float.valueOf(frags[12]))
				.setCutoff(Float.valueOf(frags[13]));
		}
		// - FPPCameraComponent
		ArrayList<String> fppCameraComponentData = BlueprintLoader.getAllLinesWith("FPPCAMERACOMPONENT", blueprint);
		for(String dataSet : fppCameraComponentData) {
			int eID = BlueprintLoader.extractEID(dataSet);
			frags = BlueprintLoader.getDataFragments(dataSet);
			entityController.addFPPCameraComponent(eID);
		}
	}
	
	public void materialize(int eID, String assetName){
		// Generate a new Renderable for <eID>
		entityController.addRenderable(eID);
		entityController.getRenderable(eID).setAssetName(assetName);
		// Sort in the new reference for instanced rendering
		if(entitiesToRender.get(assetName) == null){
			entitiesToRender.put(assetName, new HashSet<Transformable>());
			// add +1 to pointer count for this asset
			graphicsLoader.load(assetName);
		}
		entitiesToRender.get(assetName).add(entityController.getTransformable(eID));
	}
	
	public void dematerialize(int eID){
		// Get <eID>'s Renderable to access the assetName. Use that as key to narrow down the search.
		String assetName = entityController.getRenderable(eID).getAssetName();
		// Set a reference to the correct list to reduce map queries
		HashSet<Transformable> temp = entitiesToRender.get(assetName);
		// Search the list for the correct Transformable and remove it when found.
		for(Transformable transformable : temp){
			if(transformable.getEID() == eID){
				temp.remove(transformable);
				break;
			}
		}
		graphicsLoader.unload(assetName);
		// Make change "official"
		entityController.removeRenderable(eID);
	}
	
	private void prepareForRendering(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		glClearColor(0.529f, 0.807f, 0.95f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	private void renderScene(FPPCameraComponent camera){
		prepareForRendering();
		
		skyboxRenderer.render(graphicsLoader, camera);
		
		terrainRenderer.render(graphicsLoader, camera, entityController.getPointLightComponents());
		
		entityRenderer.render(graphicsLoader, camera, entitiesToRender, entityController.getPointLightComponents());
		
		if (drawDebugLines) {
			lineRenderer.render(camera, getDeltaTime());
		}
		
		// Swap buffer to make changes visible
		GLFW.glfwSwapBuffers(Display_old.window);
	}
}
