package utils;

import items.Item;

import java.util.Vector;

import blocks.Block;

/**
 * RecipeManager contains the raw Recipe data for the entire game. All recipes are registered in a Vector which is not
 * organized, and can then by requested by Block type using {@link #getRecipesByBlockType(Block)}. 
 * 
 * <br><br>
 * 
 * Additionally, all Recipe declarations are stored in RecipeManager.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class RecipeManager 
{
	private final Vector<Recipe> unorganizedRecipes = new Vector<Recipe>(128);
	
	public RecipeManager()
	{
	}
	
	/**
	 * Requests all the crafting recipes associated with a given Block. If the block is not a registered crafting Block 
	 * this will return a Recipe[] of length 0.
	 * @param block the Block that will be used to find associated crafting recipes
	 * @return a Recipe[] of all the crafting recipes associated to this Block
	 */
	public Recipe[] getRecipesByBlockType(Block block)
	{
		Vector<Recipe> recipes = new Vector<Recipe>();
		for (Recipe recipe : unorganizedRecipes)
		{
			if(recipe.getRequiredBlock().getID() == block.getID()) //Compare IDs to ensure accuracy
			{
				recipes.add(recipe);
			}
		}
		Recipe[] recipesAsArray = new Recipe[recipes.size()];
		recipes.copyInto(recipesAsArray);
		return recipesAsArray;
	}
		
	/**
	 * Registers a crafting recipe in the unorganizedRecipes Vector. Ensure it has a Block matching a possible crafting block
	 * in order to be retrieved for later use.
	 * @param recipe the Recipe to register
	 */
	public void registerRecipe(Recipe recipe)
	{
		unorganizedRecipes.add(recipe);
	}
	
	/** Recipe Declarations **/
	
	Recipe copperAxe = new Recipe(this, new DisplayableItemStack(Item.copperAxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 6), new DisplayableItemStack(Block.plank, 5)
	});		
	Recipe copperPickaxe = new Recipe(this, new DisplayableItemStack(Item.copperPickaxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 7), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe copperBroadSword = new Recipe(this, new DisplayableItemStack(Item.copperSword), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 6), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe copperChestplate = new Recipe(this, new DisplayableItemStack(Item.copperBody), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 10) 
	});	
	Recipe copperHelmet = new Recipe(this, new DisplayableItemStack(Item.copperHelmet), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 8) 
	});	
	Recipe copperGreaves = new Recipe(this, new DisplayableItemStack(Item.copperPants), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 7)
	});	
	Recipe copperBelt = new Recipe(this, new DisplayableItemStack(Item.copperBelt), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 5)
	});	
	Recipe copperBoots = new Recipe(this, new DisplayableItemStack(Item.copperBoots), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 5)
	});	
	Recipe copperGloves = new Recipe(this, new DisplayableItemStack(Item.copperGloves), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 5)
	});	
	
	Recipe bronzeAxe = new Recipe(this, new DisplayableItemStack(Item.bronzeAxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 7), new DisplayableItemStack(Block.plank, 5)
	});		
	Recipe bronzePickaxe = new Recipe(this, new DisplayableItemStack(Item.bronzePickaxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 9), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe bronzeBroadSword = new Recipe(this, new DisplayableItemStack(Item.bronzeSword), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 8), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe bronzeChestplate = new Recipe(this, new DisplayableItemStack(Item.bronzeBody), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 12) 
	});	
	Recipe bronzeHelmet = new Recipe(this, new DisplayableItemStack(Item.bronzeHelmet), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 10)
	});	
	Recipe bronzeGreaves = new Recipe(this, new DisplayableItemStack(Item.bronzePants), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 8)
	});		
	Recipe bronzeBelt = new Recipe(this, new DisplayableItemStack(Item.bronzeBelt), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 5)
	});	
	Recipe bronzeBoots = new Recipe(this, new DisplayableItemStack(Item.bronzeBoots), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 5)
	});	
	Recipe bronzeGloves = new Recipe(this, new DisplayableItemStack(Item.bronzeGloves), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.bronzeIngot, 5)
	});	
	
	Recipe ironAxe = new Recipe(this, new DisplayableItemStack(Item.ironAxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 9), new DisplayableItemStack(Block.plank, 5)
	});		
	Recipe ironPickaxe = new Recipe(this, new DisplayableItemStack(Item.ironPickaxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 10), new DisplayableItemStack(Block.plank, 5)
	});	
	Recipe ironBroadSword = new Recipe(this, new DisplayableItemStack(Item.ironSword), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 9), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe ironChestplate = new Recipe(this, new DisplayableItemStack(Item.ironBody), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 13) 
	});	
	Recipe ironHelmet = new Recipe(this, new DisplayableItemStack(Item.ironHelmet), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 11)
	});	
	Recipe ironGreaves = new Recipe(this, new DisplayableItemStack(Item.ironPants), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 10)
	});	
	Recipe ironBelt = new Recipe(this, new DisplayableItemStack(Item.ironBelt), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 7)
	});	
	Recipe ironBoots = new Recipe(this, new DisplayableItemStack(Item.ironBoots), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 7)
	});	
	Recipe ironGloves = new Recipe(this, new DisplayableItemStack(Item.ironGloves), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironIngot, 7)
	});	
	
	Recipe silverAxe = new Recipe(this, new DisplayableItemStack(Item.silverAxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 10), new DisplayableItemStack(Block.plank, 5)
	});		
	Recipe silverPickaxe = new Recipe(this, new DisplayableItemStack(Item.silverPickaxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 12), new DisplayableItemStack(Block.plank, 5)
	});	
	Recipe silverBroadSword = new Recipe(this, new DisplayableItemStack(Item.silverSword), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 11), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe silverChestplate = new Recipe(this, new DisplayableItemStack(Item.silverBody), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 15) 
	});	
	Recipe silverHelmet = new Recipe(this, new DisplayableItemStack(Item.silverHelmet), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 13)
	});	
	Recipe silverGreaves = new Recipe(this, new DisplayableItemStack(Item.silverPants), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 12)
	});	
	Recipe silverBelt = new Recipe(this, new DisplayableItemStack(Item.silverBelt), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 9)
	});	
	Recipe silverBoots = new Recipe(this, new DisplayableItemStack(Item.silverBoots), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 9)
	});	
	Recipe silverGloves = new Recipe(this, new DisplayableItemStack(Item.silverGloves), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverIngot, 9)
	});	
	
	Recipe goldAxe = new Recipe(this, new DisplayableItemStack(Item.goldAxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 13), new DisplayableItemStack(Block.plank, 5)
	});		
	Recipe goldPickaxe = new Recipe(this, new DisplayableItemStack(Item.goldPickaxe), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 15), new DisplayableItemStack(Block.plank, 5)
	});	
	Recipe goldBroadSword = new Recipe(this, new DisplayableItemStack(Item.goldSword), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 14), new DisplayableItemStack(Block.plank, 5) 
	});	
	Recipe goldChestplate = new Recipe(this, new DisplayableItemStack(Item.goldBody), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 18), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	Recipe goldHelmet = new Recipe(this, new DisplayableItemStack(Item.goldHelmet), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 16), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	Recipe goldGreaves = new Recipe(this, new DisplayableItemStack(Item.goldPants), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 15), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	Recipe goldBelt = new Recipe(this, new DisplayableItemStack(Item.goldBelt), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 12), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	Recipe goldBoots = new Recipe(this, new DisplayableItemStack(Item.goldBoots), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 12), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	Recipe goldGloves = new Recipe(this, new DisplayableItemStack(Item.goldGloves), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldIngot, 12), new DisplayableItemStack(Item.silverIngot, 1)
	});	
	
	Recipe woodenBow = new Recipe(this, new DisplayableItemStack(Item.woodenBow), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 10)
	});
	
	Recipe woodenArrow = new Recipe(this, new DisplayableItemStack(Item.woodenArrow, 5), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 5)
	});
	Recipe bronzeArrow = new Recipe(this, new DisplayableItemStack(Item.bronzeArrow, 5), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 5), new DisplayableItemStack(Item.bronzeIngot)
	});
	Recipe ironArrow = new Recipe(this, new DisplayableItemStack(Item.ironArrow, 5), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 5), new DisplayableItemStack(Item.ironIngot)
	});
	Recipe silverArrow = new Recipe(this, new DisplayableItemStack(Item.silverArrow, 5), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 5), new DisplayableItemStack(Item.silverIngot)
	});
	
	Recipe snowBlock = new Recipe(this, new DisplayableItemStack(Block.snow, 1), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.snowball, 5)
	});
	Recipe furnace = new Recipe(this, new DisplayableItemStack(Block.furnace), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.stone, 15), new DisplayableItemStack(Item.coal, 5)
	});	
	Recipe gemcraftingBench = new Recipe(this, new DisplayableItemStack(Block.gemcraftingBench), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.craftingTable), new DisplayableItemStack(Block.stone, 10), new DisplayableItemStack(Item.silverIngot, 2) 
	});	
	Recipe alchemyStation = new Recipe(this, new DisplayableItemStack(Block.alchemyStation), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.stone, 20), new DisplayableItemStack(Item.vialEmpty), new DisplayableItemStack(Item.coal)
	});	
	Recipe chest = new Recipe(this, new DisplayableItemStack(Block.chest), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 20) 
	});	
	Recipe ironChest = new Recipe(this, new DisplayableItemStack(Block.ironChest), Block.craftingTable, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 5), new DisplayableItemStack(Item.ironIngot, 10)
	});
	
	Recipe fence = new Recipe(this, new DisplayableItemStack(Block.fence), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.plank, 1)
	});	
	Recipe woodTable = new Recipe(this, new DisplayableItemStack(Block.woodTable), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.plank, 10)
	});	
	Recipe stoneTable = new Recipe(this, new DisplayableItemStack(Block.stoneTable), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.stone, 10)
	});	
	Recipe woodLeftChair = new Recipe(this, new DisplayableItemStack(Block.woodChairLeft), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.plank, 5)
	});	
	Recipe woodRightChair = new Recipe(this, new DisplayableItemStack(Block.woodChairRight), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.plank, 5)
	});	
	Recipe stoneLeftChair = new Recipe(this, new DisplayableItemStack(Block.stoneChairLeft), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.stone, 5)
	});	
	Recipe stoneRightChair = new Recipe(this, new DisplayableItemStack(Block.stoneChairRight), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.stone, 5)
	});	
	
	Recipe stonePillar = new Recipe(this, new DisplayableItemStack(Block.stonePillar), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.stone, 2)
	});	
	
	Recipe diamondPillar = new Recipe(this, new DisplayableItemStack(Block.diamondPillar), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.diamond, 2)
	});
	Recipe vial = new Recipe(this, new DisplayableItemStack(Item.vialOfWater), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Block.glass, 2)
	});
	Recipe goldRing = new Recipe(this, new DisplayableItemStack(Item.goldRing), Block.craftingTable, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldIngot, 2)
	});
	
	//TODO: Fix recipes with affixes
	// Gemcrafting Bench (currently crafting bench)	
