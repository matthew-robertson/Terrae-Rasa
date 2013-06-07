package utils;
import java.io.Serializable;
import java.util.Vector;




public class CraftingManager implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Recipe[] craftingRecipes;
	public Recipe[] furnaceRecipes;
	public Recipe[] inventoryRecipes;	
	
	public CraftingManager()
	{
		RecipeManager manager = new RecipeManager(); //when initializing an instance of CraftingManager, initialize the recipe arrays
		craftingRecipes = manager.getCraftingRecipesAsArray();
		furnaceRecipes = manager.getFurnaceRecipesAsArray();
		inventoryRecipes = manager.getInventoryRecipesAsArray();
		manager = null;
	}
	
	/**
	 * Get all possible recipes in the craftingRecipes
	 * @param inventory Entityplayer's inventory to check against 
	 * @return all possible recipes in craftingRecipes[]
	 */
	public Recipe[] getPossibleCraftingRecipes(InventoryPlayer inventory)
	{		
		Vector<Recipe> vector = new Vector<Recipe>();
		boolean hasEnough = true;
		
		for(int i = 0; i < craftingRecipes.length; i++) //for each recipe in craftingRecipes[]
		{
			hasEnough = true;
			for(int j = 0; j < craftingRecipes[i].getRecipe().length; j++) //for each ingredient
			{
				if(inventory.getTotalInInventory(craftingRecipes[i].getRecipe()[j].getItemName()) < craftingRecipes[i].getRecipe()[j].getStackSize())
				{//check if there's enough. if there isnt then the recipe cant be crafted
					hasEnough = false;
					break;
				}
			}			
			if(hasEnough)//The player has enough items to craft the recipe
			{
				vector.add(craftingRecipes[i]);
			}
		}
		
		Recipe[] temp = new Recipe[vector.size()];
		vector.copyInto(temp);		
		return temp;
	}
	
	/**
	 * Get all possible recipes in the furnaceRecipes
	 * @param inventory Entityplayer's inventory to check against 
	 * @return all possible recipes in furnaceRecipes[]
	 */
	public Recipe[] getPossibleFurnaceRecipes(InventoryPlayer inventory)
	{		
		Vector<Recipe> vector = new Vector<Recipe>();
		boolean hasEnough = true;
		
		for(int i = 0; i < furnaceRecipes.length; i++)//for each recipe in furnaceRecipes[]
		{
			hasEnough = true;
			for(int j = 0; j < furnaceRecipes[i].getRecipe().length; j++) //for each ingredient needed
			{
				if(inventory.getTotalInInventory(furnaceRecipes[i].getRecipe()[j].getItemName()) < furnaceRecipes[i].getRecipe()[j].getStackSize())
				{//check if there's enough. if there isnt then the recipe cant be crafted
					hasEnough = false;
					break;
				}
			}			
			if(hasEnough)//The player has enough items to craft the recipe
			{
				vector.add(furnaceRecipes[i]);
			}
		}
		
		Recipe[] temp = new Recipe[vector.size()];
		vector.copyInto(temp);						
		return temp;
	}
	
	/**
	 * Get all possible recipes in the inventoryRecipes
	 * @param inventory Entityplayer's inventory to check against 
	 * @return all possible recipes in inventoryRecipes[]
	 */
	public Recipe[] getPossibleInventoryRecipes(InventoryPlayer inventory)
	{		
		Vector<Recipe> vector = new Vector<Recipe>();
		boolean hasEnough = true;
		
		for(int i = 0; i < inventoryRecipes.length; i++) //for each recipe in inventoryRecipes[]
		{
			hasEnough = true;
			for(int j = 0; j < inventoryRecipes[i].getRecipe().length; j++) //for each ingredient needed
			{
				if(inventory.getTotalInInventory(inventoryRecipes[i].getRecipe()[j].getItemName()) < inventoryRecipes[i].getRecipe()[j].getStackSize())
				{ //check if there's enough. if there isnt then the recipe cant be crafted
					hasEnough = false;
					break;
				}
			}			
			if(hasEnough) //The player has enough items to craft the recipe
			{
				vector.add(inventoryRecipes[i]);
			}
		}
		
		Recipe[] temp = new Recipe[vector.size()];
		vector.copyInto(temp);		
		return temp;
	}
}
