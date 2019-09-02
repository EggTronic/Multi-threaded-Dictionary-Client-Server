package server;
import java.util.*;
import java.io.*;

public class Dictionary {
	static HashMap<String, String> dictionary = new HashMap<String, String>();
	//static boolean lock = false;
	
	private static Dictionary dict = new Dictionary();
	
	private Dictionary() {};
	
	public static Dictionary getInstance() throws IOException, ClassNotFoundException {
		File dictFile=new File("dictionary");
		if (dictFile.createNewFile())
		{	
			// create the dictionary file
		    System.out.println("Dict is created!");
		} else {
			// load from source
			FileInputStream fis=new FileInputStream(dictFile);
	        ObjectInputStream ois=new ObjectInputStream(fis);
	        
	        dictionary = (HashMap<String, String>)ois.readObject();
	        
	        ois.close();
	        fis.close();
	        
		    System.out.println("Dict has been loaded from local!");	    
		} 
		return dict;
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
		try {
			File dictFile = new File("dictionary");
			FileOutputStream fos=new FileOutputStream(dictFile);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(dictionary);
	        oos.flush();
	        oos.close();
	        fos.close();
	        return true;
		} catch (IOException e) {
			System.out.println("Failed to save change to the dictionary");
			System.out.println(e);
			return false;
		} 
	}
	
	// FUnction to print the dictionary
	public String toString() {
		String result = "";
		for(Map.Entry<String,String> m :dictionary.entrySet()){
			result += m.getKey()+" : "+m.getValue() + "\n";
		}
		return result;
	}
}
