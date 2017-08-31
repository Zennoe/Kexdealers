package example;

import org.joml.Vector3f;

import textures.Material;
import wrapper.RawMesh;

public class AssetData {

	private RawMesh rawMesh;
	
	private Material material;
	
	private Vector3f phongAmbient;
	private Vector3f phongDiffuse;
	private Vector3f phongSpecular;
	private float specularExp = 0.5f;
	
	private float transparency;
	
	public AssetData(RawMesh rawMesh, Material material){
		this.rawMesh = rawMesh;
		this.material = material;
	}
	
	public AssetData setPhongData(Vector3f ambient, Vector3f diffuse, Vector3f specular, float specExp){
		this.phongAmbient = ambient;
		this.phongDiffuse = diffuse;
		this.phongSpecular = specular;
		this.specularExp = specExp;
		return this;
	}
	
	public AssetData setTransparency(float transparency){
		this.transparency = transparency;
		return this;
	}
	
	public RawMesh getRawMesh(){
		return rawMesh;
	}

	public Vector3f getPhongAmbient(){
		return phongAmbient;
	}

	public Vector3f getPhongDiffuse(){
		return phongDiffuse;
	}

	public Vector3f getPhongSpecular(){
		return phongSpecular;
	}
	
	public float getPhongSpecularExponent(){
		return specularExp;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public float getTransparency(){
		return transparency;
	}
}
