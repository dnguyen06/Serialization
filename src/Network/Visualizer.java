package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Visualizer {
	
	public static int port = 5003;

	public static void main(String[] args) {
		try {
			int done = 0;
			Scanner keyboard = new Scanner(System.in);
			while (done != 1) {
				startConnection("LocalHost", port++);
				System.out.println("Enter 0 to keep client alive || Enter 1 to close client");
				done = keyboard.nextInt();
			}
			System.out.println("Client program exited");
			keyboard.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void startConnection(String ip, int port) throws UnknownHostException, IOException {		// Code referenced from Amir in tutorial
		Socket clientSocket = new Socket(ip, port);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.println("Message received from Server");
		String more = "";
		String output = in.readLine() + "\n";
		while (more != null) {
			more = in.readLine();
			if (more == null) {
				break;
			}
			output = output + more + "\n";
		}
		System.out.println(output);
	
		in.close();
		clientSocket.close();
	}

}
