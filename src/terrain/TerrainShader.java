package terrain;

import java.util.HashSet;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ecs.PointLightComponent;
import example.DirectionalLight;
import render.ShaderProgram;

public class TerrainShader extends ShaderProgram{

	private static final String VERTEX_FILE = "terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "terrainFragmentShader.txt";
	
	private static final int MAX_LIGHTS = 4; // Limit on how many lights can affect one point
	
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	// MultiTexture struct attributes
	private int location_defaultTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	// Directional Light struct attributes
	private int location_dirLightDirection;
	private int location_dirLightAmbient;
	private int location_dirLightDiffuse;
	// Point Light struct attributes
	private int[] location_lightPosition;
	private int[] location_lightAmbient;
	private int[] location_lightDiffuse;
	private int[] location_lightRadius;
	private int[] location_lightCutoff;
	
	public TerrainShader(){
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}
	
	protected void getAllUniformLocations(){
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		// MultiTexture
		location_defaultTexture = super.getUniformLocation("multiTexture.defaultDiffuse");
		location_rTexture = super.getUniformLocation("multiTexture.rDiffuse");
		location_gTexture = super.getUniformLocation("multiTexture.gDiffuse");
		location_bTexture = super.getUniformLocation("multiTexture.bDiffuse");
		location_blendMap = super.getUniformLocation("multiTexture.blendMap");
		// Directional Light
		location_dirLightDirection = super.getUniformLocation("dirLight.direction");
		location_dirLightAmbient = super.getUniformLocation("dirLight.ambient");
		location_dirLightDiffuse = super.getUniformLocation("dirLight.diffuse");
		// Point Lights
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightAmbient = new int[MAX_LIGHTS];
		location_lightDiffuse = new int[MAX_LIGHTS];
		location_lightRadius = new int[MAX_LIGHTS];
		location_lightCutoff = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++){
			location_lightPosition[i] = super.getUniformLocation("pointLights[" +i +"].position");
			location_lightAmbient[i] = super.getUniformLocation("pointLights[" +i +"].ambient");
			location_lightDiffuse[i] = super.getUniformLocation("pointLights[" +i +"].diffuse");
			location_lightRadius[i] = super.getUniformLocation("pointLights[" +i +"].radius");
			location_lightCutoff[i] = super.getUniformLocation("pointLights[" +i +"].cutoff");
		}
	}
	
	public void uploadMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix){
		super.loadMatrix4f(location_modelMatrix, modelMatrix);
		super.loadMatrix4f(location_viewMatrix, viewMatrix);
		super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
	}
	
	public void connectMultiTexture(){
		super.loadInt(location_defaultTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	public void uploadDirectionalLight(DirectionalLight dirLight){
		super.loadVector3f(location_dirLightDirection, dirLight.getDirection());
		super.loadVector3f(location_dirLightAmbient, dirLight.getAmbient());
		super.loadVector3f(location_dirLightDiffuse, dirLight.getDiffuse());
	}
	
	public void uploadPointLights(HashSet<PointLightComponent> pointLights){
		PointLightComponent pointLight;
		Iterator<PointLightComponent> plcIterator = pointLights.iterator();
		for(int i = 0; i < MAX_LIGHTS; i++){
			if(i < pointLights.size()){
				pointLight = (PointLightComponent) plcIterator.next();
				super.loadVector3f(location_lightPosition[i], pointLight.getPosition());
				super.loadVector3f(location_lightAmbient[i], pointLight.getAmbient());
				super.loadVector3f(location_lightDiffuse[i], pointLight.getDiffuse());
				super.loadFloat(location_lightRadius[i], pointLight.getRadius());
				super.loadFloat(location_lightCutoff[i], pointLight.getCutoff());
			}else{
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightAmbient[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightDiffuse[i], new Vector3f(0, 0, 0));
				super.loadFloat(location_lightRadius[i], 0);
				super.loadFloat(location_lightCutoff[i], 0);
			}
		}
	}
}
