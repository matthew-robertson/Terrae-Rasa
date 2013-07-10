package client;

import java.util.Iterator;
import java.util.Vector;



public class Settings 
{
	public boolean gamePaused = false;
	/** A boolean which indicates whether or not the ingame pause menu is active. (True = paused) */
	public boolean menuOpen = true;
	public double soundVolume = 0.5;
	public double musicVolume = 0.5;
	public boolean smoothLighting = false;
	public boolean weatherEnabled = true;
	public boolean autosave = false;
	public int autosaveMinutes = 10;
	public boolean vsyncEnabled = false;
	public Vector<ServerInfo> servers = new Vector<ServerInfo>();
	public Keybinds keybinds = new Keybinds();
	
	public ServerInfo[] getServersArray()
	{
		if(servers == null)
		{
			servers = new Vector<ServerInfo>();
		}
		ServerInfo[] vals = new ServerInfo[servers.size()];
		servers.copyInto(vals);
		return vals;
	}
	
	public void registerServer(ServerInfo info)
	{
		this.servers.add(info);
	}
	
	public void removeServer(ServerInfo info)
	{
		this.servers.remove(info);
	}
	
	public boolean removeServer(String name)
	{
		Iterator<ServerInfo> iterator = servers.iterator();
		while(iterator.hasNext())
		{
			ServerInfo info = iterator.next();
			if(info.equals(name))
			{
				iterator.remove();
				return true;
			}
		}
		return false;
	}
}
