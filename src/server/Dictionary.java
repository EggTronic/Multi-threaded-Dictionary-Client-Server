/** 
 * Name: Yang Xu
 * StudentID: 961717
 * COMP90015 Project 1
 */

package server;
import java.util.*;
import java.io.*;

public class Dictionary {
	static HashMap<String, String> dictionary = new HashMap<String, String>();

	private static Dictionary dict = new Dictionary();
	
	private Dictionary() {};
	
	@SuppressWarnings("unchecked")
	public static Dictionary getInstance() {
		try 
		{
			File dictFile=new File("dictionary");
			
			if (dictFile.createNewFile()){	
				// create new dictionary file if there is not one
			    System.out.println("Dictionary is created!");
			} else if (dictFile.length() != 0) {
				// load from source dictionary file
				FileInputStream fis=new FileInputStream(dictFile);
		        ObjectInputStream ois=new ObjectInputStream(fis);
		        
		        dictionary = (HashMap<String, String>)ois.readObject();
		        
		        ois.close();
		        fis.close();
			} 
			
			System.out.println("Dictionary has been loaded from local!");	  
			
			return dict;
			
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to initialize dictionary instance: IOException");
			e.printStackTrace();
			return null;
			
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("Failed to initialize dictionary instance: Dictionary Class not found");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Search the meaning of a word from dictionary 
	 * @param word
	 * @return string
	 */
	public String search(String word) {
		String result = dictionary.get(word);
		if  (result == null || result.isEmpty() ) {
			result = "Word '" + word + "' not found in the dictionary";
		} else {
			result = "The meaning of word '" + word + "' is: " + result; 
		}
		return result;
	}
	
	/**
	 * Add a word with meaning to dictionary and save the change
	 * @param word
	 * @param meaning
	 * @return string
	 */
	public String add(String word, String meaning) {
		String result = dictionary.get(word);
		
		// add
		if  (result == null || result.isEmpty() ) {
			dictionary.put(word, meaning);
			result = "Word '" + word + "' has been added succesfully";
		} else {
			result = "Word '" + word + "' already exist in the dictionary";
		}
		
		// save
		if(this.save()) {
			return result;
		} else {
			return "Failed to add word: " + word;
		}
	}
	
	/**
	 * Remove a word from dictionary and save the change
	 * @param word
	 * @return string
	 */
	public String remove(String word) {
		String result = dictionary.get(word);
		
		// remove
		if  (result == null || result.isEmpty() ) {
			result = "Word '" + word + "' not exist in the dictionary";
		} else {
			dictionary.remove(word);
			result = "Word '" + word + "' has been removed successfully";
		}
		
		// save
		if (this.save()) {
			return result;
		} else {
			return "Failed to remove word: " + word;
		}
		
	}
	
	// Save change to the local dictionary file
	private boolean save() {
		try 
		{
			File dictFile = new File("dictionary");
			FileOutputStream fos=new FileOutputStream(dictFile);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(dictionary);
	        oos.flush();
	        oos.close();
	        fos.close();
	        return true;
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to save change to the dictionary");
			e.printStackTrace();
			return false;
		} 
	}
	
	// Function to print the dictionary
	public String toString() {
		String result = "";
		for(Map.Entry<String,String> m :dictionary.entrySet()){
			result += m.getKey()+" : "+m.getValue() + "\n";
		}
		return result;
	}
}
