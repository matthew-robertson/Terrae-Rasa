package client;

import hardware.Keys;
import hardware.MouseInput;
import io.Chunk;
import io.ChunkManager;
import items.Item;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import render.GuiChatbox;
import render.MainMenu;
import render.Render;
import render.RenderGlobal;
import render.RenderMenu;
import spells.Spell;
import transmission.BlockUpdate;
import transmission.ChunkExpander;
import transmission.ClientUpdate;
import transmission.CompressedClientUpdate;
import transmission.CompressedPlayer;
import transmission.CompressedServerUpdate;
import transmission.EntityUpdate;
import transmission.PositionUpdate;
import transmission.StatUpdate;
import transmission.SuperCompressedChunk;
import transmission.UpdateWithObject;
import utils.ColoredText;
import utils.ErrorUtils;
import utils.FileManager;
import utils.ItemStack;
import world.World;
import world.WorldHell;
import world.WorldSky;
import affix.AffixSturdy;
import audio.SoundEngine;
import blocks.Block;
import blocks.MinimalBlock;
import entities.EntityItemStack;
import entities.EntityNPCEnemy;
import entities.EntityNPCFriendly;
import entities.EntityPlayer;
import enums.EnumColor;
import enums.EnumEventType;
import enums.EnumHardwareInput;
import enums.EnumPlayerDifficulty;

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
	/** In single player one render mode can be active. This decides what world is updated, loaded, and rendered. The names
	 * correspond directly to that respective world. */
	public final static int RENDER_MODE_WORLD_EARTH = 1,
							   RENDER_MODE_WORLD_HELL = 2,
							   RENDER_MODE_WORLD_SKY  = 3;
	/** The currently selected RENDER_MODE_WORLD value, detailing what world to update, load, and render.*/
	private int renderMode;
	//private GuiMainMenu mainMenu;
	private MainMenu mainMenu;
	private World world;
	private WorldHell worldHell;
	private WorldSky worldSky;
	private EntityPlayer sentPlayer;
	private Settings settings;
	private SoundEngine soundEngine;
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
		renderMode = RENDER_MODE_WORLD_EARTH;
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
		        		sentPlayer.entityID = activePlayerID;
		        		world.entitiesByID.put(""+activePlayerID, sentPlayer);
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
		        	if (soundEngine.getCurrentMusic() != null){
		        		soundEngine.setVolume((float) settings.musicVolume);
		        		//System.out.println("Settings: " + settings.volume + " Music: " + soundEngine.getVolume());
		        	}
		        	
		        	if(TerraeRasa.isMainMenuOpen) //if the main menu is open, render that, otherwise render the game
			        {
			        	mainMenu.update(settings);
				    }
		        	else if(settings.menuOpen) //Ingame pause menu - distribute keyboard/mouse input appropriately
		        	{ 
		        		soundEngine.setCurrentMusic("Main Music");
		        		renderMenu.mouse(settings);
		        		renderMenu.keyboard(settings);
		        	}
		        	else if(!TerraeRasa.isMainMenuOpen) //Handle game inputs if the main menu isnt open (aka the game is being played)
		        	{		        		
		        		soundEngine.setCurrentMusic("Pause Music");
		        		
		        		//Update the actual chatbox
		        		chatbox.update();
		        		
		        		//Hardware inputs
		        		if(chatbox.isOpen())
		        		{
		        			chatbox.keyboard(player.entityID, update);
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
		        	soundEngine.setCurrentMusic("Placeholder Music");
		        	mainMenu.render(settings);
			    }
		        else
		        {		        
//		        	if(renderMode == RENDER_MODE_WORLD_EARTH)
//		    		{
		        	RenderGlobal.render(update, world, player, renderMode, settings); //Renders Everything on the screen for the game
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
				soundEngine.destroy();
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
						player.inventory.putItemStackInSlot(world, player, (ItemStack)(update.object), Integer.parseInt(split[3]));
					}
				}
				else if(update.command.startsWith("/fullplayersend"))
				{
					String[] split = update.command.split(" ");
					if(Integer.parseInt(split[1]) == activePlayerID)
					{
						clientPlayer.mergeOnto((CompressedPlayer)(update.object));
						try {
							FileManager manager = new FileManager();
							manager.savePlayer(clientPlayer);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						clientPlayer = null;
						world = null;
						TerraeRasa.isMainMenuOpen = true;
						resetMainMenu();
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
						player = EntityPlayer.expand((CompressedPlayer)update.updatedEntity);
					}
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						world.overwriteEntityByID(player.entityID, player);
					}
					else if(update.action == 'a') {
						world.addPlayer(player);
					}
				}
				else if(update.type == 4) { //Projectile
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						world.overwriteEntityByID(update.updatedEntity.entityID, update.updatedEntity);
					}
					else if(update.action == 'a') {
						world.addUnspecifiedEntity(update.updatedEntity);
					}
				}
				else if(update.type == 3) { //ItemStack
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						world.overwriteEntityByID(update.updatedEntity.entityID, update.updatedEntity);
					}
					else if(update.action == 'a') {
						world.addUnspecifiedEntity(update.updatedEntity);
					}
				}
				else if(update.type == 2) { //Friendly
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						EntityNPCFriendly npc = (EntityNPCFriendly)update.updatedEntity;
						npc.setTexture(EntityNPCFriendly.npcList[npc.getNPCID()].getTexture());
						world.overwriteEntityByID(update.updatedEntity.entityID, npc);
					}
					else if(update.action == 'a') {
						EntityNPCFriendly npc = (EntityNPCFriendly)update.updatedEntity;
						npc.setTexture(EntityNPCFriendly.npcList[npc.getNPCID()].getTexture());
						world.addUnspecifiedEntity(npc);
					}
				}
				else if(update.type == 1) { //Enemy
					if(update.action == 'r') {
						world.removeEntityByID(update.entityID);
					}
					else if(update.action == 'c') {
						EntityNPCEnemy enemy = (EntityNPCEnemy)update.updatedEntity;
						enemy.setTexture(EntityNPCEnemy.enemyList[enemy.getNPCID()].getTexture());
						world.overwriteEntityByID(update.updatedEntity.entityID, enemy);
					}
					else if(update.action == 'a') {
						EntityNPCEnemy enemy = (EntityNPCEnemy)update.updatedEntity;
						enemy.setTexture(EntityNPCEnemy.enemyList[enemy.getNPCID()].getTexture());
						world.addUnspecifiedEntity(enemy);
					}
				}
			}
			for(PositionUpdate position : serverupdate.positionUpdates)
			{
				try {
					world.getEntityByID(position.entityID).setPosition(position.x, position.y);			
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private void processCommand(EntityPlayer player, String command)
	{
		if(command.startsWith("/placefrontblock"))
		{
			String[] split = command.split(" ");
			if(Boolean.parseBoolean(split[6]) && Integer.parseInt(split[3]) == activePlayerID)
			{
				player.inventory.removeItemsFromInventory(player, new ItemStack(Integer.parseInt(split[4]), 1)); //take the item from the player's inventory
				player.onInventoryChange();
			}
		}
		else if(command.startsWith("/placebackblock"))
		{
			String[] split = command.split(" ");
			if(Boolean.parseBoolean(split[6]) && Integer.parseInt(split[3]) == activePlayerID)
			{
				player.inventory.removeItemsFromInventory(player, new ItemStack(Integer.parseInt(split[4]), 1)); //take the item from the player's inventory
				player.onInventoryChange();
			}
			
		}
		else if(command.startsWith("/worldtext"))
		{
			String[] split = command.split(" ");
			world.addTemporaryText(split[3], Integer.parseInt(split[1]), Integer.parseInt(split[2]), 20, EnumColor.get(split[4]));
		}
		else if(command.startsWith("/pickup"))
		{
			String[] split = command.split(" ");
			if(Integer.parseInt(split[1]) != activePlayerID)
			{
				return;
			}
			ItemStack stack = ((EntityItemStack)(world.getEntityByID(Integer.parseInt(split[2])))).getStack();
			player.inventory.pickUpItemStack(world, player, new ItemStack(stack).setStackSize(Integer.parseInt(split[3])));
			stack.removeFromStack(Integer.parseInt(split[3]));
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
	public void startGame(String universeName, World world, EntityPlayer player)
	{
//		if(this.world != null)
//		{
//			throw new RuntimeException("World already exists!");
//		}
		this.universeName = universeName;
		this.world = world;
		this.world.chunkManager = chunkManager;
		this.world.chunkManager.setUniverseName(universeName);
		this.world.initSoundEngine(soundEngine);
		this.setSentPlayer(player);
		world.addPlayerToWorld(player);
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
		soundEngine = new SoundEngine(settings);
		mainMenu = new MainMenu(settings);
		renderMenu = new RenderMenu(settings);
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
//			TerraeRasa.isMainMenuOpen = false;
			FileManager fileManager = new FileManager();
//			world = fileManager.generateAndSaveWorld("World", EnumWorldSize.MINI, EnumWorldDifficulty.NORMAL);//EnumWorldSize.LARGE.getWidth(), EnumWorldSize.LARGE.getHeight());
//			world.initSoundEngine(soundEngine);
//			world.chunkManager = chunkManager;
//			world.chunkManager.setUniverseName("World");
			setSentPlayer(fileManager.generateAndSavePlayer("Debug_Player", EnumPlayerDifficulty.HARD));//new EntityLivingPlayer("Test player", EnumDifficulty.NORMAL);
//			world.addPlayerToWorld(player);
//		//	world.assessForAverageSky();
//			LightUtils utils = new LightUtils();
//			utils.applyAmbient(world);
		//	getPlayer().setAffectedByWalls(false);
//			
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.goldSword));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.copperPickaxe));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.godminiumPickaxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldPickaxe));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.goldAxe));

			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.stone, 100));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.torch, 100));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.chest, 100));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.ironChest, 100));
			
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.steadfastShield));
			
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.woodenBow));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.absorbPotion2, 50));
			
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.goldIngot, 100));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.silverIngot, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.healthPotion1, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.healthPotion2, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaPotion1, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaPotion2, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaStar, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.furnace, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.craftingTable, 6));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.plank, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldPickaxe));		
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperIngot, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironIngot, 6));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.absorbPotion1, 20));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.criticalChancePotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.criticalChancePotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.attackSpeedPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.attackSpeedPotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.damageBuffPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.damageBuffPotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.damageSoakPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.damageSoakPotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.dodgePotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.dodgePotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaRegenerationPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaRegenerationPotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.steelSkinPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.steelSkinPotion2, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.swiftnessPotion1, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.swiftnessPotion2, 5));			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperOre, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.tinOre, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldOre, 15));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.tinIngot, 100));	
 
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldHelmet));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldHelmet));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldBody));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldPants));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldGloves));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldBoots));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldBelt));

			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.jasper, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.sapphire, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.emerald, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.ruby, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.diamond, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opal, 50));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.goldRing, 6));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.berserkersEssence));
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Block.glass, 40));

			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.snowball, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.vialEmpty, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.vialOfWater, 100));
			
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldSword));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.silverSword));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironSword));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeSword));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperSword));
//			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldPickaxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.silverPickaxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironPickaxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzePickaxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperPickaxe));
//			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.goldAxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.silverAxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironAxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeAxe));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperAxe));
//			
			
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opHelmet));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opBody));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opPants));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opGloves));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opBoots));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.opBelt));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumHelmet).setAffix(new AffixSturdy()));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumBody));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumPants));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumGloves));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumBoots));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.lunariumBelt));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.gemcraftingBench));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Block.alchemyStation));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.eaglesFeather));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Item.woodenArrow, 50));
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.gemSmartheal1, 40));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.gemDefense1, 40));
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironHelmet));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironBody));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironPants));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironGloves));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironBoots));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ironBelt));
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeHelmet));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeBody));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzePants));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeGloves));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeBoots));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.bronzeBelt));
			
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperHelmet));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperBody));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperPants));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperGloves));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperBoots));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.copperBelt));

