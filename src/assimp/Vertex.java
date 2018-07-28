package assimp;

import org.joml.Vector2fc;
import org.joml.Vector3fc;

public class Vertex {
	
	public static final int CONTINUOUS_LENGTH = 2 * 3 + 2;
	public static final int POSITION_OFFSET = 0;
	public static final int NORMAL_OFFSET = 3 * 4;
	public static final int TEXCOORDS_OFFSET = 3 * 4 + NORMAL_OFFSET;
	
	private Vector3fc position;
	private Vector3fc normal;
	private Vector2fc texCoords;
	
	public Vertex(Vector3fc position, Vector3fc normal, Vector2fc texCoords) {
		this.position = position;
		this.normal = normal;
		this.texCoords = texCoords;
	}
	
	/**
	 * Retrieve Vertex contents as continuous float array to imitate C/C++ struct properties
	 * @return Vertex data in continuous form.
	 */
	public float[] getAsContinuousData() {
		float[] data = new float[CONTINUOUS_LENGTH];
		
		data[0] = position.x();
		data[1] = position.y();
		data[2] = position.z();
		
		data[3] = normal.x();
		data[4] = normal.y();
		data[5] = normal.z();
		
		data[6] = texCoords.x();
		data[7] = texCoords.y();
		
		return data;
	}
}
