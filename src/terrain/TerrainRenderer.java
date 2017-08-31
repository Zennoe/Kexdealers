package terrain;

import java.util.HashSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ecs.PointLightComponent;
import example.DirectionalLight;
import example.ResourceLoader;
import render.LatchOnCamera;
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
	
	public void render(ResourceLoader resourceLoader,
			LatchOnCamera camera,
			DirectionalLight globalLight,
			HashSet<PointLightComponent> pointLights){
		shader.start();
		shader.uploadDirectionalLight(globalLight);
		shader.uploadPointLights(pointLights);
		
		Terrain terrain = resourceLoader.getTerrain();
		
		prepareTerrain(terrain);
		
		shader.uploadMVP(terrain.getModelMatrix(), camera.getViewMatrix(), camera.getProjectionMatrix());
		GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		unbindTerrain();
	}
	
	private void prepareTerrain(Terrain terrain){
		// bind RawMesh
		RawMesh rawMesh = terrain.getRawMesh();
		GL30.glBindVertexArray(rawMesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// bind MultiTexture
		MultiTexture multiTexture = terrain.getMultiTexture();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, multiTexture.getDefaultID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, multiTexture.getRID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, multiTexture.getGID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, multiTexture.getBID());
		// bind blendMap
		Texture blendMap = terrain.getBlendMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTextureID());
	}
	
	private void unbindTerrain(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	
}
