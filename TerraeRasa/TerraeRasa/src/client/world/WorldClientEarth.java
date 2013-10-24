package client.world;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import math.MathHelper;
import transmission.ClientUpdate;
import transmission.TransmittablePlayer;
import transmission.WorldData;
import utils.Position;
import world.Biome;
import blocks.Block;
import blocks.BlockChest;
import blocks.BlockGrass;
import blocks.BlockPillar;
import blocks.ChunkClient;
import blocks.ClientMinimalBlock;
import client.entities.EntityPlayer;
import client.io.ThreadedChunkExpander;
import client.utils.LightUtils;
import entities.DisplayableEntity;
import entities.IEntityTransmitBase;
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
public class WorldClientEarth extends WorldClient
{
	
	public List<DisplayableEntity> itemsList;
	public List<DisplayableEntity> enemyList;
	public List<DisplayableEntity> npcList;
	public List<DisplayableEntity> projectileList;
	

	public ThreadedChunkExpander chunkManager;
	private ConcurrentHashMap<String, ChunkClient> chunks;
	private double previousLightLevel;
	private LightUtils utils;
	private boolean lightingUpdateRequired;
	private List<String> pendingChunkClientRequests = new ArrayList<String>();
	public List<EntityPlayer> otherPlayers = new ArrayList<EntityPlayer>();
	
