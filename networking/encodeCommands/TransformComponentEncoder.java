package encodeCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import ecs.Component;
import ecs.Transformable;

public class TransformComponentEncoder implements EncoderCommand{

	@Override
	public void encode(DataOutputStream stream, Component comp) throws IOException {
		Transformable casted = (Transformable) comp;
		// write position x y z as float
		stream.writeFloat(casted.getPosition().x());
		stream.writeFloat(casted.getPosition().y());
		stream.writeFloat(casted.getPosition().z());
		// write rotation x y z as float
		stream.writeFloat(casted.getRotX());
		stream.writeFloat(casted.getRotY());
		stream.writeFloat(casted.getRotZ());
		// write uniform scale as float
		stream.writeFloat(casted.getScale());
	}
	
}
