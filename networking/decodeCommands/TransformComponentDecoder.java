package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import org.joml.Vector3f;

import ecs.Component;
import ecs.Transformable;

public class TransformComponentDecoder implements DecoderCommand{

	@Override
	public Component decode(DataInputStream stream) throws IOException {
		Transformable comp = new Transformable(0);
		// read position x y z as float
		Vector3f position = new Vector3f(
				stream.readFloat(),
				stream.readFloat(),
				stream.readFloat());
		comp.setPosition(position);
		// read rotation x y z as float
		comp.setRotX(stream.readFloat());
		comp.setRotY(stream.readFloat());
		comp.setRotZ(stream.readFloat());
		// read uniform scale as float
		comp.setScale(stream.readFloat());
		
		return comp;
	}

}
