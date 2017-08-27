package example;

import org.joml.Vector3f;

public class AssetData {

	private int vaoID;
	private int vertexCount;
	
	private int diffuseMap;
	private int specularMap;
	
	private Vector3f phongAmbient;
	private Vector3f phongDiffuse;
	private Vector3f phongSpecular;
	private float specularExp;
	
	private float transparency;
	
	public AssetData(int vaoID, int vertexCount, int diffuseMap, int specularMap){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.diffuseMap = diffuseMap;
		this.specularMap = specularMap;
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
	
	public int getVAOID(){
		return vaoID;
	}
	
	public int getVertexCount(){
		return vertexCount;
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
	
	public int getDiffuseMap(){
		return diffuseMap;
	}
	
	public int getSpecularMap(){
		return specularMap;
	}
	
	public float getTransparency(){
		return transparency;
	}
}
