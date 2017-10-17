package example;

public class Client implements Runnable{
	
	private Encoder encoder;
	
	private TCPClient tcpClient;
	private boolean running = false;
	
	public Client(Encoder encoder){
		this.encoder = encoder;
	}
	
	public void start(){
		tcpClient = new TCPClient();
		tcpClient.connect("localhost", 81549);
		
		Thread networkThread = new Thread(this, "Network_Thread");
		running = true;
		networkThread.start();
	}
	
	@Override
	public void run(){
		
	}
	
	// Push all components with relevant changes to the server.
	public void updatePlayer(int eID){
		// prepare message 
		StringBuilder message = new StringBuilder();
		message.append("UPDATE").append("{eID=" +eID +"}\n");
		message.append(encoder.encodeEntity(eID));
		// give to networking thread for sending
		// ...
	}
	
	// After local player creation, send data to server and instruct server to sync this entity.
	// Clients need to detect newly created synced entities.
	public void registerPlayerEntity(int eID){
		// prepare message 
		StringBuilder message = new StringBuilder();
		message.append("REGISTER").append("{eID=" +eID +"}\n");
		message.append(encoder.encodeEntity(eID));
		// give to networking thread for sending
		// ...
	}
	
	// Request server to stop sync on this entity and delete it.
	// Deletion step has to be broadcasted somehow as well.
	public void unregisterPlayerEntity(int eID){
		// prepare message 
		StringBuilder message = new StringBuilder();
		message.append("UNREGISTER").append("{eID=" +eID +"}\n");
		// give to networking thread for sending
		// ...
	}
	
	// Request instance data. Same format as local saves.
	// THIS METHOD IS SUPPOSED TO BE BLOCKING UNTIL IT HAS RECEIVED AND ANSWER 
	public String loadInstanceFromServer(String instanceName){
		// prepare message 
		StringBuilder message = new StringBuilder();
		message.append("SWITCHINSTANCE").append("{" +instanceName +"}\n");
		// give to networking thread for sending
		// ...
		return "answer";
	}
	
}
