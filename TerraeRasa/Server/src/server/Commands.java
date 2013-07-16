package server;

import items.Item;
import items.ItemMagic;
import items.ItemRanged;
import items.ItemThrown;

import java.util.Vector;

import transmission.BlockUpdate;
import transmission.EntityUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedBlock;
import utils.ItemStack;
import world.World;
import blocks.Block;
import entities.EntityItemStack;
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
	
	
	public static String processClientCommand(ServerUpdate update, Object associatedObject, World world, GameEngine engine, Vector<EntityPlayer> players, String command)
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
				if(player.inventory.getMainInventoryStack(Integer.parseInt(split[5])) != null)
				{
					player.breakBlock(update,
							world, 
							Integer.parseInt(split[3]), 
							Integer.parseInt(split[4]),
							Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[5])).getItemID()]);
				}
			}
			else if(split[1].equals("backcontinue"))
			{
				if(player.inventory.getMainInventoryStack(Integer.parseInt(split[5])) != null)
				{
					player.breakBackBlock(update,
							world, 
							Integer.parseInt(split[3]), 
							Integer.parseInt(split[4]),
							Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[5])).getItemID()]);
				}
			}
			else if(split[1].equals("stop"))
			{
				player.setIsMining(false);
			}
		}
		else if(command.startsWith("/projectile"))
		{
			String[] split = command.split(" ");
			EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[2])));
			if(player.inventory.getMainInventoryStack(Integer.parseInt(split[3])) != null)
			{
				Item item = Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[3])).getItemID()];
	
	//			/projectile launch playerid selected_slot mx my
				
				if (item instanceof ItemMagic)
				{
					ItemMagic spell = (ItemMagic) item;
					player.launchProjectileMagic(update,
							world, 
							Double.parseDouble(split[4]), 
							Double.parseDouble(split[5]), 
							spell);
				}
				else if (item instanceof ItemRanged)
				{
					ItemRanged weapon = (ItemRanged) item;
					player.launchProjectileWeapon(update, 
							world, 
							Double.parseDouble(split[4]), 
							Double.parseDouble(split[5]), 
							weapon);
				}	
				else if(item instanceof ItemThrown)
				{
					ItemThrown weapon = (ItemThrown) item;
					player.launchProjectileThrown(update,
							world, 
							Double.parseDouble(split[4]), 
							Double.parseDouble(split[5]), 
							weapon,
							Integer.parseInt(split[3]));								
				}			
			}
		}
		else if(command.startsWith("/player"))
		{
			String[] split = command.split(" ");
			EntityPlayer player = (EntityPlayer)world.getEntityByID(Integer.parseInt(split[1]));
			
			if(split[2].equals("inventoryreplace"))
			{
				player.inventory.putItemStackInSlot(world, player, (ItemStack)(associatedObject), Integer.parseInt(split[3]));
			}
			else if(split[2].equals("quiverreplace"))
			{
				player.inventory.setQuiverStack(player, (ItemStack)(associatedObject), Integer.parseInt(split[3]));
			}
			else if(split[2].equals("armorreplace")) 
			{
				player.inventory.setArmorInventoryStack(player, 
						(ItemStack)(associatedObject), 
						player.inventory.getArmorInventoryStack(Integer.parseInt(split[3])), 
						Integer.parseInt(split[3]));
			}		
			else if(split[2].equals("throw"))
			{
				EntityItemStack stack = new EntityItemStack(Double.parseDouble(split[4]), Double.parseDouble(split[5]), (ItemStack)associatedObject);
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'a';
				entityUpdate.type = 3;
				entityUpdate.entityID = stack.entityID;
				entityUpdate.updatedEntity = stack;
				update.addEntityUpdate(entityUpdate);
				world.addItemStackToItemList(stack);
			}
		}
		
		
		return "";
	}
	
	
}
