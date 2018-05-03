package example;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import ecs.Component;

public class NetworkSystem implements Runnable{
	
	public volatile boolean running = false;
	
	private Socket socket = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private EncodeDelegator encodeDelegator;
	private DecodeDelegator decodeDelegator;
	
	private HashSet<Component> componentBuffer;
	
	public NetworkSystem() {
		encodeDelegator = new EncodeDelegator();
		decodeDelegator = new DecodeDelegator();
		
		componentBuffer = new HashSet<Component>();
	}
	
	public void copyDatatoECS() {
		
	}
	
	public void run() {
		running = true;
		
		try (DataInputStream stream = new DataInputStream(inputStream)) {
			while(running) {
				// Maybe check if stream is actually opened?
				decodeDelegator.delegate(stream, componentBuffer);
			}
		}catch(IOException x) {
			x.printStackTrace();
		}
		
		// If a thread is waiting on this thread to exit, it will get notified here
		// Acquiring this object's lock is required before calling notifyAll()
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	// Returns false if connection failed
	public boolean connectToServer(String address, int port, String username) {
		try {
			AbstractSystem.out.println("Trying to connect to server...");
			socket = new Socket(address, port);
			AbstractSystem.out.println("...Connected to server: " +address +":" +port);
			// Setting up streams
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			// User name verification
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			AbstractSystem.out.println(bufferedReader.readLine());
			dataOutputStream.writeChars(username +"\n");
			// ===> close top level streams?
			// This socket will get rejected by the server if the user name is "bad" in any way
		}catch (ConnectException x) {
			AbstractSystem.err.println("Firewall blocking or no server listening");
		}catch (UnknownHostException e) {
			AbstractSystem.err.println("Server address unknown: " +e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return (socket != null) ? true : false;
	}
	
	public void disconnectFromServer() {
		try {
			running = false;
			this.wait();
		}catch(InterruptedException x) {
			AbstractSystem.err.println("Error while disconnecting from server (interrupted while waiting for network_system to exit)");
			x.printStackTrace();
		}
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		}catch(IOException x) {
			AbstractSystem.err.println("Error while disconnecting from server (while closing Socket/Streams)");
			x.printStackTrace();
		}
		
	}
	
	public void loadInstanceFromServer(InstanceLoader instanceLoader) {
		
	}
	
}
