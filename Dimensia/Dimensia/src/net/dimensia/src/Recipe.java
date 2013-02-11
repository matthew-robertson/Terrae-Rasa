package net.dimensia.src;

import java.io.Serializable;

public class Recipe 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** What the recipe yields */
	private ItemStack result; 
	/** What the recipe requires. */
	private ItemStack[] recipe; 
	
	public Recipe(RecipeManager manager, ItemStack result, char type, ItemStack[] stacks)
	{
		this.recipe = stacks;
		this.result = result;
				
		//add the recipe
		if(type == 'c') //Crafting recipe
		{
			manager.addCraftingRecipe(this);
		}	
		else if(type == 'f') //Furnace recipe
		{
			manager.addFurnaceRecipe(this);			
		}	
		else if(type == 'i') //Default inventory recipe
		{
			manager.addInventoryRecipe(this);
		}	
		else //The recipe failed, throw an exception
		{
			throw new RuntimeException("Invalid recipe type");
		}
	}
	
	public final ItemStack getResult()
	{
		return new ItemStack(result);
	}
	
	public final ItemStack[] getRecipe()
	{
		return recipe;
	}
}