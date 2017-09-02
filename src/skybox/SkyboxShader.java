package skybox;

import org.joml.Matrix4f;

import render.ShaderProgram;

public class SkyboxShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "skyboxFragmentShader.txt";
     
    private int location_modelMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    
    private int location_cubeMap;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }
    
    public void loadModelMatrix(Matrix4f modelMatrix){
    	super.loadMatrix4f(location_modelMatrix, modelMatrix);
    }
    
    public void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
    }
 
    public void loadViewMatrix(Matrix4f viewMatrix){
    	// remove the translation from the viewMatrix so the player can not 
    	// reach the edge of the skybox
    	Matrix4f remTranslationMat = viewMatrix;
    	remTranslationMat.setTranslation(0.0f, 0.0f, 0.0f);
    	super.loadMatrix4f(location_viewMatrix, remTranslationMat);
    }
    
    public void loadCubeMap(){
    	super.loadInt(location_cubeMap, 0);
    }
    
    protected void getAllUniformLocations() {
    	location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        
        location_cubeMap = super.getUniformLocation("cubeMap");
    }

}
