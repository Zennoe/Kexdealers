package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import ecs.Component;
import ecs.Renderable;

public class RenderComponentDecoder implements DecoderCommand{

	@Override
	public Component decode(DataInputStream stream) throws IOException{
		Renderable comp = new Renderable(0);
		
		StringBuilder assetName = new StringBuilder();
		char next;
		while((next = stream.readChar()) != '\n') {
			assetName.append(next);
		}
		comp.setAssetName(assetName.toString());
		
		return comp;
	}

}
