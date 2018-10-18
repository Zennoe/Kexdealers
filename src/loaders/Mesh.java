package loaders;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

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
		GL30C.glDeleteVertexArrays(VAO);
		GL15C.glDeleteBuffers(vertexVBO);
		GL15C.glDeleteBuffers(tcVBO);
		GL15C.glDeleteBuffers(normalVBO);
		GL15C.glDeleteBuffers(EBO);
	}
	
	private void setupMesh() {		
		// Create and bind VAO
		VAO = GL30C.glGenVertexArrays();
		GL30C.glBindVertexArray(VAO);
		// Create, bind, and fill VBOs
		vertexVBO = storeInVBO(0, 3, vertices);
		tcVBO = storeInVBO(1, 2, textureCoords);
		normalVBO = storeInVBO(2, 3, normals);
		// Create, bind, and fill EBO with index data
		EBO = GL15C.glGenBuffers();
		GL15C.glBindBuffer(GL15C.GL_ELEMENT_ARRAY_BUFFER, EBO);
		GL15C.glBufferData(GL15C.GL_ELEMENT_ARRAY_BUFFER, 
				MiscUtility.storeDataInIntBuffer(indices), GL15C.GL_STATIC_DRAW);
		// Release VAO bind
		GL30C.glBindVertexArray(0);
	}
	
	private int storeInVBO(int index, int coordSize, float[] vertices){
		int vboID = GL15C.glGenBuffers();
		GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = MiscUtility.storeDataInFloatBuffer(vertices);
		GL15C.glBufferData(GL15C.GL_ARRAY_BUFFER, buffer, GL15C.GL_STATIC_DRAW);
		GL20C.glVertexAttribPointer(index, coordSize, GL11C.GL_FLOAT, false, 0, 0);
		GL15C.glBindBuffer(GL15C.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
}
