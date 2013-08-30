package client;

import entities.EntityPlayer;

public class ClientsideCommands 
{
	/**
	 * Fills out a command with extra info so the server actually has a clue how to process it.
	 * @param player
	 * @param originalCommand
	 * @return
	 */
	public static String fillOutChatCommand(EntityPlayer player, String originalCommand)
	{
		if(originalCommand.startsWith("/kill"))
		{
			return originalCommand.trim() + " " + player.entityID;
		}
		else if(originalCommand.startsWith("/affix"))
		{
			String[] split = originalCommand.split(" ");
//			/affix <id> 
//			/affix <affix_id> <player_id> <main_inventory_index> #server command
			return split[0] + " " + split[1] + " " + player.entityID + " " + player.selectedSlot;
		}
		return originalCommand;
	}
}
