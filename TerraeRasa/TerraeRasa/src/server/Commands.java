package server;

import items.Item;
import items.ItemMagic;
import items.ItemRanged;
import items.ItemThrown;
import items.ItemTool;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import math.MathHelper;
import server.entities.EntityPlayer;
import server.utils.MouseItemHelper;
import server.world.WorldServerEarth;
import statuseffects.StatusEffectAbsorb;
import statuseffects.StatusEffectAttackSpeedBuff;
import statuseffects.StatusEffectBleed;
import statuseffects.StatusEffectCriticalBuff;
import statuseffects.StatusEffectDamageBuff;
import statuseffects.StatusEffectDodgeBuff;
import statuseffects.StatusEffectFallHeight;
import statuseffects.StatusEffectJump;
import statuseffects.StatusEffectLastStand;
import statuseffects.StatusEffectManaRegeneration;
import statuseffects.StatusEffectPoison;
import statuseffects.StatusEffectRegeneration;
import statuseffects.StatusEffectSlowed;
import statuseffects.StatusEffectSteelSkin;
import statuseffects.StatusEffectStun;
import statuseffects.StatusEffectSwiftness;
import transmission.BlockUpdate;
import transmission.ChunkCompressor;
import transmission.PositionUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedBlock;
import transmission.UpdateWithObject;
import utils.ActionbarItem;
import utils.ItemStack;
import utils.MetaDataHelper;
import world.WeatherSnow;
import world.World;
import blocks.Block;
import blocks.BlockChest;
import blocks.MinimalBlock;
import entry.MPGameEngine;
import entry.MPGameLoop;
import entry.TerraeRasa;
import enums.EnumColor;

public class Commands 
{
	private static final Object processConsoleCommandLock = new Object();
	private static final Object processClientCommandLock = new Object();
	
	public static final int PERMISSION_ALL = 1,
			PERMISSION_MOD = 2,
			PERMISSION_ADMIN = 3,
			PERMISSION_CONSOLE = 4;
	

