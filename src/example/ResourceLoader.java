package example;

import java.util.HashMap;

import org.joml.Vector3f;

import audio.AudioResource;
import loaders.CubeMapLoader;
import loaders.MaterialLoader;
import loaders.Mesh;
import loaders.ModelLoader;
import loaders.StaticMeshLoader;
import loaders.TerrainMeshLoader;
import loaders.TextureLoader;
import skybox.Skybox;
import terrain.Terrain;
import terrain.TerrainMesh;
import textures.MultiTexture;
import textures.Texture;

public class ResourceLoader {
	
	private MaterialLoader materialLoader;
	private CubeMapLoader cubeMapLoader;
	private TextureLoader textureLoader;

	private StaticMeshLoader staticMeshLoader;
	private ModelLoader modelLoader;
	private TerrainMeshLoader terrainMeshLoader;
	
	private HashMap<String, Mesh> assets3D = new HashMap<>();
	
	private HashMap<String, Integer> pointerCounterSound = new HashMap<>();
	private HashMap<String, AudioResource> assetsSound = new HashMap<>();
	
	private Terrain terrain = null;
	
	private Skybox skybox = null;
	
	private DirectionalLight sun = new DirectionalLight();
	
	public ResourceLoader(){
		// preload keys
		assetsSound.put("default", null);
		pointerCounterSound.put("default", 0);
		
		// create tools
		materialLoader = new MaterialLoader();
		cubeMapLoader = new CubeMapLoader();
		textureLoader = new TextureLoader();
		modelLoader = new ModelLoader();
		staticMeshLoader = new StaticMeshLoader(textureLoader);
		terrainMeshLoader = new TerrainMeshLoader(modelLoader);
	}
	
	public void load(String assetName){
		Mesh mesh = assets3D.get(assetName);
		if(mesh == null) {
			Mesh[] meshes = staticMeshLoader.load(
					buildPath(assetName, "obj", "model"), 
					buildPath(assetName, "png", "texture"));
			mesh = meshes[0];
			mesh.reference();
			assets3D.put(assetName, mesh);
		}
	}
	
	public void unload(String assetName){
		Mesh mesh = assets3D.get(assetName);
		if(mesh.dereference() == 0) {
			mesh.delete();
			assets3D.put(assetName, null);
		}
	}
	
	public Mesh getRessource(String assetName){
		return assets3D.get(assetName);
	}
	
	public void loadSound(String assetName) {
		int x = pointerCounterSound.get(assetName);
		if( x == 0) {
			// load fresh from HDD
			AudioResource audioResource = new AudioResource(assetName);
			assetsSound.put(assetName, audioResource);
			pointerCounterSound.put(assetName, x++);
		}
	}
	
	public void unloadSound(String assetName) {
		int x = pointerCounterSound.get(assetName);
		if(x == 1) {
			pointerCounterSound.put(assetName, x--);
			// unload completely
			assetsSound.get(assetName).destroy();
			assetsSound.put(assetName, null);			
		} else {
			pointerCounterSound.put(assetName, x--);
		}
	}
	
	public AudioResource getSound(String assetName) {
		return assetsSound.get(assetName);
	}
	
	// grab an already loaded Terrain
	public Terrain getTerrain(){
		return terrain;
	}
	
	// There can only ever be one one terrain active and loaded for now
	public Terrain loadTerrain(int size, int maxHeight, String heightMap, String[] drgb, String blendMap){
		// unload whatever Terrain was loaded before
		unloadTerrain();
		// build TerrainMesh
		TerrainMesh terrainMesh = terrainMeshLoader.loadTerrainMesh(
				buildPath(heightMap, "png", "texture"), 
				maxHeight, size);
		// build MultiTexture
		String d = "_diffuse";
		String n = "_normal";
		String[] drgb_n = {drgb[0] + d, drgb[1] + d, drgb[2] + d, drgb[3] + d, 
				drgb[0] + n, drgb[1] + n, drgb[2] + n, drgb[3] + n, 
		};
		MultiTexture multiTexture = materialLoader.loadMultiTexture(drgb_n[0], drgb_n[1], drgb_n[2], drgb_n[3], 
				drgb_n[4], drgb_n[5], drgb_n[6], drgb_n[7]);
		// load blendMap
		Texture blendMapTexture = materialLoader.loadBlendMap(blendMap);
		// bundle together and return
		terrain = new Terrain(terrainMesh, multiTexture, blendMapTexture);
		return terrain;
	}
	
	public Terrain loadTerrain2(String terrainName) {
		// unload whatever Terrain was loaded before
		unloadTerrain();
		// load a ready terrain model instead of constructing it
		
	}
	
	public void unloadTerrain(){
		// blah blah delete stuff
		terrain = null;
	}
	
	// grab an already loaded Skybox
	public Skybox getSkybox(){
		return skybox;
	}
	
	// There can only ever be one skybox active and loaded for now
	public Skybox loadSkybox(float size, String skyboxName){
		// unload whatever Terrain was loaded before
		unloadTerrain();
		// build cube vertices
		float[] vertices = {        
		    -size,  size, -size,
		    -size, -size, -size,
		    size, -size, -size,
		     size, -size, -size,
		     size,  size, -size,
		    -size,  size, -size,

		    -size, -size,  size,
		    -size, -size, -size,
		    -size,  size, -size,
		    -size,  size, -size,
		    -size,  size,  size,
		    -size, -size,  size,

		     size, -size, -size,
		     size, -size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size,  size, -size,
		     size, -size, -size,

		    -size, -size,  size,
		    -size,  size,  size,
		     size,  size,  size,
		     size,  size,  size,
		     size, -size,  size,
		    -size, -size,  size,

		    -size,  size, -size,
		     size,  size, -size,
		     size,  size,  size,
		     size,  size,  size,
		    -size,  size,  size,
		    -size,  size, -size,

		    -size, -size, -size,
		    -size, -size,  size,
		     size, -size, -size,
		     size, -size, -size,
		    -size, -size,  size,
		     size, -size,  size
		};
		// build texture file paths
		String[] rltBoBaF = {
				skyboxName + "_right",
				skyboxName + "_left",
				skyboxName + "_top",
				skyboxName + "_bottom",
				skyboxName + "_back",
				skyboxName + "_front",
		};
		// loader mesh and textures
		skybox = new Skybox(modelLoader.loadToVAO(vertices, 3), cubeMapLoader.loadCubeMap(rltBoBaF));
		return skybox;
	}
	
	public void unloadSkybox(){
		// blah blah delete stuff
		skybox = null;
	}
	
	public DirectionalLight loadSun(Vector3f directionInverse, Vector3f ambient, Vector3f diffuse, Vector3f specular){
		sun = new DirectionalLight()
				.setDirection(directionInverse)
				.setAmbient(ambient)
				.setDiffuse(diffuse)
				.setSpecular(specular);
		return sun;
	}
	
	public DirectionalLight getSun(){
		return sun;
	}
	
	public void unloadSun(){
		sun = new DirectionalLight();
	}
	
	private static String buildPath(String fileName, String extension, String assetType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("./res/");
		
		switch(assetType) {
			case "model": sb.append("models/"); break;
			case "texture": sb.append("textures/"); break;
			case "sound": sb.append("sounds/"); break;
			default:
				System.err.println("assetType was wrongly specified for: " +fileName);
		}
		
		sb.append(fileName);
		sb.append(".");
		sb.append(extension.toLowerCase());
		
		return sb.toString();
	}
	
}
