package utils;


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
	private ItemStack item;
	private double probabilityWeight;
	private int minStackSize;
	private int maxStackSize; 
	private int stackSizeRange;
	
	/**
	 * Constructs a new ChestLootItem with the given Itemstack and proabilityweight, as well as a minimum
	 * and maximum stack size of 1.
	 * @param item the ItemStack reward of this ChestLootItem
	 * @param probabilityWeight a double indicating the probability of this ChestLootItem being chosen
	 */
	public ChestLootItem(ItemStack item, double probabilityWeight)
	{
		this.setItem(item);
		this.setProbabilityWeight(probabilityWeight);
		this.setMinStackSize(1);
		this.maxStackSize = 1;
		setStackSizeRange(0);
	}
	
	/**
	 * Constructs a new ChestLootItem with the given Itemstack, proabilityweight, minimum stack size, and
	 * maximum stack size.
	 * @param item the ItemStack reward of this ChestLootItem
	 * @param probabilityWeight a double indicating the probability of this ChestLootItem being chosen
	 */
	public ChestLootItem(ItemStack item, double probabilityWeight, int minStackSize, int maxStackSize)
	{
		this.setItem(item);
		this.setProbabilityWeight(probabilityWeight);
		this.setMinStackSize(minStackSize);
		this.maxStackSize = maxStackSize;
		setStackSizeRange(maxStackSize - minStackSize); 
	}

	public double getProbabilityWeight() {
		return probabilityWeight;
	}

	public void setProbabilityWeight(double probabilityWeight) {
		this.probabilityWeight = probabilityWeight;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getMinStackSize() {
		return minStackSize;
	}

	public void setMinStackSize(int minStackSize) {
		this.minStackSize = minStackSize;
	}

	public int getStackSizeRange() {
		return stackSizeRange;
	}
	
	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	public void setStackSizeRange(int stackSizeRange) {
		this.stackSizeRange = stackSizeRange;
	}
}
