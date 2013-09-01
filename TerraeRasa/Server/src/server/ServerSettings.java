package server;

import java.util.Random;
import java.util.Vector;

import enums.EnumWorldDifficulty;
import enums.EnumWorldSize;

public class ServerSettings 
{
	private static int entityID = 0;
	private static int connectionID = 0;
	
	public boolean forcePlayerDifficultiesNormal;
	public int maxPlayers;
	public int spawnProtectionRange;
	public boolean pvpEnabled;
	public String universeName;
	public int port;
	public EnumWorldDifficulty worldDifficulty;
	public EnumWorldSize worldSize;
	public int seed;
	public boolean usePassword;
	public boolean spawnMonsters;
	public int loadDistance;
	public String serverMessage;
	private Vector<String> banlist;
	private Vector<String> mods;
	private Vector<String> admins;
	private String password;
	
	/**
	 * Creates a new server settings object with default settings.
	 */
	public ServerSettings()
	{
		forcePlayerDifficultiesNormal = true;
		maxPlayers = 10;
		spawnProtectionRange = 10;
		pvpEnabled = false;
		universeName = "World";
		port = 48615;
		worldDifficulty = EnumWorldDifficulty.NORMAL;
		worldSize = EnumWorldSize.MEDIUM;
		seed = Math.abs(new Random().nextInt());
		usePassword = false;
		spawnMonsters = true;
		loadDistance = 4;
		serverMessage = "Just your ordinary Terrae Rasa server...";
		banlist = new Vector<String>();
		mods = new Vector<String>();
		admins = new Vector<String>();
		password = "";
	}
	
	public String[] toStringArray()
	{
		return new String[] {
				"#Terrae Rasa Server Settings",
				"#Formatting is important! Dont change the order of stuff in this file.",
				"#Make sure values are consistant too, dont replace a number with text!",
				"force_player_difficulties_normal=" + forcePlayerDifficultiesNormal,
				"max_players=" + maxPlayers,
				"spawn_protection=" + spawnProtectionRange,
				"pvp_enabled=" + pvpEnabled,
				"universe_name=" + universeName,
				"server_port=" + port,
				"world_difficulty=" + worldDifficulty.toString().toLowerCase(),
				"world_size=" + worldSize.toString().toLowerCase(),
				"use_password=" + usePassword,
				"password=" + password,
				"spawn_monsters=" + spawnMonsters,
				"load_distance=" + loadDistance,
				"message=" + serverMessage
		};
	}
	
	public void addMod(String ip)
	{
		mods.add(ip);
	}
	
	public void ban(String ip)
	{
		banlist.add(ip);
	}
	
	public void addAdmin(String ip)
	{
		admins.add(ip);
	}
	
	public void removeMod(String ip)
	{
		mods.remove(ip);
	}
	
	/**
	 * This might also be known as unban
	 * @param 
	 */
	public void pardon(String ip)
	{
		banlist.remove(ip);
	}
	
	public void removeAdmin(String ip)
	{
		admins.remove(ip);
	}
	
	public static int getEntityID()
	{
		return entityID++;
	}
	
	public static int getConnectionID()
	{
		return connectionID++;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public boolean isMod(String playerIP)
	{
		for(String ip : mods)
		{
			if(ip.equals(playerIP))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isAdmin(String playerIP)
	{
		for(String ip : admins)
		{
			if(ip.equals(playerIP))
			{
				return true;
			}
		}
		return false;
	}

	public String[] getModsAsArray() 
	{
		String[] values = new String[mods.size()];
		mods.copyInto(values);
		return values;
	}
	
	public String[] getAdminsAsArray() 
	{
		String[] values = new String[admins.size()];
		admins.copyInto(values);
		return values;
	}
	
	public String[] getBansAsArray() 
	{
		String[] values = new String[banlist.size()];
		banlist.copyInto(values);
		return values;
	}
	
	/**
	 * Determines if an IP is banned.
	 * @param ip the IP to check for a ban
	 * @return true if the IP is banned, otherwise false
	 */
	public boolean isBanned(String ip)
	{
		for(String str : banlist)
		{
			if(str.equals(ip))
			{
				return true;
			}
		}
		return false;
	}

	public boolean passwordCorrect(String password)
	{
		return this.password.equalsIgnoreCase(password);
	}
}
