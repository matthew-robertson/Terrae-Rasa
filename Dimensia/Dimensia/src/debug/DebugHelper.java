package debug;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Defines any random methods that may be useful when debugging. These are not used in an
 * actual version of the game. 
 */
public class DebugHelper {
	
	/**
	 * Debug method to print out all the key/values in a hashtable
	 */
	public void printHashTable(Hashtable<String, Object> hashtable) 
	{
        Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            String str = (String) key;
            Object obj = hashtable.get(str);
            System.out.println("Key: " + str + " Value: " + obj.toString());
        }
    }
}
