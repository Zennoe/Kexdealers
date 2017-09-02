package skybox;

import org.joml.Matrix4f;

import render.ShaderProgram;

public class SkyboxShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "skyboxFragmentShader.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    
    private int location_cubeMap;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }
     
    public void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
    }
 
    public void loadViewMatrix(Matrix4f viewMatrix){
    	//Matrix3f vm = new Matrix3f();
    	//viewMatrix.get3x3(vm);
    	super.loadMatrix4f(location_viewMatrix, viewMatrix);
    }
    
    public void loadCubeMap(){
    	super.loadInt(location_cubeMap, 0);
    }
    
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        
        location_cubeMap = super.getUniformLocation("cubeMap");
    }

}
