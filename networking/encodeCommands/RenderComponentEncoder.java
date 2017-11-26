package encodeCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import ecs.Component;
import ecs.Renderable;

public class RenderComponentEncoder implements EncoderCommand{

	@Override
	public void encode(DataOutputStream stream, Component comp) throws IOException {
		Renderable casted = (Renderable) comp;
		// write asset name
		stream.writeChars(casted.getAssetName());
		// terminate with '\n'
		stream.writeChar('\n');
	}

}
