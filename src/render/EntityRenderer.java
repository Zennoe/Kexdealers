package render;

import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ecs.PointLightComponent;
import ecs.Transformable;
import example.AssetData;
import example.FPPCamera;
import example.ResourceLoader;

public class EntityRenderer {

	private EntityShader shader;
	
	public EntityRenderer(){
		shader = new EntityShader();
	}
	
	public void render(ResourceLoader resourceLoader,
			FPPCamera camera,
			HashMap<String, HashSet<Transformable>> entitiesToRender,
			HashSet<PointLightComponent> pointLights){
		shader.start();
		shader.uploadViewPos(camera.getPosition());
		shader.uploadDirectionalLight(resourceLoader.getSun());
		shader.uploadPointLights(pointLights);
		
		for(String appearance : entitiesToRender.keySet()){
			AssetData data = resourceLoader.getRessource(appearance);
			bindEntityAppearance(data);
			
			HashSet<Transformable> transformations = entitiesToRender.get(appearance);
			for(Transformable transformation : transformations){
				if(appearance.equals("lamp")) {
					System.out.println("lamp/" +transformation.getPosition().toString());
				}
				shader.uploadMVP(transformation.getTransformation(), camera.getViewMatrix(), camera.getProjectionMatrix());
				GL11.glDrawElements(GL11.GL_TRIANGLES, data.getRawMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindEntityAppearance(data);
			System.out.println("call done");
		}
		
		
		shader.stop();
	}
	
	public void bindEntityAppearance(AssetData data){
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
		shader.uploadMaterial(data.getPhongSpecularExponent());
	}
	
	public void unbindEntityAppearance(AssetData data){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
