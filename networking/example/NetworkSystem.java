package example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkSystem {
	
	private Socket socket = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public void run() {
		/*
		 * 1) Send player position
		 * 2) Integrate all new entity positions
		 */
	}
	
	// Returns false if connection failed
	public boolean connectToServer(String address, int port, String username) {
		try {
			System.out.println("Trying to connect to server...");
			socket = new Socket(address, port);
			System.out.println("...Connected to server: " +address +":" +port);
			// User name verification
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(bufferedReader.readLine());
			outputStream.writeChars(username +"\n");
			// This socket will get rejected by the server if the user name is "bad" in any way
		}catch (ConnectException x) {
			System.err.println("Firewall blocking or no server listening");
		}catch (UnknownHostException e) {
			System.err.println("Server address unknown: " +e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return (socket != null) ? true : false;
	}
	
	public void disconnectFromServer() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		}catch(IOException x) {
			System.err.println("Error while disconnecting from server");
			x.printStackTrace();
		}
		
	}
	
	public void loadInstanceFromServer(InstanceLoader instanceLoader) {
		
	}
	
}
