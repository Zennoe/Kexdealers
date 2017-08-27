package terrain;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import wrapper.RawModel;
import utility.MathUtil;

public class TerrainTile {

	private float x, z;
	private int tileSize = 128;
	private RawModel terrainModel;
	
	private Matrix4f transformation;
	
	// Heights for access in collision 
	private float[][] heights;
	
	public TerrainTile(RawModel terrainModel, float[][] heights, int gridX, int gridZ){
		this.terrainModel = terrainModel;
		this.heights = heights;
		this.x = gridX;
		this.z = gridZ;
		
		transformation = new Matrix4f();
	}
	
	public Matrix4f getModelMatrix(){
		transformation.identity()
			.translate(x, 0.0f, z)
			.rotateX(0.0f)
			.rotateY(0.0f)
			.rotateZ(0.0f)
			.scale(1.0f);
		
		return transformation;
	}
	
	public RawModel getRawModel(){
		return terrainModel;
	}
	
	public float getHeightAtPoint(float x, float z){
		//float terrainX = x - this.x;
		//float terrainZ = z - this.z;
		float terrainX = x;
		float terrainZ = z;
		// Vertex level grid!
		float gridSquareSize = tileSize / (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		boolean one = gridX >= heights.length - 1;
		boolean two = gridZ >= heights.length - 1;
		boolean three = gridX < 0;
		boolean four = gridZ < 0;
		if(one || two || three || four){
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize);
		float zCoord = (terrainZ % gridSquareSize);
		float answer;
		if(xCoord <= (1 -zCoord)){// if true, upper left triangle, else bottom right
			answer = MathUtil
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}else{
			answer = MathUtil
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

}
