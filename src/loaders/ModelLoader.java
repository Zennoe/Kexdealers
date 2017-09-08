package loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import wrapper.RawMesh;

public class ModelLoader {
	
	private ArrayList<Integer> vaos = new ArrayList<>();
	private ArrayList<Integer> vbos = new ArrayList<>();
	private ArrayList<Integer> ebos = new ArrayList<>();
	
	// General purpose loader
	public RawMesh loadToVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals){
		int vaoID = createVAO();
		// Store vertices
		storeInVBO(0, 3, positions);
		// Store indices
		storeInEBO(indices);
		// Store textureCoords
		storeInVBO(1, 2, textureCoords);
		// Store normals
		storeInVBO(2, 3, normals);
		
		unbindVAO();
		return new RawMesh(vaoID, indices.length);
	}
	
	// Normal mapped loader - WIP
	public RawMesh loadToVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals, float[] tangents){
		int vaoID = createVAO();
		// Store vertices
		storeInVBO(0, 3, positions);
		// Store indices
		storeInEBO(indices);
		// Store textureCoords
		storeInVBO(1, 2, textureCoords);
		// Store normals
		storeInVBO(2, 3, normals);
		
		unbindVAO();
		return new RawMesh(vaoID, indices.length);
	}
	
	// Variable dimension loader
	public RawMesh loadToVAO(float[] positions, int dimensions){
		int vaoID = createVAO();
		// Store vertices
		storeInVBO(0, dimensions, positions);
		
		unbindVAO();
		return new RawMesh(vaoID, positions.length / dimensions);
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	private void storeInVBO(int index, int coordSize, float[] vertices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(vertices);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void storeInEBO(int[] indices){
		int eboID = GL15.glGenBuffers();
		ebos.add(eboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public void cleanUp(){
		for(int i : vaos){
			GL30.glDeleteVertexArrays(i);
		}
		for(int i : vbos){
			GL15.glDeleteBuffers(i);
		}
		for(int i : ebos){
			GL15.glDeleteBuffers(i);
		}
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
