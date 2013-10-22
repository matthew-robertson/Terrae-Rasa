package world;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import server.entities.Entity;
import server.entities.EntityPlayer;
import transmission.ServerUpdate;
import transmission.WorldData;
import utils.WorldText;
import blocks.Block;
import blocks.Chunk;
import blocks.MinimalBlock;
import entry.MPGameLoop;
import enums.EnumColor;
import enums.EnumEventType;
import enums.EnumWorldDifficulty;

public abstract class World 
{
	public static final int GAMETICKSPERDAY = 24 * MPGameLoop.TICKS_PER_SECOND * 60; 
	public static final int GAMETICKSPERHOUR = MPGameLoop.TICKS_PER_SECOND * 60;
	protected String worldName;
	protected int chunkWidth;
	protected int chunkHeight;
	protected long worldTime;
	protected int width; //Width in blocks, not pixels
	protected int height; //Height in blocks, not pixels
	public Dictionary<String, Object> entitiesByID;
	public List<WorldText> temporaryText; 
	protected int[] generatedHeightMap;
	protected int averageSkyHeight;
	public Hashtable<String, Boolean> chunksLoaded;
	protected EnumWorldDifficulty difficulty;

	
	public World(String name)
	{
		this.worldName = name;
		entitiesByID = new Hashtable<String, Object>();
		temporaryText = new ArrayList<WorldText>();
	}
	
	public final void setWorldTime(long worldTime) 
	{
		this.worldTime = worldTime;
	}
	
