package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Vector;

import javax.swing.JProgressBar;

import utils.DirectoryHelper;
import utils.FileHelper;
import utils.OperatingSystemHelper;

/**
 * WebDownloader is used to connect to the online game server. It can request the latest game version, or download
 * all the needed files.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class WebDownloader {
	
	/**
	 * Downloads all the required files, based on the given parameters. Currently, the only choice is whether or not
	 * to download sound.
	 * @param progressBar the progressbar to update alongside the download
	 * @param includeSound true if sound should be downloaded; otherwise false
	 */
	public void downloadAllRequiredFiles(JProgressBar progressBar, boolean includeSound, boolean includeOnlyNeededResources) {
		String[] paths = { };
		try {
			paths = getFileRequirements(includeSound, includeOnlyNeededResources);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DirectoryHelper.createDirectories();
		progressBar.setValue(0);			
		WebDownloaderAsync asyncDL = new WebDownloaderAsync(progressBar, paths);
		asyncDL.execute();
	}
	
	/**
	 * Attempts to get the latest game version from the online server.
	 * @return the latest game version from the online server, if available
	 * @throws IOException
	 */
	public String getLatestVersion() 
			throws IOException 
	{
		downloadFileFromWeb(OperatingSystemHelper.getOSIndependantOnlinePath() + "/version.txt", OperatingSystemHelper.getBasePath() + "/config/version_dl.txt");
		BufferedReader reader = new BufferedReader(new FileReader(new File(OperatingSystemHelper.getBasePath() + "/config/version_dl.txt")));
		String message = reader.readLine();
		reader.close();
		return message;
	}
	
	/**
	 * Gets all the required files for a given download. Currently, the only thing that can 
	 * change is whether or not to download sound resources.
	 * @param includeSound whether or not to download sound (true to download)
	 * @return the file requirements for a download
	 * @throws IOException
	 */
	private String[] getFileRequirements(boolean includeSound, boolean includeOnlyNeededResources) throws IOException {
		Vector<String> allPaths = new Vector<String>(10);
		Vector<String> binPaths = getBinFilepaths();
		Vector<String> soundPaths = getSoundFilepaths(includeSound, includeOnlyNeededResources);
		for(String path : binPaths) {
			allPaths.add(path);
		}
		for(String path : soundPaths) {
			allPaths.add(path);
		}			
		String[] values = new String[allPaths.size()];
		allPaths.copyInto(values);
		return values;
	}
	
	/**
	 * Gets a list of all the filepaths which are associated with the "/bin" folder
	 * @return a list of all the filepaths which are associated with the "/bin" folder
	 * @throws IOException 
	 */
	private Vector<String> getBinFilepaths() throws IOException {
		Vector<String> vector = new Vector<String>();
		String osName = OperatingSystemHelper.getOsName().toLowerCase();
		String filename = (osName.contains("windows")) ? "windows" : (osName.contains("mac")) ? "mac" : (osName.contains("linux")) ? "linux" : "";
		//Get the required files for a given OS
		downloadFileFromWeb(OperatingSystemHelper.getOSIndependantOnlinePath() + "/" + filename + "_files.txt", OperatingSystemHelper.getBasePath() + "/config/req.txt");
		
		//Read in all the required files, then return them as a Vector<String>
		BufferedReader reader = new BufferedReader(new FileReader(new File(OperatingSystemHelper.getBasePath() + "/config/req.txt")));
		String message = "";
		while((message = reader.readLine()) != null)
		{
			vector.add(message);
		}
		reader.close();	
		return vector;
	}
	
	/**
	 * Gets the sound file paths that are wanted. If sound files are not wanted, then 
	 * none are returned. If only the needed sound files are included, then this will 
	 * yield all the sound files that are required but not yet installed.
	 * @param includeSound whether or not to include sound
	 * @param includeOnlyNeededResources true if duplicate resources should not be re-installed
	 * @return all the soundfile paths required, based on given parameters.
	 */
	private Vector<String> getSoundFilepaths(boolean includeSound, boolean includeOnlyNeededResources) 
	{
		try {
			if(includeSound) {
				if(includeOnlyNeededResources) {
					//Filter out duplicates and return the result.
					Vector<String> installedSound = FileHelper.trimBasePaths(
							FileHelper.getInstalledSoundResources(OperatingSystemHelper.getBasePath() + "/Resources"));
					Vector<String> onlineSound = getOnlineSoundFilepaths();
					//Remove the already installed resources from consideration.
					for(String string : installedSound) {
						if(onlineSound.contains(string)) {
							onlineSound.remove(string);
						}
					}
					return onlineSound;
				}
				else { 
					//All the filepaths
					return getOnlineSoundFilepaths(); 
				}			
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		//An error or not including sound will yield an empty Vector<String>
		return new Vector<String>();
	}
	
	/**
	 * Gets all the sound paths which are required for a given install, from the Internet.
	 * @throws IOException 
	 */
	private Vector<String> getOnlineSoundFilepaths() throws IOException {
		Vector<String> vector = new Vector<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(OperatingSystemHelper.getBasePath() + "/config/req.txt")));
		String message = "";
		
		//Get all the sound file requirements from the internet. These are the same for every OS. 
		//Return these as a Vector<String>
		downloadFileFromWeb(OperatingSystemHelper.getOSIndependantOnlinePath() + "/sound_files.txt", OperatingSystemHelper.getBasePath() + "/config/req.txt");
		reader = new BufferedReader(new FileReader(new File(OperatingSystemHelper.getBasePath() + "/config/req.txt")));
		message = "";
		while((message = reader.readLine()) != null)
		{
			vector.add(message);
		}
		reader.close();	
		return vector;
	}
	
	/**
	 * Downloads a file from the internet to disk.
	 * @param url the internet location of the file
	 * @param location the location of the file on disk
	 */
	private void downloadFileFromWeb(String url, String location) {
    	try {
 			ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());  
 			FileOutputStream fos = new FileOutputStream(location);     
 			fos.getChannel().transferFrom(rbc, 0, (1 << 24));
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}
}
