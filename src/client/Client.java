/** 
 * Name: Yang Xu
 * StudentID: 961717
 * COMP90015 Project 1
 */

package client;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client {
	
	// IP and port
	private static String ip = "localhost";
	private static int port = 3005;
	
	// Client ID
	private static String id;
	
	// Global UI component
	private static JTextField wordTextField = new JTextField(20);
	private static JTextArea dashboard = new JTextArea();
	
	public Client(String id) {
		Client.id = id;
	}
	
	// UI initlization
	private void prepareGUI(){
		JFrame frame = new JFrame("Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,400);
        
        JPanel panel = new JPanel(); // the panel is not visible in output
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchActionListener());
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new RemoveActionListener());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddActionListener());
        
        panel.add(wordTextField);
        panel.add(searchButton);
        panel.add(removeButton);
        panel.add(addButton);
        
        
        dashboard.setEditable(false);
        dashboard.setFont(new Font("Monaco", Font.BOLD, 20));
        
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, dashboard);
        frame.setVisible(true);
	}
	
	class SearchActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String word = wordTextField.getText();
			
			if (wordValidator(word)) {
				String [] data = new String[3];
				
				data[0] = "search";
				data[1] = id;
				data[2] = word;
				
				requestHandler(data);
			} else {
				dashboard.setForeground(Color.RED);
				dashboard.setText("invalid word input");
			}
			
			
		}
	}
	
	class RemoveActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String word = wordTextField.getText();
			
			if (wordValidator(word)) {
				String [] data = new String[3];
				
				data[0] = "remove";
				data[1] = id;
				data[2] = word;
				
				requestHandler(data);
			} else {
				dashboard.setForeground(Color.RED);
				dashboard.setText("invalid word input");
			}
		}
	}
	
	class AddActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String word = wordTextField.getText();
			if (wordValidator(word)) {
				String meaning = JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
	                    "What is the mearning of '" + word + "' ?", "");
				if (meaningValidator(meaning)) {
					String [] data = new String[4];
					
					data[0] = "add";
					data[1] = id;
					data[2] = word;
					data[3] = meaning;
					
					requestHandler(data);
				} else {
					dashboard.setForeground(Color.RED);
					dashboard.setText("invalid meaning input");
				}		
			} else {
				dashboard.setForeground(Color.RED);
				dashboard.setText("invalid word input");
			}
		}
	}
	
	/** check if the input word is valid
	 *  
	 * @param word
	 * validate length between 1 to 20
	 * validate only contain alphabet 
	 * @return boolean
	 */
	private boolean wordValidator(String word) {
		return ((!word.equals("")) 
	            && (word != null) 
	            && (word.matches("^[a-zA-Z]*$")
	            && (word.length()<20)));
	}
	
	/** check if the input meaning is valid
	 *  
	 * @param meaning
	 * validate length between 1 to 100
	 * @return boolean
	 */
	private boolean meaningValidator(String meaning) {
		return ((!meaning.equals("")) 
	            && (meaning != null))
				&& (meaning.length()<100);
	}
	
	private void requestHandler(String [] data){
		try(Socket socket = new Socket(ip, port);)
		{
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
		    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		    
		    // Send request
		    Boolean received = false;
	    	output.writeObject(data);
	    	System.out.println("Request sent to Server");
	    	output.flush();
	    	
	    	// Waiting for server response
		    while(true && !received)
		    {	
		    	if (input.available() > 0) {
		    		String message = input.readUTF();
		    		dashboard.setForeground(Color.BLUE);
			    	dashboard.setText(message);
		    		System.out.println("Server response: \n" + message + "\n");
		    		received = true;
		    	}	
		    }
		} 
		catch (UnknownHostException e)
		{	
			System.out.println("Failed to handle request: UnknownHostException");
			e.printStackTrace();
		}
		catch (IOException e) 
		{	
			System.out.println("Failed to handle request: IOException");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) 
	{	
		Client client;
		
		// get client ID from command line (default id is 1)
		if (args.length > 0 && args[0] != null && !args[0].equals("")) {
			client = new Client(args[0]);
		} else {
			client = new Client("1");
		}
		
		// start UI
		client.prepareGUI();
	}

}
