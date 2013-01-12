package net.dimensia.launcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Class that can instanced to allow for downloads, and other things relating to the file system.
 * By default, it will attempt to get the correct download resources and install them in the valid locations.
 * Currently only Windows and Mac support work, but expansion should be relatively simple.
 */
public class FileManager extends SwingWorker<Void, Void>
{
	public static boolean isDownloading;
	private int downloadIndex;
	private Queue<String> _fileLocations;
	private Queue<String> _downloadLocations;
	private final String[] DIRECTORIES;
    private final String[] LWJGL_RESOURCES;
	private final String[] FILE_LOCATIONS;
	private final String[] ONLINE_LOCATIONS;
	private final int[] APPROXIMATE_DOWNLOAD_PERCENTAGES;
    private final String OS_NAME;
	private final String BASE_PATH;
	private final String ONLINE_DOWNLOAD_PATH;
	private final String[] FILE_LOCATIONS_WINDOWS = 
	{ 
	    "/Dimensia.jar",
		"/lwjgl.jar", 
		"/lwjgl_util.jar", 
		"/jinput.jar", 
		"/native/windows/jinput-dx8.dll",
		"/native/windows/jinput-dx8_64.dll",
		"/native/windows/jinput-raw.dll",
		"/native/windows/jinput-raw_64.dll",
		"/native/windows/lwjgl.dll",
		"/native/windows/lwjgl64.dll",
		"/native/windows/OpenAL32.dll",
		"/native/windows/OpenAL64.dll"
    };   
	private final String[] FILE_LOCATIONS_MAC = 
	{ 
	    "/Dimensia.jar",
		"/lwjgl.jar", 
		"/lwjgl_util.jar", 
		"/jinput.jar",
		"/native/macosx/openal.dylib",
		"/native/macosx/liblwjgl.jnilib",
		"/native/macosx/libjinput-osx.jnilib"
	};  	
	private final String[] ONLINE_LOCATIONS_WINDOWS = 
    { 
        "/Dimensia.jar",
		"/lwjgl.jar", 
		"/lwjgl_util.jar", 
		"/jinput.jar", 
		"/jinput-dx8.dll",
		"/jinput-dx8_64.dll",
		"/jinput-raw.dll",
		"/jinput-raw_64.dll",
		"/lwjgl.dll",
		"/lwjgl64.dll",
		"/OpenAL32.dll",
		"/OpenAL64.dll"
	};
	private final String[] ONLINE_LOCATIONS_MAC =
	{
		"/Dimensia.jar",
		"/lwjgl.jar", 
		"/lwjgl_util.jar", 
		"/jinput.jar", 
		"/openal.dylib",
		"/liblwjgl.jnilib",
		"/libjinput-osx.jnilib"
	};       
   	private final String[] LWJGL_RESOURCES_WINDOWS = 
	{ 
   		"/Dimensia.jar",
        "/lwjgl.jar", 
		"/lwjgl_util.jar", 
		"/jinput.jar", 
		"/native/windows/jinput-dx8.dll",
		"/native/windows/jinput-dx8_64.dll",
		"/native/windows/jinput-raw.dll",
		"/native/windows/jinput-raw_64.dll",
		"/native/windows/lwjgl.dll",
		"/native/windows/lwjgl64.dll",
		"/native/windows/OpenAL32.dll",
		"/native/windows/OpenAL64.dll"
    };
	private final String[] LWJGL_RESOURCES_MAC = 
	{ 
		"/Dimensia.jar",
	    "/lwjgl.jar", 
	    "/lwjgl_util.jar", 
		"/jinput.jar", 
		"/native/macosx/openal.dylib",
		"/native/macosx/liblwjgl.jnilib",
		"/native/macosx/libjinput-osx.jnilib"
	};
	private final String[] DIRECTORIES_WINDOWS = 
	{
		"",
		"/Player Saves",
		"/World Saves",
		"/bin",
		"/bin/native",
		"/bin/native/windows"
	};
	private final String[] DIRECTORIES_MAC = 
	{
		"",
		"/Player Saves",
		"/World Saves",
		"/bin",
		"/bin/native",
		"/bin/native/macosx"
	};
	private final int[] APPROXIMATE_DOWNLOAD_PERCENTAGES_WINDOWS = 
	{
		51, 
		18, 
		3, 
		4,
		1, 
		1,
		1,
		1,
		4,
		6,
		3,
		7
	};
	private final int[] APPROXIMATE_DOWNLOAD_PERCENTAGES_MAC = 
	{
		50,
		17,
		3,
		4,
		5,
		20,
		1
	};
	
