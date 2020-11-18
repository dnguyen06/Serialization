package Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import Objects.*;

public class ObjectCreator {

	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		boolean done = false;
		
		while(!done) {
			
			System.out.println("\nEnter option 1-5 to create an object || Enter option 0 if done");
			 int userChoice = keyboard.nextInt();
			 
			 if (userChoice == 0) {
				 done = true;
			 }
			 	 
			 else if (userChoice == 1) {
				 System.out.println("You chose 1");
				 ObjectA a = new ObjectA();
				 System.out.print("Enter an integer value for variable x: ");
				 a.x = keyboard.nextInt();
				 System.out.print("Enter a float value for variable y: ");
				 a.y = keyboard.nextFloat();
				 System.out.println("Sending object to serialize");
				 // jsonString = Serializer.serializeObject(a)
				 // send(jsonString)
				 
			 }
			 
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
				 // jsonString = Serializer.serializeObject(b)
				 // send(jsonString)
			 }
			 
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
				 // jsonString = Serializer.serializeObject(c)
				 // send(jsonString)
			 }
			 
			 else if (userChoice == 4) {
				 System.out.println("You chose 4");
				 ObjectD d = new ObjectD();
				 ObjectA a = new ObjectA();
				 System.out.print("Enter an integer value for variable x: ");
				 a.x = keyboard.nextInt();
				 System.out.print("Enter a float value for variable y: ");
				 a.y = keyboard.nextFloat();
				 d.arrayA[0] = a;
				 System.out.println("Sending object to serialize");
				 // jsonString = Serializer.serializeObject(d)
				 // send(jsonString)
				 
				 
			 }
			 
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
				 // jsonString = Serializer.serializeObject(d)
				 // send(jsonString)
			 }
			 
			 else {
				 System.out.println("**INVALID INPUT**");
			 }
			
		}
		
		keyboard.close();
	}
	
	public void send(String jsonString) throws IOException {
		ServerSocket serverSocket = new ServerSocket(5000);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        out.write(jsonString);
        out.close();
        serverSocket.close();
        clientSocket.close();
       
      
    }

}