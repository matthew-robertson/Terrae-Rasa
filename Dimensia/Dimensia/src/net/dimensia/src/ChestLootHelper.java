package net.dimensia.src;

import java.util.Random;

public class ChestLootHelper
{
	public ItemStack[] getLowLevelChest_Surface()
	{
		int size = 2 + random.nextInt(7);
		ItemStack[] items = new ItemStack[1 + size];
		
		items[0] = getLowLevelChestRareItem_Surface();
		for(int i = 1; i < size + 1; i++)
		{
			items[i] = getLowLevelChestCommonItem_Surface();
		}
		
		return items;
	}
	
	public ItemStack[] getHighLevelChest_Surface()
	{
		int size = 2 + random.nextInt(7);
		ItemStack[] items = new ItemStack[1 + size];
		
		items[0] = getHighLevelChestRareItem_Surface();
		for(int i = 1; i < size + 1; i++)
		{
			items[i] = getHighLevelChestCommonItem_Surface();
		}
		
		return items;
	}
	
	/**
	 * @deprecated NYI
	 * @return
	 */
	public ItemStack[] getDungeonLevelChest_Surface()
	{
		return null;
	}
	
	private ItemStack getLowLevelChestRareItem_Surface()
	{
		int i = random.nextInt(8);
		
		if(i == 0)
			return new ItemStack(Item.bronzeBody);
		else if(i == 1)
			return new ItemStack(Item.bronzeHelmet);
		else if(i == 2)
			return new ItemStack(Item.bronzeGreaves);
		else if(i == 3)
			return new ItemStack(Item.ironIngot);
		else if(i == 4)
			return new ItemStack(Item.bronzeSword);
		else if(i == 5)
			return new ItemStack(Item.ironPickaxe);
		else if(i == 6)
			return new ItemStack(Item.regenerationPotion1, 2);
		else
			return new ItemStack(Item.silverCoin, random.nextInt(20) + 25);
	}
	
	private ItemStack getLowLevelChestCommonItem_Surface()
	{
		int i = random.nextInt(13);
		
		if(i == 0)
			return new ItemStack(Block.dirt, 5 + random.nextInt(10));
		else if(i == 1)
			return new ItemStack(Block.stone, 5 + random.nextInt(10));
		else if(i == 2)
			return new ItemStack(Item.copperIngot, 1 + random.nextInt(1));
		else if(i == 3)
			return new ItemStack(Item.bronzeIngot);
		else if(i == 4)
			return new ItemStack(Item.silverCoin, 10 + random.nextInt(10));
		else if(i == 5)
			return new ItemStack(Item.woodenArrow, 17 + random.nextInt(10));
		else if(i == 6)
			return new ItemStack(Item.healthPotion1, 1 + random.nextInt(1));
		else if(i == 7)
			return new ItemStack(Item.manaPotion1, 1 + random.nextInt(1));
		else if(i == 8)
			return new ItemStack(Item.healingHerb1, 1 + random.nextInt(2));
		else if(i == 9)
			return new ItemStack(Item.magicHerb1, 1 + random.nextInt(2));
		else if(i == 10)
			return new ItemStack(Item.vialEmpty, 4 + random.nextInt(4));
		else if(i == 11)
			return new ItemStack(Item.vialOfWater, 4 + random.nextInt(4));
		else
			return new ItemStack(Item.copperCoin, 80 + random.nextInt(20));
	}
	
	private ItemStack getHighLevelChestRareItem_Surface()
	{
		int i = random.nextInt(13);
		
		if(i == 0)
			return new ItemStack(Item.ironBody);
		else if(i == 1)
			return new ItemStack(Item.ironHelmet);
		else if(i == 2)
			return new ItemStack(Item.ironGreaves);
		else if(i == 3)
			return new ItemStack(Item.silverBody);
		else if(i == 4)
			return new ItemStack(Item.silverHelmet);
		else if(i == 5)
			return new ItemStack(Item.silverGreaves);
		else if(i == 6)
			return new ItemStack(Item.ironIngot);
		else if(i == 7)
			return new ItemStack(Item.silverIngot);
		else if(i == 8)
			return new ItemStack(Item.silverSword);
		else if(i == 9)
			return new ItemStack(Item.silverPickaxe);
		else if(i == 10)
			return new ItemStack(Item.silverAxe);
		else if(i == 11)
			return new ItemStack(Item.regenerationPotion2, 2 + random.nextInt(2));
		else
			return new ItemStack(Item.silverCoin, random.nextInt(40) + 35);
	}
	
	private ItemStack getHighLevelChestCommonItem_Surface()
	{
		int i = random.nextInt(18);
		
		if(i == 0)
			return new ItemStack(Block.dirt, 5 + random.nextInt(20));
		else if(i == 1)
			return new ItemStack(Block.stone, 5 + random.nextInt(20));
		else if(i == 2)
			return new ItemStack(Item.copperIngot, 1 + random.nextInt(2));
		else if(i == 3)
			return new ItemStack(Item.bronzeIngot, 1 + random.nextInt(1));
		else if(i == 4)
			return new ItemStack(Item.silverCoin, 18 + random.nextInt(18));
		else if(i == 5)
			return new ItemStack(Item.woodenArrow, 18 + random.nextInt(20));
		else if(i == 6)
			return new ItemStack(Item.healthPotion1, 2 + random.nextInt(2));
		else if(i == 7)
			return new ItemStack(Item.manaPotion1, 2 + random.nextInt(2));
		else if(i == 8)
			return new ItemStack(Item.healthPotion2, 1 + random.nextInt(1));
		else if(i == 9)
			return new ItemStack(Item.manaPotion2, 1 + random.nextInt(1));
		else if(i == 10)
			return new ItemStack(Item.healingHerb1, 2 + random.nextInt(4));
		else if(i == 11)
			return new ItemStack(Item.magicHerb1, 2 + random.nextInt(4));
		else if(i == 12)
			return new ItemStack(Item.healingHerb2, 2 + random.nextInt(2));
		else if(i == 13)
			return new ItemStack(Item.magicHerb2, 2 + random.nextInt(2));
		else if(i == 14)
			return new ItemStack(Item.vialEmpty, 4 + random.nextInt(9));
		else if(i == 15)
			return new ItemStack(Item.vialOfWater, 4 + random.nextInt(9));
		else if(i == 16)
			return new ItemStack(Block.glass, 10 + random.nextInt(10));
		else
			return new ItemStack(Item.silverCoin, 5 + random.nextInt(20));
	}
	
	private ItemStack getDungeonLevelChestRareItem_Surface()
	{
		return null;
	}
	
	private ItemStack getDungeonLevelChestCommonItem_Surface()
	{
		return null;
	}
	
	
	
	private final Random random = new Random();
}
