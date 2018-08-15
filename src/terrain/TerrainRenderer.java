package terrain;

import java.util.HashSet;

import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;
import ecs.FPPCameraComponent;
import ecs.PointLightComponent;
import loaders.GraphicsLoader;
import textures.MultiTexture;
import textures.Texture;
import wrapper.RawMesh;

public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(){
		shader = new TerrainShader();
		shader.start();
		shader.connectMultiTexture();
		shader.stop();
	}
	
	public void render(GraphicsLoader graphicsLoader,
			FPPCameraComponent camera,
			HashSet<PointLightComponent> pointLights){
		
		GL11C.glEnable(GL11C.GL_DEPTH_TEST);
		GL11C.glDepthMask(true);
		
		shader.start();
		shader.uploadDirectionalLight(graphicsLoader.getSun());
		shader.uploadPointLights(pointLights);
		
		Terrain terrain = graphicsLoader.getTerrain();
		
		prepareTerrain(terrain);
		
		shader.uploadMVP(terrain.getModelMatrix(), camera.getViewMatrix(), camera.getProjectionMatrix());
		GL11C.glDrawElements(GL11C.GL_TRIANGLES, terrain.getRawMesh().getVertexCount(), GL11C.GL_UNSIGNED_INT, 0);
		
		unbindTerrain();
	}
	
	private void prepareTerrain(Terrain terrain){
		// bind RawMesh
		RawMesh rawMesh = terrain.getRawMesh();
		GL30C.glBindVertexArray(rawMesh.getVaoID());
		GL20C.glEnableVertexAttribArray(0);
		GL20C.glEnableVertexAttribArray(1);
		GL20C.glEnableVertexAttribArray(2);
		
		// bind MultiTexture
		MultiTexture multiTexture = terrain.getMultiTexture();
		GL13C.glActiveTexture(GL13C.GL_TEXTURE0);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, multiTexture.getDefaultID());
		GL13C.glActiveTexture(GL13C.GL_TEXTURE1);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, multiTexture.getRID());
		GL13C.glActiveTexture(GL13C.GL_TEXTURE2);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, multiTexture.getGID());
		GL13C.glActiveTexture(GL13C.GL_TEXTURE3);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, multiTexture.getBID());
		// bind blendMap
		Texture blendMap = terrain.getBlendMap();
		GL13C.glActiveTexture(GL13C.GL_TEXTURE4);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, blendMap.getTextureID());
	}
	
	private void unbindTerrain(){
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0);
		
		GL20C.glDisableVertexAttribArray(0);
		GL20C.glDisableVertexAttribArray(1);
		GL20C.glDisableVertexAttribArray(2);
		GL30C.glBindVertexArray(0);
	}
	
	
}
