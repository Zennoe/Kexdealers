package loaders;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import utility.MiscUtility;

public class Mesh {
	
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	
	private int VAO, EBO;
	private int vertexVBO, tcVBO, normalVBO;
	
	public Mesh(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		
		setupMesh();
	}
	
	public int getVaoID() {
		return VAO;
	}
	
	public int getVertexCount() {
		return vertices.length / 3;
	}
	
	/**
	 * Deletes data stored on the graphics card. 
	 * Don't forget that you still need to dereference this Mesh instance :) 
	 */
	public void delete() {
		GL30.glDeleteVertexArrays(VAO);
		GL15.glDeleteBuffers(vertexVBO);
		GL15.glDeleteBuffers(tcVBO);
		GL15.glDeleteBuffers(normalVBO);
		GL15.glDeleteBuffers(EBO);
	}
	
	private void setupMesh() {		
		// Create and bind VAO
		VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		// Create, bind, and fill VBOs
		vertexVBO = storeInVBO(0, 3, vertices);
		tcVBO = storeInVBO(1, 2, textureCoords);
		normalVBO = storeInVBO(2, 3, normals);
		// Create, bind, and fill EBO with index data
		EBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, 
				MiscUtility.storeDataInIntBuffer(indices), GL15.GL_STATIC_DRAW);
		// Release VAO bind
		GL30.glBindVertexArray(0);
	}
	
	private int storeInVBO(int index, int coordSize, float[] vertices){
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = MiscUtility.storeDataInFloatBuffer(vertices);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
}
