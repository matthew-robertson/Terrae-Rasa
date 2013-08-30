package world;

import io.Chunk;
import io.ChunkManager;
import items.Item;
import items.ItemTool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import savable.SavableWorld;
import savable.SaveManager;
import server.GameEngine;
import server.Log;
import server.PlayerInput;
import server.ServerSettings;
import server.TerraeRasa;
import statuseffects.StatusEffectStun;
import transmission.BlockUpdate;
import transmission.EntityUpdate;
import transmission.PositionUpdate;
import transmission.ServerUpdate;
import transmission.SuperCompressedBlock;
import transmission.WorldData;
import utils.ActionbarItem;
import utils.ChestLootGenerator;
import utils.Damage;
import utils.ItemStack;
import utils.MathHelper;
import utils.MetaDataHelper;
import utils.SpawnManager;
import utils.Vector2F;
import utils.WorldText;
import blocks.Block;
import blocks.BlockChest;
import blocks.BlockGrass;
import blocks.BlockPillar;
import blocks.MinimalBlock;
import entities.DisplayableEntity;
import entities.Entity;
import entities.EntityItemStack;
import entities.EntityNPC;
import entities.EntityNPCEnemy;
import entities.EntityPlayer;
import entities.EntityProjectile;
import enums.EnumColor;
import enums.EnumDamageSource;
import enums.EnumDamageType;
import enums.EnumEventType;
import enums.EnumWorldDifficulty;

