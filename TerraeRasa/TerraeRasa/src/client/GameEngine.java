package client;

import hardware.Keys;
import hardware.MouseInput;
import io.Chunk;
import io.ChunkManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import render.GuiChatbox;
import render.MainMenu;
import render.Render;
import render.RenderMenu;
import render.RenderWorld;
import statuseffects.DisplayableStatusEffect;
import transmission.BlockUpdate;
import transmission.ChunkExpander;
import transmission.ClientUpdate;
import transmission.CompressedClientUpdate;
import transmission.CompressedServerUpdate;
import transmission.EntityUpdate;
import transmission.PositionUpdate;
import transmission.StatUpdate;
import transmission.SuperCompressedChunk;
import transmission.TransmittablePlayer;
import transmission.UpdateWithObject;
import utils.ColoredText;
import utils.Cooldown;
import utils.DisplayableItemStack;
import utils.ErrorUtils;
import utils.FileManager;
import world.World;
import audio.SoundEngine;
import blocks.Block;
import blocks.MinimalBlock;
import entities.EntityPlayer;
import entities.IEntityTransmitBase;
import enums.EnumColor;
import enums.EnumEventType;
import enums.EnumHardwareInput;

/**
 * <code>GameEngine</code> is the class responsible for running the main game loop, and other core features of multiple worlds.
 * <code>GameEngine</code> defines 4 important methods. 
 * <br><br>
 * The method {@link #startGame(World, EntityPlayer)} defines the method to actually start the game with the specified World object
 * and player. This will close the main menu and begin rendering based on the Chunk data. 
 * <br><br>
 * Most of the application's life cycle is spent in {@link #run()} method, which contains the main game loop. This handles
 * everything from the main menu to the game, and save menu. Before exiting this method, {@link #saveSettings()} will be called
 * to update any settings changed during runtime. 
 * <br><br>
 * Additional methods of interest: <br>
 * {@link #changeWorld(int)}, {@link #closeGameToMenu()}
 *   
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GameEngine 
{
	/** The number of game ticks per second - this will always be 20 */
	public static final int TICKS_PER_SECOND = 20;
//	/** In single player one render mode can be active. This decides what world is updated, loaded, and rendered. The names
//	 * correspond directly to that respective world. */
//	public final static int RENDER_MODE_WORLD_EARTH = 1,
//							   RENDER_MODE_WORLD_HELL = 2,
//							   RENDER_MODE_WORLD_SKY  = 3;
//	/** The currently selected RENDER_MODE_WORLD value, detailing what world to update, load, and render.*/
//	private int renderMode;
	//private GuiMainMenu mainMenu;
	private MainMenu mainMenu;
	private World world;
