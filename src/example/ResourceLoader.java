package example;

import java.util.HashMap;

import org.joml.Vector3f;

import audio.AudioResource;
import loaders.CubeMapLoader;
import loaders.MaterialLoader;
import loaders.ModelLoader;
import loaders.OBJLoader;
import loaders.TerrainMeshLoader;
import render.DirectionalLight;
import skybox.Skybox;
import terrain.Terrain;
import terrain.TerrainMesh;
import textures.Material;
import textures.MultiTexture;
import textures.Texture;
import wrapper.ModelData;
import wrapper.RawMesh;

public class ResourceLoader {
	
	private MaterialLoader materialLoader;
	private CubeMapLoader cubeMapLoader;
	
	private ModelLoader modelLoader;
	private TerrainMeshLoader terrainMeshLoader;
	
	private OBJLoader objLoader;
	
	private HashMap<String, Integer> pointerCounter3D = new HashMap<>();
	private HashMap<String, AssetData> assets3D = new HashMap<>();
	
	private HashMap<String, Integer> pointerCounterSound = new HashMap<>();
	
	private Terrain terrain = null;
	
	private Skybox skybox = null;
	
	private DirectionalLight sun = new DirectionalLight();
	
	public ResourceLoader(){
	}

	public AssetData getRessource(String assetName){
		return assets3D.get(assetName);
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
		TerrainMesh terrainMesh = terrainMeshLoader.loadTerrainMesh(heightMap, maxHeight, size);
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
	
}
