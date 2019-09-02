package server;
//import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReadWriteLock;

import javax.net.ServerSocketFactory;

import server.Dictionary;

public class Server {
	
	// Declare the port number
	private static int port = 3005;
	
	// Read and Write lock to control clients' access to the dictionary object
	private static ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();

	public static void main(String[] args) throws ClassNotFoundException
	{	
		
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		
		// Start server
		try(ServerSocket server = factory.createServerSocket(port))
		{	
			Dictionary dict = Dictionary.getInstance();
			
			System.out.println("Waiting for client connection-");
			
			// Wait for client connections.
			while(true)
			{
				Socket client = server.accept();
				System.out.println("New client request");
							
				// Start a new thread for each client request
				Thread t = new Thread(() -> {
					try {
						serveClient(client, dict);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				});
				t.start();
			}
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static void serveClient(Socket client, Dictionary dict) throws ClassNotFoundException
	{
		try(Socket clientSocket = client)
		{
			// Input stream
			ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
			// Output Stream
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		    
		    String[] data = (String[]) input.readObject();
		    String result = requestHandler(data, dict);
		    
		    output.writeUTF(result);
		    output.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This function manipulate the dictionary object based on clients' request
	 * @param data
	 * @param dict
	 * Request method is stored in data[0] (search, remove, add)
	 * The client id is stored in data[1]
	 * Target word is stored in data[2]
	 * Meaning of that word is stored in data [3]
	 * @return string
	 */
	private static String requestHandler(String[] data, Dictionary dict) {
		switch(data[0]) {
		
		  // handle search request
		  case "search":
			lock.readLock().lock();
			try {
				System.out.println("Client " + data[1] + " search meaning for '" + data[2] + "'\n");
				return dict.search(data[2]);
			} finally {
				lock.readLock().unlock();
			}
		  
		  // handle remove request
		  case "remove":
			lock.writeLock().lock();
			try {
				System.out.println("Client " + data[1] + " remove '" + data[2] + "' from dictionary \n");
				return dict.remove(data[2]);
			} finally {
				lock.writeLock().unlock();
			}
		  
		  // handle add request
		  case "add":
			lock.writeLock().lock();
			try {
				System.out.println("Client " + data[1] + " add meaning for '" + data[2] + "'\n");
				return dict.add(data[2], data[3]);
			} finally {
				lock.writeLock().unlock();
			}
			
		  default:
			return "invalid request method";
		}
	}

}
