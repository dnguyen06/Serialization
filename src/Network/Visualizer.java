package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Visualizer {

	public static void main(String[] args) {
		try {
			startConnection("LocalHost", 5000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void startConnection(String ip, int port) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(ip, port);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.println("Message received from Server");
		String output = in.readLine();
		while (output != null) {
			System.out.println(output);
			output = in.readLine();
		}
		
		in.close();
		clientSocket.close();
	}

}
