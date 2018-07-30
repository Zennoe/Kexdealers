package loaders;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class Material {
	
	public static final Vector3f DEFAULT_COLOR = new Vector3f(0.5f, 0.0f, 0.5f);
	public static final int DEFAULT_MAP = 0;
	
	private final Vector3f ambientColor;
	private final Vector3f diffuseColor;
	private final Vector3f specularColor;

	private final int diffuseMap;
	private final int specularMap;
	private final int normalMap;
	
	public Material(Vector3f ambientColor, Vector3f diffuseColor, Vector3f specularColor, int diffuseMap,
			int specularMap, int normalMap) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.diffuseMap = diffuseMap;
		this.specularMap = specularMap;
		this.normalMap = normalMap;
	}
	
	/**
	 * Creates and loads a new Material. 
	 * Any supplied maps will be loaded to video memory immediately.
	 * 
	 * @param ambientColor
	 * @param diffuseColor
	 * @param specularColor
	 * @param mappings -> Takes optional mappings for diffuse, specular, and normal mappings. Have to be in this order.
	 * Having a normal map requires a specular map. Yeah.
	 * @return
	 * Returns the loaded Material instance.
	 */
	public static Material createMaterial(Vector3f ambientColor, Vector3f diffuseColor, Vector3f specularColor, String...mappings) {
		
		Vector3f aC = (ambientColor != null) ? ambientColor : DEFAULT_COLOR;
		Vector3f dC = (diffuseColor != null) ? diffuseColor : DEFAULT_COLOR;
		Vector3f sC = (specularColor != null) ? specularColor : DEFAULT_COLOR;
		
		int diffuseMap = DEFAULT_MAP;
		int specularMap = DEFAULT_MAP;
		int normalMap = DEFAULT_MAP;
		
		for(int i = 0; (i < mappings.length) || (i >= 2); i++) {
			switch(i) {
			case 0: diffuseMap = loadMappingToVideoMemory(mappings[i], true); break;
			case 1: specularMap = loadMappingToVideoMemory(mappings[i], false); break;
			case 2: normalMap = loadMappingToVideoMemory(mappings[i], false); break;
			}
		}
		
		return new Material(aC, dC, sC, diffuseMap, specularMap, normalMap);
	}
	
	/**
	 * Deletes data stored on the graphics card. 
	 * Don't forget that you still need to dereference this Material instance :) 
	 */
	public void destroyMaterial() {
		GL11.glDeleteTextures(diffuseMap);
		GL11.glDeleteTextures(specularMap);
		GL11.glDeleteTextures(normalMap);
	}
	
	private static int loadMappingToVideoMemory(String name, boolean gammaCorrected) {
		// TODO: implement
		return DEFAULT_MAP;
	}

	public Vector3f getAmbientColor() {
		return ambientColor;
	}

	public Vector3f getDiffuseColor() {
		return diffuseColor;
	}

	public Vector3f getSpecularColor() {
		return specularColor;
	}

	public int getDiffuseMapID() {
		return diffuseMap;
	}

	public int getSpecularMapID() {
		return specularMap;
	}

	public int getNormalMapID() {
		return normalMap;
	}
	
}
