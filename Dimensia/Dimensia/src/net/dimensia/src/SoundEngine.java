package net.dimensia.src;

/**
 * Sort of complements sound manager. Right now its main use is to hold sounds as static things I guess
 */
public class SoundEngine 
{
	private final String BASE_PATH;

	public SoundEngine()
	{
		String osName = System.getProperty("os.name");
		if(osName.toLowerCase().contains("window"))
		{
			BASE_PATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/Dimensia").toString();
		}
		else if(osName.toLowerCase().contains("mac"))
		{
			BASE_PATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application Support/Dimensia").toString();
		}
		else
		{
			throw new RuntimeException("Invalid OS");
		}
	}
	
	public int addSound(SoundManager manager, String fileName)
	{
		return manager.addSound(BASE_PATH + "/Resources/" + fileName);
	}
	
	public String getBasePath()
	{
		return BASE_PATH;
	}
}
