package server;

import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import entities.EntityPlayer;

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
	 * are banned.
	 * @param settings the Server's ServerSetting object
	 * @param socket the socket for the respective connection check
	 * @param message a parameter which will be modified to include a message to return to the client
	 * @return
	 */
	public static boolean verifyConnectionIsAllowed(ServerSettings settings, Socket socket, String[] message)
	{
		String ip = (socket.getInetAddress().toString()).substring(1);
		boolean banned = isBanned(settings, ip);
		message[0] = (!banned) ? "Allowed : no-banned" : "Not Allowed : banned";
		return !banned;
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
	
	/**
	 * Revokes all mod-status for a given IP
	 * @param players
	 * @param IP
	 */
	public static void removeMod(Vector<EntityPlayer> players, String IP)
	{
		Iterator<EntityPlayer> it = players.iterator();
		while(it.hasNext())
		{
			EntityPlayer player = it.next();
			if(player.getAssociatedIP().equals(IP))
			{
				player.setIsMod(false);
			}
		}
	}

	/**
	 * Revokes all admin-status for a given IP
	 * @param players
	 * @param IP
	 */
	public static void removeAdmin(Vector<EntityPlayer> players, String IP)
	{
		Iterator<EntityPlayer> it = players.iterator();
		while(it.hasNext())
		{
			EntityPlayer player = it.next();
			if(player.getAssociatedIP().equals(IP))
			{
				player.setIsAdmin(false);
			}
		}
	}
	
	/**
	 * Gives all mod-status for a given IP
	 * @param players
	 * @param IP
	 */
	public static void addMod(Vector<EntityPlayer> players, String IP)
	{
		Iterator<EntityPlayer> it = players.iterator();
		while(it.hasNext())
		{
			EntityPlayer player = it.next();
			if(player.getAssociatedIP().equals(IP))
			{
				player.setIsMod(true);
			}
		}
	}

	/**
	 * Gives all admin-status for a given IP
	 * @param players
	 * @param IP
	 */
	public static void addAdmin(Vector<EntityPlayer> players, String IP)
	{
		Iterator<EntityPlayer> it = players.iterator();
		while(it.hasNext())
		{
			EntityPlayer player = it.next();
			if(player.getAssociatedIP().equals(IP))
			{
				player.setIsAdmin(true);
			}
		}
	}
	
}