//	private WorldHell worldHell;
//	private WorldSky worldSky;
//	private EntityPlayer sentPlayer;
	private EntityPlayer activePlayer;
	private String activePlayerName;
	private Settings settings;
	private RenderMenu renderMenu;
	public ChunkManager chunkManager;
	private String universeName;
	private EngineLock engineLock;
	private static boolean canPlayMP;
	private int activePlayerID = -1;
	private GuiChatbox chatbox;
	private boolean closeRequested;
	
	public EngineLock getEngineLock()
	{
		return engineLock;
	}
	
	/**
	 * Creates a new instance of GameEngine. This includes setting the renderMode to RENDER_MODE_WORLD_EARTH
	 * and loading the settings object from disk. If a settings object cannot be found a new one is created. 
	 */
	public GameEngine()
	{
//		renderMode = RENDER_MODE_WORLD_EARTH;
		activePlayerName = "";
		try {
			loadSettings();
		} catch (IOException e) {
			settings = new Settings();
		} catch (ClassNotFoundException e) {
			settings = new Settings();
		}
		engineLock = new EngineLock(this);
	}
	
	public void run()
	{
		try {
			//Variables for the gameloop cap (20 times / second)	        
			final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
			final int MAX_FRAMESKIP = 5;
			long next_game_tick = System.currentTimeMillis();
			long start, end;
			long fps = 0;
			int loops;
			start = System.currentTimeMillis();
			
			Vector<EnumHardwareInput> hardwareInput = new Vector<EnumHardwareInput>(10);
			Vector<String> clientCommands = new Vector<String>();
			ClientUpdate update = new ClientUpdate();
			
		    while(!TerraeRasa.done) //Main Game Loop
		    {
		    	EntityPlayer player = null;
		    	try {
			    	if(activePlayerID != -1 && world.entitiesByID.get(""+activePlayerID) != null)
			    	{
			    		player = (EntityPlayer) world.getEntityByID(activePlayerID);
			    	}
			    	//TODO: make more elegant
		        	if(activePlayerID != -1 && world.entitiesByID.get(""+activePlayerID) == null)
		        	{
		        		activePlayer.entityID = activePlayerID;
		        		world.entitiesByID.put(""+activePlayerID, activePlayer);
		        	}
		    	} catch(NullPointerException e) {		    		
		    	}
		    	
		    	if(!TerraeRasa.isMainMenuOpen && (!canPlayMP || player == null))
		    	{
		    		Thread.sleep(100);
		    		continue;
		    	}
		    	
		    	
		    	loops = 0;
		        while(System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) //Update the game 20 times/second 
		        {
//		        	if(getPlayer() != null && getPlayer().defeated)
//		        	{
//		        		hardcoreDeath();
//		        	}
		        	
		        	hardwareInput.clear();
		        	Keys.universalKeyboard(update, world, player, settings, settings.keybinds, hardwareInput);
		        	
		        	//If the music is already playing,set the volume to whatever the settings say it is
		        	if (SoundEngine.getCurrentMusic() != null){
		        		SoundEngine.setVolume((float) settings.musicVolume);
		        		//System.out.println("Settings: " + settings.volume + " Music: " + soundEngine.getVolume());
		        	}
		        	
		        	if(TerraeRasa.isMainMenuOpen) //if the main menu is open, render that, otherwise render the game
			        {
			        	mainMenu.update(settings);
				    }
		        	else if(settings.menuOpen) //Ingame pause menu - distribute keyboard/mouse input appropriately
		        	{ 
		        		SoundEngine.setCurrentMusic("Main Music");
		        		renderMenu.mouse(settings);
		        		renderMenu.keyboard(settings);
		        	}
		        	else if(!TerraeRasa.isMainMenuOpen) //Handle game inputs if the main menu isnt open (aka the game is being played)
		        	{		        		
		        		SoundEngine.setCurrentMusic("Pause Music");
		        		
		        		//Update the actual chatbox
		        		chatbox.update();
		        		
		        		//Hardware inputs
		        		if(chatbox.isOpen())
		        		{
		        			chatbox.keyboard(player, player.entityID, update);
		        		}
		        		else 
		        		{
		        			Keys.keyboard(this, update, world, player, settings, settings.keybinds, hardwareInput);	            
		        			MouseInput.mouse(world, player, clientCommands, hardwareInput);
		        		}
		        		
		        		//Client player tick
		        		world.onClientWorldTick(update, player);
		        		
		        		

				        
		        		CompressedClientUpdate compUpdate = new CompressedClientUpdate();
		        		compUpdate.playerID = activePlayerID;
		        		//Hardware IO
		        		EnumHardwareInput[] inputsArray = new EnumHardwareInput[hardwareInput.size()];
		        		hardwareInput.copyInto(inputsArray);
		        		compUpdate.clientInput = inputsArray;
		        		hardwareInput.clear();
		        		//Client Updates (String stuff)
		        		clientCommands.addAll(update.commands);
		        		if(closeRequested) {
		        			clientCommands.add("/quit " + activePlayerID);
		        		}
		        		String[] clientUpdates = new String[clientCommands.size()];
		        		clientCommands.copyInto(clientUpdates);
		        		compUpdate.commands = clientUpdates;
		        		clientCommands.clear();
		        		//Special updates
		        		compUpdate.objectUpdates = update.getObjectUpdates();		        		
		        		//register the update for the server to recieve
		        		engineLock.addClientUpdate(compUpdate);
		        		
		        		if(engineLock.hasUpdates())
		        		{
		        			//Process the outputs
		        			CompressedServerUpdate[] updates = engineLock.yieldServerUpdates();
		        			processUpdates(player, updates);
		        		}		        		
		        		update = new ClientUpdate();
		        	}
		        	
		        	TerraeRasa.terraeRasa.checkWindowSize();		        	 
		        	next_game_tick += SKIP_TICKS;
 		            loops++;
		        }
		        
		        //Make sure the game loop doesn't fall very far behind and have to accelerate the 
		        //game for an extended period of time
		        if(System.currentTimeMillis() - next_game_tick > 1000)
		        {
		        	next_game_tick = System.currentTimeMillis();
		        }
		        
		        Display.update();
		    	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glClearColor(0,0,0,0);
				GL11.glColor4f(1, 1, 1, 1);
				GL11.glPushMatrix();		        
		        
		        if(TerraeRasa.isMainMenuOpen) //if the main menu is open, render that, otherwise render the game
		        {
		        	SoundEngine.setCurrentMusic("Placeholder Music");
		        	mainMenu.render(settings);
			    }
		        else
		        {		        
//		        	if(renderMode == RENDER_MODE_WORLD_EARTH)
//		    		{
		        	RenderWorld.render(update, world, world.otherPlayers, player, settings); //Renders Everything on the screen for the game
		     		if(chatbox.isOpen())
		     		{
		     			chatbox.draw();
		     		}		 
		     		else
		     		{
		     			chatbox.drawPartially();
		     		}
		        
//		    		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		    		{
//		    		 	RenderGlobal.render(worldHell, player, renderMode, settings); //Renders Everything on the screen for the game
//		    		}
//		    		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		    		{
//		    		 	RenderGlobal.render(worldSky, player, renderMode, settings); //Renders Everything on the screen for the game
//		    		}
		        	fps++;
		        }
		        
		        if(settings.menuOpen)
		        {
		        	renderMenu.render(world, settings);
		        }
		        
		        
		        GL11.glPopMatrix();		        
		        Display.swapBuffers(); //allows the display to update when using VBO's, probably
		        Display.update(); //updates the display
				
		        if(System.currentTimeMillis() - start >= 5000)
		        {
		        	start = System.currentTimeMillis();
	        		end = System.currentTimeMillis();     
	        		fps = 0;
		    	}
	        	//     System.out.println(end - start);
		    }     
		} catch(Exception e) {
			//Fatal error catching
			System.err.println("Fatal client error caused by: ");
			e.printStackTrace();			
			ErrorUtils errorUtils = new ErrorUtils();
			errorUtils.writeErrorToFile(e, true);			
		} finally {
			//Save the settings; Cleanup
		    try {
		    	SoundEngine.destroy();
		    	saveSettings();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processUpdates(EntityPlayer clientPlayer, CompressedServerUpdate[] updates)
	{
		for(CompressedServerUpdate serverupdate : updates)
		{
			for(StatUpdate update : serverupdate.statUpdates)
			{
				if(activePlayerID == update.entityID)
				{
					((EntityPlayer)(world.getEntityByID(activePlayerID))).setStats(update);
				}
			}
			for(UpdateWithObject update : serverupdate.objectUpdates)
			{
				if(update.command.startsWith("/player"))
				{
					String[] split = update.command.split(" ");
					if(split[2].equals("inv_and_action_update"))
					{
						EntityPlayer player = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1]))));
						player.selectedSlot = Integer.parseInt(split[3]);
						player.inventory.putDisplayableItemStackInSlot(world, player, (DisplayableItemStack)(update.object), Integer.parseInt(split[3]), true);
					}
					else if(split[2].equals("statuseffectadd"))
					{
						EntityPlayer player = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1]))));
						player.registerStatusEffect((DisplayableStatusEffect)(update.object));
					}
					else if(split[2].equals("mouseitemset"))
					{
						///player <id> mouseitemset ---> Includes DisplayableItemStack Object
						((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).heldMouseItem = (DisplayableItemStack)(update.object);
					}
					else if(split[2].equals("inventoryset"))
					{
//						/player <id> inventoryset <inventory_id> <inventory_slot_index> ---> Includes ItemStack Object
//						1- Main
//						2- Armor
//						3- Quiver
//						4- Trash

						EntityPlayer player = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1]))));
						int inventoryID = Integer.parseInt(split[3]);
						int index = Integer.parseInt(split[4]);
						
						if(inventoryID == 1) //Main
						{
							player.inventoryChanged = true;
							player.inventory.putDisplayableItemStackInSlot(world, player, (DisplayableItemStack)(update.object), index, false);
						}
						else if(inventoryID == 2) //Armor
						{
							player.inventoryChanged = true;
							player.inventory.setArmorInventoryStack(player, (DisplayableItemStack)(update.object), player.inventory.getArmorInventoryStack(index), index, false);
							player.forceDownHMS();
						}
						else if(inventoryID == 3) //Quiver
						{
							player.inventoryChanged = true;
							player.inventory.setQuiverStack(player, (DisplayableItemStack)(update.object), index, false);
						}
						else if(inventoryID == 4) //Trash
						{
							player.inventoryChanged = true;
							player.inventory.setTrashStack((DisplayableItemStack)(update.object), index);
						}
					}	
					else if(split[2].equals("putoncooldown"))
					{
						EntityPlayer player = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1]))));
						Cooldown cooldown = (Cooldown)(update.object);
						player.putOnCooldown(cooldown.id, cooldown.ticksLeft);
					}
				}
				else if(update.command.startsWith("/recievesavable"))
				{
					String[] split = update.command.split(" ");
					if(Integer.parseInt(split[1]) == activePlayerID)
					{
						try {
							FileManager manager = new FileManager();
							manager.savePlayer(activePlayerName, (String)(update.object));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						clientPlayer = null;
						world = null;
						TerraeRasa.isMainMenuOpen = true;
						resetMainMenu();
						TerraeRasa.terminateClientConnection();
					}
				}
			}
			for(String command : serverupdate.values)
			{
				processCommand(clientPlayer, command);
			}
			for(SuperCompressedChunk compchunk : serverupdate.chunks)
			{
				Chunk chunk = ChunkExpander.expandChunk(compchunk);
				String command = "/player " + activePlayerID + " chunkrequest " + chunk.getX();
				world.removePendingChunkRequest(command);
				world.registerChunk(chunk, chunk.getX());
			}
			for(BlockUpdate update : serverupdate.blockUpdates)
			{
				if(update.type == 0)
				{
					Block block = Block.blocksList[update.block.id].clone().mergeOnto(new MinimalBlock(update.block));
					if(block.lightStrength > 0) {
						world.setBlock(block, update.x, update.y, EnumEventType.EVENT_BLOCK_PLACE_LIGHT);
						world.setBitMap(update.x-1, update.y, world.updateBlockBitMap(update.x-1, update.y));
						world.setBitMap(update.x, update.y-1, world.updateBlockBitMap(update.x, update.y-1));
						world.setBitMap(update.x, update.y, world.updateBlockBitMap(update.x, update.y));
						world.setBitMap(update.x+1, update.y, world.updateBlockBitMap(update.x+1, update.y));
						world.setBitMap(update.x, update.y+1, world.updateBlockBitMap(update.x, update.y+1));
					}
					else {
						world.setBlock(block, update.x, update.y, EnumEventType.EVENT_BLOCK_PLACE);
						
						world.setBitMap(update.x-1, update.y, world.updateBlockBitMap(update.x-1, update.y));
						world.setBitMap(update.x, update.y-1, world.updateBlockBitMap(update.x, update.y-1));
						world.setBitMap(update.x, update.y, world.updateBlockBitMap(update.x, update.y));
						world.setBitMap(update.x+1, update.y, world.updateBlockBitMap(update.x+1, update.y));
						world.setBitMap(update.x, update.y+1, world.updateBlockBitMap(update.x, update.y+1));
						
					}
				}
				else 
				{
					world.setBackBlock(Block.blocksList[update.block.id].clone().mergeOnto(new MinimalBlock(update.block)), update.x, update.y);
				}
			}
			for(EntityUpdate update : serverupdate.entityUpdates)
			{
				if(update.type == 5) { //Player
					EntityPlayer player = null;
					if(update.updatedEntity != null)
					{
						player = new EntityPlayer((TransmittablePlayer)update.updatedEntity);
					}
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						world.overwriteEntityByID(player.entityID, player);
					}
					else if(update.action == 'a') {
						world.addPlayer((TransmittablePlayer)update.updatedEntity);
					}
				}				
				else if(update.type == 4 || update.type == 3 || update.type == 2 || update.type == 1) { //Friendly
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						world.overwriteEntityByID(((IEntityTransmitBase)(update.updatedEntity)).getEntityID(), update.updatedEntity);
					}
					else if(update.action == 'a') {
						world.addUnspecifiedEntity((IEntityTransmitBase)update.updatedEntity);
					}
				}				
			}
			for(PositionUpdate position : serverupdate.positionUpdates)
			{
				try {
					((IEntityTransmitBase) world.getEntityByID(position.entityID)).setPosition(position.x, position.y);			
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private void processCommand(EntityPlayer player, String command)
	{
		if(command.startsWith("/worldtext"))
		{
			String[] split = command.split(" ");
			world.addTemporaryText(split[3], Integer.parseInt(split[1]), Integer.parseInt(split[2]), 20, EnumColor.get(split[4]));
		}
		else if(command.startsWith("/player"))
		{
			String[] split = command.split(" ");
			if(split[2].equals("swingangle"))
			{
				((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).setSwingAngle(Double.parseDouble(split[3]));
			}
			else if(split[2].equals("stopswing"))
			{
				((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).clearSwing();
			}
			else if(split[2].equals("sethms"))
			{
				EntityPlayer p = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1]))));
				if(p != null) 
				{
					p.setHealth(Double.parseDouble(split[3]));
					p.mana = Double.parseDouble(split[4]);
					p.specialEnergy = Double.parseDouble(split[5]);
				}
			}
			else if(split[2].equals("statuseffectupdate"))
			{
				DisplayableStatusEffect effect = ((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).getStatusEffectByID(Long.parseLong(split[3]));
				if(effect != null) 
				{
					effect.ticksLeft = (Integer.parseInt(split[4]));
				}
			}
			else if(split[2].equals("statuseffectremove"))
			{
				((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).removeStatusEffectByID(Long.parseLong(split[3]));
			}
			else if(split[2].equals("setactionbarslot"))
			{
//				/player <id> setactionbarslot <slot>
				((EntityPlayer)(world.getEntityByID(Integer.parseInt(split[1])))).selectedSlot = Integer.parseInt(split[3]);
			}

			//These only affect this client's player
			if(!(activePlayerID == Integer.parseInt(split[1])))
			{
				return;
			}
			if(split[2].equals("quiverremove"))
			{
				player.inventory.removeItemsFromQuiverStack(player, Integer.parseInt(split[4]), Integer.parseInt(split[3]));
			}
			else if(split[2].equals("inventoryremove"))
			{
				player.inventory.removeItemsFromInventoryStack(player, Integer.parseInt(split[4]), Integer.parseInt(split[3]));
			}
			
		
		}
		else if(command.startsWith("/say"))
		{
			// "/say <name> <color> <message>"
			String[] split = command.split(" ");
			EnumColor color = EnumColor.get(split[2]);
			String remaining = split[1] + ": " + command.substring(command.indexOf(" ", 
					command.indexOf(" ", 
							command.indexOf(" ", 
									command.indexOf(" ") + 1)) + 1) + 1);
			ColoredText text = new ColoredText(color, remaining);
			chatbox.log(text);
		}
		else if(command.startsWith("/servermessage"))
		{
			// "/servermessage <color> <message>"
			String[] split = command.split(" ");
			EnumColor color = EnumColor.get(split[1]);
			String remaining = command.substring(command.indexOf(" ", 
					command.indexOf(" ", 
							command.indexOf(" ") + 1)) + 1);
			ColoredText text = new ColoredText(color, remaining);
			chatbox.log(text);
		}
		else if(command.startsWith("/soundeffect"))
		{
			///soundeffect <effect_name>
			String effectName = command.substring(command.indexOf(" ") + 1);
			System.out.println("Playing effect : " + effectName);
			SoundEngine.playSoundEffect(effectName);
		}
		else if(command.startsWith("/worldtimeset"))
		{
//			/worldtimeset <time_in_ticks>
			String[] split = command.split(" ");
			world.setWorldTime(Integer.parseInt(split[1]));
		}
	}
	
	public void addColoredText(ColoredText text)
	{
		chatbox.addText(text);
	}
	
	public void addUncoloredText(String text)
	{
		ColoredText whitetext = new ColoredText(EnumColor.WHITE, text);
		chatbox.addText(whitetext);
	}
	
	public void addUncoloredText(String text, int ticks)
	{
		ColoredText whitetext = new ColoredText(EnumColor.WHITE, text, ticks);
		chatbox.addText(whitetext);
	}
	
	public void setChatOpen(boolean flag)
	{
		chatbox.setIsOpen(flag);
	}
	
	public void toggleChatOpen()
	{
		for( ; Keyboard.next(); ) { 
			Keyboard.getEventCharacter();
			Keyboard.getEventKey();
		}
		chatbox.setIsOpen(!chatbox.isOpen());
	}
	
	public static void flagAsMPPlayable()
	{
		canPlayMP = true;
	}
	
	public void startMPGame()
	{
		closeRequested = false;
		TerraeRasa.isMainMenuOpen = false;
		mainMenu = null;
	}
	
	public void setActivePlayerID(int id)
	{
		this.activePlayerID = id;
	}
	
	/**
	 * Starts a game from the main menu.
	 * @param world the world to play on.
	 * @param player the player to play on.
	 */
	public void startGame(String universeName, World world, String playerName)
	{
//		if(this.world != null)
//		{
//			throw new RuntimeException("World already exists!");
//		}
		this.universeName = universeName;
		this.world = world;
		this.world.chunkManager = chunkManager;
		this.world.chunkManager.setUniverseName(universeName);
		this.activePlayerName = playerName;
		TerraeRasa.isMainMenuOpen = false;
		mainMenu = null;
	}
		
	/**
	 * Initializes miscellaneous things within the game engine. This should be called after object creation so 
	 * that the UI thread does not throw an exception while loading textures.
	 */
	public void init()
	{
		Render.initializeTextures(getWorld());
		chatbox = new GuiChatbox();
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);
		addUncoloredText(" ", 0);		
		chunkManager = new ChunkManager();
		mainMenu = new MainMenu(settings);
		renderMenu = new RenderMenu(settings);
		SoundEngine.initialize(settings);
		Display.setVSyncEnabled(settings.vsyncEnabled);
		debugCheats();
	}
	
	/**
	 * Loads the Settings for the entire game upon starting the game.
	 * @throws IOException Indicates the saving operation has failed
	 * @throws ClassNotFoundException Indicates the Settings class does not exist with the correct version
	 */
	public void loadSettings() 
			throws IOException, ClassNotFoundException
	{
		FileManager fileManager = new FileManager();
		this.settings = fileManager.loadSettings();
	}
	
	/**
	 * Saves the Settings for the entire game to disk, this is called before exiting the run method.
	 * @throws IOException Indicates the saving operation has failed
	 * @throws FileNotFoundException Indicates the desired directory (file) is not found on the filepath
	 */
	public void saveSettings() 
			throws FileNotFoundException, IOException
	{
		FileManager fileManager = new FileManager();
		fileManager.saveSettings(settings);
	}
	
	/**
	 * Inits some cheats and inventory stuff for debugging
	 */
	private void debugCheats()
	{
		if(TerraeRasa.initInDebugMode)
		{
			//TODO move this serverside now that the player is loaded there. If debug then give stuff to the player, basically.
//			FileManager fileManager = new FileManager();
//			EntityPlayer player = new EntityPlayer(fileManager.generateAndSavePlayer("Debug_Player", EnumPlayerDifficulty.HARD));
//			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.goldSword));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.copperPickaxe));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.godminiumPickaxe));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.goldAxe));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.stone, 100));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.torch, 100));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.chest, 100));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.ironChest, 100));			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.steadfastShield));			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.woodenBow));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.absorbPotion2, 50));			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.goldIngot, 100));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.silverIngot, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.healthPotion1, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.healthPotion2, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.manaPotion1, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.manaPotion2, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.manaStar, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Block.furnace, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Block.craftingTable, 6));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Block.plank, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.goldPickaxe));		
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.copperIngot, 100));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.ironIngot, 6));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.absorbPotion1, 20));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.criticalChancePotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.criticalChancePotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.attackSpeedPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.attackSpeedPotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.damageBuffPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.damageBuffPotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.damageSoakPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.damageSoakPotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.dodgePotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.dodgePotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.manaRegenerationPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.manaRegenerationPotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.steelSkinPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.steelSkinPotion2, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.swiftnessPotion1, 5));
//			player.inventory.pickUpDisplayableItemStack(world, player, new DisplayableItemStack(Item.swiftnessPotion2, 5));	
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.jasper, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.sapphire, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.emerald, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.ruby, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.diamond, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opal, 50));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.goldRing, 6));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.berserkersEssence));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.snowball, 100));			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opHelmet));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opBody));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opPants));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opGloves));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opBoots));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.opBelt));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumHelmet));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumBody));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumPants));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumGloves));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumBoots));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.lunariumBelt));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.gemcraftingBench));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Block.alchemyStation));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.eaglesFeather));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Item.woodenArrow, 50));			
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Spell.rejuvenate));
//			player.inventory.pickUpDisplayableItemStack(world, sentPlayer, new DisplayableItemStack(Spell.bulwark));
//			
//			try {
//				fileManager.savePlayer(player);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}		
	}
	
	/**
	 * Initiates a hardcore death - which deletes the player and exits to the main menu. For now.
	 */
	public void hardcoreDeath()
	{
		//TODO: broke client side hardcore mode.
		//Delete the player
//		FileManager manager = new FileManager();
//		manager.deletefile("/Player Saves/" + getPlayer().getName());
//		this.setPlayer(null);
//		//Save the world
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			world.saveRemainingWorld();
//		}
//		//Reset to the main menu
//		resetMainMenu();
//		TerraeRasa.isMainMenuOpen = true; //Sets menu open
//		System.out.println("Game Over.");
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	/**
	 * Gets the world object currently in use by the game. This will be null if the main menu is open or something breaks.
	 * @return the world object for the current game, which may be null if on the main menu
	 */
	public World getWorld()
	{
		return world;
	}
	
	/**
	 * Changes the loaded world to something else. For example, changing from Earth to Hell would use this. Calling
	 * this method forces a save of the remaining World and then loads what's required for the new World. The value 
	 * of the param newMode should correspond to the class variables in GameEngine such as RENDER_MODE_WORLD_EARTH or
	 * RENDER_MODE_WORLD_HELL. Supplying an incorrect value will not load a new World.
	 * @param newMode the final integer value for the new world (indicating what object to manipulate)
	 * @throws IOException indicates a general failure to load the file, not relating to a version error
	 * @throws ClassNotFoundException indicates the saved world version is incompatible
	 */
	public void changeWorld(int newMode)
			throws IOException, ClassNotFoundException
	{
		System.out.println("GameEngine.changeWorld(int) - This does nothing");
//		String worldName = "";
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			world.saveRemainingWorld();
//			world = null;
//		}
//		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		{
//			worldHell.saveRemainingWorld();
//			worldHell = null;
//		}
//		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		{
//			worldName = worldSky.getWorldName();
//			worldSky.saveRemainingWorld();
//			worldSky = null;
//		}
//		
//		this.renderMode = newMode;
//		FileManager manager = new FileManager();
		
//		if(renderMode == RENDER_MODE_WORLD_EARTH)
//		{
//			manager.loadWorld("Earth", worldName);
//		}
//		else if(renderMode == RENDER_MODE_WORLD_HELL)
//		{
//			manager.loadWorld("Hell", worldName);
//		}
//		else if(renderMode == RENDER_MODE_WORLD_SKY)
//		{
//			manager.loadWorld("Sky", worldName);
//		}
	}
	
	/**
	 * Saves the remaining world that's loaded and the player to their respective save locations before
	 * exiting to the main menu.
	 * @throws FileNotFoundException indicates a failure to find the save location of the player or world
	 * @throws IOException indicates a general failure to save, not relating to the file
	 */
	public void requestClose() 
			throws FileNotFoundException, IOException
	{
		closeRequested = true;	
	}
	
	/**
	 * Creates a new GuiMainMenu and assigns it to mainMenu.
	 */
	public void resetMainMenu()
	{
		mainMenu = new MainMenu(settings);
	}

	public EntityPlayer getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(EntityPlayer player) {
		this.activePlayer = player;
	}
	
	public void setActivePlayerName(String name)
	{
		this.activePlayerName = name;
	}

	public String getActivePlayerName() {
		return activePlayerName;
	}
	
	public String getUniverseName()
	{
		return universeName;
	}
}