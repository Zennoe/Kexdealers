package loaders;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector4f;
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

import textures.Material;
import textures.Texture;
import utility.JavaCollectionHacks;

public class StaticMeshLoader {
	
	private static final float[] DEFAULT_VERTICES = {
		-1, -1, -1,
		1, -1, 1,
		1, 1, -1,
		-1, 1, -1,
		-1, -1, 1,
		1, -1, 1,
		1, 1, 1,
		-1, 1, 1
		};
	private static final float[] DEFAULT_TEXTURECOORDS = {
		0, 0,
		1, 0,
		1, 1,
		0, 1
	};
	private static final float[] DEFAULT_NORMALS = {
		0, 0, 1,
		1, 0, 0,
		0, 0, -1,
		-1, 0, 0,
		0, 1, 0,
		0, -1, 0
	};
	private static final int[] DEFAULT_INDICES = {
		0, 1, 3, 3, 1, 2,
	    1, 5, 2, 2, 5, 6,
	    5, 4, 6, 6, 4, 7,
	    4, 0, 7, 7, 0, 3,
	    3, 2, 7, 7, 2, 6,
	    4, 5, 0, 0, 5, 1
	};
	
	private TextureLoader textureLoader;
	private Map<String, Texture> textureCache;
	
	public StaticMeshLoader(TextureLoader textureLoader) {
		this.textureLoader = textureLoader;
		textureCache = new HashMap<>();
	}
	
	public Mesh[] load(String modelPath, String texturePath) {
		return load(modelPath, texturePath, 
				Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals);
	}
	
	public Mesh[] load(String modelPath, String texturePath, int flags) {
		AIScene aiScene = Assimp.aiImportFile(modelPath, flags);
		Mesh[] meshes;
		if(aiScene == null) {
			System.err.println("Failed to load model: " +modelPath +" - using default model");
			meshes = new Mesh[1];
			meshes[0] = new Mesh(DEFAULT_VERTICES, DEFAULT_TEXTURECOORDS, DEFAULT_NORMALS, DEFAULT_INDICES);
			
			return meshes;
		}
		
		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		ArrayList<Material> materials = new ArrayList<>();
		for(int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMaterial(aiMaterial, materials, texturePath);
		}
		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		meshes = new Mesh[numMeshes];
		for(int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = processMesh(aiMesh, materials);
			meshes[i] = mesh;
		}
		
		return meshes;
	}
	
	private void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDir){
		
		AIColor4D color = AIColor4D.create();
		
		AIString path = AIString.calloc();
		Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		String textPath = path.dataString();
		Texture texture = null;
		String texturePath = texturesDir +textPath;
		System.out.println("texturePath: " +texturePath);
		if(textPath != null && textPath.length() > 0) {
			texture = textureCache.get(texturePath);
			if(texture == null) {
				texture = textureLoader.loadTexture(texturePath, false, -1, 0); // TODO: Mipmapping & Anisotropic filtering
				textureCache.put(texturePath, texture);
			}
		}
		// Check if loading has worked
		if(texture == null) {
			texture = textureLoader.createEmptyTexture(256, 256, GL11.GL_RGBA);
			System.err.println("Failed to load texture: " +texturePath +" - using default texture");
		}
		Vector4f ambient = Material.DEFAULT_COLOR;
		int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, color);
		if(result == 0) {
			ambient = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}
		
		Vector4f diffuse = Material.DEFAULT_COLOR;
		result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, color);
		if(result == 0) {
			diffuse = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}
		
		Vector4f specular = Material.DEFAULT_COLOR;
		result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, color);
		if(result == 0) {
			specular = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}
		
		Material material = new Material(ambient, diffuse, specular, 1.0f);
		material.setTexture(texture);
		
		// fix hack for specular maps
		material.setSpecularMap(textureLoader.createEmptyTexture(256, 256, GL11.GL_RGB));
		
		materials.add(material);
	}
	
	private Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
		List<Float> vertices = new ArrayList<>();
		List<Float> textureCoords = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		processVertices(aiMesh, vertices);
		processTextureCoords(aiMesh, textureCoords);
		processNormals(aiMesh, normals);
		processIndices(aiMesh, indices);
		
		Mesh mesh = new Mesh(
				JavaCollectionHacks.listToFloatArray(vertices),
				JavaCollectionHacks.listToFloatArray(textureCoords),
				JavaCollectionHacks.listToFloatArray(normals),
				JavaCollectionHacks.listToIntArray(indices)
		);
		
		Material material;
		int materialID = aiMesh.mMaterialIndex();
		if((materialID >= 0) && (materialID < materials.size())) {
			material = materials.get(materialID);
		} else {
			material = new Material();
		}
		
		mesh.setMaterial(material);
		
		return mesh;
	}
	
	private void processVertices(AIMesh aiMesh, List<Float> vertices) {
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
	}
	
	private void processTextureCoords(AIMesh aiMesh, List<Float> textureCoords) {
		AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
        int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
        for (int i = 0; i < numTextCoords; i++) {
            AIVector3D textCoord = textCoords.get();
            textureCoords.add(textCoord.x());
            textureCoords.add(1 - textCoord.y());
        }
	}
	
	private void processNormals(AIMesh aiMesh, List<Float> normals) {
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals != null && aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
	}
	
	private void processIndices(AIMesh aiMesh, List<Integer> indices) {
		int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
	}
	
}
