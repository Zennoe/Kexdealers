package encodeCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import ecs.Component;

public interface EncoderCommand {
	
	public void encode(DataOutputStream stream, Component comp) throws IOException;
}
