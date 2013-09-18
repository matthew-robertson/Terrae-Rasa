package utils;

import blocks.Block;

/**
 * A Recipe stores the data for a crafting recipe. This includes the recipe requirements, the result of the 
 * recipe, and the Block required nearby to craft the recipe (Block.none indicates that it can be crafted
 * at any time).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Recipe 
{
	/** What the recipe yields */
	private RecipeResult result; 
	/** What the recipe requires. */
	private ItemStack[] recipe; 
	/** The block that has to be nearby to craft this. Block.none indicates no nearby block is needed. */
	private Block requiredBlock;
	private final int id;
	
	/**
	 * Creates a new Recipe and registers it in the RecipeManager.
	 * @param manager the RecipeManager to register this Recipe in
	 * @param result the DisplayableItemStack result of this Recipe
	 * @param requiredBlock the required nearby Block to craft this Recipe - Block.none means no nearby Block requirement
	 * @param stacks the materials required to craft this Recipe
	 */
	public Recipe(RecipeManager manager, RecipeResult result, Block requiredBlock, ItemStack[] stacks)
	{
		this.recipe = stacks;
		this.result = result;
		this.id = manager.getNextID();
		this.requiredBlock = requiredBlock;
		manager.registerRecipe(this);
	}
	
	public final ItemStack getResult()
	{
		return result.getResult();
	}
	
	public final ItemStack[] getRecipe()
	{
		return recipe;
	}
	
	public final Block getRequiredBlock()
	{
		return requiredBlock;
	}
	
	public final int getID()
	{
		return id;
	}
	
	/**
	 * Overrides Object.toString() for slightly improved naming. toString() now returns the name of the 
	 * resulting DisplayableItemStack.
	 */
	public String toString()
	{
		return result.getResult().getItemName();
	}
}