//	Recipe frenziedRubyRing1 = new Recipe(this, new DisplayableItemStack(Item.rubyRing).setAffix(new AffixFrenzied()), Block.gemcraftingBench, new DisplayableItemStack[]{
//		new DisplayableItemStack(Item.berserkersEssence), new DisplayableItemStack(Item.rubyRing)
//	});
//	Recipe frenziedRuby = new Recipe(this, new DisplayableItemStack(Item.ruby).setAffix(new AffixFrenzied()), Block.gemcraftingBench, new DisplayableItemStack[]{
//		new DisplayableItemStack(Item.berserkersEssence), new DisplayableItemStack(Item.ruby)
//	});
//	Recipe sturdyOpalRing = new Recipe(this, new DisplayableItemStack(Item.opalRing).setAffix(new AffixSturdy()), Block.gemcraftingBench, new DisplayableItemStack[]{
//		new DisplayableItemStack(Item.steadfastShield), new DisplayableItemStack(Item.opalRing)
//	});
//	Recipe sturdyRuby = new Recipe(this, new DisplayableItemStack(Item.opal).setAffix(new AffixSturdy()), Block.gemcraftingBench, new DisplayableItemStack[]{
//		new DisplayableItemStack(Item.steadfastShield), new DisplayableItemStack(Item.opal)
//	});
	
	Recipe sapphireRing = new Recipe(this, new DisplayableItemStack(Item.sapphireRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.sapphire)
	});
	Recipe emeraldRing = new Recipe(this, new DisplayableItemStack(Item.emeraldRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.emerald)
	});
	Recipe rubyRing = new Recipe(this, new DisplayableItemStack(Item.rubyRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.ruby)
	});
	Recipe diamondRing = new Recipe(this, new DisplayableItemStack(Item.diamondRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.diamond)
	});
	Recipe opalRing = new Recipe(this, new DisplayableItemStack(Item.opalRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.opal)
	});
	Recipe jasperRing = new Recipe(this, new DisplayableItemStack(Item.jasperRing), Block.gemcraftingBench, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.goldRing), new DisplayableItemStack(Item.jasper)
	});
	
	//Alchemy Station (currently crafting bench)
	Recipe healingPotion1 = new Recipe(this, new DisplayableItemStack(Item.healthPotion1), Block.alchemyStation, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.vialOfWater), new DisplayableItemStack(Item.healingHerb1)
	});
	Recipe healingPotion2 = new Recipe(this, new DisplayableItemStack(Item.healthPotion2), Block.alchemyStation, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.vialOfWater), new DisplayableItemStack(Item.healingHerb2)
	});
	Recipe magicPotion1 = new Recipe(this, new DisplayableItemStack(Item.manaPotion1), Block.alchemyStation, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.vialOfWater), new DisplayableItemStack(Item.magicHerb1)
	});
	Recipe magicPotion2 = new Recipe(this, new DisplayableItemStack(Item.manaPotion2), Block.alchemyStation, new DisplayableItemStack[]{
		new DisplayableItemStack(Item.vialOfWater), new DisplayableItemStack(Item.magicHerb2)
	});
	
	//Furnace Recipes:
	Recipe copperIngot = new Recipe(this, new DisplayableItemStack(Item.copperIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperOre, 3) 
	});
	Recipe tinIngot = new Recipe(this, new DisplayableItemStack(Item.tinIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Ore = new Recipe(this, new DisplayableItemStack(Item.bronzeIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperOre, 3), new DisplayableItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Bar = new Recipe(this, new DisplayableItemStack(Item.bronzeIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.copperIngot, 1), new DisplayableItemStack(Item.tinIngot, 1) 
	});
	Recipe ironIngot = new Recipe(this, new DisplayableItemStack(Item.ironIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.ironOre, 4) 
	});
	Recipe silverIngot = new Recipe(this, new DisplayableItemStack(Item.silverIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.silverOre, 4) 
	});
	Recipe goldIngot = new Recipe(this, new DisplayableItemStack(Item.goldIngot), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.goldOre, 5) 
	});
	Recipe glass = new Recipe(this, new DisplayableItemStack(Block.sand), Block.furnace, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.glass) 
	});	
	
	//Inventory Defaults:
	Recipe torches = new Recipe(this, new DisplayableItemStack(Block.torch, 6), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Item.coal, 1), new DisplayableItemStack(Block.plank, 1) 
	});	
//	Recipe manaCrystal = new Recipe(this, new DisplayableItemStack(Item.manaCrystal), Block.none, new DisplayableItemStack[] {
//		new DisplayableItemStack(Item.manaStar, 15)
//	});	
	Recipe craftingTable = new Recipe(this, new DisplayableItemStack(Block.craftingTable), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.plank, 10)
	});	
	
	//Block to backwall conversions
	Recipe backDirt = new Recipe(this, new DisplayableItemStack(Block.backDirt, 4), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.dirt)
	});
	Recipe backStone = new Recipe(this, new DisplayableItemStack(Block.backStone, 4), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.stone)
	});
	
	//Backwall to block conversions
	Recipe dirt = new Recipe(this, new DisplayableItemStack(Block.dirt), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.backDirt, 4)
	});
	Recipe stone = new Recipe(this, new DisplayableItemStack(Block.stone), Block.none, new DisplayableItemStack[] {
		new DisplayableItemStack(Block.backStone, 4)
	});
	

}
