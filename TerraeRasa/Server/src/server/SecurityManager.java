package server;

import java.net.Socket;

/**
 * SecurityManager contains several methods which relate to server security. These focus on determining if a player is a mod, determining 
 * if a player is an admin, checking if a connection is allowed, and checking if a player is banned.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SecurityManager
{
	/**
	 * Checks if a given IP has mod status.
	 * @param settings the Server's ServerSetting object
	 * @param playerIP the IP to check for mod status
	 * @return
	 */
	public static boolean isMod(ServerSettings settings, String playerIP)
	{
		return settings.isMod(playerIP);
	}
	
	/**
	 * Checks if a given IP has admin status.
	 * @param settings the Server's ServerSetting object
	 * @param playerIP the IP to check for admin status
	 * 	 * @return
	 */
	public static boolean isAdmin(ServerSettings settings, String playerIP)
	{
		return settings.isAdmin(playerIP);
	}
	/**
	 * Checks if a given socket is allowed to connect and play the game. This will fail if they 
	 * do not have the password, or are banned.
	 * @param settings the Server's ServerSetting object
	 * @param socket the socket for the respective connection check
	 * @param message a parameter which will be modified to include a message to return to the client
	 * @return
	 */
	public static boolean verifyConnectionIsAllowed(ServerSettings settings, Socket socket, String[] message)
	{
		String ip = (socket.getInetAddress().toString()).substring(1);

		if(settings.usePassword)
		{
			//TODO: make passwords and bans work properly
			return true;
//			boolean whitelisted = isWhitelisted(settings, ip);
//			message[0] = (whitelisted) ? "Allowed : whitelisted" : "Not Allowed : no-whitelist";
//			return whitelisted;
		}		
		else
		{
			boolean banned = isBanned(settings, ip);
			message[0] = (!banned) ? "Allowed : no-banned" : "Not Allowed : banned";
			return !banned;
		}
	}
	
	/**
	 * Determines if an IP is banned.
	 * @param settings the Server's ServerSetting object
	 * @param ip the ip to check for a ban
	 * @return true if the ip is banned, otherwise false
	 */
	private static boolean isBanned(ServerSettings settings, String ip)
	{
		return settings.isBanned(ip);
	}
}
