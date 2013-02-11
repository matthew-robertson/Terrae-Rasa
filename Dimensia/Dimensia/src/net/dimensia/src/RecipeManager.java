package net.dimensia.src;

import java.util.Vector;

public class RecipeManager 
{
	public Vector<Recipe> craftingList;
	public Vector<Recipe> furnaceList;
	public Vector<Recipe> inventoryList;
	
	public RecipeManager()
	{
		if(craftingList == null)
		{
			craftingList = new Vector<Recipe>();
		}
		if(inventoryList == null)
		{	
			inventoryList = new Vector<Recipe>();
		}
		if(furnaceList == null)
		{	
			furnaceList = new Vector<Recipe>();
		}	
	}
	
	/**
	 * Get the craftingList Vector as an array
	 * @return craftingList vector as an array
	 */
	public Recipe[] getCraftingRecipesAsArray()
	{
		Recipe[] recipes = new Recipe[craftingList.size()];
		craftingList.copyInto(recipes);
		return recipes;
	}

	/**
	 * Get the furnaceList Vector as an array
	 * @return furnaceList vector as an array
	 */
	public Recipe[] getFurnaceRecipesAsArray()
	{
		Recipe[] recipes = new Recipe[furnaceList.size()];
		furnaceList.copyInto(recipes);
		return recipes;
	}
	
	/**
	 * Get the inventoryList Vector as an array
	 * @return inventoryList vector as an array
	 */
	public Recipe[] getInventoryRecipesAsArray()
	{
		Recipe[] recipes = new Recipe[inventoryList.size()];
		inventoryList.copyInto(recipes);
		return recipes;
	}
	
	/**
	 * add a recipe to the craftingList vector
	 * @param recipe recipe to add.
	 */
	public void addCraftingRecipe(Recipe recipe)
	{
		if(craftingList == null)
		{
			craftingList = new Vector<Recipe>();
		}
		craftingList.add(recipe);
	}

	/**
	 * add a recipe to the furnaceList vector
	 * @param recipe recipe to add.
	 */
	public void addFurnaceRecipe(Recipe recipe)
	{
		if(furnaceList == null)
		{
			furnaceList = new Vector<Recipe>();
		}
		furnaceList.add(recipe);
	}
	
	/**
	 * add a recipe to the inventoryList vector
	 * @param recipe recipe to add.
	 */
	public void addInventoryRecipe(Recipe recipe)
	{
		if(inventoryList == null)
		{
			inventoryList = new Vector<Recipe>();
		}
		inventoryList.add(recipe);
	}
	
	/** Recipe Declarations **/
	
