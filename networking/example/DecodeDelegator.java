package example;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import decodeCommands.DecoderCommand;
import decodeCommands.PointLightComponentDecoder;
import decodeCommands.RenderComponentDecoder;
import decodeCommands.TransformComponentDecoder;
import ecs.Component;
import ecs.EntityController;

public class DecodeDelegator {
	
	private HashMap<Byte, DecoderCommand> commands;
	
	private HashMap<Byte, String> cTypeTable;
	
	private EntityController entityController;
	
	public DecodeDelegator(EntityController entityController) {
		// Populate the look up table
		commands = new HashMap<>();
		commands.put((byte) 0x01, new TransformComponentDecoder());
		commands.put((byte) 0x02, new RenderComponentDecoder());
		commands.put((byte) 0x03, new PointLightComponentDecoder());
		
		cTypeTable = new HashMap<>();
		cTypeTable.put((byte) 0x01, "transformable");
		cTypeTable.put((byte) 0x02, "renderable");
		cTypeTable.put((byte) 0x03, "pointlightcomponent");
		
		this.entityController = entityController;
	}
	
	public void delegate(DataInputStream stream) throws IOException{
		/*
		 * fetch next message.
		 * if message is 0x00 => start of block
		 * 1) read entity ID and remember
		 * 2) read component type and delegate accordingly. Pass lookup table along 
		 * 	so the command object knows how many more messages to expect. Also pass along input stream.
		 * 3) pass the entity ID and the component object from the returning command to the ECS
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
			// hand the component over to the ECS
			entityController.addComponentOfType(nextEID, cTypeTable.get(cType), comp);
			
		}else if(control_byte == 0x01) {
			// read eID from next message
			int nextEID = stream.readInt();
			// allocate new entity on the ECS
			entityController.directAllocEID(nextEID);
			// let the reader continue so it can read the components
			
		}else {
			System.err.println("DecodeDelegator received unexpected data");
		}

	}
	
}
