package utils;

import items.Item;

import java.util.Random;


/**
 * A chest loot table stores an array of ChestLootItems and their respective probability weights. Upon being created 
 * probability weights are assigned to each Item, based on each ChestLootItem's probabilityWeight.
 * <br><br>
 * ChestLootTable exposes only 1 public method, {@link #get()} which retrieves a pseudo-random ItemStack from the chest's
 * loot table.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ChestLootTable 
{
	private final Random random = new Random();
	private ChestLootItem[] possibleLoot;
	private double[] dropBounds;

	public ChestLootTable(ChestLootItem[] possibleLoot)
	{
		this.possibleLoot = possibleLoot;
		createLootTable();
	}	
	
	/**
	 * Uses a quick and dirty way to get which loot item should be picked. From 0.0D to 1.0D, a range is assigned
	 * to each ChestLootItem based on its probabiltyWeight, which will later be used in get() to retrieve an ItemStack.
	 */
	private void createLootTable()
	{
		int totalWeight = 0;
		for(int i = 0; i < possibleLoot.length; i++)
		{
			totalWeight += possibleLoot[i].getProbabilityWeight();
		}
		dropBounds = new double[possibleLoot.length];
		
		double buffer = 0.0D;
		for(int i = 0; i < dropBounds.length; i++)
		{
			double probability = (double)possibleLoot[i].getProbabilityWeight() / (double)totalWeight;
			dropBounds[i] = buffer + probability;
			buffer += probability;
		}
	}
	
	/**
	 * Gets a pseudo-random item from the possible ChestLootItems, based on the probabilityWeights of each item.
	 * @return a pseudo-random item from the possible ChestLootItems
	 */
	public ItemStack get()
	{
		double value = random.nextDouble();
		
		for(int i = 0; i < dropBounds.length; i++)
		{
			if(i == 0)
			{
				if(value < dropBounds[0])
				{
					return new ItemStack(possibleLoot[0].getItem().getItemID(), 
							possibleLoot[0].getMinStackSize() + 
							((possibleLoot[0].getStackSizeRange() > 0) ? random.nextInt(possibleLoot[0].getStackSizeRange()) : 0));
				}
			}			
			if(value < dropBounds[i])
			{
				if(value > dropBounds[i - 1] && value < dropBounds[i])
				{
					return new ItemStack(possibleLoot[i].getItem().getItemID(), 
							possibleLoot[i].getMinStackSize() + 
							((possibleLoot[i].getStackSizeRange() > 0) ? random.nextInt(possibleLoot[i].getStackSizeRange()) : 0));
				}
			}
		}
		
		//Default loot is just 10-30 silver, no matter what chest. This should never be hit and is a 
		//final failsafe.
		return new ItemStack(Item.silverCoin, 10 + random.nextInt(20));
	}

}
