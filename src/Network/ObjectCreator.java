package Network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import Objects.*;
import Serialize.Serializer;

public class ObjectCreator {
	
	public static int port = 5010;
	public static int fileCounter = 0;

	public static void main(String[] args) throws IOException {
		
		
		Scanner keyboard = new Scanner(System.in);
		boolean done = false;
		
		while(!done) {
			
			System.out.println("\nEnter option 1-5 to create an object || Enter option 0 if done");
			 int userChoice = keyboard.nextInt();
			 
			 if (userChoice == 0) {
				 System.out.println("Server program Exited");
				 done = true;
			 }
			 
			 // Creates ObjectA with primitives int and float
			 else if (userChoice == 1) {
				 System.out.println("You chose 1");
				 ObjectA a = new ObjectA();
				 System.out.print("Enter an integer value for variable x: ");
				 a.x = keyboard.nextInt();
				 System.out.print("Enter a float value for variable y: ");
				 a.y = keyboard.nextFloat();
				 System.out.println("Sending object to serialize");
				 String jsonString = Serializer.serializeObject(a);
				 System.out.println(jsonString);
				 fileCreator(jsonString);
				 send(jsonString);
				 
			 }
			 
			 // Creates ObjectB with primitive boolean and field reference to another ObjectB
			 else if (userChoice == 2) {
				 System.out.println("You chose 2");
				 ObjectB b = new ObjectB();
				 System.out.print("Enter a boolean value for variable bool (0 for false || 1 for true): ");
				 if (keyboard.nextInt() == 0) {
					 b.bool = false;
				 } else {
					 b.bool = true;
				 }
				 
				 ObjectB b1 = new ObjectB();
				 b.other = b1;
				 System.out.print("Enter a boolean value for variable bool in referenced object (0 for false || 1 for true): ");
				 if (keyboard.nextInt() == 0) {
					 b1.bool = false;
				 } else {
					 b1.bool = true;
				 }
				 b1.other = b;
				 
				 System.out.println("Sending object to serialize");
				 String jsonString = Serializer.serializeObject(b);
				 System.out.println(jsonString);
				 fileCreator(jsonString);
				 send(jsonString);
			 }
			 
			 // Creates ObjectC with int array size 3
			 else if (userChoice == 3) {
				 System.out.println("You chose 3");
				 ObjectC c = new ObjectC();
				 System.out.print("Enter an integer for the first element in the array: ");
				 c.array[0] = keyboard.nextInt();
				 System.out.print("Enter an integer for the second element in the array: ");
				 c.array[1] = keyboard.nextInt();
				 System.out.print("Enter an integer for the last element in the array: ");
				 c.array[2] = keyboard.nextInt();
				 System.out.println("Sending object to serialize");
				 String jsonString = Serializer.serializeObject(c);
				 System.out.println(jsonString);
				 fileCreator(jsonString);
				 send(jsonString);
			 }
			 
			 // Creates ObjectD with ObjectA array size 3 
			 else if (userChoice == 4) {
				 System.out.println("You chose 4");
				 ObjectD d = new ObjectD();
				 ObjectA a = new ObjectA();
				 System.out.print("Enter an integer value for variable x: ");
				 a.x = keyboard.nextInt();
				 System.out.print("Enter a float value for variable y: ");
				 a.y = keyboard.nextFloat();
				 d.arrayA[1] = a;
				 System.out.println("Sending object to serialize");
				 String jsonString = Serializer.serializeObject(d);
				 System.out.println(jsonString);
				 fileCreator(jsonString);
				 send(jsonString);
				 
				 
			 }
			 
			 // Creates ObjectE with ObjectA arraylist
			 else if (userChoice == 5) {
				 System.out.println("You chose 5");
				 ObjectE e = new ObjectE();
				 ObjectA a = new ObjectA();
				 System.out.print("Enter an integer value for variable x: ");
				 a.x = keyboard.nextInt();
				 System.out.print("Enter a float value for variable y: ");
				 a.y = keyboard.nextFloat();
				 e.list.add(a);
				 System.out.println("Sending object to serialize");
				 String jsonString = Serializer.serializeObject(e);
				 System.out.println(jsonString);
				 fileCreator(jsonString);
				 send(jsonString);
			 }
			 
			 else {
				 System.out.println("**INVALID INPUT**");
			 }
			
		}
		
		keyboard.close();
	}
	
	// Takes json and sends to client socket
	public static void send(String jsonString) throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Waiting for Client to connect...");
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Message sent to Client");
        
        out.write(jsonString);
        out.close();
        
        serverSocket.close();
        clientSocket.close();
       
      
    }
	
	// Prints json to file
	public static void fileCreator(String jsonString) {
		try {
			PrintWriter pw = new PrintWriter("File" + Integer.toString(fileCounter++) + ".json");
			pw.write(jsonString);
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
