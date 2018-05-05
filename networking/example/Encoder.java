package example;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import ecs.PointLightComponent;
import ecs.Renderable;
import ecs.Transformable;
import ecs.EntityController;

public class Encoder {
	
	private EntityController entityController;
	
	public Encoder(EntityController entityController){
		this.entityController = entityController;
	}
	
	public String encodeEntity(int eID){
		StringBuilder output = new StringBuilder();
		
		ArrayList<String> components = entityController.getComponentsFor(eID);
		for(String component : components){
			String line = "";
			
			switch(component){
			case "transformable": line = encodeTransformable(entityController.getTransformable(eID));
					break;
			case "renderable": line = encodeRenderable(entityController.getRenderable(eID));
					break;
			case "pointlightcomponent": line = encodePointLightComponent(entityController.getPointLightComponent(eID));
					break;
			}
			
			output.append(line +"\n");
		}
		
		return output.toString();
	}
	
	public String encodeTransformable(Transformable transformable){
		
		StringBuilder output = new StringBuilder();
		
		output.append("TRANSFORMABLE");
		
		output.append("{eID=");
		output.append(transformable.getEID());
		output.append("}{");
		
		Vector3fc position = transformable.getPosition();
		float rotX = transformable.getRotX();
		float rotY = transformable.getRotY();
		float rotZ = transformable.getRotZ();
		float scale = transformable.getScale();
		
		output.append(position.x() +"/");
		output.append(position.y() +"/");
		output.append(position.z() +"/");
		
		output.append(rotX +"/");
		output.append(rotY +"/");
		output.append(rotZ +"/");
		
		output.append(scale);
		
		output.append("}");
		
		return output.toString();
	}
	
	public String encodeRenderable(Renderable renderable){
		
		StringBuilder output = new StringBuilder();
		
		output.append("RENDERABLE");
		
		output.append("{eID=");
		output.append(renderable.getEID());
		output.append("}{");
		
		String assetName = renderable.getAssetName();
		
		output.append(assetName);
		
		output.append("}");
		
		return output.toString();
	}
	
	public String encodePointLightComponent(PointLightComponent pointLightComponent){
		
		StringBuilder output = new StringBuilder();
		
		output.append("POINTLIGHTCOMPONENT");
		
		output.append("{eID=");
		output.append(pointLightComponent.getEID());
		output.append("}{");
		
		Vector3f position = pointLightComponent.getPosition();
		output.append(position.x +"/");
		output.append(position.y +"/");
		output.append(position.z +"/");
		
		Vector3f ambient = pointLightComponent.getAmbient();
		output.append(ambient.x +"/");
		output.append(ambient.y +"/");
		output.append(ambient.z +"/");
		Vector3f diffuse = pointLightComponent.getDiffuse();
		output.append(diffuse.x +"/");
		output.append(diffuse.y +"/");
		output.append(diffuse.z +"/");
		Vector3f specular = pointLightComponent.getSpecular();
		output.append(specular.x +"/");
		output.append(specular.y +"/");
		output.append(specular.z +"/");
		
		float radius = pointLightComponent.getRadius();
		output.append(radius +"/");
		
		float cutoff = pointLightComponent.getCutoff();
		output.append(cutoff +"");
		
		output.append("}");
		
		return output.toString();
	}
}
