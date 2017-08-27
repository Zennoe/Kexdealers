package render;

import java.util.HashSet;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import ecs.PointLightComponent;
import example.DirectionalLight;

public class EntityShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "entityVertexShader.txt";
	private static final String FRAGMENT_FILE = "entityFragmentShader.txt";
	
	private static final int MAX_LIGHTS = 4; // Limit on how many lights can affect one point
	
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_viewPosition;
	// Material struct attributes
	private int location_diffuseMap;
	private int location_specularMap;
	private int location_shininess;
	// Directional Light struct attributes
	private int location_dirLightDirection;
	private int location_dirLightAmbient;
	private int location_dirLightDiffuse;
	private int location_dirLightSpecular;
	// Point Light struct attributes
	private int[] location_lightPosition;
	private int[] location_lightAmbient;
	private int[] location_lightDiffuse;
	private int[] location_lightSpecular;
	private int[] location_lightAttenuation;
	
	public EntityShader(){
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}
	
	protected void getAllUniformLocations(){
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewPosition = super.getUniformLocation("viewPos");
		// Material
		location_diffuseMap = super.getUniformLocation("material.diffuse");
		location_specularMap = super.getUniformLocation("material.specular");
		location_shininess = super.getUniformLocation("material.shininess");
		// Directional Light
		location_dirLightDirection = super.getUniformLocation("dirLight.direction");
		location_dirLightAmbient = super.getUniformLocation("dirLight.ambient");
		location_dirLightDiffuse = super.getUniformLocation("dirLight.diffuse");
		location_dirLightSpecular = super.getUniformLocation("dirLight.specular");
		// Point Lights
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightAmbient = new int[MAX_LIGHTS];
		location_lightDiffuse = new int[MAX_LIGHTS];
		location_lightSpecular = new int[MAX_LIGHTS];
		location_lightAttenuation = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++){
			location_lightPosition[i] = super.getUniformLocation("pointLights[" +i +"].position");
			location_lightAmbient[i] = super.getUniformLocation("pointLights[" +i +"].ambient");
			location_lightDiffuse[i] = super.getUniformLocation("pointLights[" +i +"].diffuse");
			location_lightSpecular[i] = super.getUniformLocation("pointLights[" +i +"].specular");
			location_lightAttenuation[i] = super.getUniformLocation("pointLights[" +i +"].attenuation");
		}
	}
	
	public void uploadMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix){
		super.loadMatrix4f(location_modelMatrix, modelMatrix);
		super.loadMatrix4f(location_viewMatrix, viewMatrix);
		super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
	}
	
	public void uploadMaterial(float shininess){
		super.loadInt(location_diffuseMap, 0);
		super.loadInt(location_specularMap, 1);
		super.loadFloat(location_shininess, shininess);
	}
	
	public void uploadViewPos(Vector3f cameraPosition){
		super.loadVector3f(location_viewPosition, cameraPosition);
	}
	
	public void uploadDirectionalLight(DirectionalLight dirLight){
		super.loadVector3f(location_dirLightDirection, dirLight.getDirection());
		super.loadVector3f(location_dirLightAmbient, dirLight.getAmbient());
		super.loadVector3f(location_dirLightDiffuse, dirLight.getDiffuse());
		super.loadVector3f(location_dirLightSpecular, dirLight.getSpecular());
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
				super.loadVector3f(location_lightSpecular[i], pointLight.getSpecular());
				super.loadVector3f(location_lightAttenuation[i], pointLight.getAttenuation());
			}else{
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightAmbient[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightDiffuse[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightSpecular[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
}
