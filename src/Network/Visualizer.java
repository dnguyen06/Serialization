package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONException;

import Objects.Inspector;
import Serialize.Deserializer;


public class Visualizer {
	
	public static int port = 5010;

	public static void main(String[] args) {
		try {
			int done = 0;
			Scanner keyboard = new Scanner(System.in);
			while (done != 1) {
				startConnection("LocalHost", port);
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
	
	// Receives json from server
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
		//System.out.println(output);
	
		in.close();
		clientSocket.close();
		
		
		// Deserializes json received then sends to Inspector
		try {
			Object obj = Deserializer.deserializeObject(output);
			Inspector.inspect(obj, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	
		
	}

}
