package server;

import java.util.Random;
import java.util.Vector;

import enums.EnumWorldDifficulty;
import enums.EnumWorldSize;

public class ServerSettings 
{
	public boolean forcePlayerDifficultiesNormal;
	public int maxPlayers;
	public int spawnProtectionRange;
	public boolean pvpEnabled;
	public String universeName;
	public int port;
	public EnumWorldDifficulty worldDifficulty;
	public EnumWorldSize worldSize;
	public int seed;
	public boolean useWhitelist;
	public boolean spawnMonsters;
	public int loadDistance;
	public String serverMessage;
	public Vector<String> banlist;
	public Vector<String> whitelist;
	public Vector<String> mods;
	public Vector<String> admins;
	
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
		useWhitelist = false;
		spawnMonsters = true;
		loadDistance = 4;
		serverMessage = "Just your ordinary Terrae Rasa server...";
		whitelist = new Vector<String>();
		banlist = new Vector<String>();
		mods = new Vector<String>();
		admins = new Vector<String>();
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
				"whitelist=" + useWhitelist,
				"spawn_monsters=" + spawnMonsters,
				"load_distance=" + loadDistance,
				"message=" + serverMessage
		};
	}
	
	public void addMod(String name)
	{
		mods.add(name);
	}
	
	public void addWhitelist(String name)
	{
		whitelist.add(name);
	}
	
	public void addBanlist(String name)
	{
		banlist.add(name);
	}
	
	public void addAdmin(String name)
	{
		admins.add(name);
	}
	
	public void removeMod(String name)
	{
		mods.remove(name);
	}
	
	public void removeWhitelist(String name)
	{
		whitelist.remove(name);
	}
	
	public void removeBanlist(String name)
	{
		banlist.remove(name);
	}
	
	public void removeAdmin(String name)
	{
		admins.remove(name);
	}
	
	
}
