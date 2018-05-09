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
		case RENDER_SYSTEM: nextMessage = (renderSystemQueue.isEmpty() ? null : renderSystemQueue.removeLast()); break;
		case AUDIO_SYSTEM: nextMessage = (audioSystemQueue.isEmpty() ? null : audioSystemQueue.removeLast()); break;
		case NETWORK_SYSTEM: nextMessage = (networkSystemQueue.isEmpty() ? null : networkSystemQueue.removeLast()); break;
		case TELEPORTATION_SYSTEM: nextMessage = (teleportationSystemQueue.isEmpty() ? null : teleportationSystemQueue.removeLast()); break;
		default: return null;
		}
		return nextMessage;
	}
	
	public BusMessage messageRenderSys(Operation op) {
		BusMessage msg = new RenderSysMessage(op);
		renderSystemQueue.addFirst(msg);
		return msg;
	}
	
	public BusMessage messageAudioSys(Operation op) {
		return null;
	}
	
	public BusMessage messageNetworkSys(Operation op, Object content) {
		BusMessage msg = new NetworkSysMessage(op, content);
		networkSystemQueue.addFirst(msg);
		return msg;
	}
	
	public BusMessage messageTeleportationSys(Operation op, int targetEID, Vector3f destination) {
		BusMessage msg = new TeleportationSysMessage(op, targetEID, destination);
		teleportationSystemQueue.addFirst(msg);
		return msg;
	}
}
