package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import example.FPPCamera;
import example.ResourceLoader;

public class SkyboxRenderer {
	
	private SkyboxShader shader;
	
	public SkyboxRenderer(){
		shader = new SkyboxShader();
	}
	
	public void render(ResourceLoader resourceLoader,
			FPPCamera camera){
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
		
		Skybox skybox = resourceLoader.getSkybox();
		shader.loadModelMatrix(skybox.getModelMatrix());
		
		bindSkybox(skybox);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skybox.getMesh().getVertexCount());
		unbindSkybox(skybox);
	}
	
	public void bindSkybox(Skybox skybox){
		// bind VAO
		GL30.glBindVertexArray(skybox.getMesh().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		// Bind textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getTextureID());
		shader.loadCubeMap();
	}
	
	public void unbindSkybox(Skybox skybox){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
}