//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.absorbPotion2, 200));

//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.snowball));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.woodenArrow, 100));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaCrystal, 2));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.angelsSigil, 2));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.ringOfVigor, 5));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.regenerationPotion2, 200));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.attackSpeedPotion1, 200));
//			player.inventory.pickUpItemStack(world, player, new ItemStack(Item.manaRegenerationPotion2, 200));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Spell.rejuvenate));
			sentPlayer.inventory.pickUpItemStack(world, sentPlayer, new ItemStack(Spell.bulwark));
			
			try {
				fileManager.savePlayer(sentPlayer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		String worldName = "";
		if(renderMode == RENDER_MODE_WORLD_EARTH)
		{
			world.saveRemainingWorld();
			world = null;
		}
		else if(renderMode == RENDER_MODE_WORLD_HELL)
		{
			worldHell.saveRemainingWorld();
			worldHell = null;
		}
		else if(renderMode == RENDER_MODE_WORLD_SKY)
		{
			worldName = worldSky.getWorldName();
			worldSky.saveRemainingWorld();
			worldSky = null;
		}
		
		this.renderMode = newMode;
		FileManager manager = new FileManager();
		
		if(renderMode == RENDER_MODE_WORLD_EARTH)
		{
			manager.loadWorld("Earth", worldName);
		}
		else if(renderMode == RENDER_MODE_WORLD_HELL)
		{
			manager.loadWorld("Hell", worldName);
		}
		else if(renderMode == RENDER_MODE_WORLD_SKY)
		{
			manager.loadWorld("Sky", worldName);
		}
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

	public EntityPlayer getSentPlayer() {
		return sentPlayer;
	}

	public void setSentPlayer(EntityPlayer player) {
		this.sentPlayer = player;
	}
}