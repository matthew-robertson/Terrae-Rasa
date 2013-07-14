package utils;

import items.Item;
import blocks.Block;

/**
 * ChestLootGenerator contains loot tables and methods to retrieve a set of loot from each loot table. Pieces of 
 * loot are randomly generated based on the proability_weights assigned to each piece of loot in the loot table.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ChestLootGenerator
{	
	private final static ChestLootTable HIGH_LEVEL_RARE = new ChestLootTable(new ChestLootItem[] {
			new ChestLootItem(new ItemStack(Item.ironBody), 1),
			new ChestLootItem(new ItemStack(Item.ironHelmet), 1),
			new ChestLootItem(new ItemStack(Item.ironPants), 1),
			new ChestLootItem(new ItemStack(Item.silverBody), 1),
			new ChestLootItem(new ItemStack(Item.silverHelmet), 1),
			new ChestLootItem(new ItemStack(Item.silverPants), 1),
			new ChestLootItem(new ItemStack(Item.ironIngot), 1),
			new ChestLootItem(new ItemStack(Item.silverIngot), 1),
			new ChestLootItem(new ItemStack(Item.silverSword), 1),
			new ChestLootItem(new ItemStack(Item.silverPickaxe), 1),
			new ChestLootItem(new ItemStack(Item.silverAxe), 1),
			new ChestLootItem(new ItemStack(Item.regenerationPotion2), 3, 2, 7),
			new ChestLootItem(new ItemStack(Item.silverCoin), 3, 40, 76),
			new ChestLootItem(new ItemStack(Item.healthPotion2), 3, 10, 20),			
	});
	
	private final static ChestLootTable HIGH_LEVEL_COMMON = new ChestLootTable(new ChestLootItem[] {
			new ChestLootItem(new ItemStack(Item.silverCoin), 1, 18, 56),
			new ChestLootItem(new ItemStack(Block.glass), 1, 10, 20),
			new ChestLootItem(new ItemStack(Item.vialOfWater), 1, 4, 13),
			new ChestLootItem(new ItemStack(Item.vialEmpty), 1, 4, 13),
			new ChestLootItem(new ItemStack(Item.magicHerb2), 1, 2, 4),
			new ChestLootItem(new ItemStack(Item.healingHerb2), 1, 2, 4),
			new ChestLootItem(new ItemStack(Item.magicHerb1), 1, 3, 7),
			new ChestLootItem(new ItemStack(Item.healingHerb1), 1, 2, 6),
			new ChestLootItem(new ItemStack(Item.woodenArrow), 1, 18, 41),
			new ChestLootItem(new ItemStack(Item.copperIngot), 1, 2, 6),
			new ChestLootItem(new ItemStack(Item.bronzeIngot), 1, 1, 3),
			new ChestLootItem(new ItemStack(Item.healthPotion1), 1, 2, 4),
			new ChestLootItem(new ItemStack(Item.healthPotion2), 1, 1, 4),
			new ChestLootItem(new ItemStack(Item.manaPotion1), 1, 2, 4),
			new ChestLootItem(new ItemStack(Item.manaPotion2), 1, 3, 7),
	});
	
	private final static ChestLootTable LOW_LEVEL_COMMON = new ChestLootTable(new ChestLootItem[] {
			new ChestLootItem(new ItemStack(Block.dirt), 1, 5, 15),
			new ChestLootItem(new ItemStack(Block.stone), 1, 5, 15),
			new ChestLootItem(new ItemStack(Item.copperIngot), 1, 1, 2),
			new ChestLootItem(new ItemStack(Item.bronzeIngot), 1),
			new ChestLootItem(new ItemStack(Item.silverCoin), 1, 10, 20),
			new ChestLootItem(new ItemStack(Item.woodenArrow), 1, 17, 27),
			new ChestLootItem(new ItemStack(Item.healthPotion1), 1, 1, 2),
			new ChestLootItem(new ItemStack(Item.manaPotion1), 1, 1, 2),
			new ChestLootItem(new ItemStack(Item.healingHerb1), 1, 1, 3),
			new ChestLootItem(new ItemStack(Item.magicHerb1), 1, 1, 3),
			new ChestLootItem(new ItemStack(Item.vialEmpty), 1, 4, 8),
			new ChestLootItem(new ItemStack(Item.vialOfWater), 1, 4, 8),
			new ChestLootItem(new ItemStack(Item.copperCoin), 1, 80, 100),
	});
	
	private final static ChestLootTable LOW_LEVEL_RARE = new ChestLootTable(new ChestLootItem[] { 
			new ChestLootItem(new ItemStack(Item.bronzeBody), 1),  
			new ChestLootItem(new ItemStack(Item.bronzeHelmet), 1),
			new ChestLootItem(new ItemStack(Item.bronzePants), 1),
			new ChestLootItem(new ItemStack(Item.ironIngot), 1),
			new ChestLootItem(new ItemStack(Item.bronzeSword), 1),
			new ChestLootItem(new ItemStack(Item.ironPickaxe), 1),
			new ChestLootItem(new ItemStack(Item.regenerationPotion1), 2, 2, 4),
			new ChestLootItem(new ItemStack(Item.silverCoin), 2, 30, 50),
	});

	public ItemStack[] getLowLevelChestCommon()
	{
		return getChestLoot(LOW_LEVEL_COMMON, 10);
	}

	public ItemStack[] getLowLevelChestRare()
	{
		return getChestLoot(LOW_LEVEL_RARE, 10);
	}
	
	public ItemStack[] getHighLevelChestCommon()
	{
		return getChestLoot(HIGH_LEVEL_COMMON, 10);		
	}

	public ItemStack[] getHighLevelChestRare()
	{
		return getChestLoot(HIGH_LEVEL_RARE, 10);
	}
	
	/**
	 * Uses a custom loot table to generate loot.
	 * @param table the custom loot table to use
	 * @param numberOfStacks the number of items to generate from that loot table
	 * @return an ItemStack[] of size numberOfStacks, containing randomly generated loot from the give loot table
	 */
	public ItemStack[] getLootWithCustomTable(ChestLootTable table, int numberOfStacks)
	{
		return getChestLoot(table, numberOfStacks);
	}
	
	/**
	 * Gets an ItemStack[] based on the given ChestLootTable of length totalLootStacks. This is composed of a pseudo-random selection
	 * of loot from the ChestLootTable, based on probability_weights
	 * @param possibleLoot a ChestLootTable containing possible loot
	 * @param totalLootStacks the number of ItemStacks to generate
	 * @return an ItemStack[] of length totalLootStacks
	 */
	private ItemStack[] getChestLoot(ChestLootTable possibleLoot, int totalLootStacks)
	{
		ItemStack[] loot = new ItemStack[totalLootStacks];
		for(int i = 0; i < loot.length; i++)
		{		
			loot[i] = new ItemStack(possibleLoot.get());
		}
		return loot;
	}

}