	public Object getEntityByID(int id)
	{
		try {
			return entitiesByID.get(""+id);
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public void overwriteEntityByID(int id, Object newEntity)
	{
		IEntityTransmitBase entity = (IEntityTransmitBase) entitiesByID.get(""+id);
		if(entity.getEntityType() == DisplayableEntity.TYPE_ITEMSTACK)
		{
			itemsList.remove(entity);
			itemsList.add((DisplayableEntity) newEntity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_PROJECTILE)
		{
			projectileList.remove(entity);
			projectileList.add((DisplayableEntity) newEntity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_ENEMY)
		{
			enemyList.remove(entity);
			enemyList.add((DisplayableEntity) newEntity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_FRIENDLY) //Friendly?
		{
			npcList.remove(entity);
			npcList.add((DisplayableEntity) newEntity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_PLAYER)
		{
			otherPlayers.remove(entity);
			otherPlayers.add((EntityPlayer) newEntity);
		}
		entitiesByID.put(""+id, newEntity);		
	}
	
	public void removeEntityByID(int id)
	{
		IEntityTransmitBase entity = (IEntityTransmitBase) entitiesByID.get(""+id);
		if(entity.getEntityType() == DisplayableEntity.TYPE_ITEMSTACK)
		{
			itemsList.remove(entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_PROJECTILE)
		{
			projectileList.remove(entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_ENEMY)
		{
			enemyList.remove(entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_FRIENDLY) //Friendly?
		{
			npcList.remove(entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_PLAYER)
		{
			otherPlayers.remove(entity);
		}
		entitiesByID.remove(""+id);
	}
	
	public WorldClientEarth(WorldData data, ChunkClient[] chunks)
	{
		super(data.worldName);
		this.chunks = new ConcurrentHashMap<String, ChunkClient>(10);
		chunksLoaded = new Hashtable<String, Boolean>(25);	
		for(ChunkClient chunk : chunks)
		{
			this.chunks.put(""+chunk.getX(), chunk);
			this.chunksLoaded.put(""+chunk.getX(), true);
		}
		
		this.width = data.width;
		this.height = data.height; 
		this.difficulty = data.difficulty;
		
		
		this.enemyList = new ArrayList<DisplayableEntity>();
		for(DisplayableEntity enemy : data.enemyList)
		{
//			enemy.setTexture(EntityNPCEnemy.enemyList[enemy.getNPCID()].getTexture());
			addEntityToEnemyList(enemy);
		}
		this.npcList = new ArrayList<DisplayableEntity>();
		for(DisplayableEntity friendly : data.npcList)
		{
//			friendly.setTexture(EntityNPCFriendly.npcList[friendly.getNPCID()].getTexture());
			addEntityToNPCList(friendly);
		}		
		this.itemsList = data.itemsList;
		this.projectileList = data.projectileList;
		
		this.worldName = data.worldName;
		this.setWorldTime(data.worldTime);
		this.previousLightLevel = data.previousLightLevel;
		this.chunkWidth = data.chunkWidth;
		this.lightingUpdateRequired = data.lightingUpdateRequired;
		this.generatedHeightMap = data.generatedHeightMap;
		this.averageSkyHeight = data.averageSkyHeight;
		
		for(TransmittablePlayer player : data.otherplayers)
		{
			addPlayer(player);
		}		
		utils = new LightUtils();		
	}
			
		
	/**
	 * Keeps track of the worldtime and updates the light if needed
	 */
	public void updateWorldTime(EntityPlayer player)
	{		
		setWorldTime(getWorldTime() + 1);
		if(getWorldTime() >= GAMETICKSPERDAY)//If the time exceeds 24:00, reset it to 0:00
		{
			setWorldTime(0);
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
		double time = (double)(getWorldTime()) / GAMETICKSPERHOUR; 
		double lightLevel = (time > 8 && time < 16) ? 1.0f : (time >= 4 && time <= 8) ? MathHelper.roundDowndouble20th(((((time - 4) / 4.0F) * 0.8F) + 0.2F)) : (time >= 16 && time <= 20) ? MathHelper.roundDowndouble20th(1.0F - ((((time - 16) / 4.0F) * 0.8F) + 0.2F)) : 0.2f;
		return (lightLevel <= 0.2) ? 0.2 : (lightLevel >= 1) ? 1 : lightLevel;
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
	
	public void addUnspecifiedEntity(IEntityTransmitBase entity)
	{
		if(getEntityByID(entity.getEntityID()) != null)
		{
			return;
		}
		if(entity.getEntityType() == DisplayableEntity.TYPE_ENEMY)
		{
			enemyList.add((DisplayableEntity) entity);
			entitiesByID.put(""+entity.getEntityID(), entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_FRIENDLY)
		{
			npcList.add((DisplayableEntity) entity);
			entitiesByID.put(""+entity.getEntityID(), entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_ITEMSTACK)
		{
			itemsList.add((DisplayableEntity) entity);
			entitiesByID.put(""+entity.getEntityID(), entity);
		}
		else if(entity.getEntityType() == DisplayableEntity.TYPE_PROJECTILE)
		{
			projectileList.add((DisplayableEntity) entity);
			entitiesByID.put(""+entity.getEntityID(), entity);
		}
		
	}
	
	/**
	 * Adds an EntityNPCEnemy to the enemyList in this instance of World
	 * @param enemy the enemy to add to enemyList
	 */
	public void addEntityToEnemyList(DisplayableEntity enemy)
	{
		enemyList.add(enemy);
		entitiesByID.put(""+enemy.entityID, enemy);
	}
	
	/**
	 * Adds an EntityNPC to the npcList in this instance of World
	 * @param npc the npc to add to enemyList
	 */
	public void addEntityToNPCList(DisplayableEntity npc)
	{
		npcList.add(npc);
		entitiesByID.put(""+npc.entityID, npc);
	}
	
	/**
	 * Adds an entityProjectile to the projectileList in this instance of World
	 * @param projectile the projectile to add to projectileList
	 */
	public void addEntityToProjectileList(DisplayableEntity projectile)
	{
		projectileList.add(projectile);
		entitiesByID.put(""+projectile.entityID, projectile);
	}

	/**
	 * Adds an EntityLivingItemStack to the itemsList in this instance of World
	 * @param stack the EntityLivingItemStack to add to itemsList
	 */
	public void addItemStackToItemList(DisplayableEntity stack)
	{
		itemsList.add(stack);
		entitiesByID.put(""+stack.entityID, stack);
	}
	
	public void addPlayer(TransmittablePlayer transmittablePlayer)
	{
		for(EntityPlayer p : otherPlayers)
		{
			if(transmittablePlayer.entityID == p.entityID)
			{
				return;
			}
		}
		EntityPlayer player = new EntityPlayer(transmittablePlayer);
		otherPlayers.add(player);
		entitiesByID.put(""+player.entityID, player);
	}
	
	/**
	 * Calls all the methods to update the world and its inhabitants
	 */
	public void onClientWorldTick(ClientUpdate update, EntityPlayer player)
	{		
		updateTemporaryText();
		updateWeather();
		//Update the time
		updateWorldTime(player);
		if(getWorldTime() % 20 == 0) {
			updateChunkClients(update, player);
		}

		player.onClientTick(update, this); 
		
		applyLightingUpdates(player);
	}
		
	/**
	 * Method designed to handle the updating of all blocks
	 * @param x - x location in the world
	 * @param y - location in the world
	 * @return - updated bitmap
	 */	
	public int updateBlockBitMap(int x, int y){
		int bit = getBlock(x,y).getBitMap();
		//If the block is standard
		if (getAssociatedBlock(x, y).getTileMap() == 'g'){
			return updateGeneralBitMap(x, y);
		}
		//if the block requires special actions
		//If the block is a pillar
		else if (getAssociatedBlock(x, y).getTileMap() == 'p'){
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
		if (getBlock(x, y - 1).isSolid){
			bit += 1;
		}
		if (getBlock(x, y + 1).isSolid){
			bit += 4;
		}
		if (getBlock(x - 1, y).isSolid){
			bit += 8;
		}
		if (getBlock(x + 1, y).isSolid){
			bit += 2;
		}
		if (getAssociatedBlock(x, y) instanceof BlockGrass && (bit == 15 || bit == 11 || bit == 27 || bit == 31)){
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
		if (getAssociatedBlock(x, y + 1) instanceof BlockPillar){
			bit = 1;
		}
		
		else {
			bit = 2;
		}
		
		if (!getAssociatedBlock(x, y - 1).getIsOveridable() && !(getAssociatedBlock(x, y - 1) instanceof BlockPillar)){
			bit = 0;					
		}
		
		if (!getAssociatedBlock(x, y - 1).getIsOveridable() &&
				!getAssociatedBlock(x, y + 1).getIsOveridable() && 
				!(getAssociatedBlock(x, y + 1) instanceof BlockPillar) && 
				!(getAssociatedBlock(x, y - 1) instanceof BlockPillar)){
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
	 * Tries to make weather happen on each game tick
	 */
	private void updateWeather()
	{
		Enumeration<String> keys = chunks.keys();
        while (keys.hasMoreElements()) 
        {
            ChunkClient chunk = (ChunkClient)chunks.get((String)(keys.nextElement()));
            if(chunk.weather != null)
            {
	            chunk.weather.update(this, null);
            }
        }
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
		try {
			return Block.blocksList[getChunkClients().get(""+(x / ChunkClient.getChunkWidth())).getBackWall(x % ChunkClient.getChunkWidth(), (y)).id];
		} catch(Exception e) {
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
	public ClientMinimalBlock getBlock(int x, int y)
	{
		try {
			return getChunkClients().get(""+(x / ChunkClient.getChunkWidth())).getBlock(x % ChunkClient.getChunkWidth(), (y));
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Sets the back wall at the specified (x,y). Useful for easily setting a back wall at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur.
	 * @param ClientMinimalBlock the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBackBlock(ClientMinimalBlock ClientMinimalBlock, int x, int y)
	{
		try {
			getChunkClients().get(""+x / ChunkClient.getChunkWidth()).setBackWall(ClientMinimalBlock, x % ChunkClient.getChunkWidth(), y);
		} catch(Exception e) {
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
	public void setBlock(ClientMinimalBlock block, int x, int y)
	{
		try {
			getChunkClients().get(""+x / ChunkClient.getChunkWidth()).setBlock(block, x % ChunkClient.getChunkWidth(), y);
		} catch(Exception e) {
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
	public ClientMinimalBlock getBlock(double x, double y)
	{
		return getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getBlock((int)x % ChunkClient.getChunkWidth(), (int)y);
	}
	
	/**
	 * Get associated block has two different behaviours. If a BlockChest is at the requested position, then a new and 
	 * reference safe BlockChest will be given. If the Block is not an instanceof BlockChest then a direct reference to 
	 * that Block.blockList block will be given. This has no metadata and is final. To modify the metadata, use getBlock()
	 * to request the ClientMinimalBlock for the given (x,y) position. <i> This version of the method getAssociatedBlock accepts 
	 * integers not doubles.</i>
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified, or null if there isnt one.
	 */
	public Block getAssociatedBlock(int x, int y)
	{
		try {
			ClientMinimalBlock block = getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getBlock((int)x % ChunkClient.getChunkWidth(), (int)y);
			if(Block.blocksList[block.id] instanceof BlockChest)
			{
				return new BlockChest((BlockChest)(Block.blocksList[block.id])).mergeOnto(block);
			}
			else
			{
				return Block.blocksList[block.id];
			}
		} catch (Exception e) {
		}
		return Block.air;
	}

	/**
	 * Get associated block has two different behaviours. If a BlockChest is at the requested position, then a new and 
	 * reference safe BlockChest will be given. If the Block is not an instanceof BlockChest then a direct reference to 
	 * that Block.blockList block will be given. This has no metadata and is final. To modify the metadata, use getBlock()
	 * to request the ClientMinimalBlock for the given (x,y) position. <i> This version of the method getAssociatedBlock accepts 
	 * doubles not integers.</i>
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified, or null if there isnt one.
	 */
	public Block getAssociatedBlock(double x, double y)
	{
		try {
			ClientMinimalBlock block = getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getBlock((int)x % ChunkClient.getChunkWidth(), (int)y);
			if(Block.blocksList[block.id] instanceof BlockChest)
			{
				return new BlockChest((BlockChest)(Block.blocksList[block.id])).mergeOnto(block);
			}
			else
			{
				return Block.blocksList[block.id];
			}
		} catch (Exception e) { 
		}
		return Block.air;
	}
		
	/**
	 * Sets the block at the specified (x,y). Useful for easily setting a block at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur. This version of the method uses doubles.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBlock(ClientMinimalBlock block, double x, double y)
	{
		try {
			getChunkClients().get(""+x).setBlock(block, (int)x, (int)y);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the chunk based off the block position, so division must occur.
	 * @param x the x position of the chunk (in Blocks)
	 * @param y the y position of the chunk (in Blocks)
	 * @return the chunk at the specified position in the world's chunk map, or null if it doesn't exist or isn't loaded
	 */
	public ChunkClient getChunkClient_Division(int x)
	{
		return getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth()));
	}
	
	/**
	 * Gets the chunk based off the chunk-map coordinates. Division is not performed.
	 * @param x the x position of the chunk 
	 * @param y the y position of the chunk 
	 * @return the chunk at the specified position in the world's chunk map, or null if it doesn't exist or isn't loaded
	 */
	public ChunkClient getChunk(int x)
	{
		return (getChunkClients().get(""+x) != null) ? getChunkClients().get(""+x) : new ChunkClient(Biome.forest, x, height);
	}
	
	/**
	 * Add a new ChunkClient to the World's ChunkClient map. Usage of this method is advisable so that the game actually knows the chunk 
	 * exists in memory.
	 * @param chunk the chunk to add to the chunk map of the world
	 * @param x the x position of the chunk
	 * @param y the y position of the chunk
	 */
	public void registerChunkClient(ChunkClient chunk, int x)
	{
		chunksLoaded.put(""+x, true);
		chunks.put(""+x, chunk);
	}
	
	public void removePendingChunkClientRequest(String command)
	{
		pendingChunkClientRequests.remove(command);
	}
	
	/**
	 * Sets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally,
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the world, 
	 * so it MUST be safe, otherwise code will become very dangerous. ChunkClients are generated if there is non chunk present at that
	 * position.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBackWallGenerate(ClientMinimalBlock block, int x, int y)
	{
		//Ensure the chunk exists
		try { 
			if(getChunkClients().get(""+(x / ChunkClient.getChunkWidth())) == null)
			{
				registerChunkClient(new ChunkClient(Biome.forest, (int)(x / ChunkClient.getChunkWidth()), height), (int)(x / ChunkClient.getChunkWidth()));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//Set the block
		try { 
			chunks.get(""+(x / ChunkClient.getChunkWidth())).setBackWall(block, x % ChunkClient.getChunkWidth(), y);
		} catch(Exception e) {
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
	public ClientMinimalBlock getBackWall(int x, int y)
	{
		try {
			return getChunkClients().get(""+x / ChunkClient.getChunkWidth()).getBackWall(x % ChunkClient.getChunkWidth(), y);
		} catch (Exception e) {
		}
		return null;
	}
		
	/**
	 * Sets the block at the specified (x,y). This method is safe, as all Exceptions are handled in this method. Additionally,
	 * the (modular) division is performed automatically. The primary intention is that this method is used to generate the world, 
	 * so it MUST be safe, otherwise code will become very dangerous. ChunkClients are generated if there is non chunk present at that
	 * position.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public void setBlockGenerate(ClientMinimalBlock block, int x, int y)
	{
		//Ensure the chunk exists
		try { 
			if(getChunkClients().get(""+(x / ChunkClient.getChunkWidth())) == null)
			{
				registerChunkClient(new ChunkClient(Biome.forest, (int)(x / ChunkClient.getChunkWidth()), height), (int)(x / ChunkClient.getChunkWidth()));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//Set the block
		try { 
			chunks.get(""+(x / ChunkClient.getChunkWidth())).setBlock(block, x % ChunkClient.getChunkWidth(), y);
		} catch(Exception e) {
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
		try {
			return getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getLight((int)x % ChunkClient.getChunkWidth(), (int)y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1.0f;
	}
	
	/**
	 * Checks for chunks that need to be loaded or unloaded, based on the player's screen size. The range in which chunks stay loaded increases if the player's 
	 * screen size is larger. (It's about ((width/2.2), (height/2.2))). 
	 */
	private void updateChunkClients(ClientUpdate update, EntityPlayer player)
	{
		//How far to check for chunks (in blocks)
		int loadDistance = 2 * ChunkClient.getChunkWidth();//(((int)(Display.getWidth() / 2.2) + 3) > ChunkClient.getChunkClientWidth()) ? ((int)(Display.getWidth() / 2.2) + 3) : ChunkClient.getChunkClientWidth();
		int unloadDistance = 3 * ChunkClient.getChunkWidth();
		
		//Position to check from
		final int x = (int) (player.x / 6);
		//Where to check, in the chunk map (based off loadDistance variables)
		int leftLoad = (x - loadDistance) / ChunkClient.getChunkWidth();
		int rightLoad = (x + loadDistance) / ChunkClient.getChunkWidth();
		//Bounds checking
		if(leftLoad < 0) leftLoad = 0;
		if(rightLoad > (width / ChunkClient.getChunkWidth())) rightLoad = width / ChunkClient.getChunkWidth();
		
		int leftUnload = (x - unloadDistance) / ChunkClient.getChunkWidth();
		int rightUnload= (x + unloadDistance) / ChunkClient.getChunkWidth();
		//Bounds checking
		if(leftUnload < 0) leftUnload = 0;
		if(rightUnload > (width / ChunkClient.getChunkWidth())) rightUnload = width / ChunkClient.getChunkWidth();
				
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
            	ChunkClient chunk = chunks.get(""+cx);
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
				if(!pendingChunkClientRequests.contains(command))
				{
//					System.out.println("Request: " + i);
					pendingChunkClientRequests.add(command);
					update.addCommand(command);
				}
				//chunkManager.requestChunkClient(worldName, this, chunks, i);
			}
		}
	}
		
	/**
	 * Returns true if there are one or more chunks left in the world, or false otherwise.
	 * @return true if chunks are left, otherwise false
	 */
	public boolean hasChunkClientsLeft()
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
	
	
	
	public EnumWorldDifficulty getDifficulty()
	{
		return difficulty;
	}
	
	public ConcurrentHashMap<String, ChunkClient> getChunkClients() 
	{
		return chunks;
	}

	public void setChunkClients(ConcurrentHashMap<String, ChunkClient> chunks) 
	{
		this.chunks = chunks;
	}
	
	public void setChunkClient(ChunkClient chunk, int x, int y)
	{
		getChunkClients().put(""+x, chunk);
	}
	
//	private void checkChunkClients()
//	{
//		for(int i = 0; i < width / ChunkClient.getChunkClientWidth(); i++)
//		{
//			if(getChunkClients().get(""+i) == null)
//			{
//				registerChunkClient(new ChunkClient(Biome.forest, i, height), i);
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
		int x = (Integer.parseInt(pos)) / ChunkClient.getChunkWidth();
	
		ChunkClient chunk = getChunkClients().get(""+x);
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
	public Block getAssociatedBackBlock(double x, double y)
	{
		ClientMinimalBlock block = getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getBlock((int)x % ChunkClient.getChunkWidth(), (int)y);
		return Block.blocksList[block.id];
	}
	
	public void setAmbientLight(double x, double y, double strength)
	{
		try {
			getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).setAmbientLight(strength, (int)x % ChunkClient.getChunkWidth(), (int)y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void setDiffuseLight(double x, double y, double strength)
	{
		try {
			getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).setDiffuseLight(strength, (int)x % ChunkClient.getChunkWidth(), (int)y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getAmbientLight(int x, int y)
	{
		try {
			return getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getAmbientLight((int)x % ChunkClient.getChunkWidth(), (int)y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1.0f;
	}
	
	public double getDiffuseLight(int x, int y)
	{
		try {
			return getChunkClients().get(""+(int)(x / ChunkClient.getChunkWidth())).getDiffuseLight((int)x % ChunkClient.getChunkWidth(), (int)y);
		} catch (Exception e) {
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
            ChunkClient chunk = chunks.get((String)keys.nextElement());
        	utils.applyAmbientChunk(this, chunk);
    	}
	}
	
	/**
	 * Sets the block and applies relevant lighting to the area nearby. Includes an EnumEventType to describe the event, although
	 * it is currently not used for anything.
	 * @param minimalBlock the new block to be placed at (x,y)
	 * @param x the x position where the block will be placed
	 * @param y the y position where the block will be placed
	 * @param eventType
	 */
	public void setBlock(ClientMinimalBlock minimalBlock, int x, int y, EnumEventType eventType)
	{
		utils.fixDiffuseLightRemove(this, x, y);
		setBlock(minimalBlock, x, y);
		if(minimalBlock != null);
		{
			utils.blockUpdateAmbient(this, x, y, eventType);		
			utils.fixDiffuseLightApply(this, x, y);
		}
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
            ChunkClient chunk = chunks.get(str);
                        
            //If the chunk has been flagged for an ambient lighting update, update the lighting
            if(chunk.requiresAmbientLightingUpdate())
            {
            	utils.applyAmbientChunk(this, chunk);
            	chunk.setRequiresAmbientLightingUpdate(false);
            }
            if(chunk.requiresDiffuseApplied())
            {
            	try {
	            	for(Position position : chunk.getLightSources())
	            	{
	            		Block block = Block.blocksList[getBlock(position.x, position.y).id];
	            		utils.applyLightSource(this, position.x, position.y, block.lightRadius, block.lightStrength);
	            	}
	            	chunk.setRequiresDiffuseApplied(false);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            }
                    
            //If the light in the chunk has changed, update the light[][] used for rendering
            if(!chunk.isLightUpdated())
            {
            	chunk.updateChunkLight();
            	chunk.setLightUpdated(true);
            }
        }
	}
	
	public void setBitMap(int x, int y, int bitMap)
	{
		try {
			ClientMinimalBlock block = getChunkClients().get(""+(x / ChunkClient.getChunkWidth())).getBlock(x % ChunkClient.getChunkWidth(), (y));
			block.setBitMap((byte)bitMap);
		} catch (Exception e) {
			
		}
	}
	
	public ConcurrentHashMap<String, ChunkClient> getChunks()
	{
		return chunks;
	}
}