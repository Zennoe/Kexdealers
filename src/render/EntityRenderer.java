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
import example.ResourceLoader;
import loaders.Mesh;

public class EntityRenderer {

	private EntityShader shader;
	
	public EntityRenderer(){
		shader = new EntityShader();
	}
	
	public void render(ResourceLoader resourceLoader,
			FPPCameraComponent camera,
			HashMap<String, HashSet<Transformable>> entitiesToRender,
			HashSet<PointLightComponent> pointLights){
		shader.start();
		shader.uploadViewPos(camera.getPosition());
		shader.uploadDirectionalLight(resourceLoader.getSun());
		shader.uploadPointLights(pointLights);
		
		for(String appearance : entitiesToRender.keySet()){
			Mesh data = resourceLoader.getRessource(appearance);
			bindEntityAppearance(data);
			
			HashSet<Transformable> transformations = entitiesToRender.get(appearance);
			for(Transformable transformation : transformations){
				shader.uploadMVP(transformation.getTransformation(), camera.getViewMatrix(), camera.getProjectionMatrix());
				System.out.println("FINAL: " +GL11.glGetError());
				GL11.glDrawElements(GL11.GL_TRIANGLES, data.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindEntityAppearance(data);
		}
		
		
		shader.stop();
	}
	
	public void bindEntityAppearance(Mesh data){
		// Bind VAO
		GL30.glBindVertexArray(data.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		// Bind textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, data.getMaterial().getTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, data.getMaterial().getSpecularMap().getID());
		// Upload Phong-Shading data
		shader.uploadMaterial(data.getMaterial().getReflectance());
	}
	
	public void unbindEntityAppearance(Mesh data){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