/**
 * <code>World</code> implements many of the key features for TerraeRasa to actually run properly and update. 
 * <br><br>
 * All players, chunks, Biomes, EntityEnemies, EntityLivingItemStacks, TemporaryText, Weather are stored in the 
 * World class. 
 * <br><br>
 * Methods exist to perform hittests between anything requiring it (monster and player, etc); as well as
 * methods to update entities, and everything else in the world. These methods are private and called 
 * though {@link #onWorldTick()}, as such no external source can modify the update rate, or otherwise
 * interfere with it. 
 * <br><br>
 * Other methods of interest, aside from onWorldTick(), relate to block breaking or Block requests.
 * {@link #getBlock(int, int)} and {@link #setBlock(Block, int, int)} implement most of the features 
 * required to "grandfather in" the old world.worldMap[][] style which has since been rendered obsolete 
 * by chunks. These methods are generally considered safe to use, with the same co-ordinates as the
 * previous world.worldMap[][] style. They should perform the division and modular division required 
 * automatically. <b>NOTE: These methods are relatively slow, and unsuitable for large-scale modifications.</b>
 * These large scale operations should be done directly through chunk data, with exact values not requiring
 * division/modular division every single request. 
 * <br><br>
 * The main methods relating to Block breaking and placement are {@link #breakBlock(int, int)} for breaking,
 * and {@link #placeBlock(int, int, Block)} for placing a Block. These methods differ from getBlock(int, int)
 * and setBlock(Block, int, int) significantly. These methods are for block placement and destruction while
 * the game is running (generally the player would do this). As a result, they must obey the rules of 
 * the game. These methods are relatively simple in what they do, instantly breaking and dropping or placing
 * a block upon call, but with regard to the rules of the game. They do not actually decrease inventory 
 * totals, add to them, or interact with the player in any way. As a result, anything in the project
 * can actually request a block placement/destroy using these methods, however it's advised that only the 
 * player actually do this.
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class World
{
	public static final int GAMETICKSPERDAY = 28800; 
	public static final int GAMETICKSPERHOUR = GameEngine.TICKS_PER_SECOND * 60;
	public final double g = 1.8;
	public Hashtable<String, Boolean> chunksLoaded;
	
	private List<EntityItemStack> itemsList;
	private List<EntityNPCEnemy> entityList;
	public List<WorldText> temporaryText; 
	private List<EntityNPC> npcList;
	private List<EntityProjectile> projectileList;
	public SpawnManager manager;
	public ChestLootGenerator lootGenerator;
	
	private int[] generatedHeightMap;
	private int averageSkyHeight;
	private int chunkWidth;
	private int chunkHeight;
	public ChunkManager chunkManager;
	private EnumWorldDifficulty difficulty;
	private final Random random = new Random();
	protected String worldName;
	private long worldTime;
	private ConcurrentHashMap<String, Chunk> chunks;
	private int width; //Width in blocks, not pixels
	private int height; //Height in blocks, not pixels
	private double previousLightLevel;
	private boolean lightingUpdateRequired;
	private Vector<PlayerInput> playerInputs;
	
	public Dictionary<String, Entity> entitiesByID = new Hashtable<String, Entity>();
	
	/**
	 * Reconstructs a world from a save file. This is the first step.
	 */
	public World()
	{
		setChunks(new ConcurrentHashMap<String, Chunk>(10));
		entityList = new ArrayList<EntityNPCEnemy>(255);
		projectileList = new ArrayList<EntityProjectile>(255);
		npcList = new ArrayList<EntityNPC>(255);
		temporaryText = new ArrayList<WorldText>(100);
		itemsList = new ArrayList<EntityItemStack>(250);
		chunksLoaded= new Hashtable<String, Boolean>(25);
		manager = new SpawnManager();
		lootGenerator = new ChestLootGenerator();
		lightingUpdateRequired = true;
		playerInputs = new Vector<PlayerInput>();
	}
	
	/**
	 * Constructs a new instance of World. This involves creation of the EntityLists, and some miscellaneous
	 * fields being initialized, but largely initialization doesnt happen here. Instead things like WorldGen_
	 * initialize most of the important things, such as the 'world map', and ground-level map/averages, 
	 * @param universeName the world's name, assigned on creation
	 * @param width the width of the world, in blocks
	 * @param height the height of the world, in blocks
	 * @param difficulty the difficulty (EnumDifficulty) of the world
	 */
	public World(String universeName, int width, int height, EnumWorldDifficulty difficulty)
	{
		setChunks(new ConcurrentHashMap<String, Chunk>(10));
		this.width = width;
		this.height = height; 
		entityList = new ArrayList<EntityNPCEnemy>(255);
		projectileList = new ArrayList<EntityProjectile>(255);
		npcList = new ArrayList<EntityNPC>(255);
		temporaryText = new ArrayList<WorldText>(100);
		itemsList = new ArrayList<EntityItemStack>(250);
		chunksLoaded= new Hashtable<String, Boolean>(25);
		worldTime = (long) (6.5 * GAMETICKSPERHOUR);
		worldName = "Earth";
		previousLightLevel = getLightLevel();
		this.difficulty = difficulty;
		manager = new SpawnManager();
		lootGenerator = new ChestLootGenerator();
		chunkWidth = width / Chunk.getChunkWidth();
		chunkHeight = height / height;
		lightingUpdateRequired = true;
		playerInputs = new Vector<PlayerInput>();
	}
		
	public WorldData getWorldData()
	{
		WorldData data = new WorldData();
		data.setLists(this.itemsList, this.entityList, this.npcList, this.projectileList);
		data.generatedHeightMap = this.generatedHeightMap;
		data.averageSkyHeight = this.averageSkyHeight;
		data.chunkWidth = this.chunkWidth;
		data.chunkHeight = this.chunkHeight;
		data.difficulty = this.difficulty;
		data.worldName = this.worldName;
		data.worldTime = this.worldTime;
		data.width = this.width; 
		data.height = this.height;  
		data.previousLightLevel = this.previousLightLevel;
		data.lightingUpdateRequired = this.lightingUpdateRequired;
		return data;
	}
	
	public Entity getEntityByID(int id)
	{
		try {
			return entitiesByID.get(""+id);
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public void overwriteEntityByID(Vector<EntityPlayer> players, int id, Entity newEntity)
	{
		Entity entity = entitiesByID.get(""+id);
		if(entity instanceof EntityItemStack)
		{
			itemsList.remove(entity);
			itemsList.add((EntityItemStack) newEntity);
		}
		else if(entity instanceof EntityProjectile)
		{
			projectileList.remove(entity);
			projectileList.add((EntityProjectile) newEntity);
		}
		else if(entity instanceof EntityNPCEnemy)
		{
			entityList.remove(entity);
			entityList.add((EntityNPCEnemy) newEntity);
		}
		else if(entity instanceof EntityNPC) //Friendly?
		{
			npcList.remove(entity);
			npcList.add((EntityNPC) newEntity);
		}
		else if(entity instanceof EntityPlayer)
		{
			players.remove(entity);
			players.add((EntityPlayer) newEntity);
		}
		entitiesByID.put(""+id, newEntity);		
	}
	
	public void removeEntityByID(Vector<EntityPlayer> players, int id)
	{
		Entity entity = entitiesByID.get(""+id);
		if(entity instanceof EntityItemStack)
		{
			itemsList.remove(entity);
		}
		else if(entity instanceof EntityProjectile)
		{
			projectileList.remove(entity);
		}
		else if(entity instanceof EntityNPCEnemy)
		{
			entityList.remove(entity);
		}
		else if(entity instanceof EntityNPC) //Friendly?
		{
			npcList.remove(entity);
		}
		else if(entity instanceof EntityPlayer)
		{
			players.remove(entity);
		}
		entitiesByID.remove(""+id);
	}
	
	/**
	 * Puts the player at the highest YPosition for the spawn XPosition 
	 * @param player the player to be added
	 * @return the player with updated position (x, y)
	 */
	public EntityPlayer spawnPlayer(ServerSettings settings, EntityPlayer player) 
	{		
		player.respawnXPos = getWorldCenterOrtho();
		
		//requestRequiredChunks((int)(player.respawnXPos / 6), (int)(player.y / 6));
		//chunkManager.addAllLoadedChunks_Wait(this, chunks);
		
		requestRequiredChunks(settings, getWorldCenterBlock(), averageSkyHeight);
		chunkManager.addAllLoadedChunks_Wait(this, getChunks());
		
		try {
			for(int i = 0; i < height - 1; i++)
			{
				if(getBlock((int)(player.respawnXPos / 6), i).id == 0 && getBlock((int)(player.respawnXPos / 6) + 1, i).id == 0) 
				{
					continue;
				}
				if(getBlock((int)player.respawnXPos / 6, i).id != 0 || getBlock((int) (player.respawnXPos / 6) + 1, i).id != 0)
				{	
					player.x = player.respawnXPos;
					player.y = (i * 6) - 18;			
					player.grantImmunityTicks(6 * GameEngine.TICKS_PER_SECOND);
					return player;
				}			
			}
		} catch (Exception e) {
			//This is probably a nullpointer 
			e.printStackTrace();
			throw new RuntimeException("This is likely caused by a chunk that's required being denied due to I/O conflicts" + '\n'
			+ "If this exception is thrown, inspect the addition to chunkmanager - chunkLock from A1.0.23");
		}
		return player;
	}
		
	/**
	 * Opens the worlddata.dat file and applies all the data to the reconstructed world. Then final reconstruction is performed.
	 * @param BASE_PATH the base path for the TerraeRasa folder, stored on disk
	 * @param universeName the name of the world to be loaded
	 * @throws FileNotFoundException indicates the worlddata.dat file cannot be found
	 * @throws IOException indicates the load operation has failed to perform the required I/O. This error is critical
	 * @throws ClassNotFoundException indicates casting of an object has failed, due to incorrect class version or the class not existing
	 */
	public void loadAndApplyWorldData(final String BASE_PATH, String universeName, String dir)
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		//Open an input stream for the file	
		SaveManager manager = new SaveManager();	
		SavableWorld savable = (SavableWorld)(manager.loadFile("/" + universeName + "/" + dir + "/worlddata.xml"));
		this.width = savable.width;
		this.height = savable.height;
		this.chunkWidth = savable.chunkWidth;
		this.chunkHeight = savable.chunkHeight;
		this.averageSkyHeight = savable.averageSkyHeight;
		this.generatedHeightMap = savable.generatedHeightMap;
		this.worldTime = savable.worldTime;
		this.worldName = savable.worldName;
		this.difficulty = savable.difficulty;	
	}
	
	public void loadChunks(ServerSettings settings)
	{
		final int loadDistanceHorizontally = (settings.loadDistance * Chunk.getChunkWidth() >= 200) ? settings.loadDistance * Chunk.getChunkWidth() : 200;
		
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftOff = (getWorldCenterBlock() - loadDistanceHorizontally) / Chunk.getChunkWidth();
		int rightOff = (getWorldCenterBlock() + loadDistanceHorizontally) / Chunk.getChunkWidth();
		
		for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
		{
			chunkManager.requestChunk(worldName, this, getChunks(), i);
		}
		
		requestRequiredChunks(settings, getWorldCenterBlock(), averageSkyHeight);
		chunkManager.addAllLoadedChunks_Wait(this, getChunks());
	}
	
	/**
	 * Adds a player to the world. Currently multiplayer placeholder.
	 * @param player the player to add
	 */
	public String addPlayerToWorld(ServerSettings settings, EntityPlayer player)
	{
		requestRequiredChunks(settings, getWorldCenterBlock(), averageSkyHeight);
		//chunkManager.addAllLoadedChunks_Wait(this, getChunks());
		spawnPlayer(settings, player);
		Log.broadcast(player.getName() + " joined the game with entityID " + player.entityID);
		return "";
	}
	
	/**
	 * Loads all nearby chunks for a location, given the Display's size
	 * @param x the x value of the point to load near (in blocks)
	 * @param y the y value of the point to load near (in blocks)
	 */
	private void requestRequiredChunks(ServerSettings settings, int x, int y)
	{
		final int loadDistanceHorizontally = (settings.loadDistance * Chunk.getChunkWidth() >= 200) ? settings.loadDistance * Chunk.getChunkWidth() : 200;
		
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftOff = (x - loadDistanceHorizontally) / Chunk.getChunkWidth();
		int rightOff = (x + loadDistanceHorizontally) / Chunk.getChunkWidth();
		
		for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
		{
			chunkManager.requestChunk(worldName, this, getChunks(), i);
		}
	}
		
	/**
	 * Keeps track of the worldtime and updates the light if needed
	 */
	private void updateWorldTime()
	{
		//worldTime / GAMETICKSPERHOUR = the hour (from 00:00 to 24:00)
		worldTime++;
		if(worldTime >= GAMETICKSPERDAY)//If the time exceeds 24:00, reset it to 0:00
		{
			worldTime = 0;
		}
		
		if(getLightLevel() != previousLightLevel) //if the sunlight has changed, update it
		{
			previousLightLevel = getLightLevel();
			lightingUpdateRequired = true;
		}		
	}
	
	/**
	 * Gets the light value of the sun. Full light is a value of 1.0f, minimal (night) light is .2f
	 */
	public double getLightLevel()
	{
		//LightLevel of 1.0f is full darkness (it's reverse due to blending mechanics)
		//Light Levels By Time: 20:00-4:00->20%; 4:00-8:00->20% to 100%; 8:00-16:00->100%; 16:00-20:00->20% to 100%;  
		
		double time = (double)(worldTime) / GAMETICKSPERHOUR; 
		
		return (time > 8 && time < 16) ? 1.0f : (time >= 4 && time <= 8) ? MathHelper.roundDowndouble20th(((((time - 4) / 4.0F) * 0.8F) + 0.2F)) : (time >= 16 && time <= 20) ? MathHelper.roundDowndouble20th(1.0F - ((((time - 16) / 4.0F) * 0.8F) + 0.2F)) : 0.2f;
	}
		
	/**
	 * Gets the center of the world, with a block value. This is xsize / 2 
	 * @return xsize / 2 (giving the center block of the world)
	 */
	public final int getWorldCenterBlock()
	{
		return width / 2;
	}
	
	/**
	 * Gets the 'ortho' unit value for the world's center. 1 block = 6 ortho units, so this is xsize * 3
	 * @return xsize * 3 (the world's center in ortho)
	 */
	public final int getWorldCenterOrtho()
	{
		return width * 3;
	}
	
	public void addUnspecifiedEntity(Entity entity)
	{
		if(getEntityByID(entity.entityID) != null)
		{
			return;
		}
		if(entity instanceof EntityNPCEnemy)
		{
			entityList.add((EntityNPCEnemy) entity);
			entitiesByID.put(""+entity.entityID, entity);
		}
		else if(entity instanceof EntityNPC)
		{
			npcList.add((EntityNPC) entity);
			entitiesByID.put(""+entity.entityID, entity);
		}
		else if(entity instanceof EntityItemStack)
		{
			itemsList.add((EntityItemStack) entity);
			entitiesByID.put(""+entity.entityID, entity);
		}
		else if(entity instanceof EntityProjectile)
		{
			projectileList.add((EntityProjectile) entity);
			entitiesByID.put(""+entity.entityID, entity);
		}		
	}
	
	/**
	 * Adds an EntityNPCEnemy to the entityList in this instance of World
	 * @param enemy the enemy to add to entityList
	 */
	public void addEntityToEnemyList(EntityNPCEnemy enemy)
	{
		entityList.add(enemy);
		entitiesByID.put(""+enemy.entityID, enemy);
	}
	
	/**
	 * Adds an EntityNPC to the npcList in this instance of World
	 * @param npc the npc to add to entityList
	 */
	public void addEntityToNPCList(EntityNPC npc)
	{
		npcList.add(npc);
		entitiesByID.put(""+npc.entityID, npc);
	}
	
	/**
	 * Adds an entityProjectile to the projectileList in this instance of World
	 * @param projectile the projectile to add to projectileList
	 */
	public void addEntityToProjectileList(EntityProjectile projectile)
	{
		projectileList.add(projectile);
		entitiesByID.put(""+projectile.entityID, projectile);
	}

	/**
	 * Adds an EntityLivingItemStack to the itemsList in this instance of World
	 * @param stack the EntityLivingItemStack to add to itemsList
	 */
	public void addItemStackToItemList(EntityItemStack stack)
	{
		itemsList.add(stack);
		entitiesByID.put(""+stack.entityID, stack);
	}
	
	public void addPlayer(EntityPlayer player)
	{		
		entitiesByID.put(""+player.entityID, player);
	}
	
	/**
	 * Adds a piece of temporary text to the temporaryText ArrayList. This text is rendered until its time left runs out.
	 * This text is generally from healing, damage, (combat)
	 * @param message the text to display
	 * @param x the x position in ortho units
	 * @param y the y position in ortho units
	 * @param ticksLeft the time (in game ticks) before the text despawns
	 * @param type the type of combat text (affects the colour). For example 'g' makes the text render green
	 */
	public void addTemporaryText(String message, int x, int y, int ticksLeft, EnumColor color)
	{
		temporaryText.add(new WorldText(message, x, y, ticksLeft, color, true));
	}
	
	private synchronized void handlePlayerMovement(ServerUpdate update)
	{
		for(PlayerInput input : playerInputs)
		{
			input.handle(this);
			update.addPositionUpdate(new PositionUpdate(input.getAssociatedID(), input.newX(), input.newY()));
		}
		playerInputs.clear();
	}
	
	public synchronized void registerPlayerMovement(PlayerInput input)
	{
		playerInputs.add(input);
	}
	
	/**
	 * Calls all the methods to update the world and its inhabitants
	 */
	public void onWorldTick(ServerUpdate update, Vector<EntityPlayer> players)
	{		
		spawnMonsters(update, players);				
		updateWeather(update);		
		//TODO: weather		
		
		updateChunks(players);
		
		//Not player based stuff -- do this once per game tick
		updateMonsterStatusEffects();
		updateEntityLivingItemStacks(update);
		updateWorldTime();

		//Update Entities
		if(worldTime % 20 == 0) {
			checkForMonsterRemoval(update, players);
		}
		updateMonsters(update); 
		//TODO: NPCs
		updateNPCs(update);
		updateProjectiles(update);
		
		handlePlayerMovement(update);
		
		
		//update the player
		for(EntityPlayer player : players)
		{
			double h = player.getHealth();
			double m = player.mana;
			double s = player.specialEnergy;
			player.onWorldTick(update, this); 			
			//Hittests
			performPlayerMonsterHittests(update, player); 
			performProjectileHittests(update, player);
			performPlayerItemHittests(update, player);
			performEnemyToolHittests(update, player);
			if(player.getHealth() != h || player.mana != m || player.specialEnergy != s)
			{
				String command = "/player " + player.entityID + " sethms " + player.getHealth() + " " + player.mana + " " + player.specialEnergy;
				update.addValue(command);
			}
		}
		
		for(WorldText text : temporaryText)
		{
			String command = "/worldtext " + text.x + " " + text.y + " " + text.message + " " + text.color.toString();
			update.addValue(command);
		}	
		temporaryText.clear();
		
		if(chunkManager.isAnyLoadOperationDone())
		{
			chunkManager.addAllLoadedChunks(this, getChunks());
		}
	}
	
	public void forceloadChunk(int x)
	{
		chunkManager.requestChunk(worldName, this, getChunks(), x);
	}
	
	/**
	 * Updates (and possibly removes) monster status effects previously registered
	 */
	private void updateMonsterStatusEffects()
	{
		for(int i = 0; i < entityList.size(); i++)
		{
			entityList.get(i).checkAndUpdateStatusEffects(this);
		}
	}	
		
	/**
	 * Gets how many horizontal chunks the world has. This is equal to (width / Chunk.getChunkWidth())
	 * @return the number of horizontal chunks the world has
	 */
	public int getChunkWidth()
	{
		return chunkWidth;
	}
	
	/**
	 * Gets how many vertical chunks the world has. This is equal to (height / height)
	 * @return the number of vertical chunks the world has
	 */
	public int getChunkHeight()
	{
		return chunkHeight;
	}
	
	/**
	 * Applies gravity to all itemstacks entities
	 */
	private void updateEntityLivingItemStacks(ServerUpdate update)
	{		
		for(int i = 0; i < itemsList.size(); i++)
		{
			double x = itemsList.get(i).x;
			double y = itemsList.get(i).y;
			itemsList.get(i).move(this);
			PositionUpdate positionUpdate = new PositionUpdate(itemsList.get(i).entityID, itemsList.get(i).x, itemsList.get(i).y);
			if(itemsList.get(i).x != x || itemsList.get(i).y != y)
			{
				update.addPositionUpdate(positionUpdate);			
			}
		}		
	}
	
	/**
	 * Picks up itemstacks that the player is standing on (or very near to)
	 */
	private void performPlayerItemHittests(ServerUpdate update, EntityPlayer player)
	{
		final double PLAYER_X_CENTER = player.x + (player.width / 2);
		final double PLAYER_Y_CENTER = player.y + (player.height / 2);
		
		Iterator<EntityItemStack> it = itemsList.iterator();
		while(it.hasNext())
		{
			EntityItemStack stack = it.next();
			double distance = MathHelper.distanceBetweenTwoPoints(stack.x + (stack.width / 2), 
					stack.y + (stack.height / 2),
					PLAYER_X_CENTER, 
					PLAYER_Y_CENTER);
			//Check if the itemstack is near the player and able to be picked up
			if(distance <= stack.width * 2 * player.pickupRangeModifier && stack.canBePickedUp()) 
			{
//				int originalSize = stack.getStack().getStackSize();
				ItemStack remainingStack = player.inventory.pickUpItemStack(this, player, stack.getStack()); //if so try to pick it up
				
				if(remainingStack == null) //nothing's left, remove the null element
				{
					EntityUpdate entityUpdate = new EntityUpdate();
					entityUpdate.type = 3;
					entityUpdate.action = 'r';
					entityUpdate.entityID = stack.entityID;
					entityUpdate.updatedEntity = null;
					update.addEntityUpdate(entityUpdate);
					
//					String command = "/pickup " + player.entityID + " " + stack.entityID + " " + originalSize;

//					player.inventory.pickUpItemStack(this, player, stack.getStack());
					
//					update.addValue(command);
					it.remove();
				}
				else //otherwise, put back what's left
				{
//					if(originalSize != stack.getStack().getStackSize())
//					{
//						String command = "/pickup " + player.entityID + " " + stack.entityID + " " + (originalSize - stack.getStack().getStackSize());
//						update.addValue(command);
//					}
					stack.setStack(remainingStack);				
				}
			}
			else
			{
				stack.update(); 
				if(stack.isDead())
				{
					EntityUpdate entityUpdate = new EntityUpdate();
					entityUpdate.type = 3;
					entityUpdate.action = 'r';
					entityUpdate.entityID = stack.entityID;
					entityUpdate.updatedEntity = null;
					update.addEntityUpdate(entityUpdate);
					it.remove();
				}
			}	
		}
	}
	
	/**
	 * applies AI to npcs
	 */
	private void updateNPCs(ServerUpdate update){
		for (int i = 0; i < npcList.size(); i++){
			if (npcList.get(i).isDead()){
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'r';
				entityUpdate.entityID = npcList.get(i).entityID;
				entityUpdate.type = 2; 
				entityUpdate.updatedEntity = null;
				update.addEntityUpdate(entityUpdate);
				npcList.remove(i);
				continue;
			}
								
			double x = npcList.get(i).x;
			double y = npcList.get(i).y;
			npcList.get(i).applyAI(this);
			
			//TODO re-enable onPlayerNear() for NPCs and possibly fixates for NPCS
//			if(npcList.get(i).inBounds(player.x, player.y, player.width, player.height)){
//				npcList.get(i).onPlayerNear();
//			}
			
			npcList.get(i).applyGravity(this);
			if(npcList.get(i).x != x || npcList.get(i).y != y)
			{
				update.addPositionUpdate(new PositionUpdate(npcList.get(i).entityID, npcList.get(i).x, npcList.get(i).y));
			}
		}
	}
	
	private void checkForMonsterRemoval(ServerUpdate update, Vector<EntityPlayer> players)
	{
		final int OUT_OF_RANGE = 600;
//		if(players.size() == 0)
//		{
//			entityList.clear();
//		}
		for(EntityPlayer player : players)
		{
			Iterator<EntityNPCEnemy> it = entityList.iterator();			
			while(it.hasNext())
			{
				EntityNPCEnemy monster = it.next();
				if(!monster.isBoss && (MathHelper.distanceBetweenTwoPoints(player.x, player.y, monster.x, monster.y) > OUT_OF_RANGE))
				{ 
					EntityUpdate entityUpdate = new EntityUpdate();
					entityUpdate.action = 'r';
					entityUpdate.entityID = monster.entityID;
					entityUpdate.type = 1; 
					entityUpdate.updatedEntity = null;
					update.addEntityUpdate(entityUpdate);
					it.remove();
					continue;
				}
			}
		}
	}
	
	private void updateMonsters(ServerUpdate update)
	{
		for(int i = 0; i < entityList.size(); i++)
		{
			if(entityList.get(i).isDead()) //if the monster is dead, try to drop items
			{
				ItemStack[] drops = entityList.get(i).getDrops(); //get possible drops
				if(drops != null) //if there're drops
				{
					for(ItemStack stack : drops) //drop each of them
					{
						EntityItemStack entityStack = new EntityItemStack(entityList.get(i).x - 1, entityList.get(i).y - 1, stack);
						addItemStackToItemList(entityStack);
						EntityUpdate entityUpdate = new EntityUpdate();
						entityUpdate.action = 'a';
						entityUpdate.entityID = entityStack.entityID;
						entityUpdate.type = 3; 
						entityUpdate.updatedEntity = new DisplayableEntity(entityStack);
						update.addEntityUpdate(entityUpdate);
					}
				}
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'r';
				entityUpdate.entityID = entityList.get(i).entityID;
				entityUpdate.type = 1; 
				entityUpdate.updatedEntity = null;
				update.addEntityUpdate(entityUpdate);
				entityList.remove(i);
				continue;
			}
			
			double x = entityList.get(i).x;
			double y = entityList.get(i).y;
			entityList.get(i).invincibilityTicks--;
			entityList.get(i).applyAI(this); //otherwise apply AI
			if(entityList.get(i).x != x || entityList.get(i).y != y)
			{
				update.addPositionUpdate(new PositionUpdate(entityList.get(i).entityID, entityList.get(i).x, entityList.get(i).y));
			}
		}
	}
	
	/**
	 * Method designed to handle the updating of all blocks
	 * @param x - x location in the world
	 * @param y - location in the world
	 * @return - updated bitmap
	 */
	
	public int updateBlockBitMap(int x, int y){
		int bit = getBlockGenerate(x,y).getBitMap();
		//If the block is standard
		if (getBlockGenerate(x, y).getTileMap() == 'g'){
			return updateGeneralBitMap(x, y);
		}
		//if the block requires special actions
		//If the block is a pillar
		else if (getBlockGenerate(x, y).getTileMap() == 'p'){
			return updatePillarBitMap(x, y);	
		}
		return bit;
	}
	
	/**
	 * Method to determine the bitmap
	 * @param x - location of the block on the x axis
	 * @param y - location of the block on the y axis
	 * @return bit - the int to be used for calculating which texture to use
	 */
	private int updateGeneralBitMap(int x, int y){
		int bit = 0;
		if (getBlockGenerate(x, y - 1).isSolid){
			bit += 1;
		}
		if (getBlockGenerate(x, y + 1).isSolid){
			bit += 4;
		}
		if (getBlockGenerate(x - 1, y).isSolid){
			bit += 8;
		}
		if (getBlockGenerate(x + 1, y).isSolid){
			bit += 2;
		}
		if (getBlockGenerate(x, y) instanceof BlockGrass && (bit == 15 || bit == 11 || bit == 27 || bit == 31)){
			setBlockGenerate(Block.dirt, x,y);
			setBitMap(x, y, bit);
		}
		return bit;
	}
	
	/**
	 * Subroutine for updateBlockBitMap, specific for pillars.
	 * @param x - location of the block on the x axis
	 * @param y - location of the block on the y axis
	 * @return bit - the int to be used to calculate which texture to use
	 */
	
	private int updatePillarBitMap(int x, int y){
		int bit;
		if (getBlockGenerate(x, y + 1) instanceof BlockPillar){
			bit = 1;
		}
		
		else {
			bit = 2;
		}
		
		if (!getBlockGenerate(x, y - 1).isOveridable && !(getBlockGenerate(x, y - 1) instanceof BlockPillar)){
			bit = 0;					
		}
		
		if (!getBlockGenerate(x, y - 1).isOveridable && !getBlockGenerate(x, y + 1).isOveridable && !(getBlockGenerate(x, y + 1) instanceof BlockPillar) && !(getBlockGenerate(x, y - 1) instanceof BlockPillar)){
			bit = 3;
		}
		return bit;
	}	
	
	/**
	 * Updates (and possibly removes) projectiles
	 * @param player - player to compare distances against
	 */
	private void updateProjectiles(ServerUpdate update)
	{
		//final int OUT_OF_RANGE = 1200;//(int) ((Display.getHeight() > Display.getWidth()) ? Display.getHeight() * 0.75 : Display.getWidth() * 0.75);
		for(int i = 0; i < projectileList.size(); i++)
		{
			boolean positionUpdateValid = true;
			if (projectileList.get(i).active){
				double x = projectileList.get(i).x;
				double y = projectileList.get(i).y;
				projectileList.get(i).moveProjectile(this);
				if(projectileList.get(i).x != x || projectileList.get(i).y != y)
				{
					positionUpdateValid = false;
				}
			}
			else if (!projectileList.get(i).active){
				projectileList.get(i).ticksNonActive++;
			}
			
			//If the projectile is too far away, remove it
			if(projectileList.get(i).ticksNonActive > 80)
			{ 
				if (projectileList.get(i).ticksNonActive > 1 && projectileList.get(i).getDrop() != null){
					EntityItemStack entityStack = new EntityItemStack(projectileList.get(i).x - 1, projectileList.get(i).y - 1, projectileList.get(i).getDrop());
					addItemStackToItemList(entityStack);
					EntityUpdate entityUpdate = new EntityUpdate();
					entityUpdate.action = 'a';
					entityUpdate.entityID = entityStack.entityID;
					entityUpdate.type = 3; 
					entityUpdate.updatedEntity = new DisplayableEntity(entityStack);
				}
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'r';
				entityUpdate.entityID = projectileList.get(i).entityID;
				entityUpdate.type = 4; 
				entityUpdate.updatedEntity = null;
				update.addEntityUpdate(entityUpdate);
				projectileList.remove(i);
				continue;
			}
			else if (projectileList.get(i).ticksNonActive > 1) 
			{
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'r';
				entityUpdate.entityID = projectileList.get(i).entityID;
				entityUpdate.type = 4; 
				entityUpdate.updatedEntity = null;
				update.addEntityUpdate(entityUpdate);
				projectileList.remove(i);
				continue;
			}
			if(positionUpdateValid)
			{
				update.addPositionUpdate(new PositionUpdate(projectileList.get(i).entityID, projectileList.get(i).x, projectileList.get(i).y));
			}
		}
	}
	
	/**
	 * Sees if any monsters have hit (are in range of) the player
	 * @param update 
	 */
	private void performPlayerMonsterHittests(ServerUpdate update, EntityPlayer player)
	{
		for(int i = 0; i < entityList.size(); i++)
		{
			if(player.inBounds(entityList.get(i).x, entityList.get(i).y, entityList.get(i).width, entityList.get(i).height))
			{ //If the player is in bounds of the monster, damage them
				player.damage(this, 
						new Damage(entityList.get(i).damageDone * difficulty.getDamageModifier(),
								new EnumDamageType[] { EnumDamageType.NONE }, 
								EnumDamageSource.MELEE)
								.setIsCrit(((Math.random() < entityList.get(i).criticalStrikeChance) ? true : false)), 
						true);
			}
		}
	}
	
	/**
	 * Sees if any projectiles have hit (are in range of) players or npcs
	 * @param update 
	 */
	private void performProjectileHittests(ServerUpdate update, EntityPlayer player)
	{
		for (int i = 0; i < projectileList.size(); i++){
			if (projectileList.get(i).isFriendly){
				for(int j = 0; j < entityList.size(); j++)
				{
					if(entityList.get(j).inBounds(projectileList.get(i).x, projectileList.get(i).y, projectileList.get(i).width, projectileList.get(i).height))
					{ //If the projectile is in bounds of the monster, damage them
						entityList.get(j).damage(this, 
								new Damage(projectileList.get(i).damage, 
										new EnumDamageType[] { EnumDamageType.NONE }, 
										EnumDamageSource.RANGE)
										.setIsCrit(((Math.random() < projectileList.get(i).criticalStrikeChance) ? true : false)), 
								true);
					}
				}
			}
			if (projectileList.get(i).isHostile){
				if(player.inBounds(projectileList.get(i).x, projectileList.get(i).y, projectileList.get(i).width, projectileList.get(i).height))
				{ //If the projectile is in bounds of the player, damage them
					player.damage(this, 
							new Damage(projectileList.get(i).damage * difficulty.getDamageModifier(), 
									new EnumDamageType[] { EnumDamageType.NONE }, 
									EnumDamageSource.RANGE)
									.setIsCrit(((Math.random() < projectileList.get(i).criticalStrikeChance) ? true : false)), 
							true);
				}
			}
		}
	}
	
	/**
	 * Attempts to spawn monsters, based on random numbers
	 */
	private void spawnMonsters(ServerUpdate update, Vector<EntityPlayer> players)
	{
		double hour = (double)(worldTime) / GAMETICKSPERHOUR; 
		boolean isNight = (hour < 4 || hour > 20) ? true : false;
		double spawnChance = 1.0 / 250;
		
		playerLoop:
		for(EntityPlayer player : players)
		{
			if(random.nextDouble() <= spawnChance) //Spawn at 0.5% chance per player
			{
				forcedSpawnLoop:
				while(true)
				{
					if(entityList.size() > 15) //too many monsters spawned
					{
						return;
					}			
					
					int xoff = 0;
					int yoff = 0;				
					//how far away the monster will spawn from the player:
					if(random.nextInt(2) == 0) 
					{ 
						//spawn to the left
						xoff = MathHelper.returnIntegerInWorldMapBounds_X(this, (int)(player.x / 6) - random.nextInt(100));
					}
					else
					{
						//spawn to the right
						xoff = MathHelper.returnIntegerInWorldMapBounds_X(this, (int)(player.x / 6) + random.nextInt(100));
					}
					if(random.nextInt(2) == 0) 
					{
						//spawn above 
						yoff = MathHelper.returnIntegerInWorldMapBounds_Y(this, (int)(player.y / 6) - random.nextInt(100));
					} 
					else 
					{
						//spawn below
						yoff = MathHelper.returnIntegerInWorldMapBounds_Y(this, (int)(player.y / 6) + random.nextInt(60));
					}
					
					String active = getBiomeColumn(""+(int)(xoff));
					if(active == null) //Should indicate the chunk isnt loaded (good failsafe)
					{
						continue;
					}
					active = active.toLowerCase();
	
					EntityNPCEnemy[] spawnList = null;
					if (active.equals("forest")){
						if (isNight){
							spawnList = manager.getForestNightEnemiesAsArray();
						}
						else {
							spawnList = manager.getForestDayEnemiesAsArray();
						}
					}
					else if (active.equals("desert")){
						if (isNight){
							spawnList = manager.getDesertNightEnemiesAsArray();
						}					
						else {
							spawnList = manager.getDesertDayEnemiesAsArray();
						}
					}
					else if (active.equals("arctic")){
						if (isNight){
							spawnList = manager.getArcticNightEnemiesAsArray();
						}					
						else {
							spawnList = manager.getArcticDayEnemiesAsArray();
						}
					}
					
					int entityChoice = random.nextInt(spawnList.length);
					for(int j = 0; j < spawnList[entityChoice].getBlockHeight(); j++)//Y
					{
						for(int k = 0; k < spawnList[entityChoice].getBlockWidth(); k++)//X
						{
							if(getBlock(xoff + k, yoff + j).isSolid)
							{
								//Spawn is illegal
								continue playerLoop; 
							}
						}
					}
					
					//The entity can spawn somewhere in the that area. Determine where the ground is for a ground entity
					try
					{
						int spawnX = xoff;
						int spawnY = yoff;
						
						//Ground Entity						
						for(int y = spawnY; y < height; y++)
						{
							for(int x = spawnX; x < spawnX + spawnList[entityChoice].blockWidth; x++)
							{
								if(getBlock(x, y + spawnList[entityChoice].blockHeight).isSolid)
								{	
									//legit spawn position
									EntityNPCEnemy enemy = new EntityNPCEnemy(spawnList[entityChoice]);
									enemy.setPosition(x * 6, y * 6);
									EntityUpdate entityUpdate = new EntityUpdate();
									entityUpdate.action = 'a';
									entityUpdate.entityID = enemy.entityID;
									entityUpdate.type = 1; 
									entityUpdate.updatedEntity = new DisplayableEntity(enemy);
									update.addEntityUpdate(entityUpdate);
									addEntityToEnemyList(enemy);
									break forcedSpawnLoop;
								}			
							}
						}
					} catch(Exception e) {
					}
				}				
			}		
		}
	}
	
	/**
	 * Tries to make weather happen on each game tick
	 */
	private void updateWeather(ServerUpdate update)
	{
		Enumeration<String> keys = chunks.keys();
        while (keys.hasMoreElements()) 
        {
            Chunk chunk = (Chunk)chunks.get((String)(keys.nextElement()));

            if(chunk.weather != null)
            {
            	chunk.weather.update(this, update);
            	if(chunk.weather.isFinished())
            	{
            		chunk.weather = null;
            		String command = "/stopweather " + chunk.getX();
            		update.addValue(command);
            	}      
            }
            else
            {
    			Biome biome = chunk.getBiome();			
    			if(biome != null && biome.biomeID == Biome.arctic.biomeID) //if the biome is arctic
    			{
    				if(random.nextInt(150000) == 0) //and a random chance is met
    				{
    					//cause weather!
						chunk.weather = new WeatherSnow(this, chunk, averageSkyHeight); 
	            		String command = "/causeweather " + chunk.getX() + " " + chunk.weather.getID();
	            		update.addValue(command);
    				}
    			}            	
            }
        }
	}
	
	/**
	 * 
    nvert: Number of vertices in the polygon. Whether to repeat the first vertex at the end.
    vertx, verty: Arrays containing the x- and y-coordinates of the polygon's vertices.
    testx, testy: X- and y-coordinate of the test point.

	 * @param nvert
	 * @param vertx
	 * @param verty
	 * @param testx
	 * @param testy
	 * @return
	 */
	boolean pnpoly(int nvert, double vertx[], double verty[], double testx, double testy)
	{
	  int i, j;
	  boolean c = false;
	  for (i = 0, j = nvert-1; i < nvert; j = i++) {
	    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
	     (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
	       c = !c;
	  }
	  return c;
	}
	
	/**
	 * Performs a hittest between the player's tool and monsters. 
	 * @param player
	 */
	private void performEnemyToolHittests(ServerUpdate update, EntityPlayer player) 
	{
		//Conditions which indicate the player is not swinging or able to swing
		if(player.inventory.getMainInventoryStack(player.selectedSlot) == null ||
				player.inventory.getMainInventoryStack(player.selectedSlot).getItemID() >= ActionbarItem.spellIndex ||
				!player.isSwingingTool() || 
				(player.inventory.getMainInventoryStack(player.selectedSlot) == null) || 
				!(Item.itemsList[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()] instanceof ItemTool))
		{
			return;
		}
		
		ItemTool heldItem = ((ItemTool)(Item.itemsList[player.inventory.getMainInventoryStack(player.selectedSlot).getItemID()]));
		double size = heldItem.size;		     
		double angle = player.getToolRotationAngle();		
		double const_ = 9;
		double[] x_bounds = heldItem.xBounds;
		double[] y_bounds = heldItem.yBounds;
		Vector2F[] scaled_points = new Vector2F[x_bounds.length];
		for(int i = 0; i < scaled_points.length; i++)
		{
			scaled_points[i] = new Vector2F((float)(size * x_bounds[i]), (float)(size * ((float)y_bounds[i])) - (float)size );
		}
		double[] x_points = new double[scaled_points.length];
		double[] y_points = new double[scaled_points.length];
		
		//Rotate the points
		for(int i = 0; i < scaled_points.length; i++)
		{
			x_points[i] =  player.x + const_ + (scaled_points[i].x * Math.cos(angle)) - 
					(scaled_points[i].y * Math.sin(angle));
			y_points[i] = player.y + const_ + (scaled_points[i].x * Math.sin(angle)) + 
					( scaled_points[i].y * Math.cos(angle));
		}
		
		for(int i = 0; i < entityList.size(); i++)
		{
			if(entityList.get(i).isImmuneToDamage())
			{
				continue;
			}	
			
			if(pnpoly(scaled_points.length, 
					x_points, y_points, 
					entityList.get(i).x + entityList.get(i).width, 
					entityList.get(i).y + entityList.get(i).height)
			){	
				Damage damage = new Damage(heldItem.getDamageDone() * player.allDamageModifier * player.meleeDamageModifier, 
						new EnumDamageType[] { EnumDamageType.NONE },
						EnumDamageSource.MELEE)
						.setIsCrit(((Math.random() < player.criticalStrikeChance) ? true : false));
				player.inflictedDamageToMonster(this, damage);
				entityList.get(i).damage(this, damage, true);
				
				int knockBackValue = (int) (player.knockbackModifier * 12);
				String direction = player.getDirectionOfQuadRelativeToEntityPosition(entityList.get(i).x, entityList.get(i).y, entityList.get(i).width, entityList.get(i).height);
				
				if(direction.equals("right"))
				{
					entityList.get(i).moveEntityRight(this, knockBackValue);	
				}
				else
				{
					entityList.get(i).moveEntityLeft(this, knockBackValue);
				}
				entityList.get(i).registerStatusEffect(this, new StatusEffectStun(0.45, 1, 1, 1));
			}
		}
		
		player.updateSwing(update);		
	}
	
	/**
	 * Handles block break events, based on what the block is
	 * @param mx x position in the 'world map'
	 * @param my y position in the 'world map'
	 */
	private void handleBlockBreakEvent(ServerUpdate update, EntityPlayer player, int mx, int my)
	{
		Block block = getBlockGenerate(mx, my);
		if(!getBlock(mx, my).hasMetaData) //normal block
		{
			ItemStack stack = block.getDroppedItem();
			if(stack != null) //if there's an item to drop, add it to the list of dropped items
			{
				EntityItemStack entityItemStack = new EntityItemStack((mx * 6) - 1, (my * 6) - 2, stack);
				addItemStackToItemList(entityItemStack);
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'a';
				entityUpdate.type = 3;
				entityUpdate.entityID = entityItemStack.entityID;
				entityUpdate.updatedEntity = new DisplayableEntity(entityItemStack);
				update.addEntityUpdate(entityUpdate);
			}
			
			if(block.lightStrength > 0)
			{
				//removeLightSource(player, mx, my, ((BlockLight)(getBlock(mx, my))).lightRadius, ((BlockLight)(getBlock(mx, my))).lightStrength);
				setBlock(Block.air, mx, my, EnumEventType.EVENT_BLOCK_BREAK_LIGHT); //replace it with air
				BlockUpdate blockUpdate = new BlockUpdate();
				blockUpdate.x = mx;
				blockUpdate.y = (short) my;
				blockUpdate.block = new SuperCompressedBlock(Block.air);
				update.addBlockUpdate(blockUpdate);
			}
			else
			{
				setBlock(Block.air, mx, my, EnumEventType.EVENT_BLOCK_BREAK); //replace it with air
				BlockUpdate blockUpdate = new BlockUpdate();
				blockUpdate.x = mx;
				blockUpdate.y = (short) my;
				blockUpdate.block = new SuperCompressedBlock(Block.air);
				update.addBlockUpdate(blockUpdate);
			}			
		}
		else
		{
			if(block instanceof BlockChest)
			{
				BlockChest chest = ((BlockChest)(block));
				if(chest.metaData != 1)
				{
					int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[getBlock(mx, my).id].blockWidth / 6), 
							(int)(Block.blocksList[getBlock(mx, my).id].blockHeight / 6)); //metadata used by the block of size (x,y)
					int metaWidth = metadata.length; 
					int metaHeight = metadata[0].length;	
					int x1 = 0;
					int y1 = 0;				
					
					for(int i = 0; i < metaWidth; i++) 
					{
						for(int j = 0; j < metaHeight; j++)
						{
							if(metadata[i][j] == getBlock(mx - x1, my - y1).metaData)
							{
								x1 = i; 
								y1 = j;
								break;
							}
						}
					}			
					
					chest = (BlockChest)(getBlockGenerate(mx - x1, my - y1));
					mx -= x1;
					my -= y1;
				}	
				
				ItemStack[] stacks = chest.getMainInventory();
				for(int i = 0; i < stacks.length; i++)
				{
					if(stacks[i] != null)
					{
						//drop the item into the world
						EntityItemStack entityItemStack = new EntityItemStack((mx * 6) + random.nextInt(8) - 2, (my * 6) + random.nextInt(8) - 2, stacks[i]);
						addItemStackToItemList(entityItemStack);
						EntityUpdate entityUpdate = new EntityUpdate();
						entityUpdate.action = 'a';
						entityUpdate.type = 3;
						entityUpdate.entityID = entityItemStack.entityID;
						entityUpdate.updatedEntity = new DisplayableEntity(entityItemStack);
						update.addEntityUpdate(entityUpdate);
					}
				}
				
				player.clearViewedChest();
			}
			
			ItemStack stack = block.getDroppedItem(); //the item dropped by the block
			if(stack != null)
			{			
				int[][] metadata = MetaDataHelper.getMetaDataArray((int)(Block.blocksList[getBlock(mx, my).id].blockWidth / 6),
						(int)(Block.blocksList[getBlock(mx, my).id].blockHeight / 6)); //metadata used by the block of size (x,y)
				int metaWidth = metadata.length; //width of the metadata
				int metaHeight = metadata[0].length; //height of the metadata
	
				int x = 0;
				int y = 0;				
				for(int i = 0; i < metaWidth; i++) //cycle through the metadata until the value of the broken block is matched
				{
					for(int j = 0; j < metaHeight; j++)
					{
						if(metadata[i][j] == getBlock(mx, my).metaData)
						{
							x = i; 
							y = j;
							break;
						}
					}
				}
				
				int xOffset = x * -1; //how far over in the block the player in mining
				int yOffset = y * -1; 
							
				for(int i = 0; i < metaWidth; i++) //break the block
				{
					for(int j = 0; j < metaHeight; j++)
					{
						setBlock(Block.air, mx + i + xOffset, my + j + yOffset, EnumEventType.EVENT_BLOCK_BREAK);
						BlockUpdate blockUpdate = new BlockUpdate();
						blockUpdate.x = mx + i + xOffset;
						blockUpdate.y = (short) (my + j + yOffset);
						blockUpdate.block = new SuperCompressedBlock(Block.air);
						update.addBlockUpdate(blockUpdate);
					}					
				}
				
				EntityItemStack entityItemStack = new EntityItemStack((mx * 6) - 1, (my * 6) - 2, stack);
				addItemStackToItemList(entityItemStack);
				EntityUpdate entityUpdate = new EntityUpdate();
				entityUpdate.action = 'a';
				entityUpdate.type = 3;
				entityUpdate.entityID = entityItemStack.entityID;
				entityUpdate.updatedEntity = new DisplayableEntity(entityItemStack);
				update.addEntityUpdate(entityUpdate);
			}
		}		
		setBitMap(mx-1,my, updateBlockBitMap(mx-1, my));
		setBitMap(mx,my-1, updateBlockBitMap(mx, my-1));
		setBitMap(mx,my, updateBlockBitMap(mx, my));
		setBitMap(mx+1,my, updateBlockBitMap(mx+1, my));
		setBitMap(mx,my+1, updateBlockBitMap(mx, my+1));
		player.resetMiningVariables();
		//LightingEngine.applySunlight(this);
	}
	
	/**
	 * Handles block break events, based on what the block is
	 * @param mx x position in the 'world map'
	 * @param my y position in the 'world map'
	 */
	private void handleBackBlockBreakEvent(ServerUpdate update, EntityPlayer player, int mx, int my)
	{
		ItemStack stack = getBackBlock(mx, my).getDroppedItem();
		if(stack != null) //if there's an item to drop, add it to the list of dropped items
		{
			EntityItemStack entityItemStack = new EntityItemStack((mx * 6) - 1, (my * 6) - 2, stack);
			addItemStackToItemList(entityItemStack);
			EntityUpdate entityUpdate = new EntityUpdate();
			entityUpdate.action = 'a';
			entityUpdate.type = 3;
			entityUpdate.entityID = entityItemStack.entityID;
			entityUpdate.updatedEntity = new DisplayableEntity(entityItemStack);
			update.addEntityUpdate(entityUpdate);
		}
		setBackBlock(Block.backAir, mx, my); //replace it with air
		BlockUpdate blockUpdate = new BlockUpdate();
		blockUpdate.x = mx;
		blockUpdate.type = 1;
		blockUpdate.y = (short) my;
		blockUpdate.block = new SuperCompressedBlock(Block.backAir);
		update.addBlockUpdate(blockUpdate);
	}
	
	/**
	 * Handles player block placement
	 * @param mx x position in worldmap array, of the block being placed
	 * @param my y position in the worldmap array, of the block being placed
	 * @param block the block to be placed
	 */
	public void placeLargeBlockWorld(int mx, int my, Block block)
	{
		if(block.hasMetaData) //if the block is large
		{
			double blockWidth = block.getBlockWidth() / 6;
			double blockHeight = block.getBlockHeight() / 6;
			int[][] metadata = MetaDataHelper.getMetaDataArray((int)blockWidth, (int)blockHeight);
			
			for(int i = 0; i < blockWidth; i++) //is it possible to place the block?
			{
				for(int j = 0; j < blockHeight; j++)
				{
					if(getBlock(mx + i, my + j).id != Block.air.id && !getBlockGenerate(mx + i, my + j).getIsOveridable())
					{
						return;
					}
				}
			}
			
			boolean canBePlaced = false;
			//Check for at least one solid block on some side of the placement:
			for(int i = 0; i < blockWidth; i++) //Bottom
			{
				if(getBlock(mx + i, my + blockHeight).isSolid)
				{
					canBePlaced = true;
				}
			}					
			if(!canBePlaced) //If it cant be placed, then give up trying right here
			{
				return;
			}
			
			for(int i = 0; i < metadata.length; i++) //place the block(s)
			{
				for(int j = 0; j < metadata[0].length; j++)
				{
					if(block instanceof BlockChest)
					{
						BlockChest chest = new BlockChest((BlockChest) block);						
						setBlock(chest, mx + i, my + j);
					}
					else
					{
						setBlock(block.clone(), mx + i, my + j);
					}
					getBlock(mx + i, my + j).metaData = (byte)metadata[i][j];
				}
			}
		}	
	}
	
	/**
	 * Handles player block placement
	 * @param mx x position in worldmap array, of the block being placed
	 * @param my y position in the worldmap array, of the block being placed
	 * @param block the block to be placed
	 */
	public boolean placeBlock(EntityPlayer player, int mx, int my, Block block)
	{
		if(block.hasMetaData) //if the block is large
		{
			double blockWidth = block.getBlockWidth() / 6;
			double blockHeight = block.getBlockHeight() / 6;
			int[][] metadata = MetaDataHelper.getMetaDataArray((int)blockWidth, (int)blockHeight);
			
			for(int i = 0; i < blockWidth; i++) //is it possible to place the block?
			{
				for(int j = 0; j < blockHeight; j++)
				{
					if(getBlock(mx + i, my + j).id != Block.air.id && !getBlockGenerate(mx + i, my + j).getIsOveridable())
					{
						return false;
					}
				}
			}
			
			boolean canBePlaced = false;
			//Check for at least one solid block on some side of the placement:
			for(int i = 0; i < blockWidth; i++) //Top
			{
				if(getBlock(mx + i, my - 1).isSolid)
				{
					canBePlaced = true;
				}
			}
			for(int i = 0; i < blockWidth; i++) //Bottom
			{
				if(getBlock(mx + i, my + blockHeight).isSolid)
				{
					canBePlaced = true;
				}
			}
			for(int i = 0; i < blockHeight; i++) //Left
			{
				if(getBlock(mx - 1, my + i).isSolid)
				{
					canBePlaced = true;
				}
			}
			for(int i = 0; i < blockHeight; i++) //Right
			{
				if(getBlock(mx + blockWidth, my + i).isSolid)
				{
					canBePlaced = true;
				}
			}
			for (int i = 0; i < blockHeight; i++){
				for (int j = 0; j < blockWidth; j++){
					if (getBackBlock(mx + j, my + i).getIsSolid()){
						canBePlaced = true;
					}
				}
			}
			
			if(!canBePlaced) //If it cant be placed, then give up trying right here
			{
				return false;
			}
			
			for(int i = 0; i < metadata.length; i++) //place the block(s)
			{
				for(int j = 0; j < metadata[0].length; j++)
				{
					if(block instanceof BlockChest)
					{
						BlockChest chest = new BlockChest((BlockChest) block);       					
						setBlock(chest, mx + i, my + j, EnumEventType.EVENT_BLOCK_PLACE);
					}
					else
					{
						setBlock(block.clone(), mx + i, my + j, EnumEventType.EVENT_BLOCK_PLACE);
					}
					getBlock(mx + i, my + j).metaData = (byte)metadata[i][j];
				}
			}
			
			setBitMap(mx-1,my, updateBlockBitMap(mx-1, my));
			setBitMap(mx,my-1, updateBlockBitMap(mx, my-1));
			setBitMap(mx,my, updateBlockBitMap(mx, my));
			setBitMap(mx+1,my, updateBlockBitMap(mx+1, my));
			setBitMap(mx,my+1, updateBlockBitMap(mx, my+1));
			return true;
			//Make more generic later
		//	player.inventory.removeItemsFromInventory(player, new ItemStack(block, 1)); //take the item from the player's inventory
		}
		else
		{
			if ((getBlockGenerate(mx, my).getIsOveridable() == true || getBlock(mx, my).id == Block.air.id) && 
				(getBlock(mx-1, my).isSolid || getBlock(mx, my-1).isSolid || getBlock(mx, my+1).isSolid || getBlock(mx+1, my).isSolid ||
				getBackBlock(mx, my).getIsSolid())) //can the block be placed
			{
				//player.inventory.removeItemsFromInventory(player, new ItemStack(block, 1)); //remove the items from inventory	
				
				if(block.lightStrength > 0)
				{
					setBlock(block, mx, my, EnumEventType.EVENT_BLOCK_PLACE_LIGHT); //place it
					//		applyLightSource(player, block, mx, my, ((BlockLight)(block)).lightRadius,  ((BlockLight)(block)).lightStrength);
				}
				else
				{
					setBlock(block, mx, my, EnumEventType.EVENT_BLOCK_PLACE); //place it
					//	setBlock(block, mx, my, EnumEventType.EVENT_BLOCK_PLACE); //place it
				}
				
				setBitMap(mx-1,my, updateBlockBitMap(mx-1, my));
				setBitMap(mx,my-1, updateBlockBitMap(mx, my-1));
				setBitMap(mx,my, updateBlockBitMap(mx, my));
				setBitMap(mx+1,my, updateBlockBitMap(mx+1, my));
				setBitMap(mx,my+1, updateBlockBitMap(mx, my+1));
				return true;
			}
		}
		
	
		return false;
		
	}
	
	/**
	 * Handles player back wall placement
	 * @param mx x position in worldmap array, of the block being placed
	 * @param my y position in the worldmap array, of the block being placed
	 * @param block the block to be placed
	 */
	public boolean placeBackWall(EntityPlayer player, int mx, int my, Block block)
	{
		if ((getBackBlock(mx, my).getIsOveridable() == true || getBackBlock(mx, my).getID() == Block.backAir.getID()) && 
			(getBackBlock(mx-1, my).getIsSolid() || getBackBlock(mx, my-1).getIsSolid() || getBackBlock(mx, my+1).getIsSolid() || getBackBlock(mx+1, my).getIsSolid() || 
			getBlock(mx, my).isSolid)) //can the block be placed
		{
			player.inventory.removeItemsFromInventory(player, new ItemStack(block, 1)); //remove the items from inventory		
			setBackBlock(block.clone(), mx, my); //place it	
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the world object's name. worldName is a final field of world and is always the same. It 
	 * describes whether the world is 'Earth', 'Sky', etc.
	 * @return the world's name
	 */
	public final String getWorldName()
	{
		return worldName;
	}
	
	/**
	 * Cuts down a tree, if a log was broken
	 * @param mx x position in worldmap array, of the BlockWood
	 * @param my y position in the worldmap array, of the BlockWood
	 */
	public void breakTree(ServerUpdate update, EntityPlayer player, int mx, int my){
		
		//Loop as long as part of the tree is above
		while(my >= 1 && getBlock(mx, my-1).getID() == Block.tree.getID() || 
				getBlock(mx, my-1).getID() == Block.treetopc2.getID())
		{
			if(my >= 1)
			{
				if (getBlock(mx, my-1).getID() == Block.tree.getID()){ //If there's a tree above, break it
					handleBlockBreakEvent(update, player, mx, my-1);
				}
			}
			if(mx >= 1)
			{
				if (getBlock(mx-1, my).getID() == Block.treebranch.getID() || getBlock(mx-1, my).getID() == Block.treebase.getID()){
					handleBlockBreakEvent(update, player, mx - 1, my); //If there is a left branch/base on the same level, break it
				}
			}
			if(mx + 1 < width)
			{
				if (getBlock(mx+1, my).getID() == Block.treebranch.getID() || getBlock(mx+1, my).getID() == Block.treebase.getID()){
					handleBlockBreakEvent(update, player, mx + 1, my); //Same for right branches/bases
				}
			}
			if(mx + 1 < width && mx >= 1 && my >= 1)
			{
				if (getBlock(mx, my - 1).getID() == Block.treetopc2.getID()){
					handleBlockBreakEvent(update, player, mx + 1, my - 1); //Break a canopy
					handleBlockBreakEvent(update, player, mx + 1, my - 2);
					handleBlockBreakEvent(update, player, mx, my - 1);
					handleBlockBreakEvent(update, player, mx, my - 2);
					handleBlockBreakEvent(update, player, mx - 1, my - 1);
					handleBlockBreakEvent(update, player, mx - 1, my - 2);
				}
			}
			my--; //Move the check upwards 1 block
		}	
	}

	/**
	 * Gets the average level of the terrain. After the world object is generated, this should be invoked to map out the top of the terrain.
	 * This allows for simpler application of a background to the world, sunlight, and weather... in theory.
	 */
	public void assessForAverageSky()
	{
		List<Integer> values = new ArrayList<Integer>(width);
		long average = 0;
		
		for(int i = 0; i < width; i++) //Loop though each column
		{
			for(int j = 0; j < height; j++) //and each row
			{
				if(getBlock(i, j).id != Block.air.id) //when something not air is hit, assume it's just the ground
				{
					values.add(j);
					average += j;
					break;
				}				
			}
		}
		
		average /= width; //get Average height
		generatedHeightMap = new int[values.size()];
		averageSkyHeight = (int) average; //save value to the averageSkyHeight field 
		
		for(int i = 0; i < generatedHeightMap.length; i++) //save over the individual data as well
		{
			generatedHeightMap[i] = (Integer)(values.get(i));
		}
		
		System.out.println("Average World Height: " + average);
	}
		
	/**
	 * Breaks a cactus, from bottom to top.
	 * @param mx the x position of the first block in worldMap
	 * @param my the y position of the first block in worldMap
	 */
	public void breakCactus(ServerUpdate update, EntityPlayer player, int mx, int my)
	{
		while(getBlock(mx, my-1).getID() == Block.cactus.getID())
		{
			handleBlockBreakEvent(update, player, mx, my-1);
			my--;
		}
	}
	
	/**
	 * Provides access to handlBackBlockBreakEvent() because that method is private and has a bizzare name, that's hard to both find and
	 * remember
	 * @param x the x position of the block to break (in the 'world map')
	 * @param y the y position of the block to break (in the 'world map')
	 */
	public void breakBackBlock(ServerUpdate update, EntityPlayer player, int x, int y)
	{
		handleBackBlockBreakEvent(update, player, x, y);
	}
	
	/**
	 * Provides access to handlBlockBreakEvent() because that method is private and has a bizzare name, that's hard to both find and
	 * remember
	 * @param x the x position of the block to break (in the 'world map')
	 * @param y the y position of the block to break (in the 'world map')
	 */
	public void breakBlock(ServerUpdate update, EntityPlayer player, int x, int y)
	{
		handleBlockBreakEvent(update, player, x, y);
		breakCactus(update, player, x, y);
		breakTree(update, player, x, y);
	}

	/**
	 * Gets the world's width. This field is a final Integer and will never change.
	 * @return the world's width
	 */
	public final int getWidth()
	{
		return width;
	}
	
	/**
	 * Gets the world's height. This field is a final Integer and will never change.
	 * @return the world's height 
	 */
	public final int getHeight()
	{
		return height;
	}
	
	/**
	 * Gets the back wall at the specified (x,y). Useful for easily getting a back wall at the specified location; Terrible for mass usage,
	 * such as in rendering.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public Block getBackBlock(int x, int y)
	{
		try
		{
			MinimalBlock block = getChunks().get(""+(x / Chunk.getChunkWidth())).getBackWall(x % Chunk.getChunkWidth(), (y));
			return Block.blocksList[block.id].mergeOnto(block);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the block at the specified (x,y). Useful for easily getting a block at the specified location; Terrible for mass usage,
	 * such as in rendering.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public MinimalBlock getBlock(int x, int y)
	{
		try
		{
			return getChunks().get(""+(x / Chunk.getChunkWidth())).getBlock(x % Chunk.getChunkWidth(), (y));
		}
		catch(Exception e)
		{
	//		e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sets the back wall at the specified (x,y). Useful for easily setting a back wall at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBackBlock(Block block, int x, int y)
	{
		try
		{
			getChunks().get(""+x / Chunk.getChunkWidth()).setBackWall(block, x % Chunk.getChunkWidth(), y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
	/**
	 * Sets the block at the specified (x,y). Useful for easily setting a block at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBlock(Block block, int x, int y)
	{
		try
		{
			getChunks().get(""+x / Chunk.getChunkWidth()).setBlock(block, x % Chunk.getChunkWidth(), y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
	/**
	 * Gets the block at the specified (x,y). Useful for easily getting a block at the specified location; Terrible for mass usage, such as in rendering.
	 * This version of the method accepts doubles, and casts them to Integers.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public MinimalBlock getBlock(double x, double y)
	{
		return getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getBlock((int)x % Chunk.getChunkWidth(), (int)y);
	}
	
	/**
	 * Method designed to grow (or at least attempt to grow) a tree
	 * @param space - How high/wide the space between trees and terrain must be
	 * @param x - x location on the world
	 * @param y - y location on the world
	 */
	public void growTree(int space, int x, int y){
		boolean isOpen = true;
		int height = (int)(Math.random() * 5 + 4); //Determine the height of the tree
		if (y-height-space <= 0 || x <= 2 || x >= getWidth() - 2){ //If the tree would go off the map
			height = 0; //don't place a tree
			space = 0; //Don't place a tree
		}
		//If there is room for the tree up and to the left/right	
		for (int j = y; j >= y - height - space; j--){
			for (int i = x - space; i <= x + space; i++){
				if (!getBlockGenerate(i, j).getIsOveridable()){
					isOpen = false;
					break;
				}
			}
			if (!isOpen) break;			
		}
		if (isOpen){
			setBlockGenerate(Block.dirt, x, y + 1);
			int count = 1;
			
			if ((getBlockGenerate(x-1, y+1).getID() == Block.grass.getID()|| getBlockGenerate(x-1, y+1).getID() == Block.dirt.getID())){
				setBlockGenerate(Block.treebase, x-1, y);
				setBitMap(x-1, y, 0);
				setBlockGenerate(Block.dirt, x-1, y+1);
			}
			
			if ((getBlockGenerate(x+1, y+1).getID() == Block.grass.getID()|| getBlockGenerate(x+1, y+1).getID() == Block.dirt.getID())){
				setBlockGenerate(Block.treebase, x+1, y);
				setBitMap(x+1, y, 3);
				setBlockGenerate(Block.dirt, x+1, y+1);
			}
			
			for (int k = y; k >= y - height; k--){ //Place the tree
				if (getBlockGenerate(x, k).getID() == Block.air.getID()){ //If the cell is empty
					if (k == y-height){ //If at the top of the tree
						setBlockGenerate(Block.treetopr2, x+1, k); //Place the tree top
						setBlockGenerate(Block.treetopr1, x+1, k-1);
						setBlockGenerate(Block.treetopc2, x, k);
						setBlockGenerate(Block.treetopc1, x, k-1);
						setBlockGenerate(Block.treetopl2, x-1, k);
						setBlockGenerate(Block.treetop, x-1, k-1);
					}
					else{
						setBlockGenerate(Block.tree, x, k); //Otherwise, place a tree trunk
						setBitMap(x, k, 1);
					}
					if (count > 2 && k > y - height + 1){ //For each slice of tree, if it is more than the third log, determine if there should be a branch
						int branchl = (int)(Math.random()*60); //Decide if a block should be placed left
						int branchr = (int)(Math.random()*60); //Decide if a branch should be placed right
						
						if (branchl < 5){
							setBlockGenerate(Block.treebranch, x-1, k);
							setBitMap(x-1, k, branchl * 2);
						}
						if (branchr < 5){
							setBlockGenerate(Block.treebranch, x+1, k);
							setBitMap(x+1, k, branchr * 2 + 1);
						}															
					}
					count++; //increment the counter 
				}
				else{
					break;
				}
			}
		}
	}
	
	/**
	 * A method designed to convert all exposed dirt above the minimum height to grass
	 * Note: We'll probably want to make it work via light value, rather than via y-value.
	 * @param x - the x-value to start at
	 * @param w - the width of the area to convert
	 * @param minHeight - the lowest y value a dirt block can have and still be converted
	 * @param maxHeight - the highest y value to check
	 */
	public void placeGrass(int x, int w, int minHeight, int maxHeight){
		for(int j = maxHeight; j > minHeight; j--){ //go through the the y-axis of the world
			for(int k = 1; k < x + w; k++){ //x-axis	
				//Search above, left and right of dirt block for air
				if (getBlockGenerate(k, j).getID() == Block.dirt.getID()){
					if (k > 0 && k < getWidth() && j > 0){
						if (getBlockGenerate(k - 1, j).getID() == Block.air.getID()){
							setBlockGenerate(Block.grass, k, j);
						}
					}
					if (k < getWidth()){
						if (getBlockGenerate(k + 1, j).getID() == Block.air.getID()){
							setBlockGenerate(Block.grass, k, j);
						}
					}
					if (j > 0){
						if (getBlockGenerate(k, j-1).getID() == Block.air.getID()){
							setBlockGenerate(Block.grass, k, j);
						}
					}
				}
			}
		}
	}
		
	/**
	 * Sets the block at the specified (x,y). Useful for easily setting a block at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur. This version of the method uses doubles.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBlock(Block block, double x, double y)
	{
		try
		{
			getChunks().get(""+x).setBlock(block, (int)x, (int)y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the chunk based off the block position, so division must occur.
	 * @param x the x position of the chunk (in Blocks)
	 * @param y the y position of the chunk (in Blocks)
	 * @return the chunk at the specified position in the world's chunk map, or null if it doesn't exist or isn't loaded
	 */
	public Chunk getChunk_Division(int x)
	{
		return getChunks().get(""+(int)(x / Chunk.getChunkWidth()));
	}
	
	/**
	 * Gets the chunk based off the chunk-map coordinates. Division is not performed.
	 * @param x the x position of the chunk 
	 * @param y the y position of the chunk 
	 * @return the chunk at the specified position in the world's chunk map, or null if it doesn't exist or isn't loaded
	 */
	public Chunk getChunk(int x)
	{
		return (getChunks().get(""+x) != null) ? getChunks().get(""+x) : new Chunk(Biome.forest, x, height);
	}
	
	/**
	 * Add a new Chunk to the World's Chunk map. Usage of this method is advisable so that the game actually knows the chunk 
	 * exists in memory.
	 * @param chunk the chunk to add to the chunk map of the world
	 * @param x the x position of the chunk
	 * @param y the y position of the chunk
	 */
	public void registerChunk(Chunk chunk, int x)
	{
		chunksLoaded.put(""+x, true);
		chunks.put(""+x, chunk);
	}
	
	/**
	 * Gets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally, 
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the 
	 * world, so it MUST be safe, otherwise that code will become dangerous. 
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified, or null if there isnt one.
	 */
	public Block getBlockGenerate(int x, int y)
	{
		try
		{
			MinimalBlock block = getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getBlock((int)x % Chunk.getChunkWidth(), (int)y);
			return Block.blocksList[block.id].mergeOnto(block);
		}
		catch (Exception e)
		{
		}
		return Block.air;
	}

	public Block getBlockGenerate(double x, double y)
	{
		try
		{
			MinimalBlock block = getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getBlock((int)x % Chunk.getChunkWidth(), (int)y);
			return Block.blocksList[block.id].mergeOnto(block);
		}
		catch (Exception e)
		{
		}
		return Block.air;
	}
	
	/**
	 * Sets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally,
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the world, 
	 * so it MUST be safe, otherwise code will become very dangerous. Chunks are generated if there is non chunk present at that
	 * position.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBackWallGenerate(Block block, int x, int y)
	{
		try
		{ //Ensure the chunk exists
			if(getChunks().get(""+(x / Chunk.getChunkWidth())) == null)
			{
				registerChunk(new Chunk(Biome.forest, (int)(x / Chunk.getChunkWidth()), height), (int)(x / Chunk.getChunkWidth()));
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		try 
		{ //Set the block
			chunks.get(""+(x / Chunk.getChunkWidth())).setBackWall(block, x % Chunk.getChunkWidth(), y);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally, 
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the 
	 * world, so it MUST be safe, otherwise that code will become dangerous. 
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified, or null if there isnt one.
	 */
	public Block getBackWallGenerate(int x, int y)
	{
		try
		{
			MinimalBlock block = getChunks().get(""+x / Chunk.getChunkWidth()).getBackWall(x % Chunk.getChunkWidth(), y);
			return Block.blocksList[block.id].mergeOnto(block);
		}
		catch (Exception e)
		{
		}
		return Block.backAir;
	}
		
	/**
	 * Sets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally,
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the world, 
	 * so it MUST be safe, otherwise code will become very dangerous. Chunks are generated if there is non chunk present at that
	 * position.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBlockGenerate(Block block, int x, int y)
	{
		try
		{ //Ensure the chunk exists
			if(getChunks().get(""+(x / Chunk.getChunkWidth())) == null)
			{
				registerChunk(new Chunk(Biome.forest, (int)(x / Chunk.getChunkWidth()), height), (int)(x / Chunk.getChunkWidth()));
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		try 
		{ //Set the block
			chunks.get(""+(x / Chunk.getChunkWidth())).setBlock(block, x % Chunk.getChunkWidth(), y);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks for chunks that need to be loaded or unloaded, based on the player's screen size. The range in which chunks stay loaded increases if the player's 
	 * screen size is larger. (It's about ((width/2.2), (height/2.2))). 
	 */
	private void updateChunks(Vector<EntityPlayer> players)
	{
		List<String> requiredChunks = new ArrayList<String>();
		List<String> removedChunks = new ArrayList<String>();
		//How far to check for chunks (in blocks)
		final int loadDistanceHorizontally = ((2 * Chunk.getChunkWidth()) + TerraeRasa.terraeRasa.getSettings().loadDistance * Chunk.getChunkWidth()) - 1;
		
		// Force Chunks next to the original spawn to be loaded for speed
		//Position to check from
		int x = getWorldCenterBlock();
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftOff = (x - loadDistanceHorizontally) / Chunk.getChunkWidth();
		int rightOff = (x + loadDistanceHorizontally) / Chunk.getChunkWidth();
		//Bounds checking
		if(leftOff < 0) leftOff = 0;
		if(rightOff > (width / Chunk.getChunkWidth())) rightOff = width / Chunk.getChunkWidth();
		
		Enumeration<String> keys = chunksLoaded.keys();
        while (keys.hasMoreElements()) 
        {
            String key = (String) keys.nextElement();
            int cx = Integer.parseInt(key);
            if(chunksLoaded.get(key) && (cx < leftOff || cx > rightOff) && x != leftOff && x != rightOff)
			{
            	removedChunks.add(key);
			}
		}
		for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
		{
			requiredChunks.add(""+i);	
		}
		
		//Chunks for every player
		Iterator<EntityPlayer> it = players.iterator();
		while(it.hasNext())
		{
			EntityPlayer player = it.next();
			
			//Position to check from
			x = (int) (player.x / 6);
			//Where to check, in the chunk map (based off loadDistance variables)
			leftOff = (x - loadDistanceHorizontally) / Chunk.getChunkWidth();
			rightOff = (x + loadDistanceHorizontally) / Chunk.getChunkWidth();
			//Bounds checking
			if(leftOff < 0) leftOff = 0;
			if(rightOff > (width / Chunk.getChunkWidth())) rightOff = width / Chunk.getChunkWidth();
			
			//SSystem.out.println(leftOff + " " + rightOff + " " + x);
			
			keys = chunksLoaded.keys();
	        while (keys.hasMoreElements()) 
	        {
	            String key = (String) keys.nextElement();
	            int cx = Integer.parseInt(key);
	            if(chunksLoaded.get(key) && (cx < leftOff || cx > rightOff) && x != leftOff && x != rightOff)
				{
	            	removedChunks.add(key);
				}
			}
			for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
			{
				requiredChunks.add(""+i);
			}	
		}
		
		String[] sortedChunkRequirement = removeDuplicates(requiredChunks);
		String[] sortedRemoveRequirement = removeDuplicates(removedChunks);
		List<String> list = Arrays.asList(sortedChunkRequirement);
		
        for(String str : sortedRemoveRequirement) 
        {
        	if(!list.contains(str))
        	{
        		//If a chunk isnt needed, request a save.
    			chunkManager.saveChunk(worldName, chunks, Integer.parseInt(str));
    			chunksLoaded.put(str, false);
            }
        }
	
		for(String str : sortedChunkRequirement) //Check for chunks that need loaded
		{
			if(Integer.parseInt(str) >= 0 && chunksLoaded.get(str) == null)
			{
				chunksLoaded.put(str, false);
			}
			if(!chunksLoaded.get(str)) //If a needed chunk isnt loaded, request it.
			{
				chunkManager.requestChunk(worldName, this, chunks, Integer.parseInt(str));
			}			
		}
		
	}
	
	/**
	 * Removes all the duplicates from a List<String> then returns a sorted String[] containing the non-duplicate values.
	 * @param l the List<String> to remove duplicates from 
	 * @return a sorted String[] containing all the non-duplicates in the given List<String>
	 */
	public static String[] removeDuplicates(List<String> l) {
	    Set<Object> s = new TreeSet<Object>(new Comparator<Object>() {
	        @Override
	        public int compare(Object o1, Object o2) {
	        	if(o1.equals(o2)) {	
	        		return 0;
	        	}
	        	return 1;
	        }
	    });
	    
	    s.addAll(l);
	    List<Object> res = Arrays.asList(s.toArray());
	    String[] objs = new String[res.size()];
	    for(int i = 0; i < res.size(); i++) {
	    	objs[i] = (String)res.get(i);
	    }
	    Arrays.sort(objs);
	    return objs;
	}
	
	/**
	 * Saves all chunks loaded in chunks (ConcurrantHashMap) to disk.
	 * @param dir the sub-directory to save the chunks in (ex. "Earth" for the overworld)
	 */
	private void saveAllRemainingChunks()
	{
		Enumeration<String> keys = getChunks().keys();
        while (keys.hasMoreElements()) 
        {
            Chunk chunk = getChunks().get((String)(keys.nextElement()));
            chunkManager.saveChunkAndLockThread(worldName, getChunks(), chunk.getX());		
        }
        System.gc();
	}
	
	/**
	 * Saves all the chunks loaded and the important variables in world to disk. The important variables are saved in the world's
	 * main directory under the name "worlddata.dat" and the chunks are saved in the "Earth" directory (or the applicable dimension)
	 * @param dir the sub-directory to save the world data in (Ex. "Earth" for the over-world)
	 */
	public void saveRemainingWorld()
	{
		saveAllRemainingChunks();
		chunkManager.saveWorldData(this);
	}
	
	/**
	 * Returns true if there are one or more chunks left in the world, or false otherwise.
	 * @return true if chunks are left, otherwise false
	 */
	public boolean hasChunksLeft()
	{
		return chunks.size() > 0;
	}
	
	/**
	 * Gets the average sky height. This is actually the average Block at which the ground begins. This is measured from the 
	 * top of the screen and should vary based on the world's height.
	 * @return the average block at which the ground begins
	 */
	public int getAverageSkyHeight()
	{
		return averageSkyHeight;
	}
	
	/**
	 * A detailed Integer array of where the ground begins, based on how the world was generated. This is measured from the top of
	 * the screen (sky). The average of these values should equal the value of {@link #getAverageSkyHeight()}.
	 * @return a detailed Integer array of where the ground begins
	 */
	public int[] getGeneratedHeightMap()
	{
		return generatedHeightMap;
	}
	
	public long getWorldTime()
	{
		return worldTime;
	}
	
	public EnumWorldDifficulty getDifficulty()
	{
		return difficulty;
	}
	
	public ConcurrentHashMap<String, Chunk> getChunks() 
	{
		return chunks;
	}

	public void setChunks(ConcurrentHashMap<String, Chunk> chunks) 
	{
		this.chunks = chunks;
	}
	
	public void setChunk(Chunk chunk, int x, int y)
	{
		getChunks().put(""+x, chunk);
	}
	
	/**
	 * Gets the biome for the specified chunk value, NOT block value
	 * @param pos a string in the form of "x", indicating which chunk to check for a biome
	 * @return the chunk's biome if it's loaded, otherwise null if it isnt
	 */
	public Biome getBiome(String pos)
	{
		int x = (Integer.parseInt(pos)) / Chunk.getChunkWidth();
	
		Chunk chunk = getChunks().get(""+x);
		if(chunk != null)
		{
			return chunk.getBiome();
		}
			
		return null;
	}
	
	public String getBiomeColumn(String pos)
	{
		Biome biome = getBiome(pos);
		
		if(biome != null)
		{
			return biome.getBiomeName();
		}
		
		return null;
	}
		
	/**
	 * Gets the background block at the specified (x,y). Useful for easily getting a backgroundblock at the specified location; 
	 * Terrible for mass usage, such as in rendering. This method accepts doubles, and casts them to Integers.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public Block getBackBlock(double x, double y)
	{
		MinimalBlock block = getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getBlock((int)x % Chunk.getChunkWidth(), (int)y);
		return Block.blocksList[block.id].mergeOnto(block);
	}
			
	/**
	 * Sets the block and applies relevant lighting to the area nearby. Includes an EnumEventType to describe the event, although
	 * it is currently not used for anything.
	 * @param block the new block to be placed at (x,y)
	 * @param x the x position where the block will be placed
	 * @param y the y position where the block will be placed
	 * @param eventType
	 */
	public void setBlock(Block block, int x, int y, EnumEventType eventType)
	{
		setBlock(block, x, y);
	}
			
	/** 
	 * Launches a projectile
	 * @param world - current world
	 * @param angle - the angle at which to launch the projectile
	 * @param projectile - the projectile to launch
	 */
	public void launchProjectile(World world, int angle, EntityProjectile projectile, double x, double y)
	{
		addEntityToProjectileList(new EntityProjectile(projectile).setXLocAndYLoc(x, y).setDirection(angle));
	}
	
	public void setBitMap(int x, int y, int bitMap)
	{
		MinimalBlock block = getChunks().get(""+(x / Chunk.getChunkWidth())).getBlock(x % Chunk.getChunkWidth(), (y));
		block.setBitMap(bitMap);
	}
	
	/**
	 * Sets the game time based on an hour in game ticks.
	 * @param timeInTicks the time in game ticks
	 */
	public void setTime(int timeInTicks)
	{
		this.worldTime = timeInTicks;
	}
}