package commands;

import java.io.InputStream;
import java.util.HashMap;

import ecs.Component;

public interface DecoderCommand {
	
	public Component decode(InputStream inputStream, HashMap<Byte, Integer> table);
}
