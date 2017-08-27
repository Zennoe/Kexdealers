package example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import terrain.TerrainTile;
import textures.MultiTexture;
import textures.Texture;

public class ResourceLoader {
	
	private final String defaultTexture = "";
	private final String rTex = "";
	private final String gTex = "";
	private final String bTex = "";
	private final String blendMap = "";
	
	AssetDataFactory assetDataFactory;
	MaterialLoader materialLoader;
	
	private HashMap<String, Integer> pointerCounter = new HashMap<>();
	private HashMap<String, AssetData> assets = new HashMap<>();
	
	public ResourceLoader(){
		// preload keys
		assets.put("player", null);
		pointerCounter.put("player", 0);
		
		// create tools
		assetDataFactory = new AssetDataFactory();
		materialLoader = new MaterialLoader();
	}
	
	public void load(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 0){
			// load fresh from HDD
			assets.put(assetName, assetDataFactory.build(assetName, materialLoader));
		}
		x++;
	}
	
	public void unload(String assetName){
		int x = pointerCounter.get(assetName);
		if(x == 1){
			x--;
			//unload completely
			assets.put(assetName, null);
		}else{
			x--;
		}
	}
	
	public AssetData getRessource(String assetName){
		return assets.get(assetName);
	}

	public TerrainTile getTerrainTile(){
		
		BufferedImage heightMap = null;
		int max_pixel_color = 256 * 256 * 256;
		int max_height = 64;
		
		try{
			heightMap = ImageIO.read(new File("res/heightMap.png"));
		}catch(IOException e){
			System.err.println("Error loading heightMap");
		}
		
		return generateTerrainTile(heightMap, 0, 0, 500);
	}
	
	public MultiTexture getMultiTexture(){
		return materialLoader.loadMultiTexture(defaultTexture, rTex, gTex, bTex);
	}
	
	public Texture getBlendMap(){
		return materialLoader.loadBlendMap(blendMap);
	}
	
	private TerrainTile generateTerrainTile(BufferedImage heightMap, int gridX, int gridZ, int tileSize){
		
		float[][] heights = new float[tileSize][tileSize];
		
		int count = tileSize * tileSize;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(tileSize-1)*(tileSize-1)];
		int vertexPointer = 0;
		for(int i=0;i<tileSize;i++){
			for(int j=0;j<tileSize;j++){
				vertices[vertexPointer*3] = (float)j/((float)tileSize - 1) * tileSize;
				float height = getColorHeight(j + gridX, i + gridZ, heightMap);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)tileSize - 1) * tileSize;
				Vector3f normal = calculateTerrainNormal(j + gridX, i + gridZ);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)tileSize - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)tileSize - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<tileSize-1;gz++){
			for(int gx=0;gx<tileSize-1;gx++){
				int topLeft = (gz*tileSize)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*tileSize)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return new TerrainTile(modelLoader.loadToVAO(vertices, indices, textureCoords, normals), heights, gridX, gridZ);
	}
	
	private float getColorHeight(int x, int z, BufferedImage heightMap){
		if(x < 0 || x >= heightMap.getWidth() || z < 0 || z >= heightMap.getWidth()){
			return 0;
		}
		float height = heightMap.getRGB(x, z);
		height += max_pixel_color / 2f;
		height /= max_pixel_color / 2f;
		height *= max_height;
		return height;
	}
	
	private Vector3f calculateTerrainNormal(int x, int z){
		float heightL = getColorHeight(x-1, z);
		float heightR = getColorHeight(x+1, z);
		float heightD = getColorHeight(x, z-1);
		float heightU = getColorHeight(x, z+1);
		return new Vector3f(heightL-heightR, 2.0f, heightD-heightU).normalize();
	}
}
