package utils;

/**
 * OperatingSystemHelper helps with operating system specific paths and functionality including OS Name.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class OperatingSystemHelper {
	private static String osName;
	private static String basePath;
	private static String onlinePath;
	
	/**
	 * Initializes the OperatingSystemHelper.
	 */
	public static void init() {
		osName = System.getProperty("os.name");
		
		if(osName.toLowerCase().contains("window")) {
			basePath = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/terraerasa").toString();
			onlinePath = "http://dl.dropbox.com/u/11203435/TerraeRasa/Windows";	
		}
		else if(osName.toLowerCase().contains("mac")) {
			basePath = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application Support/terraerasa").toString();
			onlinePath = "http://dl.dropbox.com/u/11203435/TerraeRasa/Mac";	
		}
		else if(osName.toLowerCase().contains("linux")) { 
			basePath = new StringBuilder().append("/home/").append(System.getProperty("user.name")).append("/terraerasa").toString();
			onlinePath = "http://dl.dropbox.com/u/11203435/TerraeRasa/Linux";	
		}
		else {
			basePath = "";
			onlinePath = "";
		}
	}
	
	/**
	 * Gets the online path, but without a specific OS attached.
	 * @return an online download path without any specific OS
	 */
	public static final String getOSIndependantOnlinePath() {
		return "http://dl.dropbox.com/u/11203435/TerraeRasa";
	}
	
	/**
	 * Gets the online resource download path for a user's OS.
	 * @return the online resource download path for the user's specific OS
	 */
	public static final String getOnlinePath() {
		return onlinePath;
	}
	
	/**
	 * Gets the user's operating system name
	 * @return the user's operating system name
	 */
	public static final String getOsName() {
		return osName;
	}
	
	/**
	 * Gets the base file system path for a given OS
	 * @return the base file system path for an OS
	 */
	public static final String getBasePath() {
		return basePath;
	}
}
