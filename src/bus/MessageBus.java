package bus;

import java.util.LinkedList;

import org.joml.Vector3f;

public class MessageBus {
	
	// Singleton instance
	private static MessageBus messageBusInstance = null;
	
	private LinkedList<BusMessage> renderSystemQueue = new LinkedList<>();
	private LinkedList<BusMessage> audioSystemQueue = new LinkedList<>();
	private LinkedList<BusMessage> networkSystemQueue = new LinkedList<>();
	private LinkedList<BusMessage> teleportationSystemQueue = new LinkedList<>();
	
	private MessageBus() {
		// - / -
	}
	
	public static MessageBus getInstance() {
		if(messageBusInstance == null) {
			messageBusInstance = new MessageBus();
		}
		return messageBusInstance;
	}
	
	public BusMessage getNextMessage(MessageListener listener) {
		BusMessage nextMessage;
		switch(listener) {
		case RENDER_SYSTEM: nextMessage = renderSystemQueue.removeLast(); break;
		case AUDIO_SYSTEM: nextMessage = audioSystemQueue.removeLast(); break;
		case NETWORK_SYSTEM: nextMessage = networkSystemQueue.removeLast(); break;
		case TELEPORTATION_SYSTEM: nextMessage = (teleportationSystemQueue.size() > 0) ? teleportationSystemQueue.removeLast() : null; break;
		default: return null;
		}
		return nextMessage;
	}
	
	public void messageRenderSys(Operation op) {
		
	}
	
	public void messageAudioSys(Operation op) {

	}
	
	public void messageNetworkSys(Operation op) {
		
	}
	
	public BusMessage messageTeleportationSys(Operation op, int targetEID, Vector3f destination) {
		BusMessage msg = new TeleportationSysMessage(op, targetEID, destination);
		teleportationSystemQueue.addFirst(msg);
		return msg;
	}
}
