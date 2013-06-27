package utils;

import java.io.IOException;

/**
 * GameLauncher implements a single public method to start the game from a proper install.
 * Issuing a call to {@link #play()} will start the game and close the launcher.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GameLauncher {
	
	/**
	 * Starts the game and closes the launcher.
	 */
	public static void play() {
		try {
			if(OperatingSystemHelper.getOsName().toLowerCase().contains("mac")) {		
				startMac();
			}
			else if(OperatingSystemHelper.getOsName().toLowerCase().contains("window")) {
				startWindows();
			}
			else if(OperatingSystemHelper.getOsName().toLowerCase().contains("linux")) {
				startLinux();
			}
			else {
				throw new RuntimeException("Failed Launch @" + OperatingSystemHelper.getOsName());
			}
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch(RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the game on windows OS
	 * @throws IOException 
	 */
	private static void startWindows() throws IOException
	{
		Process process = Runtime.getRuntime().exec("java -jar " + OperatingSystemHelper.getBasePath() + "/bin/terraerasa.jar \"no-debug\"");
	}
	
	/**
	 * Starts the game on max OS X
	 * @throws IOException 
	 */
	private static void startMac() throws IOException
	{
		String applescriptCommand = "do shell script \"" + new StringBuilder().append("java -jar /Users/" + System.getProperty("user.name") + "/Library/Application").append((char)(92)).append((char)(92)).append(" Support/terraerasa/bin/terraerasa.jar \"no-debug\"").toString() + "\" \n";
		String[] args1 = { "osascript", "-e", applescriptCommand };
		Process process = Runtime.getRuntime().exec(args1);
	}
	
	/**
	 * Starts the game on linux
	 */
	private static void startLinux()
	{
		executeLinuxCommand("java -jar " + OperatingSystemHelper.getBasePath() + "/bin/terraerasa.jar \"no-debug\"");
	}
	
	/**
	 * Executes a given command as though this were the linux terminal
	 * @param command the command to execute
	 * @return the output of a given command(?)
	 */
	private static String executeLinuxCommand(String command) {
		StringBuilder sb = new StringBuilder();
		String[] commands = new String[] { "/bin/sh", "-c", command };
		try {
			Process proc = new ProcessBuilder(commands).start();
			/*
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}

			while ((s = stdError.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
