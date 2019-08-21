package server;
import java.util.*;
import java.io.*;

public class Dictionary {
	static HashMap<String, String> dictionary = new HashMap<String, String>();
	//static boolean lock = false;
	
	private static Dictionary dict = new Dictionary();
	
	private Dictionary() {};
	
	public static Dictionary getInstance() throws IOException, ClassNotFoundException {
		// load from source
		if (dictionary.isEmpty()) {
			File dictFile=new File("fileone");
	        FileInputStream fis=new FileInputStream(dictFile);
	        ObjectInputStream ois=new ObjectInputStream(fis);
	        
	        dictionary = (HashMap<String, String>)ois.readObject();
	        
	        ois.close();
	        fis.close();
		}
		return dict;
	}
	
	public String addWord(String word, String meaning) {
		String result = dictionary.get(word);
		if  (result == null || result.isEmpty() ) {
			dictionary.put(word, meaning);
			result = "success";
		} else {
			result = "word already exist";
		}
		return result;
	}
	
	public String getMeaning(String word) {
		String result = dictionary.get(word);
		if  (result == null || result.isEmpty() ) {
			result = "not found";
		}
		return result;
	}
	
	public void save() throws IOException {
		File dictFile = new File("dictionary");
		FileOutputStream fos=new FileOutputStream(dictFile);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(dictionary);
        oos.flush();
        oos.close();
        fos.close();
	}
	
	public String toString() {
		String result = "";
		for(Map.Entry<String,String> m :dictionary.entrySet()){
			result += m.getKey()+" : "+m.getValue() + "\n";
		}
		return result;
	}
}
