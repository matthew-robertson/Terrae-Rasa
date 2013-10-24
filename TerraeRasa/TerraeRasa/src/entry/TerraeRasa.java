package entry;

public class TerraeRasa 
{
	private final static String WINDOWS_BASE_PATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/terraerasa").toString();
	private final static String MAC_BASE_PATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application").append(" Support/terraerasa").toString();
	private final static String LINUX_BASE_PATH = new StringBuilder().append("/home/").append(System.getProperty("user.name")).append("/terraerasa").toString();
	private final static String VERSION = "Alpha 0.2.0";	
	public static boolean IS_MP_LAUNCH = false;
	private static String basePath;
	public static volatile boolean isMPServerRunning = false;

	public static void main(String[] args)
	{
		if(IS_MP_LAUNCH)
		{
			initializeAsMP(args);
		}
		else
		{
			initializeAsSP(args);
		}
	}

	/**
	 * Gets the game version for the current launch
	 * @return the game version for the current launch
	 */
	public final static String getVersion()
	{
		return VERSION;
	}
	
	public final static String getBasePath()
	{
		return basePath;
	}
	
	private static void initializeAsSP(String[] args)
	{
		String osName = System.getProperty("os.name").toLowerCase();
		
		//Load the OpenGL libraries for rendering later on
		if(osName.contains("win")) //Windows
		{
			System.setProperty("org.lwjgl.librarypath", WINDOWS_BASE_PATH + "/bin/native/windows");
			basePath = WINDOWS_BASE_PATH;
		}
		else if(osName.contains("mac")) //Mac 
		{
			System.setProperty("org.lwjgl.librarypath", MAC_BASE_PATH + "/bin/native/macosx");
			basePath = MAC_BASE_PATH;
		}
		else if(osName.contains("ubuntu") || osName.contains("linux")) //Ubuntu/Linux. Ubuntu tested.
		{
			System.setProperty("org.lwjgl.librarypath", LINUX_BASE_PATH + "/bin/native/linux");
			basePath = LINUX_BASE_PATH;
		}
		else //Solaris and any other OS are out of luck for now
		{
			throw new RuntimeException("OS not supported");
		}
		

		SPGameEngine.terraeRasa = new SPGameEngine(args);
		SPGameEngine.terraeRasa.start(); 
	}
	
	private static void initializeAsMP(String[] args)
	{
		setBasePath("/home/alec/terraerasaserver");
		MPGameEngine.terraeRasa = new MPGameEngine();
		MPGameEngine.terraeRasa.start();
	}

	public static void setBasePath(String basePath) 
	{
		TerraeRasa.basePath = basePath;
	}
}
