package render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL20C;

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
		vShaderID = GL20C.glCreateShader(GL20C.GL_VERTEX_SHADER);
		GL20C.glShaderSource(vShaderID, readShaderFile(vShader));
		GL20C.glCompileShader(vShaderID);
		if(GL20C.glGetShaderi(vShaderID, GL20C.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20C.glGetShaderInfoLog(vShaderID));
			System.exit(1);
		}
		
		// CREATE FRAGMENT SHADER
		fShaderID = GL20C.glCreateShader(GL20C.GL_FRAGMENT_SHADER);
		GL20C.glShaderSource(fShaderID, readShaderFile(fShader));
		GL20C.glCompileShader(fShaderID);
		if(GL20C.glGetShaderi(fShaderID, GL20C.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20C.glGetShaderInfoLog(fShaderID));
			System.exit(1);
		}
		
		// LINK SHADER PROGRAM
		shaderProgramID = GL20C.glCreateProgram();
		GL20C.glAttachShader(shaderProgramID, vShaderID);
		GL20C.glAttachShader(shaderProgramID, fShaderID);
		GL20C.glLinkProgram(shaderProgramID);
		if(GL20C.glGetProgrami(shaderProgramID, GL20C.GL_LINK_STATUS) != 1){
			System.err.println(GL20C.glGetProgramInfoLog(shaderProgramID));
			System.exit(1);
		}
		GL20C.glDeleteShader(vShaderID);
		GL20C.glDeleteShader(fShaderID);
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
		return GL20C.glGetUniformLocation(programID, uniformName);
	}
	
	protected void bindAttribute(int attribute, String variableName){
		GL20C.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void start(){
		GL20C.glUseProgram(programID);
	}
	
	public void stop(){
		GL20C.glUseProgram(0);
	}
	
	protected void loadInt(int location, int value){
		GL20C.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value){
		GL20C.glUniform1f(location, value);
	}
	
	protected void loadVector2f(int location, Vector2fc vector){
		GL20C.glUniform2f(location, vector.x(), vector.y());
	}
	
	protected void loadVector3f(int location, Vector3fc vector){
		GL20C.glUniform3f(location, vector.x(), vector.y(), vector.z());
	}
	
	protected void loadVector4f(int location, Vector4fc vector){
		GL20C.glUniform4f(location, vector.x(), vector.y(), vector.z(), vector.w());
	}
	
	protected void loadMatrix3f(int location, Matrix3fc matrix){
		matrixBuffer3f = matrix.get(matrixBuffer3f);
		GL20C.glUniformMatrix3fv(location, false, matrixBuffer3f);
	}
	
	protected void loadMatrix4f(int location, Matrix4fc matrix){
		matrixBuffer4f = matrix.get(matrixBuffer4f);
		GL20C.glUniformMatrix4fv(location, false, matrixBuffer4f);
	}
	
}