	public final long getWorldTime()
	{
		return worldTime;
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
	 * Gets the world object's name. worldName is a final field of world and is always the same. It 
	 * describes whether the world is 'Earth', 'Sky', etc.
	 * @return the world's name
	 */
	public final String getWorldName()
	{
		return worldName;
	}
	
	/**
	 * Gets how many vertical chunks the world has. This is equal to (height / height)
	 * @return the number of vertical chunks the world has
	 */
	public final int getChunkHeight()
	{
		return chunkHeight;
	}
	
	/**
	 * Gets how many horizontal chunks the world has. This is equal to (width / Chunk.getChunkWidth())
	 * @return the number of horizontal chunks the world has
	 */
	public final int getChunkWidth()
	{
		return chunkWidth;
	}
	
	public double getG()
	{
		return 0;
	}
	
	public abstract Biome getBiome(String pos);
	
	public abstract String getBiomeColumn(String pos);
		
	/**
	 * Gets the background block at the specified (x,y). Useful for easily getting a backgroundblock at the specified location; 
	 * Terrible for mass usage, such as in rendering. This method accepts doubles, and casts them to Integers.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public abstract Block getBackBlock(double x, double y);
			
	/**
	 * Sets the block and applies relevant lighting to the area nearby. Includes an EnumEventType to describe the event, although
	 * it is currently not used for anything.
	 * @param block the new block to be placed at (x,y)
	 * @param x the x position where the block will be placed
	 * @param y the y position where the block will be placed
	 * @param eventType
	 */
	public abstract void setBlock(Block block, int x, int y, EnumEventType eventType);

	
	/**
	 * Sets the game time based on an hour in game ticks.
	 * @param timeInTicks the time in game ticks
	 */
	public final void setTime(int timeInTicks)
	{
		this.worldTime = timeInTicks;
	}
	
	/**
	 * Gets the world time in hours, between 0 and 24
	 * @return gets the world time in hours, between 0 and 24
	 */
	public double getWorldTimeInHours()
	{
		return (double)(worldTime) / GAMETICKSPERHOUR;
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
	public final void addTemporaryText(String message, int x, int y, int ticksLeft, EnumColor color)
	{
		temporaryText.add(new WorldText(message, x, y, ticksLeft, color, true));
	}
	
	public abstract Object getEntityByID(int id);
		
	public abstract void overwriteEntityByID(Vector<EntityPlayer> players, int id, Entity newEntity);
	
	public abstract void removeEntityByID(Vector<EntityPlayer> players, int id);
	
	public abstract Block getAssociatedBlock(double x, double y);
	
	/**
	 * Gets the back wall at the specified (x,y). Useful for easily getting a back wall at the specified location; Terrible for mass usage,
	 * such as in rendering.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public abstract Block getBackBlock(int x, int y);

	/**
	 * Gets the block at the specified (x,y). Useful for easily getting a block at the specified location; Terrible for mass usage,
	 * such as in rendering.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public abstract MinimalBlock getBlock(int x, int y);
	
	/**
	 * Sets the back wall at the specified (x,y). Useful for easily setting a back wall at the specified location; Terrible for mass usage.
	 * This version of the method does not check if the chunk is actually loaded, therefore it may sometimes fail for bizarre or very, very
	 * far away requests. It will however, simply catch that Exception, should it occur.
	 * @param block the block that the specified (x,y) will be set to
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 */
	public abstract void setBackBlock(Block block, int x, int y);

	/**
	 * Gets the block at the specified (x,y). Useful for easily getting a block at the specified location; Terrible for mass usage, such as in rendering.
	 * This version of the method accepts doubles, and casts them to Integers.
	 * @param x the block's x location in the new world map
	 * @param y the block's y location in the new world map
	 * @return the block at the location specified (this should never be null)
	 */
	public abstract MinimalBlock getBlock(double x, double y);
	
	/**
	 * Gets the chunk based off the chunk-map coordinates. Division is not performed.
	 * @param x the x position of the chunk 
	 * @param y the y position of the chunk 
	 * @return the chunk at the specified position in the world's chunk map, or null if it doesn't exist or isn't loaded
	 */
	public abstract Chunk getChunk(int x);
	
	/**
	 * Add a new Chunk to the World's Chunk map. Usage of this method is advisable so that the game actually knows the chunk 
	 * exists in memory.
	 * @param chunk the chunk to add to the chunk map of the world
	 * @param x the x position of the chunk
	 * @param y the y position of the chunk
	 */
	public abstract void registerChunk(Chunk chunk, int x);
	
	public abstract Object getChunks();
	
	/**
	 * Adds an EntityNPCEnemy to the enemyList in this instance of World
	 * @param enemy the enemy to add to enemyList
	 */
	public abstract void addEntityToEnemyList(Object enemy);
	
	/**
	 * Adds an EntityNPC to the npcList in this instance of World
	 * @param npc the npc to add to enemyList
	 */
	public abstract void addEntityToNPCList(Object npc);
	
	/**
	 * Adds an entityProjectile to the projectileList in this instance of World
	 * @param projectile the projectile to add to projectileList
	 */
	public abstract void addEntityToProjectileList(Object projectile);

	/**
	 * Adds an EntityLivingItemStack to the itemsList in this instance of World
	 * @param stack the EntityLivingItemStack to add to itemsList
	 */
	public abstract void addItemStackToItemList(Object stack);


	/**
	 * Provides access to handlBackBlockBreakEvent() because that method is private and has a bizzare name, that's hard to both find and
	 * remember
	 * @param x the x position of the block to break (in the 'world map')
	 * @param y the y position of the block to break (in the 'world map')
	 */
	public abstract void breakBackBlock(ServerUpdate update, EntityPlayer player, int x, int y);
	
	/**
	 * Provides access to handlBlockBreakEvent() because that method is private and has a bizzare name, that's hard to both find and
	 * remember
	 * @param x the x position of the block to break (in the 'world map')
	 * @param y the y position of the block to break (in the 'world map')
	 */
	public abstract void breakBlock(ServerUpdate update, EntityPlayer player, int x, int y);

	/**
	 * Breaks a cactus, from bottom to top.
	 * @param mx the x position of the first block in worldMap
	 * @param my the y position of the first block in worldMap
	 */
	public abstract void breakCactus(ServerUpdate update, EntityPlayer player, int mx, int my);

	/**
	 * Cuts down a tree, if a log was broken
	 * @param mx x position in worldmap array, of the BlockWood
	 * @param my y position in the worldmap array, of the BlockWood
	 */
	public abstract void breakTree(ServerUpdate update, EntityPlayer player, int mx, int my);

	public abstract void setBlockGenerate(Block block, int x, int y);

	public abstract void setBitMap(int x, int y, int i);

	public abstract WorldData getWorldData();

	public abstract int getWorldCenterBlock();

}
