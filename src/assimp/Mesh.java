package assimp;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {
	
	private Vertex[] vertices;
	private Texture[] textures;
	private int[] indices;
	
	private int VAO, VBO, EBO;
	
	public Mesh(Vertex[] vertices, Texture[] textures, int[] indices) {
		this.vertices = vertices;
		this.textures = textures;
		this.indices = indices;
	}

	public void delete() {
		// TODO: implement Mesh deletion
	}
	
	private void setupMesh() {
		// Transform data into continuous float arrays
		float[] verticesContinuous = new float[vertices.length * Vertex.CONTINUOUS_LENGTH];
		for(int i = 0; i < vertices.length; i++) {
			float[] contVertexData = vertices[i].getAsContinuousData();
			System.arraycopy(contVertexData, 0, verticesContinuous, i * Vertex.CONTINUOUS_LENGTH, Vertex.CONTINUOUS_LENGTH);
		}
		
		// Create and bind VAO
		VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		// Create, bind, and fill VBO
		VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 
				storeDataInFloatBuffer(verticesContinuous), GL15.GL_STATIC_DRAW);
		// Create, bind, and fill EBO with index data
		EBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, 
				storeDataInIntBuffer(indices), GL15.GL_STATIC_DRAW);
		
		// Vertex Positions
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.CONTINUOUS_LENGTH, Vertex.POSITION_OFFSET);
		// Vertex Normals
		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, Vertex.CONTINUOUS_LENGTH, Vertex.NORMAL_OFFSET); // # of floats times 4 bytes
		// Vertex Texture Coords
		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, Vertex.CONTINUOUS_LENGTH, Vertex.TEXCOORDS_OFFSET);
		
		// Release VAO bind
		GL30.glBindVertexArray(0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}
