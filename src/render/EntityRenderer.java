package render;

import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ecs.FPPCameraComponent;
import ecs.PointLightComponent;
import ecs.Transformable;
import example.AssetData;
import loaders.GraphicsLoader;

public class EntityRenderer {

	private EntityShader shader;
	
	public EntityRenderer(){
		shader = new EntityShader();
	}
	
	public void render(GraphicsLoader graphicsLoader,
			FPPCameraComponent camera,
			HashMap<String, HashSet<Transformable>> entitiesToRender,
			HashSet<PointLightComponent> pointLights){
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		
		shader.start();
		shader.uploadViewPos(camera.getPosition());
		shader.uploadDirectionalLight(graphicsLoader.getSun());
		shader.uploadPointLights(pointLights);
		
		for(String appearance : entitiesToRender.keySet()){
			AssetData data = graphicsLoader.getRessource(appearance);
			bindEntityAppearance(data);
			
			HashSet<Transformable> transformations = entitiesToRender.get(appearance);
			for(Transformable transformation : transformations){
				shader.uploadMVP(transformation.getTransformation(), camera.getViewMatrix(), camera.getProjectionMatrix());
				GL11.glDrawElements(GL11.GL_TRIANGLES, data.getRawMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindEntityAppearance(data);
		}
		
		shader.stop();
	}
	
	private void bindEntityAppearance(AssetData data){
		// Bind VAO
		GL30.glBindVertexArray(data.getRawMesh().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		// Bind textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, data.getMaterial().getDiffuseID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, data.getMaterial().getSpecularID());
		// Upload Phong-Shading data
		shader.uploadMaterial(0, 1, data.getPhongSpecularExponent());
	}
	
	private void unbindEntityAppearance(AssetData data){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
}
