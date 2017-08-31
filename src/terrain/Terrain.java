package terrain;

import org.joml.Matrix4f;

import textures.MultiTexture;
import textures.Texture;
import wrapper.RawMesh;

public class Terrain {

	private TerrainMesh terrainMesh;
	private MultiTexture multiTexture;
	private Texture blendMap;
	
	public Terrain(TerrainMesh terrainMesh, MultiTexture multiTexture, Texture blendMap){
		this.terrainMesh = terrainMesh;
		this.multiTexture = multiTexture;
		this.blendMap = blendMap;
	}
	
	public RawMesh getRawMesh(){
		return terrainMesh.getRawMesh();
	}
	
	public MultiTexture getMultiTexture(){
		return multiTexture;
	}
	
	public Texture getBlendMap(){
		return blendMap;
	}
	
	public float getHeightAtPoint(float x, float z){
		return terrainMesh.getHeightAtPoint(x, z);
	}
	
	public Matrix4f getModelMatrix(){
		return terrainMesh.getModelMatrix();
	}
}
