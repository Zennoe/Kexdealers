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
import terrain.TerrainTile;
import textures.MultiTexture;
import textures.Texture;
import wrapper.RawModel;

public class TerrainRenderer {

	private TerrainShader shader;
	
	private MultiTexture multiTexture;
	private Texture blendMap;
	
	public TerrainRenderer(MultiTexture multiTexture, Texture blendMap){
		this.multiTexture = multiTexture;
		this.blendMap = blendMap;
		
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
		
		// Shortcut code to reduce to one single terrain tile until LOD is done
		HashSet<TerrainTile> terrainTiles = new HashSet<TerrainTile>();
		terrainTiles.add(resourceLoader.getTerrainTile());
		// -end Shortcut
		for(TerrainTile terrainTile : terrainTiles){
			prepareTerrainTile(terrainTile);
			
			shader.uploadMVP(terrainTile.getModelMatrix(), camera.getViewMatrix(), camera.getProjectionMatrix());
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrainTile.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTerrainTile();
		}
	}
	
	private void prepareTerrainTile(TerrainTile terrainTile){
		RawModel rawModel = terrainTile.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		multiTexture.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTextureID());
	}
	
	private void unbindTerrainTile(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	
}
