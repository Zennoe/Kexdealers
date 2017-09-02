package render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public  abstract class ShaderProgram{

	private int programID;
	
	private float[] matrixBuffer4f = new float[16];
	private float[] matrixBuffer3f = new float[9];
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		programID = buildShaderProgram(vertexFile, fragmentFile);
	}
	
	private int buildShaderProgram(String vShader, String fShader){
		int vShaderID;
		int fShaderID;
		int shaderProgramID;
		// CREATE VERTEX SHADER
		vShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vShaderID, readShaderFile(vShader));
		GL20.glCompileShader(vShaderID);
		if(GL20.glGetShaderi(vShaderID, GL20.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20.glGetShaderInfoLog(vShaderID));
			System.exit(1);
		}
		
		// CREATE FRAGMENT SHADER
		fShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fShaderID, readShaderFile(fShader));
		GL20.glCompileShader(fShaderID);
		if(GL20.glGetShaderi(fShaderID, GL20.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20.glGetShaderInfoLog(fShaderID));
			System.exit(1);
		}
		
		// LINK SHADER PROGRAM
		shaderProgramID = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgramID, vShaderID);
		GL20.glAttachShader(shaderProgramID, fShaderID);
		GL20.glLinkProgram(shaderProgramID);
		if(GL20.glGetProgrami(shaderProgramID, GL20.GL_LINK_STATUS) != 1){
			System.err.println(GL20.glGetProgramInfoLog(shaderProgramID));
			System.exit(1);
		}
		GL20.glDeleteShader(vShaderID);
		GL20.glDeleteShader(fShaderID);
		return shaderProgramID;
	}
	
	private String readShaderFile(String fileName){
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(new File("./res/shaders/" +fileName)));
			String line;
			while((line = br.readLine()) != null){
				string.append(line);
				string.append("\n");
			}
			br.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return string.toString();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector2f(int location, Vector2f vector){
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadVector3f(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector4f(int location, Vector4f vector){
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void loadMatrix3f(int location, Matrix3f matrix){
		matrixBuffer3f = matrix.get(matrixBuffer3f);
		GL20.glUniformMatrix4fv(location, false, matrixBuffer3f);
	}
	
	protected void loadMatrix4f(int location, Matrix4f matrix){
		matrixBuffer4f = matrix.get(matrixBuffer4f);
		GL20.glUniformMatrix4fv(location, false, matrixBuffer4f);
	}
	
}
