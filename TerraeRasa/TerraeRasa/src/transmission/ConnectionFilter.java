package transmission;

import java.util.Iterator;
import java.util.Vector;

import server.entities.EntityPlayer;


public class ConnectionFilter 
{
	public ConnectionFilter()
	{
	}
	
	public CompressedServerUpdate filterOutgoing(ServerUpdate update, EntityPlayer player)
	{
		CompressedServerUpdate compressedUpdate = new CompressedServerUpdate();
		
		//TODO other worlds filter
		//TODO block updates for chunks not loaded
		if(update.getStatUpdateLength() > 0)
		{
			Vector<StatUpdate> statUpdates = new Vector<StatUpdate>();
			statUpdates.addAll(update.statUpdates());
			
			Iterator<StatUpdate> it = statUpdates.iterator();
			while(it.hasNext())
			{
				StatUpdate statUpdate = it.next();
				if(statUpdate.entityID != player.entityID)
				{
//					System.out.println("[Removed " + statUpdate.entityID + "->" + player.entityID + "] " + "Removed stat update ");
					it.remove();
				}				
			}
			StatUpdate[] stats = new StatUpdate[statUpdates.size()];
			statUpdates.copyInto(stats);
			compressedUpdate.statUpdates = stats;
		}		
		if(update.getUpdateObjectLength() > 0)
		{
			Vector<UpdateWithObject> objectUpdates = new Vector<UpdateWithObject>();
			objectUpdates.addAll(update.objectUpdates());
			
			//Parse out the recievableplayers
			Iterator<UpdateWithObject> it = objectUpdates.iterator();
			while(it.hasNext())
			{
				UpdateWithObject objUpdate = it.next();
				if(objUpdate.command.startsWith("/recievesavable"))
				{
//					System.out.println("[Removed " + objUpdate.command + "->" + player.entityID + "] " + "Removed stat update ");
					it.remove();
				}		
				else if(objUpdate.command.startsWith("/chunk"))
				{
					String[] split = objUpdate.command.split(" ");
					if(Integer.parseInt(split[1]) != player.entityID)
					{
//						System.out.println("[Removed " + objUpdate.command + "->" + player.entityID + "] " + "Removed stat update ");

						it.remove();
					}
				}
			}
			UpdateWithObject[] objects = new UpdateWithObject[objectUpdates.size()];
			objectUpdates.copyInto(objects);
			compressedUpdate.objectUpdates = objects;
		}
		
		if(update.getCommandLength() > 0)
		{
			Vector<String> commands = new Vector<String>();
			commands.addAll(update.values());
			
			Iterator<String> it = commands.iterator();
			while(it.hasNext())
			{				
				String command = it.next();
	
				if(command.startsWith("/player"))
				{
					String[] split = command.split(" ");
					if(split[2].equals("sethms") && player.entityID != Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}
					else if(split[2].equals("statuseffectupdate") && player.entityID != Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}
					else if(split[2].equals("statuseffectremove") && player.entityID != Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}
					if(split[2].equals("quiverremove") && player.entityID != Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}
					else if(split[2].equals("inventoryremove") && player.entityID != Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}				
					else if(split[2].equals("setactionbarslot") && player.entityID == Integer.parseInt(split[1]))
					{
//						System.out.println("[Removed " + command + "->" + player.entityID + "] " + "Removed stat update ");
						it.remove();
					}
				}
				else if(command.startsWith("/say"))
				{
					// "/say <name> <color> <message>"
					//TODO [ENHANCEMENT] a /sayone command
				}					
			}
			String[] coms = new String[commands.size()];
			commands.copyInto(coms);
			compressedUpdate.values = coms;
		}		
		
		
		//These currently have no filter.
		compressedUpdate.blockUpdates = update.getBlockUpdates();
		compressedUpdate.entityUpdates = update.getEntityUpdates();
		compressedUpdate.positionUpdates = update.getPositionUpdates();
		return compressedUpdate;
	}

	public CompressedClientUpdate[] filterIn(CompressedClientUpdate[] input, int legalPlayerID)
	{
		for(CompressedClientUpdate update : input)
		{
			for(int i = 0; i < update.commands.length; i++)
			{	
				try {
					if(update.commands[i].startsWith("/placefrontblock"))
					{
						///placefrontblock <block-x> <block-y> <player-id> <blockID> <player's selected slot> 
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[3]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
					else if(update.commands[i].startsWith("/placebackblock"))
					{
						///placebackblock <block-x> <block-y> <player-id> <blockID> <player's selected slot> 
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[3]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
					else if(update.commands[i].startsWith("/mine"))
					{
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[2]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
					else if(update.commands[i].startsWith("/projectile"))
					{
//						/projectile launch playerid selected_slot mx my
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[2]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
					else if(update.commands[i].startsWith("/player"))
					{
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[1]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
					else if(update.commands[i].startsWith("/quit"))
					{
						String[] split = update.commands[i].split(" ");
						if(Integer.parseInt(split[1]) != legalPlayerID)
						{
							update.commands[i] = "";
						}
					}
				} catch (Exception e) {
					System.err.println("parse failed illegal command is: " + update.commands[i]);
					e.printStackTrace();
				}
			}			
		}
		return input;
	}
}