	private static void causeStatusEffect(WorldServerEarth world, EntityPlayer player, int effectID, int timeSeconds, double power)
	{
//		/effect <id> <time> <power> <player_id> 
//		  1 - Absorb
//	      2 - Attack Speed
//	      3 - Critical Strike
//	      4 - Damage
//	      5 - Dodge
//	      6 - Fall Height
//	      7 - Jump
//	      8 - Last Stand
//	      9 - Mana Regen
//	      10 - Regeneration
//	      11 - Steel Skin
//	      12 - Swiftness
//	      13 - Bleed
//	      14 - Poison
//	      15 - Slowed
//	      16 - Stun
		if(effectID == 1)
		{
			StatusEffectAbsorb effect = new StatusEffectAbsorb(timeSeconds, 1, (int) power);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 2)
		{
			StatusEffectAttackSpeedBuff effect = new StatusEffectAttackSpeedBuff(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 3)
		{
			StatusEffectCriticalBuff effect = new StatusEffectCriticalBuff(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 4)
		{
			StatusEffectDamageBuff effect = new StatusEffectDamageBuff(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 5)
		{
			StatusEffectDodgeBuff effect = new StatusEffectDodgeBuff(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 6)
		{
			StatusEffectFallHeight effect = new StatusEffectFallHeight(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 7)
		{
			StatusEffectJump effect = new StatusEffectJump(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 8)
		{
			StatusEffectLastStand effect = new StatusEffectLastStand(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 9)
		{
			StatusEffectManaRegeneration effect = new StatusEffectManaRegeneration(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 10)
		{
			StatusEffectRegeneration effect = new StatusEffectRegeneration(timeSeconds, 1, (int) power, 20, false);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 11)
		{
			StatusEffectSteelSkin effect = new StatusEffectSteelSkin(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 12)
		{
			StatusEffectSwiftness effect = new StatusEffectSwiftness(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 13)
		{
			StatusEffectBleed effect = new StatusEffectBleed(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 14)
		{
			StatusEffectPoison effect = new StatusEffectPoison(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 15)
		{
			StatusEffectSlowed effect = new StatusEffectSlowed(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		else if(effectID == 16)
		{
			StatusEffectStun effect = new StatusEffectStun(timeSeconds, 1, (int) power, 20);
			player.registerStatusEffect(world, effect);
		}
		
		
		
		
		
	}
	
	public static int getCommandTier(String command)
	{
		//Everyone or above			
		if(command.startsWith("/kill"))
		{
			return PERMISSION_ALL;
		}
		if(command.startsWith("/whisper"))
		{
			return PERMISSION_ALL;
		}

		//Mod or above
		if(command.startsWith("/teleport"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/settime"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/mod"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/unmod"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/kick"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/ban"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/unban"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/stop"))
		{
			return PERMISSION_MOD;
		}
		if(command.startsWith("/save-world"))
		{
			return PERMISSION_MOD;
		}
		
		//Admin only
		if(command.startsWith("/admin"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/unadmin"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/heal"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/mana"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/special"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/give"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/weather"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/effect"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/affix"))
		{
			return PERMISSION_ADMIN;
		}
		if(command.startsWith("/say"))
		{
			return PERMISSION_ADMIN;
		}
		return 	PERMISSION_CONSOLE;
	}
		
	// -- These are server commands
	public static void processConsoleCommand(ServerSettings settings, ServerUpdate update, Vector<EntityPlayer> players, WorldServerEarth world, MPGameLoop engine, String command)
	{	
		synchronized(processConsoleCommandLock)
		{
			try {
//				String[] commandComponents = command.split(" ");
				//Everyone or above			
				if(command.startsWith("/kill"))
				{
					String[] split = command.split(" ");
					EntityPlayer player = (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])));
					player.kill();
				}
				if(command.trim().equals("/password"))
				{
					String logMessage = "The current server password is: " + settings.getPassword();
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);
					return; //There is another password that sets
				}
				if(command.startsWith("/version"))
				{
					String logMessage = "The current server version is: " + TerraeRasa.getVersion();
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);
				}
				if(command.startsWith("/players"))
				{
					String logMessage = "(" + players.size() + "/" + settings.maxPlayers + "): ";
					for(EntityPlayer player : players)
					{
						logMessage += player.getName() + " ";
					}
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);
				}
				if(command.startsWith("/time"))
				{
					String logMessage = "The current server time is: " + new DecimalFormat("##.##").format(world.getWorldTimeInHours()) + " hours";
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);
				}
				if(command.startsWith("/port"))
				{
					String logMessage = "The current server port is: " + settings.port;
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);
				}
				if(command.startsWith("/maxplayers"))
				{
					String logMessage = "The maximum players is: " + settings.maxPlayers;
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
					update.addValue(clientCommand);					
				}
				if(command.trim().equals("/motd"))
				{
					String logMessage = settings.serverMessage;
					Log.log(logMessage);
					String clientCommand = "/servermessage " + EnumColor.GREEN.toString() + " " + logMessage;
					update.addValue(clientCommand);
					return;
				}
								
				//Mod or above
				if(command.startsWith("/teleport"))
				{
					String[] split = command.split(" ");
					if(split.length == 3)
					{
//						/teleport <p1> <p2>
						EntityPlayer teleportTarget = engine.getPlayer(split[1]);
						EntityPlayer teleportLocation = engine.getPlayer(split[2]);
						if(teleportTarget == null)
						{
							String logMessage = "Player 1 does not exist";
							Log.log(logMessage);
							String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
							update.addValue(clientCommand);
							return;
						}
						if(teleportLocation == null)
						{
							String logMessage = "Player 2 does not exist";
							Log.log(logMessage);
							String clientCommand = "/servermessage " + EnumColor.YELLOW.toString() + " " + logMessage;
							update.addValue(clientCommand);
							return;
						}
						teleportTarget.setPosition((int)teleportLocation.x, (int)teleportLocation.y);
						PositionUpdate positionUpdate = new PositionUpdate(teleportTarget.entityID, teleportTarget.x, teleportTarget.y);
						update.addPositionUpdate(positionUpdate);
					}
					else if(split.length == 4)
					{
//						/teleport <p1> <x> <y>
						EntityPlayer teleportTarget = engine.getPlayer(split[1]);
						int xLoc = Integer.parseInt(split[2]);
						int yLoc = Integer.parseInt(split[3]);
						teleportTarget.setPosition(xLoc, yLoc);
						PositionUpdate positionUpdate = new PositionUpdate(teleportTarget.entityID, teleportTarget.x, teleportTarget.y);
						update.addPositionUpdate(positionUpdate);
					}
				}
				if(command.startsWith("/settime"))
				{
	//				/time-set <hour>
					String[] split = command.split(" ");
					double hour = Double.parseDouble(split[1]) % 24;
					int timeInTicks = (int) (hour * World.GAMETICKSPERHOUR);
					world.setTime(timeInTicks);
					MPGameEngine.terraeRasa.gameEngine.addCommandUpdate("/worldtimeset " + timeInTicks);
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
					SecurityManager.addMod(players, player.getIP());
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
					SecurityManager.removeMod(players, player.getIP());
				}
				if(command.startsWith("/kick"))
				{
	//				/kick <player-name>
					String[] split = command.split(" ");
					MPGameEngine.kick(split[1]);
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
					MPGameEngine.finish();
				}
				if(command.startsWith("/save-world"))
				{
					engine.saveChunks();
				}
				if(command.startsWith("/password"))
				{
//					/password <new_password>
					String newPassword = command.substring(command.indexOf(" ") + 1);
					settings.setPassword(newPassword);
				}
				if(command.startsWith("/motd"))
				{
//					/motd <new_motd>
					String newMOTD = command.substring(command.indexOf(" ") + 1);
					settings.serverMessage = newMOTD;
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
					SecurityManager.addAdmin(players, player.getIP());
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
					SecurityManager.removeAdmin(players, player.getIP());
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
							if(Block.blocksList[ActionbarItem.blockIndex + itemID] != null)
							{
								ItemStack stack = new ItemStack(Block.blocksList[ActionbarItem.blockIndex + itemID], amount);
								player.inventory.pickUpItemStack(player, stack);
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
								player.inventory.pickUpItemStack(player, stack);
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
					String[] split = command.split(" ");
					if(split[1].equals("off"))
					{
//						/weather off
						world.turnOffWeather(update);
					}
					else
					{
//						/weather <x> <id> <time_seconds>
						int x = Integer.parseInt(split[1]);
						int weatherID = Integer.parseInt(split[2]);
						int timeSeconds = Integer.parseInt(split[3]);
					
						//Create a WeatherSnow effect
						if(weatherID == WeatherSnow.ID)
						{
							WeatherSnow snow = new WeatherSnow(world.getChunk(x), timeSeconds);
							snow.setTicksLeft(timeSeconds * MPGameLoop.TICKS_PER_SECOND);
							world.getChunk(x).weather = snow;
						}
					}
				}
				if(command.startsWith("/effect"))
				{
					String[] split = command.split(" ");
//					/effect <id> <time> <power> <player_id> 
					causeStatusEffect(world, (EntityPlayer)(world.getEntityByID(Integer.parseInt(split[4]))), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Double.parseDouble(split[3]));
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
	}
		
	public static String processClientCommand(ServerUpdate update, Object associatedObject, WorldServerEarth world, 
			MPGameLoop engine, Vector<EntityPlayer> players, String command, List<String> pendingChunkRequests)
	{
		synchronized(processClientCommandLock)
		{
			if(command == null || command.trim().equals(""))
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
					boolean success = world.placeBlock(update,
							player, 
							Integer.parseInt(split[1]), 
							Integer.parseInt(split[2]), 
							Block.blocksList[Integer.parseInt(split[4])]);
					if(success)
					{
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
						blockUpdate.block = new SuperCompressedBlock(world.getBackBlock(blockUpdate.x, blockUpdate.y));
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
							ServerUpdate chunkServerUpdate = new ServerUpdate();
		        			chunkServerUpdate.deferCompression = true;
							UpdateWithObject objUpdate = new UpdateWithObject();
		        			objUpdate.command = "/chunk " + split[1]; 
		        			objUpdate.object = ChunkCompressor.compressChunk(world.getChunk(Integer.parseInt(split[3])));
		        			chunkServerUpdate.addObjectUpdate(objUpdate);
		        			MPGameEngine.addWorldUpdate(chunkServerUpdate);
//							TODO: ?deferCompression here --> create + register new update object?
						}		
						else
						{
							System.out.println("Deferred : " + split[3]);
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
							objUpdate.object = new ItemStack(player.inventory.getMainInventoryStack(player.selectedSlot));
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
							
							engine.registerServerCommand(player, remaining, false);
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
					else if(split[2].equals("chestevent"))
					{
	//					/player <id> chestpickup <x> <y> <index> <qty>
						int x = Integer.parseInt(split[3]);
						int y = Integer.parseInt(split[4]);
						int index = Integer.parseInt(split[5]);
						
						//Get the metadata for the block's size
						int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[world.getBlock(x, y).id].blockWidth / 6), 
								(int)(Block.blocksList[world.getBlock(x, y).id].blockHeight / 6)); //metadata used by the block of size (x,y)
						int metaWidth = metadata.length; 
						int metaHeight = metadata[0].length;	
						int xOffset = 0;
						int yOffset = 0;					
						BlockChest chest = (BlockChest)world.getAssociatedBlock(x, y);
						MinimalBlock MinimalBlock = world.getBlock(x, y);
						if(MinimalBlock.metaData != 1) //Make sure its metadata is 1 (otherwise it doesnt technically exist)
						{
							//Loop until a the current chest's metadata value is found
							//This provides the offset to find the 'real' chest, with the actual items in it
							for(int i = 0; i < metaWidth; i++) 
							{
								for(int j = 0; j < metaHeight; j++)
								{
									if(metadata[i][j] == world.getBlock(x - xOffset, y - yOffset).metaData)
									{
										xOffset = i; 
										yOffset = j;
										break;
									}
								}
							}			
							//Update the chest
							MinimalBlock = world.getBlock(x - xOffset, y - yOffset);
							chest = (BlockChest)(world.getAssociatedBlock(x - xOffset, y - yOffset));
						}	
						
						if(chest.getItemStack(index) != null && player.getHeldMouseItem() == null) //The mouse doesn't have something picked up, so this is straightforward
						{
							if(split[6].equals("all"))
							{
								ItemStack mouseItem = new ItemStack(chest.getItemStack(index));
								chest.removeItemStack(index);
								player.setHeldMouseItem(mouseItem);
							}
							else if(split[6].equals("1/2"))
							{
								ItemStack mouseItem = new ItemStack(chest.getItemStack(index));
								mouseItem.setStackSize((int)(MathHelper.floorOne(mouseItem.getStackSize() / 2)));
								chest.removeItemsFromInventoryStack((mouseItem.getStackSize()), index);
								player.setHeldMouseItem(mouseItem);
							}
							else
							{
								throw new RuntimeException("This will not work -- NYI");
	//							int qty = Integer.parseInt(split[6]);
	//							chest.removeItemsFromInventoryStack(qty, index);
							}
							
							//Cause a block update for the chest
							BlockUpdate blockUpdate = new BlockUpdate();
							blockUpdate.x = x - xOffset;
							blockUpdate.y = (short) (y - yOffset);
							blockUpdate.block = new SuperCompressedBlock(chest);
							update.addBlockUpdate(blockUpdate);
							world.setBlock(chest, x - xOffset, y - yOffset);
						}
						else if(player.getHeldMouseItem() != null) //The mouse has something picked up, things need swapped
						{
							//Reference safe swap
							ItemStack mouseItem = new ItemStack(player.getHeldMouseItem());
							ItemStack chestItem = chest.takeItemStack(index);
							chest.placeItemStack(mouseItem, index);
							player.setHeldMouseItem(chestItem);
	
							//Cause a block update for the chest
							BlockUpdate blockUpdate = new BlockUpdate();
							blockUpdate.x = x - xOffset;
							blockUpdate.y = (short) (y - yOffset);
							blockUpdate.block = new SuperCompressedBlock(chest);
							update.addBlockUpdate(blockUpdate);
							world.setBlock(chest, x - xOffset, y - yOffset);
						}
					
					}
				}
				else if(command.startsWith("/quit"))
				{
//					/quit <player_id>
					String[] split = command.split(" ");
					MPGameEngine.requestThreadCloseByID(Integer.parseInt(split[1]));
				}
			
			} catch (Exception e) {
				System.err.println("Client-Based-Command failed: " + command);
				e.printStackTrace();
			}
			return "";
		}
	}
	
	
}
