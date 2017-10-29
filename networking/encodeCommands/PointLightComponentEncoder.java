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
		
		// write diffuse r g b as float
		
		// write specular r g b as float
		
		// write attenuation l q c as float
		
	}
}
