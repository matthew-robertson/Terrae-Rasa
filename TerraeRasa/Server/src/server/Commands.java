package server;

import items.Item;

import java.util.Vector;

import transmission.BlockUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedBlock;
import world.World;
import blocks.Block;
import entities.EntityPlayer;

public class Commands 
{
	// -- req world and player?
	public static void processConsoleCommand(String command)
	{
		String[] commandComponents = command.split(" ");
		//Everyone or above
		if(command.startsWith("/help"))
		{
			
		}
		if(command.startsWith("/kill"))
		{
			
		}
		if(command.startsWith("/whisper"))
		{
			
		}
		//Mod or above
		if(command.startsWith("/teleport"))
		{
			if(commandComponents.length == 3)
			{
				//player player tp
			}
			else if(commandComponents.length == 4)
			{
				//player <x,y> tp
			}
		}
		if(command.startsWith("/time-set"))
		{
			
		}
		if(command.startsWith("/mod"))
		{
			
		}
		if(command.startsWith("/unmod"))
		{
			
		}
		if(command.startsWith("/kick"))
		{
			
		}
		if(command.startsWith("/ban"))
		{
			
		}
		if(command.startsWith("/unban"))
		{
			
		}
		if(command.startsWith("/stop"))
		{
			
		}
		if(command.startsWith("/save-world"))
		{
			
		}
		if(command.startsWith("/whitelist"))
		{
			
		}
		//Admin only
		if(command.startsWith("/admin"))
		{

		}
		if(command.startsWith("/unadmin"))
		{
			
		}
		if(command.startsWith("/heal"))
		{
			
		}
		if(command.startsWith("/mana"))
		{
			
		}
		if(command.startsWith("/special"))
		{
			
		}
		if(command.startsWith("/give"))
		{
			
		}
		if(command.startsWith("/weather"))
		{
			//off and on
		}
		if(command.startsWith("/effect"))
		{
			
		}
		if(command.startsWith("/affix"))
		{
			
		}
		if(command.startsWith("/say"))
		{
			
		}

		
		
	}
	
	
	public static String processClientCommand(ServerUpdate update, World world, GameEngine engine, Vector<EntityPlayer> players, String command)
	{
		if(command.startsWith("/placefrontblock"))
		{
			// /place(front/back)block x,y playerid blockid
			String[] split = command.split(" ");
			boolean success = world.placeBlock((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3]))), 
					Integer.parseInt(split[1]), 
					Integer.parseInt(split[2]), 
					Block.blocksList[Integer.parseInt(split[4])]);
			
			if(success)
			{
				BlockUpdate blockUpdate = new BlockUpdate();
				blockUpdate.x = Integer.parseInt(split[1]);
				blockUpdate.y = Short.parseShort(split[2]);
				blockUpdate.block = new SuperCompressedBlock(world.getBlockGenerate(blockUpdate.x, blockUpdate.y));
				update.addBlockUpdate(blockUpdate);
			}
			
			return command + " " + success;
		}
		else if(command.startsWith("/placebackblock"))
		{
			String[] split = command.split(" ");
			boolean success = world.placeBackWall((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3]))), 
					Integer.parseInt(split[1]), 
					Integer.parseInt(split[2]), 
					Block.blocksList[Integer.parseInt(split[4])]);
		
			if(success)
			{
				BlockUpdate blockUpdate = new BlockUpdate();
				blockUpdate.x = Integer.parseInt(split[1]);
				blockUpdate.y = Short.parseShort(split[2]);
				blockUpdate.block = new SuperCompressedBlock(world.getBackWallGenerate(blockUpdate.x, blockUpdate.y));
				update.addBlockUpdate(blockUpdate);
			}
			return command + " " + success;
		}
		else if(command.startsWith("/mine"))
		{
			String[] split = command.split(" ");
			EntityPlayer player = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[2]))));
			if(split[1].equals("frontcontinue"))
			{
				//player.breakBlock(world, mouseBX, mouseBY, Item.itemsList[player.inventory.getMainInventoryStack(active).getItemID()]);
				//String command = "/mine frontcontinue " + player.entityID + " " + mouseBX + " " + mouseBY + " " + active;
				//commandUpdates.add(command);
				player.breakBlock(update,
						world, 
						Integer.parseInt(split[3]), 
						Integer.parseInt(split[4]),
						Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[5])).getItemID()]);
				
			}
			else if(split[1].equals("backcontinue"))
			{
				//player.breakBackBlock(world, mouseBX, mouseBY, Item.itemsList[player.inventory.getMainInventoryStack(active).getItemID()]);
//				String command = "/mine backcontinue " + player.entityID + " " + mouseBX + " " + mouseBY + " " + active;
//				commandUpdates.add(command);
				player.breakBackBlock(update,
						world, 
						Integer.parseInt(split[3]), 
						Integer.parseInt(split[4]),
						Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[5])).getItemID()]);
				
			}
			else if(split[1].equals("stop"))
			{
				//String command = "/mine stop " + player.entityID;
				//clientCommands.add(command);
				///mine continue playerid x y activeslot 
				player.setIsMining(false);
			}
			
		}
		
		
		return "";
	}
	
	
}
