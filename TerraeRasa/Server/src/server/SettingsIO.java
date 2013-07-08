package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import enums.EnumWorldDifficulty;

//Settings loader might be a more apt name
public class SettingsIO
{
	private static String[] getFileContents(String filepath)
	{
		File file = new File(filepath);
		try {
			Vector<String> mods = new Vector<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				if(line.startsWith("#"))
				{
					continue;
				}
				else if(line.contains("#"))
				{
					line = line.substring(0, line.indexOf("#"));
				}
				mods.add(line);
			}			
			reader.close();
			String[] temp = new String[mods.size()];
			mods.copyInto(temp);
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return new String[] { };		
	}
	
	private static String[] loadBanned()
	{
		return getFileContents(TerraeRasa.basePath + "/banlist.txt");
	}
	
	private static String[] loadWhiteList()
	{
		return getFileContents(TerraeRasa.basePath + "/whitelist.txt");
	}
	
	private static String[] loadMods()
	{
		return getFileContents(TerraeRasa.basePath + "/mods.txt");
	}
	
	private static String[] loadAdmins()
	{
		return getFileContents(TerraeRasa.basePath + "/admins.txt");
	}
	
	public static ServerSettings loadSettings()
	{
		ServerSettings settings = new ServerSettings();
		for(String str : SettingsIO.loadBanned()) {
			settings.banlist.add(str);
		}
		for(String str : SettingsIO.loadWhiteList()) {
			settings.whitelist.add(str);
		}
		for(String str : SettingsIO.loadMods()) {
			settings.mods.add(str);
		}
		for(String str : SettingsIO.loadAdmins()) {
			settings.admins.add(str);
		}

		File file = new File(TerraeRasa.basePath + "/server.properties");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				try{
					processSettingLine(settings, line);
				} catch (Exception e) {
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				createSettingsFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return settings;
	}
	
	private static void processSettingLine(ServerSettings settings, String line)
	{
		if(line.startsWith("#")) //Do nothing
		{
			return;
		}

		String remainingLine = line.substring(line.indexOf("=") + 1);
		if(line.startsWith("force_player_difficulties_normal="))
		{
			settings.forcePlayerDifficultiesNormal = Boolean.parseBoolean(remainingLine);
		}
		else if(line.startsWith("max_players="))
		{
			settings.maxPlayers = Integer.parseInt(remainingLine);
		}
		else if(line.startsWith("spawn_protection="))
		{
			settings.spawnProtectionRange = Integer.parseInt(remainingLine);
		}
		else if(line.startsWith("pvp_enabled="))
		{
			settings.pvpEnabled = Boolean.parseBoolean(remainingLine);
		}
		else if(line.startsWith("universe_name="))
		{
			settings.universeName = remainingLine;
		}
		else if(line.startsWith("server_port="))
		{
			settings.port = Integer.parseInt(remainingLine);
		}
		else if(line.startsWith("world_difficulty="))
		{
			settings.worldDifficulty = EnumWorldDifficulty.getDifficulty(remainingLine);
		}
		else if(line.startsWith("seed="))
		{
			settings.seed = Integer.parseInt(remainingLine);
		}
		else if(line.startsWith("whitelist="))
		{
			settings.useWhitelist = Boolean.parseBoolean(remainingLine);
		}
		else if(line.startsWith("spawn_monsters="))
		{
			settings.spawnMonsters = Boolean.parseBoolean(remainingLine);
		}
		else if(line.startsWith("load_distance="))
		{
			settings.loadDistance = Integer.parseInt(remainingLine);
		}
		else if(line.startsWith("message="))
		{
			settings.serverMessage = remainingLine;
		}
	}
	
	private static void createSettingsFile() 
			throws IOException
	{
		boolean propertiesExist = new File(TerraeRasa.basePath + "/server.properties").exists();
		if(!propertiesExist)
		{
			new File(TerraeRasa.basePath + "/server.properties").createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TerraeRasa.basePath + "/server.properties")));
			writer.write("#Terrae Rasa Server Settings" + '\n');
			writer.write("#Formatting is important! Dont change the order of stuff in this file." + '\n');
			writer.write("#Make sure values are consistant too, dont replace a number with text!" + '\n');
			writer.write("force_player_difficulties_normal=true" + '\n');
			writer.write("max_players=10" + '\n');
			writer.write("spawn_protection=10" + '\n');
			writer.write("pvp_enabled=false" + '\n');
			writer.write("universe_name=World" + '\n');
			writer.write("server_port=48615" + '\n');
			writer.write("world_difficulty=normal" + '\n');
			writer.write("whitelist=false" + '\n');
			writer.write("spawn_monsters=true" + '\n');
			writer.write("load_distance=4" + '\n');
			writer.write("message=Just your ordinary Terrae Rasa server..." + '\n');
			writer.close();
		}		
	}
	
	public static void saveSettings(ServerSettings settings)
	{
		try {
			writeFile(TerraeRasa.basePath + "/server.properties", settings.toStringArray());
			writeFile(TerraeRasa.basePath + "/admins.txt", settings.admins);
			writeFile(TerraeRasa.basePath + "/mods.txt", settings.mods);
			writeFile(TerraeRasa.basePath + "/banlist.txt", settings.banlist);
			writeFile(TerraeRasa.basePath + "/whitelist.txt", settings.whitelist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeFile(String filepath, String[] contents) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath)));
		for(int i = 0; i < contents.length; i++)
		{
			writer.write(contents[i] + '\n');
		}
		writer.close();		
	}
	
	private static void writeFile(String filepath, Vector<String> contents) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath)));
		for(int i = 0; i < contents.size(); i++)
		{
			writer.write(contents.get(i) + '\n');
		}
		writer.close();
	}
}
