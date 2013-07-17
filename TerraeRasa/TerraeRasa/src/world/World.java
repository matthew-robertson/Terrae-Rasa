package world;

import io.Chunk;
import io.ChunkManager;
import items.Item;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.opengl.Display;

import savable.SavableWorld;
import savable.SaveManager;
import transmission.ClientUpdate;
import transmission.CompressedPlayer;
import transmission.WorldData;
import utils.ItemStack;
import utils.LightUtils;
import utils.MathHelper;
import utils.SpawnManager;
import utils.WorldText;
import audio.SoundEngine;
import blocks.Block;
import blocks.BlockGrass;
import blocks.BlockPillar;
import blocks.MinimalBlock;
import entities.Entity;
import entities.EntityItemStack;
import entities.EntityNPC;
import entities.EntityNPCEnemy;
import entities.EntityNPCFriendly;
import entities.EntityPlayer;
import entities.EntityProjectile;
import enums.EnumColor;
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
	private static final int GAMETICKSPERDAY = 28800; 
	private static final int GAMETICKSPERHOUR = 1200;
	public final double g = 1.8;
	public Hashtable<String, Boolean> chunksLoaded;
	
	public Weather weather;
	public List<EntityItemStack> itemsList;
	public List<WorldText> temporaryText; 
	public List<EntityNPCEnemy> entityList;
	public List<EntityNPC> npcList;
	public List<EntityProjectile> projectileList;
	public SpawnManager manager;
	public SoundEngine soundEngine;
	
	private int[] generatedHeightMap;
	private int averageSkyHeight;
	private int totalBiomes;
	private int chunkWidth;
	public ChunkManager chunkManager;
	private EnumWorldDifficulty difficulty;
	private final Random random = new Random();
	protected String worldName;
	private long worldTime;
	private ConcurrentHashMap<String, Chunk> chunks;
	private int width; //Width in blocks, not pixels
	private int height; //Height in blocks, not pixels
	private double previousLightLevel;
	private LightUtils utils;
	private boolean lightingUpdateRequired;
	
	private List<String> pendingChunkRequests = new ArrayList<String>();
	public List<EntityPlayer> otherPlayers = new ArrayList<EntityPlayer>();
	
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
		utils = new LightUtils();
//		checkChunks();
		lightingUpdateRequired = true;
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
		
	public void overwriteEntityByID(int id, Entity newEntity)
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
			otherPlayers.remove(entity);
			otherPlayers.add((EntityPlayer) newEntity);
		}
		entitiesByID.put(""+id, newEntity);		
	}
	
	public void removeEntityByID(int id)
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
			otherPlayers.remove(entity);
		}
		entitiesByID.remove(""+id);
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
		totalBiomes = 0;
		previousLightLevel = getLightLevel();
		this.difficulty = difficulty;
		manager = new SpawnManager();
		chunkWidth = width / Chunk.getChunkWidth();
		utils = new LightUtils();
		lightingUpdateRequired = true;
