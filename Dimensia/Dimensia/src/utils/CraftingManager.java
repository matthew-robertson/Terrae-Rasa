package utils;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import blocks.Block;

/**
 * CraftingManager holds organized Recipe data and Blocks which are registered as 'crafting blocks'.
 * When created, an instance of CraftingManager will compile all the Recipes for each crafting block. A crafting block
 * is a block that is required to craft a recipe, or Block.none if no block is required. All crafting blocks should
 * be registered in the craftingBlocks[] alongside blocks like Block.craftingTable and Block.furnace.
 * 
 * <br><br>
 * 
 * An array of blocks which are considered as 'crafting blocks' can be obtained by calling {@link #getCraftingBlocks()}. A
 * complete array of all the recipes a player can currently craft (IE has materials for) can be obtained by 
 * calling {@link #getAllRecipesByBlockType(Block)}.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class CraftingManager 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final Dictionary<String, Recipe[]> recipesByBlock = new Hashtable<String, Recipe[]>();
	/** A list of all Blocks that can be considered a prerequisite to crafting something. For example, a Crafting Table. 
	 	Block.none is not included here because the recipes are always available, independant to blocks */
	private static final Block[] craftingBlocks = 
	{ 
		Block.craftingTable,
		Block.furnace
	};
	
	/**
	 * Creates a new CraftingManager instance, which will initialize the Recipe[] for each crafting block.
	 */
	public CraftingManager()
	{
		//When initializing an instance of CraftingManager, initialize the recipe arrays
		RecipeManager recipeManager = new RecipeManager(); 
		for(Block block: craftingBlocks)
		{
			Recipe[] allRecipes = recipeManager.getRecipesByBlockType(block);
			recipesByBlock.put(block.getName(), allRecipes);
		}
		recipesByBlock.put(Block.none.getName(), recipeManager.getRecipesByBlockType(Block.none));
	}

	private Recipe[] getAllRecipesByBlockType(Block block)
	{
		return recipesByBlock.get(block.getName());
	}
	
	/**
	 * Returns the constant Block[] craftingBlocks, indicating all the Blocks which are a prerequisite crafting something 
	 * @return all the Blocks which are a prerequisite to crafting
	 */
	public static final Block[] getCraftingBlocks()
	{
		return craftingBlocks;
	}
	
	/**
	 * Gets all the possible recipes for a given block type. A recipe is considered to be possible if 
	 * the player has the materials required to craft it.
	 * @param inventory Entityplayer's inventory to check against 
	 * @return a Recipe[] of all the recipes a player has materials to craft
	 */
	public Recipe[] getPossibleRecipesByBlock(InventoryPlayer inventory, Block block)
	{		
		Recipe[] allRecipes = getAllRecipesByBlockType(block);
		Vector<Recipe> vector = new Vector<Recipe>();
		boolean hasEnough = true;
		for(int i = 0; i < allRecipes.length; i++) //for each recipe in allRecipes[]
		{
			hasEnough = true;
			for(int j = 0; j < allRecipes[i].getRecipe().length; j++) //for each ingredient
			{
				//check if there's enough materials to craft it. If there isn't enough then the recipe can't be crafted
				if(inventory.getTotalInInventory(allRecipes[i].getRecipe()[j].getItemName()) < allRecipes[i].getRecipe()[j].getStackSize())
				{					
					hasEnough = false;
					break;
				}
			}			
			if(hasEnough)//The player has enough items to craft the recipe
			{
				vector.add(allRecipes[i]);
			}
		}
		
		Recipe[] temp = new Recipe[vector.size()];
		vector.copyInto(temp);		
		return temp;
	}
	
}
