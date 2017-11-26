package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import org.joml.Vector3f;

import ecs.Component;
import ecs.PointLightComponent;

public class PointLightComponentDecoder implements DecoderCommand{
	
	@Override
	public Component decode(DataInputStream stream) throws IOException {
		PointLightComponent comp = new PointLightComponent(0, null, null, null, null, null);
		// read position x y z as float
		Vector3f position = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setPosition(position);
		// read ambient r b g as float
		Vector3f ambient = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setAmbient(ambient);
		// read diffuse r b g as float
		Vector3f diffuse = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setDiffuse(diffuse);
		// read specular r b g as float
		Vector3f specular = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setSpecular(specular);
		// read attenuation l q c as float
		Vector3f attenuation = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setAttenuation(attenuation);
		
		return comp;
	}
}
