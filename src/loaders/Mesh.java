package loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import textures.Material;
import textures.Texture;

public class Mesh {
	
	private final int vaoID;
	
	private final List<Integer> vboIDList;
	
	private final int vertexCount;
	
	private Material material;
	
	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		FloatBuffer vecNormalsBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			vertexCount = indices.length;
			vboIDList = new ArrayList<>();
			
			vaoID = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoID);
			
			// Position VBO
			int vboID = GL15.glGenBuffers();
			vboIDList.add(vboID);
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, posBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
			
			// Texture coordinates VBO
			vboID = GL15.glGenBuffers();
			vboIDList.add(vboID);
			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			textCoordsBuffer.put(textCoords).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textCoordsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboID = GL15.glGenBuffers();
            vboIDList.add(vboID);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vecNormalsBuffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);

            // Index VBO
            vboID = GL15.glGenBuffers();
            vboIDList.add(vboID);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
            
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void delete() {
		GL20.glDisableVertexAttribArray(0);
		// Delete the VBOs
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		for(int vboID : vboIDList) {
			GL15.glDeleteBuffers(vboID);
		}
		// Delete the texture
		Texture texture = material.getTexture();
		if(texture != null) {
			texture.delete();
		}
		// Delete the VAO
		GL30.glBindVertexArray(vaoID);
		GL30.glDeleteVertexArrays(vaoID);
	}
}
