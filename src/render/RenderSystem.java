package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import ecs.EntityController;
import ecs.FPPCameraComponent;
import ecs.Transformable;
import example.Display;
import example.ResourceLoader;
import skybox.SkyboxRenderer;
import terrain.TerrainRenderer;

public class RenderSystem {

	private EntityController entityController;
	
	private ResourceLoader resourceLoader;
	
	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	
	private HashMap<String, HashSet<Transformable>> entitiesToRender = new HashMap<>(); // All the currently active transforms for one asset
	
	public RenderSystem(EntityController entityController, ResourceLoader resourceLoader){
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
		
		// Back-face culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BACK);
		// Automatic Gamma-correction
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
		// Wire frame mode: GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		entityRenderer = new EntityRenderer();
		terrainRenderer = new TerrainRenderer();
		skyboxRenderer = new SkyboxRenderer();
	}
	
	public void run(int observerEID){
		// Do all the message processing
		// ???
		// Do other processing
		
		// render
		renderScene(entityController.getFPPCameraComponent(observerEID));
	}
	
	public void materialize(int eID, String assetName){
		// Generate a new Renderable for <eID>
		entityController.addRenderable(eID);
		entityController.getRenderable(eID).setAssetName(assetName);
		// Sort in the new reference for instanced rendering
		if(entitiesToRender.get(assetName) == null){
			entitiesToRender.put(assetName, new HashSet<Transformable>());
			// add +1 to pointer count for this asset
			resourceLoader.load(assetName);
		}
		entitiesToRender.get(assetName).add(entityController.getTransformable(eID));
		System.out.println("materialize complete for: " +eID);
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
		resourceLoader.unload(assetName);
		// Make change "official"
		entityController.removeRenderable(eID);
	}
	
	// ============================================================================
	// =================== MASTER RENDERER ACTIVITIES =============================
	// ============================================================================
	
	private void prepareForRendering(){
		glClearColor(0.529f, 0.807f, 0.95f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private void renderScene(FPPCameraComponent camera){
		prepareForRendering();
		
		terrainRenderer.render(resourceLoader, camera, entityController.getPointLightComponents());
		
		// count entities rendered
		ArrayList<Integer> ids = new ArrayList<>();
		for(HashSet<Transformable> mats : entitiesToRender.values()) {
			for(Transformable comp : mats) {
				if(!ids.contains(comp.getEID())) {
					ids.add(comp.getEID());
				}
			}
		}
		if(ids.size() > 8) {
			//System.out.println("yyeah");
		}
		
		entityRenderer.render(resourceLoader, camera, entitiesToRender, entityController.getPointLightComponents());
		
		skyboxRenderer.render(resourceLoader, camera);
		
		// Swap buffer to make changes visible
		GLFW.glfwSwapBuffers(Display.window);
	}
}
