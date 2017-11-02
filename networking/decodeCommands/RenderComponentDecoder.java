package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import ecs.Component;
import ecs.Renderable;

public class RenderComponentDecoder implements DecoderCommand{

	@Override
	public Component decode(DataInputStream stream) throws IOException{
		Renderable comp = new Renderable(0, null);
		// map asset anmes to some number
		return comp;
	}

}
