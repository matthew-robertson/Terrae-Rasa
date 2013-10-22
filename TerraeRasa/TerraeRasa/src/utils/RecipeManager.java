package utils;

import items.Item;

import java.util.Vector;


import affix.AffixFrenzied;
import affix.AffixSturdy;
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
	public int nextRecipeID = 0;

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
	
	public int getNextID()
	{
		return nextRecipeID++;
	}
	
	/** Recipe Declarations **/
	
	Recipe copperAxe = new Recipe(this, new RecipeResult(new ItemStack(Item.copperAxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 6), new ItemStack(Block.plank, 5)
	});		
	Recipe copperPickaxe = new Recipe(this, new RecipeResult(new ItemStack(Item.copperPickaxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 7), new ItemStack(Block.plank, 5) 
	});	
	Recipe copperBroadSword = new Recipe(this, new RecipeResult(new ItemStack(Item.copperSword)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 6), new ItemStack(Block.plank, 5) 
	});	
	Recipe copperChestplate = new Recipe(this, new RecipeResult(new ItemStack(Item.copperBody)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 10) 
	});	
	Recipe copperHelmet = new Recipe(this, new RecipeResult(new ItemStack(Item.copperHelmet)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 8) 
	});	
	Recipe copperGreaves = new Recipe(this, new RecipeResult(new ItemStack(Item.copperPants)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 7)
	});	
	Recipe copperBelt = new Recipe(this, new RecipeResult(new ItemStack(Item.copperBelt)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 5)
	});	
	Recipe copperBoots = new Recipe(this, new RecipeResult(new ItemStack(Item.copperBoots)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 5)
	});	
	Recipe copperGloves = new Recipe(this, new RecipeResult(new ItemStack(Item.copperGloves)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.copperIngot, 5)
	});	
	
	Recipe bronzeAxe = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeAxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 7), new ItemStack(Block.plank, 5)
	});		
	Recipe bronzePickaxe = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzePickaxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 9), new ItemStack(Block.plank, 5) 
	});	
	Recipe bronzeBroadSword = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeSword)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 8), new ItemStack(Block.plank, 5) 
	});	
	Recipe bronzeChestplate = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeBody)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 12) 
	});	
	Recipe bronzeHelmet = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeHelmet)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 10)
	});	
	Recipe bronzeGreaves = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzePants)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 8)
	});		
	Recipe bronzeBelt = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeBelt)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 5)
	});	
	Recipe bronzeBoots = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeBoots)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 5)
	});	
	Recipe bronzeGloves = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeGloves)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.bronzeIngot, 5)
	});	
	
	Recipe ironAxe = new Recipe(this, new RecipeResult(new ItemStack(Item.ironAxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 9), new ItemStack(Block.plank, 5)
	});		
	Recipe ironPickaxe = new Recipe(this, new RecipeResult(new ItemStack(Item.ironPickaxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 10), new ItemStack(Block.plank, 5)
	});	
	Recipe ironBroadSword = new Recipe(this, new RecipeResult(new ItemStack(Item.ironSword)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 9), new ItemStack(Block.plank, 5) 
	});	
	Recipe ironChestplate = new Recipe(this, new RecipeResult(new ItemStack(Item.ironBody)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 13) 
	});	
	Recipe ironHelmet = new Recipe(this, new RecipeResult(new ItemStack(Item.ironHelmet)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 11)
	});	
	Recipe ironGreaves = new Recipe(this, new RecipeResult(new ItemStack(Item.ironPants)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 10)
	});	
	Recipe ironBelt = new Recipe(this, new RecipeResult(new ItemStack(Item.ironBelt)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 7)
	});	
	Recipe ironBoots = new Recipe(this, new RecipeResult(new ItemStack(Item.ironBoots)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 7)
	});	
	Recipe ironGloves = new Recipe(this, new RecipeResult(new ItemStack(Item.ironGloves)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.ironIngot, 7)
	});	
	
	Recipe silverAxe = new Recipe(this, new RecipeResult(new ItemStack(Item.silverAxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 10), new ItemStack(Block.plank, 5)
	});		
	Recipe silverPickaxe = new Recipe(this, new RecipeResult(new ItemStack(Item.silverPickaxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 12), new ItemStack(Block.plank, 5)
	});	
	Recipe silverBroadSword = new Recipe(this, new RecipeResult(new ItemStack(Item.silverSword)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 11), new ItemStack(Block.plank, 5) 
	});	
	Recipe silverChestplate = new Recipe(this, new RecipeResult(new ItemStack(Item.silverBody)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 15) 
	});	
	Recipe silverHelmet = new Recipe(this, new RecipeResult(new ItemStack(Item.silverHelmet)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 13)
	});	
	Recipe silverGreaves = new Recipe(this, new RecipeResult(new ItemStack(Item.silverPants)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 12)
	});	
	Recipe silverBelt = new Recipe(this, new RecipeResult(new ItemStack(Item.silverBelt)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 9)
	});	
	Recipe silverBoots = new Recipe(this, new RecipeResult(new ItemStack(Item.silverBoots)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 9)
	});	
	Recipe silverGloves = new Recipe(this, new RecipeResult(new ItemStack(Item.silverGloves)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.silverIngot, 9)
	});	
	
	Recipe goldAxe = new Recipe(this, new RecipeResult(new ItemStack(Item.goldAxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 13), new ItemStack(Block.plank, 5)
	});		
	Recipe goldPickaxe = new Recipe(this, new RecipeResult(new ItemStack(Item.goldPickaxe)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 15), new ItemStack(Block.plank, 5)
	});	
	Recipe goldBroadSword = new Recipe(this, new RecipeResult(new ItemStack(Item.goldSword)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 14), new ItemStack(Block.plank, 5) 
	});	
	Recipe goldChestplate = new Recipe(this, new RecipeResult(new ItemStack(Item.goldBody)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 18), new ItemStack(Item.silverIngot, 1)
	});	
	Recipe goldHelmet = new Recipe(this, new RecipeResult(new ItemStack(Item.goldHelmet)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 16), new ItemStack(Item.silverIngot, 1)
	});	
	Recipe goldGreaves = new Recipe(this, new RecipeResult(new ItemStack(Item.goldPants)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 15), new ItemStack(Item.silverIngot, 1)
	});	
	Recipe goldBelt = new Recipe(this, new RecipeResult(new ItemStack(Item.goldBelt)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 12), new ItemStack(Item.silverIngot, 1)
	});	
	Recipe goldBoots = new Recipe(this, new RecipeResult(new ItemStack(Item.goldBoots)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 12), new ItemStack(Item.silverIngot, 1)
	});	
	Recipe goldGloves = new Recipe(this, new RecipeResult(new ItemStack(Item.goldGloves)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.goldIngot, 12), new ItemStack(Item.silverIngot, 1)
	});	
	
	Recipe woodenBow = new Recipe(this, new RecipeResult(new ItemStack(Item.woodenBow)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 10)
	});
	
	Recipe woodenArrow = new Recipe(this, new RecipeResult(new ItemStack(Item.woodenArrow, 5)), Block.none, new ItemStack[] {
		new ItemStack(Block.plank, 5)
	});
	Recipe bronzeArrow = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeArrow, 5)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 5), new ItemStack(Item.bronzeIngot)
	});
	Recipe ironArrow = new Recipe(this, new RecipeResult(new ItemStack(Item.ironArrow, 5)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 5), new ItemStack(Item.ironIngot)
	});
	Recipe silverArrow = new Recipe(this, new RecipeResult(new ItemStack(Item.silverArrow, 5)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 5), new ItemStack(Item.silverIngot)
	});
	
	Recipe snowBlock = new Recipe(this, new RecipeResult(new ItemStack(Block.snow, 1)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Item.snowball, 5)
	});
	Recipe furnace = new Recipe(this, new RecipeResult(new ItemStack(Block.furnace)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.stone, 15), new ItemStack(Item.coal, 5)
	});	
	Recipe gemcraftingBench = new Recipe(this, new RecipeResult(new ItemStack(Block.gemcraftingBench)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.craftingTable), new ItemStack(Block.stone, 10), new ItemStack(Item.silverIngot, 2) 
	});	
	Recipe alchemyStation = new Recipe(this, new RecipeResult(new ItemStack(Block.alchemyStation)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.stone, 20), new ItemStack(Item.vialEmpty), new ItemStack(Item.coal)
	});	
	Recipe chest = new Recipe(this, new RecipeResult(new ItemStack(Block.chest)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 20) 
	});	
	Recipe ironChest = new Recipe(this, new RecipeResult(new ItemStack(Block.ironChest)), Block.craftingTable, new ItemStack[] {
		new ItemStack(Block.plank, 5), new ItemStack(Item.ironIngot, 10)
	});
	
	Recipe fence = new Recipe(this, new RecipeResult(new ItemStack(Block.fence)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.plank, 1)
	});	
	Recipe woodTable = new Recipe(this, new RecipeResult(new ItemStack(Block.woodTable)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.plank, 10)
	});	
	Recipe stoneTable = new Recipe(this, new RecipeResult(new ItemStack(Block.stoneTable)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.stone, 10)
	});	
	Recipe woodLeftChair = new Recipe(this, new RecipeResult(new ItemStack(Block.woodChairLeft)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.plank, 5)
	});	
	Recipe woodRightChair = new Recipe(this, new RecipeResult(new ItemStack(Block.woodChairRight)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.plank, 5)
	});	
	Recipe stoneLeftChair = new Recipe(this, new RecipeResult(new ItemStack(Block.stoneChairLeft)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.stone, 5)
	});	
	Recipe stoneRightChair = new Recipe(this, new RecipeResult(new ItemStack(Block.stoneChairRight)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.stone, 5)
	});	
	
	Recipe stonePillar = new Recipe(this, new RecipeResult(new ItemStack(Block.stonePillar)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.stone, 2)
	});	
	
	Recipe diamondPillar = new Recipe(this, new RecipeResult(new ItemStack(Block.diamondPillar)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Item.diamond, 2)
	});
	Recipe vial = new Recipe(this, new RecipeResult(new ItemStack(Item.vialOfWater)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Block.glass, 2)
	});
	Recipe goldRing = new Recipe(this, new RecipeResult(new ItemStack(Item.goldRing)), Block.craftingTable, new ItemStack[]{
		new ItemStack(Item.goldIngot, 2)
	});
	
	// Gemcrafting Bench (currently crafting bench)	
	Recipe frenziedRubyRing1 = new Recipe(this, new RecipeResult(new ItemStack(Item.rubyRing), false).addAffix(AffixFrenzied.getAffixID()), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.berserkersEssence), new ItemStack(Item.rubyRing)
	});
	Recipe frenziedRuby = new Recipe(this, new RecipeResult(new ItemStack(Item.ruby), false).addAffix(AffixFrenzied.getAffixID()), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.berserkersEssence), new ItemStack(Item.ruby)
	});
	Recipe sturdyOpalRing = new Recipe(this, new RecipeResult(new ItemStack(Item.opalRing), false).addAffix(AffixSturdy.getAffixID()), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.steadfastShield), new ItemStack(Item.opalRing)
	});
	Recipe sturdyRuby = new Recipe(this, new RecipeResult(new ItemStack(Item.opal), false).addAffix(AffixSturdy.getAffixID()), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.steadfastShield), new ItemStack(Item.opal)
	});
	
	Recipe sapphireRing = new Recipe(this, new RecipeResult(new ItemStack(Item.sapphireRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.sapphire)
	});
	Recipe emeraldRing = new Recipe(this, new RecipeResult(new ItemStack(Item.emeraldRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.emerald)
	});
	Recipe rubyRing = new Recipe(this, new RecipeResult(new ItemStack(Item.rubyRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.ruby)
	});
	Recipe diamondRing = new Recipe(this, new RecipeResult(new ItemStack(Item.diamondRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.diamond)
	});
	Recipe opalRing = new Recipe(this, new RecipeResult(new ItemStack(Item.opalRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.opal)
	});
	Recipe jasperRing = new Recipe(this, new RecipeResult(new ItemStack(Item.jasperRing)), Block.gemcraftingBench, new ItemStack[]{
		new ItemStack(Item.goldRing), new ItemStack(Item.jasper)
	});
	
	//Alchemy Station (currently crafting bench)
	Recipe healingPotion1 = new Recipe(this, new RecipeResult(new ItemStack(Item.healthPotion1)), Block.alchemyStation, new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.healingHerb1)
	});
	Recipe healingPotion2 = new Recipe(this, new RecipeResult(new ItemStack(Item.healthPotion2)), Block.alchemyStation, new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.healingHerb2)
	});
	Recipe magicPotion1 = new Recipe(this, new RecipeResult(new ItemStack(Item.manaPotion1)), Block.alchemyStation, new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.magicHerb1)
	});
	Recipe magicPotion2 = new Recipe(this, new RecipeResult(new ItemStack(Item.manaPotion2)), Block.alchemyStation, new ItemStack[]{
		new ItemStack(Item.vialOfWater), new ItemStack(Item.magicHerb2)
	});
	
	//Furnace Recipes:
	Recipe copperIngot = new Recipe(this, new RecipeResult(new ItemStack(Item.copperIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.copperOre, 3) 
	});
	Recipe tinIngot = new Recipe(this, new RecipeResult(new ItemStack(Item.tinIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Ore = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.copperOre, 3), new ItemStack(Item.tinOre, 3) 
	});
	Recipe bronzeIngot_Bar = new Recipe(this, new RecipeResult(new ItemStack(Item.bronzeIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.copperIngot, 1), new ItemStack(Item.tinIngot, 1) 
	});
	Recipe ironIngot = new Recipe(this, new RecipeResult(new ItemStack(Item.ironIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.ironOre, 4) 
	});
	Recipe silverIngot = new Recipe(this, new RecipeResult(new ItemStack(Item.silverIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.silverOre, 4) 
	});
	Recipe goldIngot = new Recipe(this, new RecipeResult(new ItemStack(Item.goldIngot)), Block.furnace, new ItemStack[] {
		new ItemStack(Item.goldOre, 5) 
	});
	Recipe glass = new Recipe(this, new RecipeResult(new ItemStack(Block.sand)), Block.furnace, new ItemStack[] {
		new ItemStack(Block.glass) 
	});	
	
	//Inventory Defaults:
	Recipe torches = new Recipe(this, new RecipeResult(new ItemStack(Block.torch, 6)), Block.none, new ItemStack[] {
		new ItemStack(Item.coal, 1), new ItemStack(Block.plank, 1) 
	});	
//	Recipe manaCrystal = new Recipe(this, new ItemStack(Item.manaCrystal), Block.none, new ItemStack[] {
//		new ItemStack(Item.manaStar, 15)
//	});	
	Recipe craftingTable = new Recipe(this, new RecipeResult(new ItemStack(Block.craftingTable)), Block.none, new ItemStack[] {
		new ItemStack(Block.plank, 10)
	});	
	
	//Block to backwall conversions
	Recipe backDirt = new Recipe(this, new RecipeResult(new ItemStack(Block.backDirt, 4)), Block.none, new ItemStack[] {
		new ItemStack(Block.dirt)
	});
	Recipe backStone = new Recipe(this, new RecipeResult(new ItemStack(Block.backStone, 4)), Block.none, new ItemStack[] {
		new ItemStack(Block.stone)
	});
	
	//Backwall to block conversions
	Recipe dirt = new Recipe(this, new RecipeResult(new ItemStack(Block.dirt)), Block.none, new ItemStack[] {
		new ItemStack(Block.backDirt, 4)
	});
	Recipe stone = new Recipe(this, new RecipeResult(new ItemStack(Block.stone)), Block.none, new ItemStack[] {
		new ItemStack(Block.backStone, 4)
	});
	

}
