package skybox;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

import ecs.FPPCameraComponent;
import loaders.GraphicsLoader;

public class SkyboxRenderer {
	
	private SkyboxShader shader;
	
	public SkyboxRenderer(){
		shader = new SkyboxShader();
	}
	
	public void render(GraphicsLoader graphicsLoader,
			FPPCameraComponent camera){
		
		GL11C.glDisable(GL11C.GL_DEPTH_TEST);
		GL11C.glDepthMask(false);
		
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
		
		Skybox skybox = graphicsLoader.getSkybox();
		shader.loadModelMatrix(skybox.getModelMatrix());
		
		bindSkybox(skybox);
		GL11C.glDrawArrays(GL11C.GL_TRIANGLES, 0, skybox.getMesh().getVertexCount());
		unbindSkybox(skybox);
	}
	
	public void bindSkybox(Skybox skybox){
		// bind VAO
		GL30C.glBindVertexArray(skybox.getMesh().getVaoID());
		GL20C.glEnableVertexAttribArray(0);
		// Bind textures
		GL13C.glActiveTexture(GL13C.GL_TEXTURE0);
		GL11C.glBindTexture(GL13C.GL_TEXTURE_CUBE_MAP, skybox.getTextureID());
		shader.loadCubeMap();
	}
	
	public void unbindSkybox(Skybox skybox){
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		GL20C.glDisableVertexAttribArray(0);
		GL30C.glBindVertexArray(0);
	}
	
}
