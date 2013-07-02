package utils;

import java.io.File;
import java.util.Vector;

import client.TerraeRasa;

public class MainMenuHelper 
{
	/**
	 * Gets how many worlds exist in the ~/World Saves/ Directory. 
	 * @return the number of worlds in the directory, or in the case of a failure 0
	 */
	public int getTotalWorlds()
	{
		try {
			return (new File(TerraeRasa.getBasePath() + "/World Saves/")).list().length; 
		} catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * Gets how many players exist in the ~/Player Saves/ Directory. 
	 * @return the number of players in the directory, or in the case of a failure 0
	 */
	public int getTotalPlayers()
	{
		try {
			String[] children = (new File(TerraeRasa.getBasePath() + "/Player Saves/")).list();
			int totalPlayers = 0;
			//for each entry, check if it's actually a valid file
		    for (int i = 0; i < children.length; i++)  
		    {
		        String filename = children[i];
		        if(filename.endsWith(".xml") && filename.length() > 4) 
		        {
		        	totalPlayers++;
		        }
		    }
			return totalPlayers;			
		} catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * Gets the player names in the ~/Player Saves/ Directory. 
	 * @return the names for players, or in the case of a failure an empty String[]
	 */
	public String[] getPlayerFileNames()
	{
		try {
			File dir = new File(TerraeRasa.getBasePath() + "/Player Saves/");
			String[] children = dir.list(); //get the file names
			Vector<String> playerNames = new Vector<String>();
			for (int i = 0; i < children.length; i++) //For each name
		    {
		        String fileName = children[i];
		        //Check for a valid ending
		        if(fileName.endsWith(".xml") && fileName.length() > 4) 
		        {
		        	//Trim the ending
		        	playerNames.add(fileName.substring(0, fileName.length() - 4)); 
		        }
		    }
			String[] temp = new String[playerNames.size()]; 
			playerNames.copyInto(temp);			
			return temp;
		} catch(Exception e) {
			return new String[] { };
		}
	}
	
	/**
	 * Gets the world save names in the ~/World Saves/ Directory. 
	 * @return the names for worlds, or in the case of a failure a String[]
	 */
	public String[] getWorldFileNames()
	{
		try {
			return (new File(TerraeRasa.getBasePath() + "/World Saves/")).list();			
		} catch(Exception e) {
			return new String[] { };
		}
	}
}
