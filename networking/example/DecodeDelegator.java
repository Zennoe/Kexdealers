package example;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import decodeCommands.DecoderCommand;
import decodeCommands.PointLightComponentDecoder;
import decodeCommands.RenderComponentDecoder;
import decodeCommands.TransformComponentDecoder;
import ecs.Component;

public class DecodeDelegator {
	
	private HashMap<Byte, DecoderCommand> commands;
	
	private HashMap<Byte, String> cTypeTable;
	
	public DecodeDelegator() {
		// Populate the look up table
		commands = new HashMap<>();
		commands.put((byte) 0x01, new TransformComponentDecoder());
		commands.put((byte) 0x02, new RenderComponentDecoder());
		commands.put((byte) 0x03, new PointLightComponentDecoder());
		
		cTypeTable = new HashMap<>();
		cTypeTable.put((byte) 0x01, "transformable");
		cTypeTable.put((byte) 0x02, "renderable");
		cTypeTable.put((byte) 0x03, "pointlightcomponent");
	}
	
	public void delegate(DataInputStream stream, Set<Component> buffer) throws IOException{
		/*
		 * fetch next message.
		 * if message is 0x00 => start of block
		 * 1) read entity ID and remember
		 * 2) read component type and delegate accordingly. Pass lookup table along 
		 * 	so the command object knows how many more messages to expect. Also pass along input stream.
		 * 3) put the assembled component into the buffer
		 * 4) await next start byte 
		 * 	Should next message not by a start byte, dump it to error console. -> 4)
		 */
		// 0x00 signals a new block transmission
		byte control_byte = stream.readByte();
		if(control_byte == 0x00) {
			// read eID from next message
			int nextEID = stream.readInt();
			// read cType from next message
			byte cType = (byte) stream.readByte();
			// delegate to command object and catch the returned component object
			Component comp = commands.get(cType).decode(stream);
			comp.setEID(nextEID);
			buffer.add(comp);
			
		} else {
			AbstractSystem.err.println("DecodeDelegator received unexpected data");
		}

	}
	
	
	
}
