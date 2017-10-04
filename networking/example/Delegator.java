package example;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

import commands.DecoderCommand;
import commands.RenderComponentDecoder;
import commands.TransformComponentDecoder;
import ecs.Component;
import ecs.EntityController;

public class Delegator {
	
	// Read documentation for mapping. Populated in constructor.
	private HashMap<Byte, Integer> msgLengthLookUp;
	private HashMap<Byte, DecoderCommand> commands;
	
	private EntityController entityController;
	
	public Delegator(EntityController entityController) {
		// Populate the look up tables
		msgLengthLookUp.put((byte) 0x01, 12);
		msgLengthLookUp.put((byte) 0x02, 1);
		// ---
		commands.put((byte) 0x01, new TransformComponentDecoder());
		commands.put((byte) 0x02, new RenderComponentDecoder());
		
		this.entityController = entityController;
	}
	
	public void delegate(BufferedInputStream stream) {
		
		/*
		 * fetch next message.
		 * if message is 0x00 => start of block
		 * 1) read entity ID and remember
		 * 2) read component type and delegate accordingly. Pass lookup table along 
		 * 	so the command object knows how many more messages to expect. Also pass along input stream.
		 * 3) pass the entity ID and the component object from the returning command to the ECS
		 * 4) await next start byte 
		 * 	Should next message not by a start byte, dump it to error console at warning priority. -> 4)
		 */
		try {
			while(true) {
				// 0x00 signals a new block transmission
				if(stream.read() == 0x00) {
					// read and validate eID from next message
					int nextEID = stream.read();
					// read and validate cType from next message
					byte cType = (byte) stream.read();
					// delegate to command object and catch the returned component object
					Component comp = commands.get(cType).decode(stream, msgLengthLookUp);
					// hand the component over to the ECS
					entityController.addComponentOfType(nextEID, "unknown", comp);// API PROBLEM HERE  (Component type not known)
				}else {
					System.err.println("Delegator received unexpected data");
				}
			}
		}catch(IOException x) {
			x.printStackTrace();
		}

	}
	
}
