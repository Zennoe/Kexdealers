package example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
	
	private Socket clientSocket;
	
	private DataOutputStream outputStream;
	private BufferedReader bufferedReader;
	
	public void send(String output){
		try{
			outputStream.writeChars(output +'\n');
		}catch(IOException x){
			x.printStackTrace();
		}
	}
	
	public String receive(){
		String line = "";
		try{
			line = bufferedReader.readLine();
		}catch(IOException x){
			x.printStackTrace();
		}
		return line;
	}
	
	public void connect(String address, int port){
		try{
			clientSocket = new Socket(address, port);
			outputStream = new DataOutputStream(clientSocket.getOutputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}catch(IOException x){
			x.printStackTrace();
		}
	}
	
	public void disconnect(){
		try{
			bufferedReader.close();
			outputStream.close();
			clientSocket.close();
		}catch(IOException x){
			x.printStackTrace();
		}
	}
}