	public FileManager()
	{
		_fileLocations = new LinkedList<String>();
		_downloadLocations = new LinkedList<String>();
		OS_NAME = System.getProperty("os.name");
		
		if(OS_NAME.toLowerCase().contains("window")) //Windows
		{
			BASE_PATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/Dimensia").toString();
			DIRECTORIES = DIRECTORIES_WINDOWS;
			LWJGL_RESOURCES = LWJGL_RESOURCES_WINDOWS;
			FILE_LOCATIONS = FILE_LOCATIONS_WINDOWS;
			ONLINE_LOCATIONS = ONLINE_LOCATIONS_WINDOWS;
			ONLINE_DOWNLOAD_PATH = "http://dl.dropbox.com/u/11203435/Dimensia/Windows";	
			APPROXIMATE_DOWNLOAD_PERCENTAGES = APPROXIMATE_DOWNLOAD_PERCENTAGES_WINDOWS;
		}
		else if(OS_NAME.toLowerCase().contains("mac")) //Mac
		{
			BASE_PATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application Support/Dimensia").toString();
			DIRECTORIES = DIRECTORIES_MAC;
			LWJGL_RESOURCES = LWJGL_RESOURCES_MAC;
			FILE_LOCATIONS = FILE_LOCATIONS_MAC;
			ONLINE_LOCATIONS = ONLINE_LOCATIONS_MAC;
			ONLINE_DOWNLOAD_PATH = "http://dl.dropbox.com/u/11203435/Dimensia/Mac";
			APPROXIMATE_DOWNLOAD_PERCENTAGES = APPROXIMATE_DOWNLOAD_PERCENTAGES_MAC;
		}
		else 
		{
			BASE_PATH = "";
			GuiDownload.log("Os Not Supported. Sorry");
			throw new RuntimeException("OS not supported : " + OS_NAME); 
		}		
	}
	
	public Void doInBackground() throws Exception 
	{
    	startDownload();
		return null;
	}
	
	public String getOsName()
	{
		return OS_NAME;
	}
	
	public void startDownload()
	{
	    _downloadLocations = new LinkedList<String>();
	    _fileLocations = new LinkedList<String>();
		createDirectories();
	    fillQueues();
	    GuiDownload.log(null);
		isDownloading = true;
		downloadIndex = 0;
		setProgress(0);
		GuiDownload.log("Starting Download");
		downloadFile(); 
	}
	
	private void downloadFile()
    {
		while(true)
		{
			if (_downloadLocations.size() > 0 && isDownloading) //Are There any download links left?
			{
				String url = _downloadLocations.poll();
				String fileName = _fileLocations.poll();
				downloadFile(url, fileName);	           
	            GuiDownload.log("Downloading: " + ((FILE_LOCATIONS[downloadIndex]).replaceAll("/", "")));
	            setProgress(getProgress() + APPROXIMATE_DOWNLOAD_PERCENTAGES[downloadIndex]);
	            downloadIndex++;
			}
			else
			{
				break;
			}
		}
	
    	isDownloading = false;
    	GuiDownload.log("Download Complete!");
    	JOptionPane.showMessageDialog(null, "Your Download was Successful!");        
    }
	     
    private void downloadFile(String url, String location)
    {
    	try
 		{
 			ReadableByteChannel rbc = Channels.newChannel(new URL(ONLINE_DOWNLOAD_PATH + url).openStream());  
 			FileOutputStream fos = new FileOutputStream(BASE_PATH + "/bin" + location);     
 			fos.getChannel().transferFrom(rbc, 0, (1 << 24));
 		}
 		catch (IOException e)
 		{
 			e.printStackTrace();
 		}
 	}
	
	private void createDirectories()
	{
		for(int i = 0; i < DIRECTORIES.length; i++)
		{
			File file = new File(BASE_PATH + DIRECTORIES[i]);
			file.mkdir();
		}		
	}
	
	private void fillQueues()
	{
		for(int i = 0; i < ONLINE_LOCATIONS.length; i++)
		{
			_downloadLocations.add(ONLINE_LOCATIONS[i]);
		}
		
		for(int i = 0; i < FILE_LOCATIONS.length; i++)
		{
			_fileLocations.add(FILE_LOCATIONS[i]);
		}
	}
	
	public String getOnlinePath()
	{
		return ONLINE_DOWNLOAD_PATH;
	}
	
	public String getBasePath()
	{
		return BASE_PATH;
	}
	
    public boolean canLaunch()
    {
        return doAllFilesExist();
    }

    private void createFolders()
    {
    	new File(BASE_PATH + "/World Saves").mkdir();
        new File(BASE_PATH + "/Resources").mkdir();
        new File(BASE_PATH + "/Player Saves").mkdir();
    }

    private boolean fileExists(String name)
    {
        return new File(name).exists();
    }
    
    private boolean doAllFilesExist()
    {
    	if(!doesFolderExist())
    	{
    		return false;
    	}    	
    	if(!doesDimensiaJarExist())
    	{
    		return false;
    	}
        if (!doLWJGLResourcesExist())
        {
        	return false;
       	}
        
        createFolders();
        return true;
    }

    private boolean doesFolderExist()
    {
        // Determine whether the directory exists.
		if (new File(BASE_PATH).exists())
		{
		     return true;
		}
		else
		{
		    // Try to create the directory.
			new File(BASE_PATH).mkdir();
		}
        return false;
    }

    private boolean doesDimensiaJarExist()
    {
    	return fileExists(new StringBuilder().append(BASE_PATH).append("/bin/Dimensia.jar").toString());
    }

    private boolean doLWJGLResourcesExist()
    {
        for (int i = 0; i < LWJGL_RESOURCES.length; i++)
        {
            if (!fileExists(new StringBuilder().append(BASE_PATH).append("/bin").append(LWJGL_RESOURCES[i]).toString()))
            {
            	return false;
            }
        }
        return true;
    }
}