package example;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import ecs.Component;
import encodeCommands.EncoderCommand;
import encodeCommands.TransformComponentEncoder;

public class EncodeDelegator {
	
	private HashMap<String, EncoderCommand> commands;
	private HashMap<String, Byte> cTypeTable;
	
	public EncodeDelegator() {
		// Populate the look up table
		commands = new HashMap<>();
		commands.put("transformable", new TransformComponentEncoder());
		// ---
		cTypeTable = new HashMap<>();
		cTypeTable.put("transformable", (byte) 0x01);
		cTypeTable.put("renderable", (byte) 0x02);
	}
	
	public void delegate(OutputStream rawStream, String cType, Component comp) {
		
		try (DataOutputStream stream = new DataOutputStream(rawStream)) {
			// write the start signal byte
			stream.writeByte((byte) 0x00);
			// write the eID
			stream.writeInt(comp.getEID());
			// write the cType
			stream.writeByte(cTypeTable.get(cType));
			// delegate to command object
			commands.get(cType).encode(stream, comp);
		}catch(IOException x) {
			x.printStackTrace();
		}
	}
}
