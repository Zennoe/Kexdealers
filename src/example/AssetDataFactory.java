package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import textures.Material;
import wrapper.Vertex;

public class AssetDataFactory {
	// global
	private static final String RES_LOC = "res/";
	
	private ArrayList<Integer> vaos = new ArrayList<>();
	private ArrayList<Integer> vbos = new ArrayList<>();
	private ArrayList<Integer> ebos = new ArrayList<>();
	
	// per instance
	private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    // what it do?
    private float furthestPoint;
	
	private int vaoID;
	private int vertexCount;
	
	private int diffuseMap;
	private int specularMap;
	
	private Vector3f phongAmbient;
	private Vector3f phongDiffuse;
	private Vector3f phongSpecular;
	private float specularExp = 0.5f;
	
	private float transparency;
	
	public AssetData build(String assetName, MaterialLoader materialLoader){
		// Step 1
		loadOBJ(assetName);
		// Step 2
		loadToVAO();
		// Step 3
		Material material = materialLoader.loadMaterial(assetName, 1);
		diffuseMap = material.getDiffuseID();
		specularMap = material.getSpecularID();
		
		
		AssetData newAsset = new AssetData(vaoID, vertexCount, diffuseMap, specularMap);
		newAsset.setPhongData(phongAmbient, phongDiffuse, phongSpecular, specularExp);
		newAsset.setTransparency(transparency);
		return newAsset;
	}
	
	// STEP ONE
	// ---------------------
	public void loadOBJ(String objFileName) {
        FileReader isr = null;
        File objFile = new File(RES_LOC + objFileName + ".obj");
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read file: " +objFileName +".obj");
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> verticesArray = new ArrayList<Vertex>();
        List<Vector2f> texturesArray = new ArrayList<Vector2f>();
        List<Vector3f> normalsArray = new ArrayList<Vector3f>();
        List<Integer> indicesArray = new ArrayList<Integer>();
        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(verticesArray.size(), vertex);
                    verticesArray.add(newVertex);
 
                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]));
                    texturesArray.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    normalsArray.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, verticesArray, indicesArray);
                processVertex(vertex2, verticesArray, indicesArray);
                processVertex(vertex3, verticesArray, indicesArray);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file " +objFileName);
        }
        removeUnusedVertices(verticesArray);
        vertices = new float[verticesArray.size() * 3];
        textureCoords = new float[verticesArray.size() * 2];
        normals = new float[verticesArray.size() * 3];
        furthestPoint = convertDataToArrays(verticesArray, texturesArray, normalsArray, vertices,
                textureCoords, normals);
        indices = convertIndicesListToArray(indicesArray);
    }
 
    private void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }
 
    private int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }
 
    private float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
            float[] normalsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        return furthestPoint;
    }
 
    private void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
            int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }
 
        }
    }
     
    private void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
	
    // STEP TWO
    // ---------------------	
	public void loadToVAO(){
		vaoID = createVAO();
		// Store vertices
		storeInVBO(0, 3, vertices);
		// Store indices
		storeInEBO(indices);
		// Store textureCoords
		storeInVBO(1, 2, textureCoords);
		// Store normals
		storeInVBO(2, 3, normals);
		
		unbindVAO();
		
		vertexCount = indices.length;
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
