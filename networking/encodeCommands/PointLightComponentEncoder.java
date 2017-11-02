package encodeCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import ecs.Component;
import ecs.PointLightComponent;

public class PointLightComponentEncoder implements EncoderCommand{
	
	@Override
	public void encode(DataOutputStream stream, Component comp) throws IOException{
		PointLightComponent casted = (PointLightComponent) comp;
		// write position x y z as float
		stream.writeFloat(casted.getPosition().x);
		stream.writeFloat(casted.getPosition().y);
		stream.writeFloat(casted.getPosition().z);
		// write ambient r g b as float
		stream.writeFloat(casted.getAmbient().x);
		stream.writeFloat(casted.getAmbient().y);
		stream.writeFloat(casted.getAmbient().z);
		// write diffuse r g b as float
		stream.writeFloat(casted.getDiffuse().x);
		stream.writeFloat(casted.getDiffuse().y);
		stream.writeFloat(casted.getDiffuse().z);
		// write specular r g b as float
		stream.writeFloat(casted.getSpecular().x);
		stream.writeFloat(casted.getSpecular().y);
		stream.writeFloat(casted.getSpecular().z);		
		// write attenuation l q c as float
		stream.writeFloat(casted.getAttenuation().x);
		stream.writeFloat(casted.getAttenuation().y);
		stream.writeFloat(casted.getAttenuation().z);		
	}
}
