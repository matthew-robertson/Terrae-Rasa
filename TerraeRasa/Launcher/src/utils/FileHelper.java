package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

/**
 * FileHelper has some minor methods that help with file system management. FileHelper implements a method to get the 
 * version of the game installed to disk {@link #getInstalledVersion()} and a method to copy a file {@link #copyFile(String, String)}
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class FileHelper {

	/**
	 * Gets the version of the game installed to disk
	 * @return the version of the game installed to disk
	 * @throws IOException
	 */
	public static String getInstalledVersion() 
			throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(OperatingSystemHelper.getBasePath() + "/config/version.txt")));
		String message = reader.readLine();
		reader.close();
		return message;
	}

	/**
	 * Uses java.nio to copy a file from a source to a destination.
	 * @param src the filepath of the file to copy
	 * @param dest the filepath of the copied file
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws IOException {
	    File sourceFile = new File(src);
	    File destFile = new File(dest);
		
		if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

	/**
	 * Gets a Vector<String> of all the sound files installed to disk. 
	 * @return a Vector<String> of all the sound files installed to disk
	 */
	public static Vector<String> getInstalledSoundResources(String resourcePath) {
		Vector<String> soundPaths = new Vector<String>();
		File resourceDir = new File(resourcePath);
		//Recursively get the contents of all the sub-folders and all them to soundPaths
		if (resourceDir.isDirectory())  {
            String[] children = resourceDir.list();
            for (int i = 0; i < children.length; i++) {
            	File newDir = new File(resourceDir, children[i]);
                if(newDir.isDirectory()) {
                	//If it's a directory, get all the files inside it and any other directories
                	soundPaths.addAll(getInstalledSoundResources(newDir.toString())); 
                }
                else {
                	//Otherwise, simply add it to the soundPaths Vector
                	soundPaths.add(newDir.toString());
                }
            }
	    } 
		return soundPaths;		
	}
	
	/**
	 * Trims off the operating system's specified base path from the start of a Vector<String> of 
	 * filepaths. This is useful for trimming down a list of files in some cases.
	 * @param paths the Vector<String> of paths to trim
	 * @return a Vector<String> of filepaths, with the operating system's basepath trimmed off
	 */
	public static Vector<String> trimBasePaths(Vector<String> paths) {
		int length = OperatingSystemHelper.getBasePath().length();
		for(int i = 0; i < paths.size(); i++) {
			String path = paths.get(i);
			path = path.substring(length, path.length());
			paths.set(i, path);
		}		
		return paths;
	}
}