	Recipe copperAxe = new Recipe(this, new ItemStack(Item.copperAxe), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 6), new ItemStack(Block.plank, 5)
	});		
	Recipe copperPickaxe = new Recipe(this, new ItemStack(Item.copperPickaxe), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 7), new ItemStack(Block.plank, 5) 
	});	
	Recipe copperBroadSword = new Recipe(this, new ItemStack(Item.copperSword), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 6), new ItemStack(Block.plank, 5) 
	});	
	Recipe copperHammer = new Recipe(this, new ItemStack(Item.copperHammer), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 5), new ItemStack(Block.plank, 5)
	});		
	Recipe copperChestplate = new Recipe(this, new ItemStack(Item.copperBody), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 10) 
	});	
	Recipe copperHelmet = new Recipe(this, new ItemStack(Item.copperHelmet), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 8) 
	});	
	Recipe copperGreaves = new Recipe(this, new ItemStack(Item.copperGreaves), 'c', new ItemStack[] {
		new ItemStack(Item.copperIngot, 7)
	});	
	
	Recipe bronzeAxe = new Recipe(this, new ItemStack(Item.bronzeAxe), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 7), new ItemStack(Block.plank, 5)
	});		
	Recipe bronzePickaxe = new Recipe(this, new ItemStack(Item.bronzePickaxe), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 9), new ItemStack(Block.plank, 5) 
	});	
	Recipe bronzeBroadSword = new Recipe(this, new ItemStack(Item.bronzeSword), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 8), new ItemStack(Block.plank, 5) 
	});	
	Recipe bronzeHammer = new Recipe(this, new ItemStack(Item.bronzeHammer), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 6), new ItemStack(Block.plank, 5)
	});		
	Recipe bronzeChestplate = new Recipe(this, new ItemStack(Item.bronzeBody), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 12) 
	});	
	Recipe bronzeHelmet = new Recipe(this, new ItemStack(Item.bronzeHelmet), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 10)
	});	
	Recipe bronzeGreaves = new Recipe(this, new ItemStack(Item.bronzeGreaves), 'c', new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 8)
	});		
	
	Recipe ironAxe = new Recipe(this, new ItemStack(Item.ironAxe), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 9), new ItemStack(Block.plank, 5)
	});		
	Recipe ironPickaxe = new Recipe(this, new ItemStack(Item.ironPickaxe), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 10), new ItemStack(Block.plank, 5)
	});	
	Recipe ironBroadSword = new Recipe(this, new ItemStack(Item.ironSword), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 9), new ItemStack(Block.plank, 5) 
	});	
	Recipe ironHammer = new Recipe(this, new ItemStack(Item.ironHammer), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 7), new ItemStack(Block.plank, 5)
	});		
	Recipe ironChestplate = new Recipe(this, new ItemStack(Item.ironBody), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 13) 
	});	
	Recipe ironHelmet = new Recipe(this, new ItemStack(Item.ironHelmet), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 11)
	});	
	Recipe ironGreaves = new Recipe(this, new ItemStack(Item.ironGreaves), 'c', new ItemStack[] {
		new ItemStack(Item.ironIngot, 10)
	});	
	
	Recipe silverAxe = new Recipe(this, new ItemStack(Item.silverAxe), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 10), new ItemStack(Block.plank, 5)
	});		
	Recipe silverPickaxe = new Recipe(this, new ItemStack(Item.silverPickaxe), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 12), new ItemStack(Block.plank, 5)
	});	
	Recipe silverBroadSword = new Recipe(this, new ItemStack(Item.silverSword), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 11), new ItemStack(Block.plank, 5) 
	});	
	Recipe silverHammer = new Recipe(this, new ItemStack(Item.silverHammer), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 9), new ItemStack(Block.plank, 5)
	});		
	Recipe silverChestplate = new Recipe(this, new ItemStack(Item.silverBody), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 15) 
	});	
	Recipe silverHelmet = new Recipe(this, new ItemStack(Item.silverHelmet), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 13)
	});	
	Recipe silverGreaves = new Recipe(this, new ItemStack(Item.silverGreaves), 'c', new ItemStack[] {
		new ItemStack(Item.silverIngot, 12)
	});	
	
	Recipe goldAxe = new Recipe(this, new ItemStack(Item.goldAxe), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 13), new ItemStack(Block.plank, 5)
	});		
	Recipe goldPickaxe = new Recipe(this, new ItemStack(Item.goldPickaxe), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 15), new ItemStack(Block.plank, 5)
	});	
	Recipe goldBroadSword = new Recipe(this, new ItemStack(Item.goldSword), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 14), new ItemStack(Block.plank, 5) 
	});	
	Recipe goldHammer = new Recipe(this, new ItemStack(Item.goldHammer), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 12), new ItemStack(Block.plank, 5)
	});		
	Recipe goldChestplate = new Recipe(this, new ItemStack(Item.goldBody), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 18)
	});	
	Recipe goldHelmet = new Recipe(this, new ItemStack(Item.goldHelmet), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 16)
	});	
	Recipe goldGreaves = new Recipe(this, new ItemStack(Item.goldGreaves), 'c', new ItemStack[] {
		new ItemStack(Item.goldIngot, 15)
	});	
	
	Recipe woodenArrow = new Recipe(this, new ItemStack(Item.woodenArrow, 4), 'c', new ItemStack[] {
		new ItemStack(Block.plank, 5)
	});
	Recipe snowBlock = new Recipe(this, new ItemStack(Block.snow, 1), 'c', new ItemStack[] {
		new ItemStack(Item.snowball, 5)
	});
	Recipe furnace = new Recipe(this, new ItemStack(Block.furnace), 'c', new ItemStack[] {
		new ItemStack(Block.stone, 15), new ItemStack(Item.coal, 5)
	});	
	Recipe chest = new Recipe(this, new ItemStack(Block.chest), 'c', new ItemStack[] {
		new ItemStack(Block.plank, 20), new ItemStack(Item.bronzeIngot, 5), new ItemStack(Item.ironIngot, 3) 
	});	
	
	Recipe fence = new Recipe(this, new ItemStack(Block.fence), 'c', new ItemStack[]{
		new ItemStack(Block.plank, 1)
	});	
	Recipe woodTable = new Recipe(this, new ItemStack(Block.woodTable), 'c', new ItemStack[]{
		new ItemStack(Block.plank, 10)
	});	
	Recipe stoneTable = new Recipe(this, new ItemStack(Block.stoneTable), 'c', new ItemStack[]{
		new ItemStack(Block.stone, 10)
	});	
	Recipe woodLeftChair = new Recipe(this, new ItemStack(Block.woodChairLeft), 'c', new ItemStack[]{
		new ItemStack(Block.plank, 5)
	});	
	Recipe woodRightChair = new Recipe(this, new ItemStack(Block.woodChairRight), 'c', new ItemStack[]{
		new ItemStack(Block.plank, 5)
	});	
	Recipe stoneLeftChair = new Recipe(this, new ItemStack(Block.stoneChairLeft), 'c', new ItemStack[]{
		new ItemStack(Block.stone, 5)
	});	
	Recipe stoneRightChair = new Recipe(this, new ItemStack(Block.stoneChairRight), 'c', new ItemStack[]{
		new ItemStack(Block.stone, 5)
	});	
	Recipe stonePillarEnd = new Recipe(this, new ItemStack(Item.stonePillarEnd), 'c', new ItemStack[]{
		new ItemStack(Block.stone, 2)
	});	
	Recipe stonePillar = new Recipe(this, new ItemStack(Block.stonePillar), 'c', new ItemStack[]{
		new ItemStack(Block.stone, 2)
	});	
	Recipe diamondPillarEnd = new Recipe(this, new ItemStack(Item.diamondPillarEnd), 'c', new ItemStack[]{
		new ItemStack(Item.diamond, 2)
	});	
	Recipe diamondPillar = new Recipe(this, new ItemStack(Block.diamondPillar), 'c', new ItemStack[]{
		new ItemStack(Item.diamond, 2)
	});
	Recipe vial = new Recipe(this, new ItemStack(Item.vialOfWater), 'c', new ItemStack[]{
		new ItemStack(Block.glass, 2)
	});
	
	//Alchemy Station (currently crafting bench)
	Recipe healingPotion1 = new Recipe(this, new ItemStack(Item.healthPotion1), 'c', new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.healingHerb1)
	});
	Recipe healingPotion2 = new Recipe(this, new ItemStack(Item.healthPotion2), 'c', new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.healingHerb2)
	});
	Recipe magicPotion1 = new Recipe(this, new ItemStack(Item.manaPotion1), 'c', new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.magicHerb1)
	});
	Recipe magicPotion2 = new Recipe(this, new ItemStack(Item.manaPotion2), 'c', new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.magicHerb2)
	});
	
	//Furnace Recipes:
	Recipe copperIngot = new Recipe(this, new ItemStack(Item.copperIngot), 'f', new ItemStack[] {
		new ItemStack(Item.copperOre, 3) 
	});
	Recipe tinIngot = new Recipe(this, new ItemStack(Item.tinIngot), 'f', new ItemStack[] {
		new ItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Ore = new Recipe(this, new ItemStack(Item.bronzeIngot), 'f', new ItemStack[] {
		new ItemStack(Item.copperOre, 3), new ItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Bar = new Recipe(this, new ItemStack(Item.bronzeIngot), 'f', new ItemStack[] {
		new ItemStack(Item.copperIngot, 1), new ItemStack(Item.tinIngot, 1) 
	});
	Recipe ironIngot = new Recipe(this, new ItemStack(Item.ironIngot), 'f', new ItemStack[] {
		new ItemStack(Item.ironOre, 4) 
	});
	Recipe silverIngot = new Recipe(this, new ItemStack(Item.silverIngot), 'f', new ItemStack[] {
		new ItemStack(Item.silverOre, 4) 
	});
	Recipe goldIngot = new Recipe(this, new ItemStack(Item.goldIngot), 'f', new ItemStack[] {
		new ItemStack(Item.goldOre, 5) 
	});
	Recipe glass = new Recipe(this, new ItemStack(Block.sand), 'f', new ItemStack[] {
		new ItemStack(Block.glass) 
	});
	
	//Inventory Defaults:
	Recipe torches = new Recipe(this, new ItemStack(Block.torch), 'i', new ItemStack[] {
		new ItemStack(Item.coal, 1), new ItemStack(Block.plank, 1) 
	});	
	Recipe manaCrystal = new Recipe(this, new ItemStack(Item.manaCrystal), 'i', new ItemStack[] {
		new ItemStack(Item.manaStar, 15)
	});	
	Recipe craftingTable = new Recipe(this, new ItemStack(Block.craftingTable), 'i', new ItemStack[] {
		new ItemStack(Block.plank, 10)
	});	
	

}
