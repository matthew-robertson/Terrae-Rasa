package server;

import items.Item;
import items.ItemMagic;
import items.ItemRanged;
import items.ItemThrown;
import items.ItemTool;

import java.util.List;
import java.util.Vector;

import transmission.BlockUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedBlock;
import transmission.UpdateWithObject;
import utils.ActionbarItem;
import utils.DisplayableItemStack;
import utils.ItemStack;
import utils.MouseItemHelper;
import world.World;
import blocks.Block;
import entities.EntityPlayer;
import enums.EnumColor;

public class Commands 
{
	// -- These are server commands
	public synchronized static void processConsoleCommand(ServerSettings settings, ServerUpdate update, World world, GameEngine engine, String command)
	{	
		try {
			
			String[] commandComponents = command.split(" ");
			//Everyone or above			
			if(command.startsWith("/kill"))
			{
				String[] split = command.split(" ");
				EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])));
				player.kill();
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
			if(command.startsWith("/settime"))
			{
//				/time-set <hour>
				String[] split = command.split(" ");
				double hour = Double.parseDouble(split[1]) % 24;
				int timeInTicks = (int) (hour * World.GAMETICKSPERHOUR);
				world.setTime(timeInTicks);
				TerraeRasa.terraeRasa.gameEngine.addCommandUpdate("/worldtimeset " + timeInTicks);
			}
			if(command.startsWith("/mod"))
			{
//				/mod <name|ip> 
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.addMod(player.getIP());
				}
				else if(player == null)
				{
					settings.addMod(split[1]);
				}
			}
			if(command.startsWith("/unmod"))
			{
//				/unmod <name|ip> 
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.removeMod(player.getIP());
				}
				else if(player == null)
				{
					settings.removeMod(split[1]);
				}
			}
			if(command.startsWith("/kick"))
			{
//				/kick <player-name>
				String[] split = command.split(" ");
				TerraeRasa.kick(split[1]);
			}
			if(command.startsWith("/ban"))
			{
//				/ban <name|ip>
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.ban(player.getIP());
				}
				else if(player == null)
				{
					settings.ban(split[1]);
				}
			}
			if(command.startsWith("/unban"))
			{
//				/unban <name|ip>
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.pardon(player.getIP());
				}
				else if(player == null)
				{
					settings.pardon(split[1]);
				}
			}
			if(command.startsWith("/stop"))
			{
				TerraeRasa.stop();
			}
			if(command.startsWith("/save-world"))
			{
				
			}
			
			//Admin only
			if(command.startsWith("/admin"))
			{
//				/admin <name|ip> 
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.addAdmin(player.getIP());
				}
				else if(player == null)
				{
					settings.addAdmin(split[1]);
				}				
			}
			if(command.startsWith("/unadmin"))
			{
//				/unadmin <name|ip> 
//				This will take an argument of a player name if that player is logged on. Otherwise, it will take an IP at any time.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					settings.removeAdmin(player.getIP());
				}
				else if(player == null)
				{
					settings.removeAdmin(split[1]);
				}
			}
			if(command.startsWith("/heal"))
			{
//				/heal <player-name> <amount>
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					player.heal(world, Integer.parseInt(split[2]), true);
					String newStats = "/player " + player.entityID + " sethms " + player.getHealth() + " " + player.mana + " " + player.specialEnergy;
					update.addValue(newStats);
				}
				else
				{
					Log.log("player " + split[1] + " not found.");
				}
			}
			if(command.startsWith("/mana"))
			{
//				/mana <player-name> <amount>
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					player.restoreMana(world, Integer.parseInt(split[2]), true);
					String newStats = "/player " + player.entityID + " sethms " + player.getHealth() + " " + player.mana + " " + player.specialEnergy;
					update.addValue(newStats);
				}
				else
				{
					Log.log("player " + split[1] + " not found.");
				}
			}
			if(command.startsWith("/special"))
			{
//				/special <player-name> <amount>
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					player.restoreSpecialEnergy(world, Integer.parseInt(split[2]), true);
					String newStats = "/player " + player.entityID + " sethms " + player.getHealth() + " " + player.mana + " " + player.specialEnergy;
					update.addValue(newStats);
				}
				else
				{
					Log.log("player " + split[1] + " not found.");
				}				
			}
			if(command.startsWith("/give"))
			{
//				/give <player> <id> <amount>
//				If the player cannot hold all the items, they simply wont be given.
				String[] split = command.split(" ");
				EntityPlayer player = engine.getPlayer(split[1]);
				if(player != null)
				{
					int itemID = Integer.parseInt(split[2]);
					int amount = Integer.parseInt(split[3]);
					if(itemID >= ActionbarItem.blockIndex && itemID <= ActionbarItem.itemIndex)
					{
						if(Block.blocksList[itemID] != null)
						{
							ItemStack stack = new ItemStack(Block.blocksList[itemID], amount);
							player.inventory.pickUpItemStack(world, player, stack);
							String logMessage = "Giving " + player.getName() + " " + stack.toString();
							Log.log(logMessage);
							String clientCommand = "/servermessage " + EnumColor.WHITE.toString() + " " + logMessage;
							update.addValue(clientCommand);
						}
					}
					else if(itemID >= ActionbarItem.itemIndex && itemID < ActionbarItem.spellIndex)
					{
						if(Item.itemsList[itemID] != null)
						{
							ItemStack stack = new ItemStack(Item.itemsList[itemID], amount);
							player.inventory.pickUpItemStack(world, player, stack);
							String logMessage = "Giving " + player.getName() + " " + stack.toString();
							Log.log(logMessage);
							String clientCommand = "/servermessage " + EnumColor.WHITE.toString() + " " + logMessage;
							update.addValue(clientCommand);
						}
					}				
				}
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
//				/affix <id> 
//				/affix <affix_id> <player_id> <main_inventory_index> #server command
				String[] split = command.split(" ");
				EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[2])));
				player.inventory.getMainInventory()[Integer.parseInt(split[3])].rollAffixBonuses(Integer.parseInt(split[1]));
			}
			if(command.startsWith("/say"))
			{
				String clientCommand = command.replace("say", "servermessage");
				update.addValue(clientCommand);
			}
		} catch (Exception e) {
			System.err.println("Server-Command failed: " + command);
			e.printStackTrace();
		}
	}
		
	public static String processClientCommand(ServerUpdate update, Object associatedObject, World world, 
			GameEngine engine, Vector<EntityPlayer> players, String command, List<String> pendingChunkRequests)
	{
		if(command == null)
		{
			return "";
		}
		try {
			if(command.startsWith("/placefrontblock"))
			{
				///placefrontblock <block-x> <block-y> <player-id> <blockID> <player's selected slot> 
				String[] split = command.split(" ");
				//Check if the player is stunned. If so, block this action.
				if(((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3])))).isStunned())
				{
					return "";
				}
				EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3])));
				boolean success = world.placeBlock(player, 
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
					player.inventory.removeItemsFromInventoryStack(player, 1, Integer.parseInt(split[5]));
				}
				
				return command + " " + success;
			}
			else if(command.startsWith("/placebackblock"))
			{
				///placebackblock <block-x> <block-y> <player-id> <blockID> <player's selected slot> 
				String[] split = command.split(" ");
				//Check if the player is stunned. If so, block this action.
				if(((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3])))).isStunned())
				{
					return "";
				}
				EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[3])));
				boolean success = world.placeBackWall(player, 
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
					player.inventory.removeItemsFromInventoryStack(player, 1, Integer.parseInt(split[5]));
				}
				return command + " " + success;
			}
			else if(command.startsWith("/mine"))
			{
				String[] split = command.split(" ");
				//Check if the player is stunned. If so, block this action.
				if(((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[2])))).isStunned())
				{
					return "";
				}
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
				//Check if the player is stunned. If so, block this action.
				if(((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[2])))).isStunned())
				{
					return "";
				}
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
				if(split[2].equals("chunkrequest"))
				{
					if(world.chunksLoaded.get(split[3]))
					{
						update.addChunkUpdate(world.getChunk(Integer.parseInt(split[3])));
					}		
					else
					{
						pendingChunkRequests.add(command);
					}
				}
				else if(split[2].equals("startswing"))
				{
					///player <playerID> startswing <selected_slot>
					//Check if the player is stunned. If so, block this action.
					if(player.isStunned())
					{
						return "";
					}
					if(!player.isSwingingTool()) 
					{
						player.selectedSlot = Integer.parseInt(split[3]);
						UpdateWithObject objUpdate = new UpdateWithObject();
						objUpdate.command = "/player " + player.entityID + " inv_and_action_update " + player.selectedSlot;
						objUpdate.object = new DisplayableItemStack(player.inventory.getMainInventoryStack(player.selectedSlot));
						update.addObjectUpdate(objUpdate);
						player.startSwingingTool();
						ItemTool tool = (ItemTool) Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[3])).getItemID()];
						update.addValue("/soundeffect " + tool.hitSound);
					}
				}
				else if(split[2].equals("cancelswing"))
				{
					player.clearSwing();
					//"/player <player_id> stopswing"
					command = "/player " + player.entityID + " stopswing";
					update.addValue(command);
				}
				else if(split[2].equals("say"))
				{
					if(split[4].startsWith("/"))
					{
						//A server command that the player can issue
						String remaining = command.substring(command.indexOf(" ", command.indexOf("say") + 4) + 1);
						
						//Put what the player said onto the console
						String[] remainingSplit = remaining.split(" ");
						
						String message = "";
						if(remainingSplit.length == 1)
						{
							message = remaining;
						}
						else
						{
							message = remainingSplit[1] + ": " + remaining.substring(remaining.indexOf(" ", 
									remaining.indexOf(" ", 
										remaining.indexOf(" ", 
												remaining.indexOf(" ") + 1)) + 1) + 1);
							
						}
						Log.log(message);
						
						//TODO: check for permissions
						engine.registerServerCommand(remaining);
					}
					else
					{
						//Chat to everyone
						//"/say <name> <color> <message>"
						String chatmessage = "/say " + player.getName() + " " + command.substring(command.indexOf("say") + 4);
	
						//Put what the player said onto the console
						String[] remainingSplit = chatmessage.split(" ");
						String message = remainingSplit[1] + ": " + chatmessage.substring(chatmessage.indexOf(" ", 
								chatmessage.indexOf(" ", 
										chatmessage.indexOf(" ", 
												chatmessage.indexOf(" ") + 1)) + 1) + 1);
						Log.log(message);
						
						update.addValue(chatmessage);
					}				
				}
				else if(split[2].equals("use")) //Uses the currently held item
				{
					//Check if the player is stunned. If so, block this action.
					if(player.isStunned())
					{
						return "";
					}
					player.selectedSlot = Integer.parseInt(split[3]);
					Item.itemsList[player.inventory.getMainInventoryStack(Integer.parseInt(split[3])).getItemID()].onRightClick(world, player);
				}
				else if(split[2].equals("mousepickup"))
				{
	//				/player <id> mousepickup <inventoryid> <index> <qty> #qty of 1, 1/2, all
					MouseItemHelper.pickupItemInInventory(world, player, Integer.parseInt(split[3]), Integer.parseInt(split[4]), split[5]);
				}
				else if(split[2].equals("mouseplace"))
				{
	//				/player <id> mouseplace <inventoryid> <index> <qty> #qty of 1, 1/2, all
					MouseItemHelper.placeItemInInventory(world, player, Integer.parseInt(split[3]), Integer.parseInt(split[4]), split[5]);
				}
				else if(split[2].equals("mousethrow"))
				{
	//				/player <id> mousethrow 
					MouseItemHelper.throwMouseItem(update, world, player, split[3]);
				}
				else if(split[2].equals("craft"))
				{
	//				/player <id> craft <recipe_id>
					MouseItemHelper.craftRecipe(world, player, Integer.parseInt(split[3]));
				}
				else if(split[2].equals("mouseremove"))
				{
					MouseItemHelper.removeMouseItem(world, player, split[3]);
				}
				else if(split[2].equals("shiftclick"))
				{
					MouseItemHelper.handleShiftClick(world, player, Integer.parseInt(split[3]), Integer.parseInt(split[4]));
				}
				else if(split[2].equals("socketgem"))
				{
					MouseItemHelper.socketGem(world, player, Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
				}
				else if(split[2].equals("setactionbarslot"))
				{
//					/player <id> setactionbarslot <slot>
					player.selectedSlot = Integer.parseInt(split[3]);
					update.addValue(command);
				}
			}
			else if(command.startsWith("/quit"))
			{
				String[] split = command.split(" ");
				TerraeRasa.requestThreadCloseByID(Integer.parseInt(split[1]));
			}
		
		} catch (Exception e) {
			System.err.println("Client-Based-Command failed: " + command);
			e.printStackTrace();
		}
		return "";
	}
	
	
}
