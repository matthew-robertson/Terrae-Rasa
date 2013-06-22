package utils;

import java.io.File;

/**
 * DirectoryHelper manages all the directories required for a given operating system. Most directories are 
 * the same, but there's a slight difference for the openGL dependencies. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DirectoryHelper {
	private static final String[] DIR_WINDOWS = { 
			"", 
			"/Player Saves",
			"/World Saves", 
			"/Resources",
			"/Resources/Audio",
			"/Resources/Audio/Music",
			"/Resources/Audio/Sound",
			"/config",
			"/bin", 
			"/bin/native", 
			"/bin/native/windows" 
		};
	private static final String[] DIR_MAC = { 
			"", 
			"/Player Saves",
			"/World Saves", 
			"/Resources",
			"/Resources/Audio",
			"/Resources/Audio/Music",
			"/Resources/Audio/Sound",
			"/config",
			"/bin", 
			"/bin/native", 
			"/bin/native/macosx" 
		};
	private static final String[] DIR_LINUX = { 
			"", 
			"/Player Saves",
			"/World Saves", 
			"/Resources",
			"/Resources/Audio",
			"/Resources/Audio/Music",
			"/Resources/Audio/Sound",
			"/config",
			"/bin", 
			"/bin/native", 
			"/bin/native/linux" 
		};
	private static String[] directories;
	
	/**
	 * Initializes the DirectoryHelper, after the OperatingSystemHelper has been initialized.
	 */
	public static void init() {
		String os = OperatingSystemHelper.getOsName();
		if(os.toLowerCase().contains("window")) {
			directories = DIR_WINDOWS;
		}
		else if(os.toLowerCase().contains("mac")) {
			directories = DIR_MAC;
		}
		else if(os.toLowerCase().contains("linux")) { 
			directories = DIR_LINUX;
		}
		else {
			directories = new String[] { };
		}
	}

	/**
	 * Gets the directories required based on the user's operating system.
	 * @return the directories required based on the user's operating system
	 */
	public static String[] getDirectories() {
		return directories;
	}

	/**
	 * Creates all the directories for a given operating system. 
	 */
	public static void createDirectories() {
		for(int i = 0; i < directories.length; i++) {
			new File(OperatingSystemHelper.getBasePath() + directories[i]).mkdir();
		}		
	}
	
	/**
	 * Checks if a folder within the game's main folder, or any sub-folders, exists. 
	 * @param filepath the folder to check for, not including the OperatingSystemHelper's basepath
	 * @return true if the folder exists, otherwise false
	 */
	public static boolean doesFolderExist(String filepath) { 
		return new File(OperatingSystemHelper.getBasePath() + filepath).exists();
	}

	/**
	 * Creates a folder at the given location. Note: Ensure the parent folder actually exists.
	 * @param filepath the folder to create, not including the OperatingSystemHelper's basepath
	 */
	public static void createFolder(String filepath) { 
		new File(OperatingSystemHelper.getBasePath() + filepath).mkdir();
	}
}
