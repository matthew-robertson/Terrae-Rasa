package net.dimensia.src;

/**
 * ChestLootItem is a data structure containing information on a piece of loot in a ChestLootTable.
 * ChestLootItem contains an ItemStack, probabiltyWeight, minimum stack size, maximum stack size, and
 * the difference between maximum and minimum stack sizes. 
 * <br>
 * A probability weight is a value indicating how likely this item it to come up. The chance is 
 * (probabilityWeight / total_probability_weight_of_table).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ChestLootItem 
{
	public ItemStack item;
	public double probabilityWeight;
	public int minStackSize;
	public int maxStackSize; 
	public int stackSizeRange;
	
	/**
	 * Constructs a new ChestLootItem with the given Itemstack and proabilityweight, as well as a minimum
	 * and maximum stack size of 1.
	 * @param item the ItemStack reward of this ChestLootItem
	 * @param probabilityWeight a double indicating the probability of this ChestLootItem being chosen
	 */
	public ChestLootItem(ItemStack item, double probabilityWeight)
	{
		this.item = item;
		this.probabilityWeight = probabilityWeight;
		this.minStackSize = 1;
		this.maxStackSize = 1;
		stackSizeRange = 0;
	}
	
	/**
	 * Constructs a new ChestLootItem with the given Itemstack, proabilityweight, minimum stack size, and
	 * maximum stack size.
	 * @param item the ItemStack reward of this ChestLootItem
	 * @param probabilityWeight a double indicating the probability of this ChestLootItem being chosen
	 */
	public ChestLootItem(ItemStack item, double probabilityWeight, int minStackSize, int maxStackSize)
	{
		this.item = item;
		this.probabilityWeight = probabilityWeight;
		this.minStackSize = minStackSize;
		this.maxStackSize = maxStackSize;
		stackSizeRange = maxStackSize - minStackSize; 
	}
}
