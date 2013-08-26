package entities;
import items.Item;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import transmission.ClientUpdate;
import transmission.StatUpdate;
import transmission.TransmittablePlayer;
import transmission.UpdateWithObject;
import utils.Cooldown;
import utils.CraftingManager;
import utils.DisplayableItemStack;
import utils.InventoryPlayer;
import utils.MathHelper;
import utils.Recipe;
import world.World;
import audio.SoundEngine;
import blocks.Block;
import enums.EnumPlayerDifficulty;

/**
 * <br><br>
 * EntityPlayer implements all the features needed for the user's character. 
 * <br><br>
 * There are several methods of significance in EntityPlayer, serving different purposes. 
 * The first is {@link #onWorldTick(World)}. This method should be called every single world tick,
 * and this is how the player gets updated regularly. This should apply things like regen, gravity,
 * and update cooldowns or invincibility (things based on world ticks). onWorldTick(World) can also
 * do things like check nearby blocks, to see if recipes need updated. Anything that should be 
 * done regularly, but not every frame, essentially.
 * <br><br>
 * A second major component of <code>EntityPlayer</code> is recipe updating. The Recipe[] used are based off 
 * of what craftManager decides are possible for the inventory object passed to it. 
 * <code>EntityPlayer</code> provides a method to check for nearby blocks, and update recipes as required 
 * {@link #checkForNearbyBlocks(World)}, based on the result for craftingManager. 
 * <br><br>
 * The third major component of <code>EntityPlayer</code> is armour management. This is done almost 
 * automatically through a single method - {@link #onArmorChange()}. This method recalculates the armor
 * bonuses of the armour, as well as the set bonuses. It then proceeds to cancel the previous ones. 
 * <b>NOTE: This method does dangerous things, and more than likely is going to terribly ruin any attempt 
 * at adding a +defense potion, or -defense debuff. It WILL have to be recoded, with little doubt.</b>
 * <br><br>
 * The final major component of <code>EntityPlayer</code> is block breaking, through the method 
 * {@link #breakBlock(World, int, int, Item)}. This method is what handles mining, and any 
 * gradual block breaking done by the player. A block break is automatically send to the 'world map' upon
 * completion of the mining.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class EntityPlayer extends EntityLiving
		implements IEntityTransmitBase
{
	private static final long serialVersionUID = 1L;
	private boolean isSwingingRight;
	private boolean hasSwungTool;
	private double rotateAngle;
	private boolean isMining;
	private CraftingManager craftingManager;
	private Recipe[] allPossibleRecipes;
	private final String playerName;
	private boolean inventoryChanged;
	private final int MAX_BLOCK_PLACE_DISTANCE = 42;
	private Hashtable<String, Cooldown> cooldowns;
	private Dictionary<String, Boolean> nearBlock;
	private Dictionary<String, Recipe[]> possibleRecipesByBlock;
	public double specialEnergy;
	public double maxSpecialEnergy;
	public int viewedChestX;
	public int viewedChestY;
	public boolean isViewingChest;	
	public double respawnXPos;
	public double respawnYPos;	
	public int selectedRecipe;
	public int selectedSlot;
	public boolean isFacingRight;
	public boolean isInventoryOpen;	
	public InventoryPlayer inventory;
	public Vector<String> changedInventorySlots = new Vector<String>();
	/** A flag indicating if the player has been forever defeated. If they have, they will not be saved to disk.*/
	public boolean defeated;
	public DisplayableItemStack heldMouseItem; 
	
	/**
	 * Constructs a new instance of EntityPlayer with default settings, inventory (includes 3 basic tools),
	 * and the specified name/difficulty. The default reset position is set to (50, 0), but this
	 * value is essentially worthless, and should be reset.
	 * @param name the player's name
	 * @param difficulty the difficulty setting the player has selected (EnumDifficulty)
	 */
	public EntityPlayer(String name, EnumPlayerDifficulty difficulty)
	{
		super();		
		specialEnergy = 100;
		isMining = false;
		width = 12;
		height = 18;
		maxSpecialEnergy = 100;
		blockHeight = 3;
		blockWidth = 2;
		setRespawnPosition(50, 0);		
		inventory = new InventoryPlayer();
		inventory.pickUpItemStack(null, this, new DisplayableItemStack(Item.copperSword));
		inventory.pickUpItemStack(null, this, new DisplayableItemStack(Item.copperPickaxe));
		inventory.pickUpItemStack(null, this, new DisplayableItemStack(Item.copperAxe));
		inventory.pickUpItemStack(null, this, new DisplayableItemStack(Block.craftingTable));
		playerName = name;
		rotateAngle = -120.0f;		
		health = 100;
		maxHealth = 100;
		maxMana = 0;
		invincibilityTicks = 10;
		selectedSlot = 0;
		viewedChestX = 0;
		viewedChestY = 0;
		defeated = false;
		craftingManager = new CraftingManager();
		inventoryChanged = true;	
		selectedRecipe = 0;
		cooldowns = new Hashtable<String, Cooldown>();
		nearBlock = new Hashtable<String, Boolean>();
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			nearBlock.put(block.getName(), false);
		}
		nearBlock.put(Block.none.getName(), true);
		possibleRecipesByBlock = new Hashtable<String, Recipe[]>();
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			possibleRecipesByBlock.put(block.getName(), new Recipe[] { });
		}
		
		jumpSound = "Player Jump";
		deathSound = "Player Death";
	}
	
	public EntityPlayer(TransmittablePlayer player) 
	{
		rotateAngle = -120.0f;			
		invincibilityTicks = 10;
		selectedSlot = 0;
		viewedChestX = 0;
		viewedChestY = 0;
		isViewingChest = false;
		isMining = false;
		craftingManager = new CraftingManager();
		inventoryChanged = true;	
		selectedRecipe = 0;
		nearBlock = new Hashtable<String, Boolean>();
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			nearBlock.put(block.getName(), false);
		}
		nearBlock.put(Block.none.getName(), true);
		possibleRecipesByBlock = new Hashtable<String, Recipe[]>();
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			possibleRecipesByBlock.put(block.getName(), new Recipe[] { });
		}

		this.entityID = player.entityID;
		this.x = player.x;
		this.y = player.y;
		this.playerName = player.playerName;
		this.mana = player.mana;
		this.health = player.health;
		this.specialEnergy = player.specialEnergy;
		this.maxSpecialEnergy = player.maxSpecialEnergy;
		this.maxHealth = player.maxHealth;
		this.maxMana = player.maxMana;
		this.statusEffects = player.statusEffects;
		this.cooldowns = player.cooldowns;
		this.inventory = new InventoryPlayer(player.mainInventory, player.armorInventory, player.quiver);
		this.heldMouseItem = player.heldMouseItem;
		
	}
	
	public void onClientTick(ClientUpdate update, World world)
	{
		checkForNearbyBlocks(world);	
		verifyChestRange();

//		String[] changes = new String[changedInventorySlots.size()];
//		changedInventorySlots.copyInto(changes);
//		changedInventorySlots.clear();
//		
//		for(String change : changes)
//		{
//			UpdateWithObject objUpdate = new UpdateWithObject();
//			
//			String[] split = change.split(" ");
//			if(split[0].equals("a"))
//			{
//				objUpdate.command = "/player " + entityID + " armorreplace " + split[1];
//				objUpdate.object = inventory.getArmorInventoryStack(Integer.parseInt(split[1]));
//			}
//			else if(split[0].equals("q"))
//			{
//				objUpdate.command = "/player " + entityID + " quiverreplace " + split[1];
//				objUpdate.object = inventory.getQuiverStack(Integer.parseInt(split[1]));
//			}
//			else if(split[0].equals("i") || split[0].equals("m"))
//			{
//				objUpdate.command = "/player " + entityID + " inventoryreplace " + split[1];
//				objUpdate.object = inventory.getMainInventoryStack(Integer.parseInt(split[1]));
//			}	
//			update.addObjectUpdate(objUpdate);
//			
//		}
	}
	
	public synchronized void clearChangedSlots()
	{
		changedInventorySlots.clear();
	}
		
	/**
	 * Checks if the player is still near their selected chest (should there be one selected). If the player is not near that
	 * chest, it is cleared and no longer rendered.
	 */
	private void verifyChestRange() 
	{
		if(isViewingChest)
		{
			if(MathHelper.distanceBetweenTwoPoints(viewedChestX * 6 + 6, viewedChestY * 6 + 6, x + (blockWidth / 2), y + (blockHeight / 2)) >= 48)
			{
				clearViewedChest();
			}			
		}
	}

	/**
	 * Gets the player's name, assigned upon player creation
	 * @return the player's name
	 */
	public final String getName()
	{
		return playerName;
	}
	
	/**
	 * Get every recipe the player is able to craft, based on what they're standing by and what's in 
	 * their inventory
	 * @return the allPossibleRecipes[], indicating possible craftable recipes
	 */
	public final Recipe[] getAllPossibleRecipes()
	{
		return allPossibleRecipes;
	}
	
	public void setIsMining(boolean b){
		isMining = b;
	}
	
	/**
	 * Sets the respawn position of the player (this may be broken between world sizes)
	 * @param x the x position in ortho units
	 * @param y the y position in ortho units
	 */
	public void setRespawnPosition(int x, int y) 
	{
		respawnXPos = x;		
		if(x % 6 != 0)
		{
			x = (int)(x / 6);
		}		
		respawnYPos = y;
	}
	
	/**
	 * Overrides EntityLiving onDeath() to provide special things like hardcore (mode) and itemdrops
	 */
	public void onDeath(World world)
	{	
		//If the player is still dead, then trigger the respawn code
		if(health <= 0)
		{
			SoundEngine.playSoundEffect(deathSound);
		}
	}
	
	/**
	 * Recreates the allPossibleRecipes[] based on what the player is standing by.
	 */
	private void setAllRecipeArray()
	{
		//'int size' indicates how many recipes are able to be created, in total
		//InventoryRecipes are always possible, add their total no matter what
		int size = possibleRecipesByBlock.get(Block.none.getName()).length;
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			if(nearBlock.get(block.getName()))
			{
				size += possibleRecipesByBlock.get(block.getName()).length;
			}
		}
		Recipe[] recipes = new Recipe[size]; //create a temperary Recipe[] to store stuff in.S
		
		int i = 0; //used for standard looping. declared here so its value can be known after the loop
		int k = 0; //the index in recipes[] to begin saving recipes
		
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			Recipe[] possibleRecipes = possibleRecipesByBlock.get(block.getName());
			if(nearBlock.get(block.getName()))
			{
				for(i = 0; i < possibleRecipes.length; i++)
				{
					recipes[k + i] = possibleRecipes[i];
				}
				k += i;				
			}
		}
		//Inventory Default recipes
		Recipe[] possibleRecipes = possibleRecipesByBlock.get(Block.none.getName());
		if(nearBlock.get(Block.none.getName()))
		{
			for(i = 0; i < possibleRecipes.length; i++)
			{
				recipes[k + i] = possibleRecipes[i];
			}
			k += i;				
		}
		
		allPossibleRecipes = recipes; //set the possible recipes to the temperary Recipe[]
		if(selectedRecipe >= allPossibleRecipes.length) //Fix the selectedRecipe Integer so that the crafting scroller doesnt go out of bounds
		{
			selectedRecipe = (allPossibleRecipes.length > 0) ? allPossibleRecipes.length - 1 : 0;
		}
	}
	
	/**
	 * Serves for detecting what can be crafting, currently
	 */
	private void checkForNearbyBlocks(World world) 
	{
		Dictionary<String, Boolean> nearby = new Hashtable<String, Boolean>();
		boolean recalculateRecipes = false;
		final int detectionRadius = 2; //blocks away a player can detect a crafting_table/furnace/etc
		
		//Check if the blocks in CraftingManager.getCraftingBlocks() are nearby
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			nearby.put(block.getName(), 
					   blockInBounds(world, -detectionRadius, detectionRadius, -detectionRadius, detectionRadius, block)); 
			if(nearBlock.get(block.getName()) != nearby.get(block.getName())) //and they weren't just near one
			{ 
				recalculateRecipes = true; //recipes need recalculated
			}
		}
		nearBlock.put(Block.none.getName(), true);
			
		//if the recipes need recalculated or the inventory has changed then recalculate recipes.
		if(recalculateRecipes || inventoryChanged) 
		{	
			for(Block block : CraftingManager.getCraftingBlocks())
			{
				if(inventoryChanged || nearBlock.get(block.getName()) != nearby.get(block.getName()))
				{
					nearBlock.put(block.getName(), nearby.get(block.getName()));
					updateRecipes(block);
				}					
			}
		
			updateRecipes(Block.none); //update inventory recipes
			setAllRecipeArray(); //set the crafting recipe array to the new recipes
			inventoryChanged = false;
		}
	}
	
	/**
	 * Emergency or init function to brute force calculate all recipes
	 */
	public void updateAllPossibleRecipes()
	{
		updateAllRecipes();
		setAllRecipeArray();
	}
	
	/**
	 * Updates the entire possibleRecipesByBlock to be accurate, after an inventoryChange generally
	 */
	private void updateAllRecipes()
	{
		for(Block block : CraftingManager.getCraftingBlocks())
		{
			possibleRecipesByBlock.put(block.getName(), craftingManager.getPossibleRecipesByBlock(inventory, block));		
		}
	}
	
	/**
	 * Updates one crafting block in the possibleRecipesByBlock Dictionary
	 * @param block the block in possibleRecipesByBlock to update
	 */
	private void updateRecipes(Block block)
	{
		possibleRecipesByBlock.put(block.getName(), craftingManager.getPossibleRecipesByBlock(inventory, block));		
	}
	
	/**
	 * Gets whether or not the player is currently mining a block
	 * @return whether or not the player is currently mining
	 */
	public boolean getIsMining(){
		return isMining;
	}
	
	/**
	 * Gets the maximum distance the player can place a block, measured in ortho units. this field is final
	 * @return MAX_BLOCK_PLACE_DISTANCE field
	 */
	public final int getMaximumBlockPlaceDistance()
	{
		return MAX_BLOCK_PLACE_DISTANCE;
	}
	
	/**
	 * Sets the position of the viewed chest (from 'world map'), and changes isViewingChest to true.
	 * @param x the x position of the chest, in the 'world map'
	 * @param y the y position of the chest, in the 'world map'
	 */
	public void setViewedChest(int x, int y)
	{
		isViewingChest = true;
		viewedChestX = x;
		viewedChestY = y;
	}
	
	/**
	 * Clears the currently viewed chest position to 0,0, and sets isViewingChest to false
	 */
	public void clearViewedChest()
	{
		isViewingChest = false;
		viewedChestX = -1;
		viewedChestY = -1;
	}
			
	/**
	 * Gets the Block the player is holding, if it's a Block and it's an instanceof BlockLight. Used for handheld lighting.
	 * @return an instanceof BlockLight if the player is holding one, otherwise null
	 */
	public Block getHandheldLight()
	{
		DisplayableItemStack stack = inventory.getMainInventoryStack(selectedSlot);
		if(stack != null)
		{
			if(stack.getItemID() < Item.itemIndex && (Block.blocksList[stack.getItemID()]).lightStrength > 0)
			{
				return Block.blocksList[stack.getItemID()];
			}
		}
		return null;
	}
		
	/**
	 * Puts an ID on cooldown, which generally relates to an item or something else. If that ID is already on cooldown and
	 * with a longer time, this will fail and the current cooldown will continue, otherwise the new cooldown will be implemented.
	 * @param id the id to put on cooldown
	 * @param duration the duration of the cooldown in game ticks
	 */
	public void putOnCooldown(int id, int duration)
	{
		if(cooldowns.get("" + id) != null)
		{
			Cooldown current = cooldowns.get("" + id);
			if(current.ticksLeft < duration)
			{
				cooldowns.put("" + id, new Cooldown(id, duration));
			}		
			return;	
		}
		cooldowns.put("" + id, new Cooldown(id, duration));
	}
	
	/**
	 * Gets whether or not an ID is on cooldown. This does not verify that ID actually exists.
	 * @param id the id that should be checked for a cooldown
	 * @return if the ID is on cooldown (true if it is, false otherwise)
	 */
	public boolean isOnCooldown(int id)
	{
		return cooldowns.get("" + id) != null;
	}
	
	/**
	 * Gets the time remaining on an ID's cooldown. This does not verify that ID actually exists.
	 * @param id the id that should be queried for a cooldown remaining
	 * @return the time left on the specified ID's cooldown, in ticks
	 */
	public int getTicksLeftOnCooldown(int id)
	{
		return cooldowns.get("" + id).ticksLeft;
	}
	
	/**
	 * Restores special energy, upto the cap
	 * @param restore the amount restored
	 * @return whether or not any special energy was restored
	 */
	public boolean restoreSpecialEnergy(int restore)
	{
		if(specialEnergy == maxSpecialEnergy)
		{
			return false;
		}
		
		specialEnergy += restore;
		if(specialEnergy > maxSpecialEnergy)
		{
			specialEnergy = maxSpecialEnergy;
		}
		return true;
	}
	
	/**
	 * Spends the given amount of special energy - even if the player doesnt have it.
	 * @param amount the amount of energy to spend
	 */
	public void spendSpecialEnergy(int amount)
	{
		specialEnergy -= amount;
	}
	
	public double getToolRotationAngle()
	{
		return rotateAngle;
	}
	
	public boolean isSwingingTool()
	{
		return hasSwungTool;
	}
	
	public boolean getIsSwingingRight()
	{
		return isSwingingRight;
	}
	
	/**
	 * Adds the given radian value to the tool rotation angle. If this is negative, it will
	 * be subtracted.
	 * @param addition the amount to add to the tool rotation angle, in radians
	 */
	public void updateRotationAngle(double addition)
	{
		rotateAngle += addition;
	}
	
	/**
	 * Stops the swinging of the player weapon
	 */
	public void clearSwing()
	{
		hasSwungTool = false;
	}

	/**
	 * Gets the player's health percentage, from 0.0-1.0, where 0.0 indicates 0% and 1.0 indicates 100%
	 * @return the player's health percent as a double between 0.0 and 1.0
	 */
	public double getHealthPercent() 
	{
		return health / maxHealth;
	}
	
	/**
	 * Gets the player's mana percentage, from 0.0-1.0, where 0.0 indicates 0% and 1.0 indicates 100%.
	 * If the player has no mana, this will yield a value of 0.0
	 * @return the player's mana percent as a double between 0.0 and 1.0
	 */
	public double getManaPercent() 
	{
		return (maxMana == 0) ? 0 : mana / maxMana;
	}
	
	/**
	 * Removes a status effect by its unique ID - a long only that effect has.
	 * @param id a long that is unique to that status effect
	 */
	public void removeStatusEffectByID(long id)
	{
		for(int i = 0; i < statusEffects.size(); i++)
		{
			if(id == statusEffects.get(i).getID())
			{
				statusEffects.remove(i);
			}
		}
	}
	
	
	public void setStats(StatUpdate newStats)
	{
		this.defense = newStats.defense;
		this.mana = newStats.mana;
		this.maxMana = (int) newStats.maxMana;
		this.health = newStats.health;
		this.maxHealth = (int) newStats.maxHealth;
		this.specialEnergy = newStats.specialEnergy;
		this.maxSpecialEnergy = newStats.maxSpecialEnergy;
		this.isSwingingRight = newStats.isSwingingRight;
		this.hasSwungTool = newStats.hasSwungTool;
		this.rotateAngle = newStats.rotateAngle;
		this.statusEffects = newStats.statusEffects;
		this.cooldowns = newStats.cooldowns;
		this.defeated = newStats.defeated;
	}

	public void setSwingAngle(double angle) 
	{
		hasSwungTool = true;
		this.rotateAngle = angle;
	}

	@Override
	public int getEntityID() {
		return entityID;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getEntityType() {
		return DisplayableEntity.TYPE_PLAYER;
	}
	
	public DisplayableItemStack getHeldItem()
	{
		return heldMouseItem;
	}
}