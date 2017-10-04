package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import ecs.Component;

public interface DecoderCommand {
	
	public Component decode(DataInputStream stream) throws IOException;
}
