package loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import terrain.TerrainMesh;

public class TerrainMeshLoader {
	
	private static final int MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	ModelLoader modelLoader;
	
	public TerrainMeshLoader(ModelLoader modelLoader){
		this.modelLoader = modelLoader;
	}
	
	public TerrainMesh loadTerrainMesh(String heightMapFile, int maxHeight, int tileSize){
		
		BufferedImage heightMap = null;
		
		try{
			heightMap = ImageIO.read(new File("res/" +heightMapFile +".png"));
		}catch(IOException e){
			System.err.println("Error loading heightMap");
		}
		
		return generateTerrainTile(heightMap, maxHeight, tileSize);
	}
	
	private TerrainMesh generateTerrainTile(BufferedImage heightMap, int maxHeight, int tileSize){
		
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
				float height = getColorHeight(j, i, heightMap, maxHeight);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)tileSize - 1) * tileSize;
				Vector3f normal = calculateTerrainNormal(j, i, heightMap, maxHeight);
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
		
		return new TerrainMesh(modelLoader.loadToVAO(vertices, indices, textureCoords, normals), heights, tileSize);
	}
	
	private float getColorHeight(int x, int z, BufferedImage heightMap, int maxHeight){
		if(x < 0 || x >= heightMap.getWidth() || z < 0 || z >= heightMap.getWidth()){
			return 0;
		}
		float height = heightMap.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= maxHeight;
		return height;
	}
	
	private Vector3f calculateTerrainNormal(int x, int z, BufferedImage heightMap, int maxHeight){
		float heightL = getColorHeight(x-1, z, heightMap, maxHeight);
		float heightR = getColorHeight(x+1, z, heightMap, maxHeight);
		float heightD = getColorHeight(x, z-1, heightMap, maxHeight);
		float heightU = getColorHeight(x, z+1, heightMap, maxHeight);
		return new Vector3f(heightL-heightR, 2.0f, heightD-heightU).normalize();
	}
}
