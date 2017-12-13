package example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector3f;

import ecs.EntityController;
import ecs.Transformable;
import render.RenderSystem;

public class InstanceLoader {
	
	private EntityController entityController;
	private ResourceLoader resourceLoader;
	private RenderSystem renderSystem;
	
	public InstanceLoader(EntityController entityController, ResourceLoader resourceLoader, RenderSystem renderSystem){
		this.entityController = entityController;
		this.resourceLoader = resourceLoader;
		this.renderSystem = renderSystem;
	}
	
	public void loadInstanceFromLocal(String filePath){
		ArrayList<String> lines = loadWithoutCommentLines(filePath);
		String[] frags = null;
		// Skybox
		String skyboxData = getLineWith("SKYBOX", lines);
		frags = getDataFragments(skyboxData);
		resourceLoader.loadSkybox(
				Float.valueOf(frags[0]),
				frags[1]);
		// Terrain
		String terrainData = getLineWith("TERRAIN", lines);
		frags = getDataFragments(terrainData);
		String[] drgb = {frags[4], frags[5], frags[6], frags[7]};
		resourceLoader.loadTerrain(
				Integer.valueOf(frags[0]), 
				Integer.valueOf(frags[1]), 
				frags[2], 
				drgb, 
				frags[3]);
		// Sun
		String sunData = getLineWith("SUN", lines);
		frags = getDataFragments(sunData);
		Vector3f directionInverse = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
		Vector3f DL_ambient = new Vector3f(Float.valueOf(frags[3]), Float.valueOf(frags[4]), Float.valueOf(frags[5]));
		Vector3f DL_diffuse = new Vector3f(Float.valueOf(frags[6]), Float.valueOf(frags[7]), Float.valueOf(frags[8]));
		Vector3f DL_specular = new Vector3f(Float.valueOf(frags[9]), Float.valueOf(frags[10]), Float.valueOf(frags[11]));
		resourceLoader.loadSun(
				directionInverse, 
				DL_ambient, DL_diffuse, DL_specular);
		// Entities
		String entityIDs = getLineWith("ENTITIES", lines);
		frags = getDataFragments(entityIDs);
		for(String frag : frags){
			entityController.directAllocEID(Integer.valueOf(frag));
		}
		// Components
		// - Transformable
		ArrayList<String> transformableData = getAllLinesWith("TRANSFORMABLE", lines);
		for(String dataSet : transformableData){
			int eID = extractEID(dataSet);
			frags = getDataFragments(dataSet);
			Transformable transformable = entityController.getTransformable(eID);
			Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
			float rotX = Float.valueOf(frags[3]);
			float rotY = Float.valueOf(frags[4]);
			float rotZ = Float.valueOf(frags[5]);
			float scale = Float.valueOf(frags[6]);
			transformable
				.setPosition(position)
				.setRotX(rotX)
				.setRotY(rotY)
				.setRotZ(rotZ)
				.setScale(scale);
		}
		// - Renderable
		ArrayList<String> renderableData = getAllLinesWith("RENDERABLE", lines);
		for(String dataSet : renderableData){
			int eID = extractEID(dataSet);
			frags = getDataFragments(dataSet);
			renderSystem.materialize(eID, frags[0]);
		}
		// - PointLightComponent
		ArrayList<String> pointLightComponentData = getAllLinesWith("POINTLIGHTCOMPONENT", lines);
		for(String dataSet : pointLightComponentData){
			int eID = extractEID(dataSet);
			frags = getDataFragments(dataSet);
			Vector3f position = new Vector3f(Float.valueOf(frags[0]), Float.valueOf(frags[1]), Float.valueOf(frags[2]));
			Vector3f ambient = new Vector3f(Float.valueOf(frags[3]), Float.valueOf(frags[4]), Float.valueOf(frags[5]));
			Vector3f diffuse = new Vector3f(Float.valueOf(frags[6]), Float.valueOf(frags[7]), Float.valueOf(frags[8]));
			Vector3f specular = new Vector3f(Float.valueOf(frags[9]), Float.valueOf(frags[10]), Float.valueOf(frags[11]));
			Vector3f attenuation = new Vector3f(Float.valueOf(frags[12]), Float.valueOf(frags[13]), Float.valueOf(frags[14]));
			entityController.addPointLightComponent(eID);// BUILDER PATTERN! Have it return the result
			entityController.getPointLightComponent(eID)
				.setPosition(position)
				.setAmbient(ambient)
				.setDiffuse(diffuse)
				.setSpecular(specular)
				.setAttenuation(attenuation);
		}
		// - AudioSourceComponent
		ArrayList<String> audioSourceComponentData = getAllLinesWith("AUDIOSOURCECOMPONENT", lines);
		for(String dataSet : audioSourceComponentData) {
			int eID = extractEID(dataSet);
			frags = getDataFragments(dataSet);
			entityController.addAudioSourceComponent(eID, frags[0]);
		}
		
	}
	
	public void loadInstanceFromServer(String fromServer){
		
	}
	
	private ArrayList<String> loadWithoutCommentLines(String filePath){
		ArrayList<String> processedLines = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
			for(String line; (line = br.readLine()) != null;){
				if(line.startsWith(">c")){
					continue;
				}else{
					processedLines.add(line);
				}
			}
		}catch (FileNotFoundException x){
			x.printStackTrace();
		}catch (IOException x){
			x.printStackTrace();
		}
		return processedLines;
	}
	
	public int extractEID(String input){
		String substring = input.substring(
				input.indexOf("eID=") + 4, 
				input.indexOf("}"));
		
		return Integer.valueOf(substring);
	}
	
	private String getLineWith(String exp, ArrayList<String> lines){
		for(String line : lines){
			if(line.startsWith(exp)){
				return line;
			}
		}
		return "";
	}
	
	private ArrayList<String> getAllLinesWith(String exp, ArrayList<String> lines){
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < lines.size(); i++){
			if(lines.get(i).startsWith(exp)){
				result.add(lines.get(i));
			}
		}
		return result;
	}
	
	private String[] getDataFragments(String line){
		// -1 to cut away the trailing "}"
		String inner = line.substring(line.lastIndexOf("{") + 1, line.length() - 1);
		return inner.split("/");
	}
	
}
