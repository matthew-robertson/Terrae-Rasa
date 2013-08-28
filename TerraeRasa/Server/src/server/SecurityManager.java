package server;

import java.net.Socket;

public class SecurityManager
{
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
	
	private static boolean isBanned(ServerSettings settings, String ip)
	{
		for(String str : settings.banlist)
		{
			if(str.equals(ip))
			{
				return true;
			}
		}
		return false;
	}
}
