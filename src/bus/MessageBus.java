package bus;

import java.util.LinkedList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class MessageBus {
	
	// Singleton instance
	private static MessageBus messageBusInstance = null;
	
	private LinkedList<Message> renderSystemQueue = new LinkedList<>();
	private LinkedList<Message> audioSystemQueue = new LinkedList<>();
	private LinkedList<Message> networkSystemQueue = new LinkedList<>();
	private LinkedList<Message> teleportationSystemQueue = new LinkedList<>();
	private LinkedList<Message> playerQueue = new LinkedList<>();
	
	private MessageBus() {
		// - / -
	}
	
	public static MessageBus getInstance() {
		if(messageBusInstance == null) {
			messageBusInstance = new MessageBus();
		}
		return messageBusInstance;
	}
	
	public Message getNextMessage(Systems listener) {
		Message nextMessage;
		switch(listener) {
		case RENDER_SYSTEM: nextMessage = (renderSystemQueue.isEmpty() ? null : renderSystemQueue.removeLast()); break;
		case AUDIO_SYSTEM: nextMessage = (audioSystemQueue.isEmpty() ? null : audioSystemQueue.removeLast()); break;
		case NETWORK_SYSTEM: nextMessage = (networkSystemQueue.isEmpty() ? null : networkSystemQueue.removeLast()); break;
		case TELEPORTATION_SYSTEM: nextMessage = (teleportationSystemQueue.isEmpty() ? null : teleportationSystemQueue.removeLast()); break;
		case PLAYER: nextMessage = (playerQueue.isEmpty() ? null : playerQueue.removeLast()); break;
		default: return null;
		}
		return nextMessage;
	}
	
	/** New generic message function to reduce API clutter.
	 * @param recipient = a registered recipient system.
	 * @param behaviourID = a specific behavior (function) to be applied to some data. Resolved inside the recipient system.
	 * @param args = data to be passed along to the behavior function that is mapped to behaviorID.
	 * @return
	 */
	public Message messageSystem(Systems recipient, int behaviorID, Object[] args) {
		return null;
		// TODO: Actually support this throughout all systems :)
	}
	
	public Message messageRenderSys(Operation op) {
		Message msg = new RenderSysMessage(op);
		renderSystemQueue.addFirst(msg);
		return msg;
	}
	
	public Message messageRenderSys(Operation op, Vector3fc lineBegin, Vector3fc lineEnd, Vector3fc lineColour, double lifeTime) {
		Message msg = new RenderSysMessage(op, lineBegin, lineEnd, lineColour, lifeTime);
		renderSystemQueue.addFirst(msg);
		return msg;
	}
	
	public Message messageAudioSys(Operation op) {
		return null;
	}
	
	public Message messageNetworkSys(Operation op, Object content) {
		Message msg = new NetworkSysMessage(op, content);
		networkSystemQueue.addFirst(msg);
		return msg;
	}
	
	public Message messageTeleportationSys(Operation op, int targetEID, Vector3f destination) {
		Message msg = new TeleportationSysMessage(op, targetEID, destination);
		teleportationSystemQueue.addFirst(msg);
		return msg;
	}
	
	public Message messagePlayer(Operation op) {
		Message msg = new PlayerMessage(op);
		playerQueue.addFirst(msg);
		return msg;
	}
	
	public Message messagePlayer(Operation op, Vector2f vec) {
		Message msg = new PlayerMessage(op, vec);
		playerQueue.addFirst(msg);
		return msg;
	}
}
