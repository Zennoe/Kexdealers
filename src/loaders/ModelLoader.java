package loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import assimp.Mesh;
import assimp.Model;
import textures.Material;
import utility.MiscUtility;
import wrapper.RawMesh;

public class ModelLoader {
	
	private ArrayList<Integer> vaos = new ArrayList<>();
	private ArrayList<Integer> vbos = new ArrayList<>();
	private ArrayList<Integer> ebos = new ArrayList<>();
	
	// ASSIMP MODEL LOADER
	public Model loadModelAssimp(String filename, String texturesDir, int flags) {
		
		AIScene aiScene = Assimp.aiImportFile(filename, flags);
		if(aiScene == null) {
			throw new NullPointerException("Error loading model: " +filename);
		}
		
		int numMaterials = 0;
		AIScene.nmNumMaterials(numMaterials);
		PointerBuffer aiMaterials = aiScene.mMaterials();
		Material[] materials = new Material[numMaterials];
		for(int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMaterial(i, aiMaterial, materials, texturesDir);
		}
		
		int numMeshes = 0;
		AIScene.nmNumMeshes(numMeshes);
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		for(int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			meshes[i] = processMesh(aiMesh, aiScene);
		}
		
		return new Model(materials, meshes);
	}
	
	private static void processMaterial(int index, AIMaterial aiMaterial, Material[] materials, String texturesDir) {
	    AIColor4D colour = AIColor4D.create();
	    
	    AIString path = AIString.calloc();
	    Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
	    String texturePath = path.dataString();
	    
	    Vector4f ambient = Material.DEFAULT_COLOR;
	    int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }
	    
	    Vector4f diffuse = Material.DEFAULT_COLOR;
	    result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }
	    
	    Vector4f specular = Material.DEFAULT_COLOR;
	    result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }
	    
	    Material material = new Material(ambient, diffuse, specular, 0.1f);
	    material.setDiffuseID(MaterialLoader.loadTexture2DbyID(texturePath, "diffuse", true));
	    materials[index] = material;
	}
	
	private Mesh processMesh(AIMesh aiMesh, AIScene aiScene) {
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Float> textureCoords = new ArrayList<>();
		ArrayList<Float> normals = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		while(aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			vertices.add(aiVertex.x());
			vertices.add(aiVertex.y());
			vertices.add(aiVertex.z());
		}
		// I think Assimp is allowing for up to 8 texCoords per vertex, so I'm guessing "index" asks for which one you want.
		AIVector3D.Buffer aiTextureCoords = aiMesh.mTextureCoords(0);
		while(aiTextureCoords.remaining() > 0) {
			AIVector3D aiTC = aiTextureCoords.get();
			textureCoords.add(aiTC.x());
			textureCoords.add(aiTC.y());
			textureCoords.add(aiTC.z());
		}
		
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
		while(aiNormals.remaining() > 0) {
			AIVector3D aiNormal = aiNormals.get();
			normals.add(aiNormal.x());
			normals.add(aiNormal.y());
			normals.add(aiNormal.z());
		}
		
		AIFace.Buffer aiIndices = aiMesh.mFaces();
		Iterator<AIFace> iterator = aiIndices.iterator();
		while(iterator.hasNext()) {
			IntBuffer buffer = iterator.next().mIndices();
			for(int x : buffer.array()) {
				indices.add(x);
			}
			
		}
		
		return new Mesh(
				MiscUtility.toFloatArray(vertices),
				MiscUtility.toFloatArray(textureCoords),
				MiscUtility.toFloatArray(normals),
				MiscUtility.toIntArray(indices)
				);
	}
	
	
	
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
