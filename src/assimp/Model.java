package assimp;

import textures.Material;

public class Model {
	
	private Material[] materials;
	private Mesh[] meshes;
	
	public Model(Material[] materials, Mesh[] meshes) {
		this.materials = materials;
		this.meshes = meshes;
	}

	public Material[] getMaterials() {
		return materials;
	}

	public Mesh[] getMeshes() {
		return meshes;
	}
	
	public int getPartCount() {
		return meshes.length;
	}
	
}
