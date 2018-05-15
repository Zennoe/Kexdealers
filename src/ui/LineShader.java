package ui;

import org.joml.Matrix4f;

import render.ShaderProgram;

public class LineShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "lineVertexShader.txt";
    private static final String FRAGMENT_FILE = "lineFragmentShader.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    
    public LineShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        getAllUniformLocations();
    }
    
    public void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix4f(location_projectionMatrix, projectionMatrix);
    }
 
    public void loadViewMatrix(Matrix4f viewMatrix){
    	super.loadMatrix4f(location_viewMatrix, viewMatrix);
    }
    
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projection");
        location_viewMatrix = super.getUniformLocation("view");
    }

}
