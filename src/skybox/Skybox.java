package skybox;

import wrapper.RawMesh;

public class Skybox {
	
	private RawMesh mesh;
	private int textureID;
	
	public Skybox(RawMesh mesh, int textureID){
		this.mesh = mesh;
		this.textureID = textureID;
	}

	public RawMesh getMesh() {
		return mesh;
	}

	public int getTextureID() {
		return textureID;
	}
	
}
