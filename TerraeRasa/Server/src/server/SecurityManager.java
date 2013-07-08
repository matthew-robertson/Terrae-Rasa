package server;

import java.net.Socket;

public class SecurityManager
{
	public static boolean verifyConnectionIsAllowed(ServerSettings settings, Socket socket)
	{
		String ip = socket.getInetAddress().toString();
		ip = ip.substring(1);
		boolean allowed = true;
		if(settings.useWhitelist)
		{
			allowed = false;
			for(String str : settings.whitelist)
			{
				if(str.equals(ip))
				{
					allowed = true;
				}
			}
		}		
		else
		{
			allowed = true;
			for(String str : settings.banlist)
			{
				if(str.equals(ip))
				{
					allowed = false;
				}
			}
		}
		
		System.out.println(allowed);
		return allowed;
	}
	
}