//		checkChunks();
	}
	
	public World(WorldData data, Chunk[] chunks)
	{
		setChunks(new ConcurrentHashMap<String, Chunk>(10));
		chunksLoaded = new Hashtable<String, Boolean>(25);	
		for(Chunk chunk : chunks)
		{
			this.chunks.put(""+chunk.getX(), chunk);
			this.chunksLoaded.put(""+chunk.getX(), true);
		}
		
		this.width = data.width;
		this.height = data.height; 
		this.difficulty = data.difficulty;
		
		
		this.entityList = new ArrayList<EntityNPCEnemy>();
		for(EntityNPCEnemy enemy : data.entityList)
		{
			enemy.setTexture(EntityNPCEnemy.enemyList[enemy.getNPCID()].getTexture());
			addEntityToEnemyList(enemy);
		}
		this.npcList = new ArrayList<EntityNPC>();
		for(EntityNPC friendly : data.npcList)
		{
			friendly .setTexture(EntityNPCFriendly.npcList[friendly.getNPCID()].getTexture());
			addEntityToNPCList(friendly);
		}		
		this.itemsList = data.itemsList;
		this.projectileList = data.projectileList;
		this.temporaryText = new ArrayList<WorldText>();
		
		this.worldName = data.worldName;
		this.worldTime = data.worldTime;
		this.previousLightLevel = data.previousLightLevel;
		this.chunkWidth = data.chunkWidth;
		this.lightingUpdateRequired = data.lightingUpdateRequired;
		this.generatedHeightMap = data.generatedHeightMap;
		this.averageSkyHeight = data.averageSkyHeight;
		
		for(CompressedPlayer player : data.otherplayers)
		{
			addPlayer(EntityPlayer.expand(player));
		}		
		manager = new SpawnManager();
		utils = new LightUtils();		
	}
			
	/**
	 * Puts the player at the highest YPosition for the spawn XPosition 
	 * @param player the player to be added
	 * @return the player with updated position (x, y)
	 */
	public EntityPlayer spawnPlayer(EntityPlayer player) 
	{
		if(player.inventory.isEmpty())
		{
			player.inventory.pickUpItemStack(this, player, new ItemStack(Item.copperSword));
			player.inventory.pickUpItemStack(this, player, new ItemStack(Item.copperPickaxe));
			player.inventory.pickUpItemStack(this, player, new ItemStack(Item.copperAxe));
			player.inventory.pickUpItemStack(this, player, new ItemStack(Block.craftingTable));
		}
		
		player.respawnXPos = getWorldCenterOrtho();
		
		//requestRequiredChunks((int)(player.respawnXPos / 6), (int)(player.y / 6));
		//chunkManager.addAllLoadedChunks_Wait(this, chunks);
		
		requestRequiredChunks(getWorldCenterBlock(), averageSkyHeight);
		chunkManager.addAllLoadedChunks_Wait(this, getChunks());
		
		try
		{
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
					return player;
				}			
			}
		}
		catch (Exception e) //This is probably a nullpointer 
		{
			e.printStackTrace();
			throw new RuntimeException("This is likely caused by a chunk that's required being denied due to I/O conflicts");
			//If this exception is thrown, inspect the addition to chunkmanager - chunkLock from A1.0.23
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
		SavableWorld savable = (SavableWorld)(manager.loadFile("/World Saves/" + universeName + "/" + dir + "/worlddata.xml"));
		this.width = savable.width;
		this.height = savable.height;
		this.chunkWidth = savable.chunkWidth;
		this.averageSkyHeight = savable.averageSkyHeight;
		this.generatedHeightMap = savable.generatedHeightMap;
		this.worldTime = savable.worldTime;
		this.worldName = savable.worldName;
		this.totalBiomes = savable.totalBiomes;
		this.difficulty = savable.difficulty;
	}
	
	/**
	 * Adds a player to the world. Currently multiplayer placeholder.
	 * @param player the player to add
	 */
	public void addPlayerToWorld(EntityPlayer player)
	{
		requestRequiredChunks(getWorldCenterBlock(), averageSkyHeight);
		chunkManager.addAllLoadedChunks_Wait(this, getChunks());
		player = spawnPlayer(player);
	}
	
	/**
	 * Loads all nearby chunks for a location, given the Display's size
	 * @param x the x value of the point to load near (in blocks)
	 * @param y the y value of the point to load near (in blocks)
	 */
	private void requestRequiredChunks(int x, int y)
	{
		final int loadDistanceHorizontally = (((int)(Display.getWidth() / 2.2) + 3) > Chunk.getChunkWidth()) ? 
				((int)(Display.getWidth() / 2.2) + 3) : Chunk.getChunkWidth();
		
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftOff = (x - loadDistanceHorizontally) / Chunk.getChunkWidth();
		int rightOff = (x + loadDistanceHorizontally) / Chunk.getChunkWidth();
		
		for(int i = leftOff; i <= rightOff; i++) //Check for chunks that need loaded
		{
			chunkManager.requestChunk(worldName, this, getChunks(), i);
		}
	}
	
	/**
	 * Clears all monsters from entityList, generally invoked after a single player death to provide some mercy to the player.
	 */
	public void clearEntityList()
	{
//		entityList.clear();
		entityList = new ArrayList<EntityNPCEnemy>(255);
	}
	
	/**
	 * Clears all projectiles from projectileList
	 */
	public void clearProjectileList()
	{
		projectileList.clear();
	}
	
	/**
	 * Clears all NPCs from npcList
	 */
	public void clearNPCList()
	{
		npcList.clear();
	}
	
	/**
	 * Keeps track of the worldtime and updates the light if needed
	 */
	public void updateWorldTime(EntityPlayer player)
	{		
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
		for(EntityPlayer p : otherPlayers)
		{
			if(player.entityID == p.entityID)
			{
				return;
			}
		}
		otherPlayers.add(player);
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
	
	/**
	 * Calls all the methods to update the world and its inhabitants
	 */
	public void onClientWorldTick(ClientUpdate update, EntityPlayer player)
	{		
		player.onClientTick(update, this); 
		
		updateTemporaryText();
		
		//Update the time
		updateWorldTime(player);
		
		if(worldTime % 20 == 0) {
			//TODO client chunk request
			updateChunks(update, player);
		}
		//TODO client side light recognizes torches that already exist
		applyLightingUpdates(player);
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
	 * Displays all temporary text in the world, or remove it if it's past its life time
	 */
	private void updateTemporaryText()
	{
		for(int i = 0; i < temporaryText.size(); i++)
		{
			temporaryText.get(i).ticksLeft--; //reduce time remaining
			if(temporaryText.get(i).ticksLeft <= 0)
			{ //remove obsolete text
				temporaryText.remove(i);
			}
		}
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
	 * Updates the weather object(s) in the world or do nothing if one doesnt exist
	 */
	public void updateWeather()
	{
		if(weather == null) //No weather
		{
			return;
		}
		if(weather instanceof WeatherSnow) //Snow
		{ 
			WeatherSnow weatherSnow = (WeatherSnow)(weather);
			weatherSnow.update(this);
			weather = weatherSnow;
		}
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
	
	public void removePendingChunkRequest(String command)
	{
		pendingChunkRequests.remove(command);
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
			Block tmp = Block.blocksList[block.id].mergeOnto(block);
			return tmp;
		}
		catch (Exception e)
		{
			System.out.println(4);
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
	 * Gets the lighting value of the indicated Block. This may fail if the chunk requested isn't loaded into memory or doesnt exist.
	 * In this case, a lighting value of 1.0f will be returned. All Exceptions are handled in this method.
	 * @param x the x position of the Block to check for light in the world map
	 * @param y the y position of the Block to check for light in the world map
	 * @return the light value of that square, or 1.0f if that square is null or doesnt exist.
	 */
	public double getLight(int x, int y)
	{
		try
		{
			return getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getLight((int)x % Chunk.getChunkWidth(), (int)y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1.0f;
	}
	
	/**
	 * Checks for chunks that need to be loaded or unloaded, based on the player's screen size. The range in which chunks stay loaded increases if the player's 
	 * screen size is larger. (It's about ((width/2.2), (height/2.2))). 
	 */
	private void updateChunks(ClientUpdate update, EntityPlayer player)
	{
		//How far to check for chunks (in blocks)
		int loadDistance = 2 * Chunk.getChunkWidth();//(((int)(Display.getWidth() / 2.2) + 3) > Chunk.getChunkWidth()) ? ((int)(Display.getWidth() / 2.2) + 3) : Chunk.getChunkWidth();
		int unloadDistance = 3 * Chunk.getChunkWidth();
		
		//Position to check from
		final int x = (int) (player.x / 6);
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftLoad = (x - loadDistance) / Chunk.getChunkWidth();
		int rightLoad = (x + loadDistance) / Chunk.getChunkWidth();
		//Bounds checking
		if(leftLoad < 0) leftLoad = 0;
		if(rightLoad > (width / Chunk.getChunkWidth())) rightLoad = width / Chunk.getChunkWidth();
		
		int leftUnload = (x - unloadDistance) / Chunk.getChunkWidth();
		int rightUnload= (x + unloadDistance) / Chunk.getChunkWidth();
		//Bounds checking
		if(leftUnload < 0) leftUnload = 0;
		if(rightUnload > (width / Chunk.getChunkWidth())) rightUnload = width / Chunk.getChunkWidth();
				
		Enumeration<String> keys = chunksLoaded.keys();
        while (keys.hasMoreElements()) 
        {
            String strKey = (String) keys.nextElement();
            boolean loaded = chunksLoaded.get(strKey);
          
            int cx = Integer.parseInt(strKey);
            
            if(loaded && (cx < leftUnload || cx > rightUnload) && x != leftUnload && x != rightUnload)
			{
//            	System.out.println("Unload: " + cx);
            	chunksLoaded.put(""+cx, false);
            	Chunk chunk = chunks.get(""+cx);
            	chunk = null;
            	chunks.remove(""+cx);
			}
		}
		for(int i = leftLoad; i <= rightLoad; i++) //Check for chunks that need loaded
		{
			if(i >= 0 && chunksLoaded.get(""+i) == null)
			{
				chunksLoaded.put(""+i, false);
			}
			if(chunksLoaded.get(""+i) != null && !chunksLoaded.get(""+i)) //If a needed chunk isnt loaded, request it.
			{
				String command = "/player " + player.entityID + " chunkrequest " + i;
				if(!pendingChunkRequests.contains(command))
				{
//					System.out.println("Request: " + i);
					pendingChunkRequests.add(command);
					update.addCommand(command);
				}
				//chunkManager.requestChunk(worldName, this, chunks, i);
			}
		}
	}
	
	/**
	 * Sets the total number of biomes the world has. Generally this is advisable only during world generation.
	 * @param total the value to set totalBiomes to
	 */
	public void setTotalBiomes(int total)
	{
		totalBiomes = total;
	}
	
	/**
	 * Gets how many biomes the world has. Merged biomes count as a single biome.
	 * @return the total biomes in the world
	 */
	public int getTotalBiomes()
	{
		return totalBiomes;
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
	
//	private void checkChunks()
//	{
//		for(int i = 0; i < width / Chunk.getChunkWidth(); i++)
//		{
//			if(getChunks().get(""+i) == null)
//			{
//				registerChunk(new Chunk(Biome.forest, i, height), i);
//			}
//		}
//	}
	
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
	
	public void setAmbientLight(double x, double y, double strength)
	{
		try
		{
			getChunks().get(""+(int)(x / Chunk.getChunkWidth())).setAmbientLight(strength, (int)x % Chunk.getChunkWidth(), (int)y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
	public void setDiffuseLight(double x, double y, double strength)
	{
		try
		{
			getChunks().get(""+(int)(x / Chunk.getChunkWidth())).setDiffuseLight(strength, (int)x % Chunk.getChunkWidth(), (int)y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public double getAmbientLight(int x, int y)
	{
		try
		{
			return getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getAmbientLight((int)x % Chunk.getChunkWidth(), (int)y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1.0f;
	}
	
	public double getDiffuseLight(int x, int y)
	{
		try
		{
			return getChunks().get(""+(int)(x / Chunk.getChunkWidth())).getDiffuseLight((int)x % Chunk.getChunkWidth(), (int)y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1.0f;
	}
	
	/**
	 * Updates the ambient lighting based on the world time and light level (from getLightLevel())
	 */
	public void updateAmbientLighting()
	{
		Enumeration<String> keys = chunks.keys();
		//Update the lighting in all the chunks (this is now efficient enough to work)
		while (keys.hasMoreElements()) 
        {
            Chunk chunk = chunks.get((String)keys.nextElement());
        	utils.applyAmbientChunk(this, chunk);
    	}
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
		utils.fixDiffuseLightRemove(this, x, y);
		setBlock(block, x, y);
		utils.blockUpdateAmbient(this, x, y, eventType);		
		utils.fixDiffuseLightApply(this, x, y);
	}
	
	/**
	 * Called on world tick. Updates the lighting if it's appropriate to do so. It is considered appropriate if the light level of
	 * the world has changed, or if the chunk (for whatever reason) has been flagged for a lighting update by a source.
	 * @param player
	 */
	public void applyLightingUpdates(EntityPlayer player)
	{
		//If the light level has changed, update the ambient lighting.
		if(lightingUpdateRequired)
		{
			updateAmbientLighting();
			lightingUpdateRequired = false;
		}
		
		Enumeration<String> keys = chunks.keys();
        while (keys.hasMoreElements()) 
        {
            Object key = keys.nextElement();
            String str = (String) key;
            Chunk chunk = chunks.get(str);
                        
            //If the chunk has been flagged for an ambient lighting update, update the lighting
            if(chunk.isFlaggedForLightingUpdate())
            {
            	utils.applyAmbientChunk(this, chunk);
            	chunk.setFlaggedForLightingUpdate(false);
            }
                    
            //If the light in the chunk has changed, update the light[][] used for rendering
            if(!chunk.isLightUpdated())
            {
            	chunk.updateChunkLight();
            	chunk.setLightUpdated(true);
            }
        }
	}
	
	public void initSoundEngine(SoundEngine engine){
		if (soundEngine == null){
			this.soundEngine = engine;
		}
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
}