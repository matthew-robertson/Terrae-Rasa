package net.dimensia.src;

/**
 * MonsterDrop implements a simple way to store a drop for a monster. 
 * <br><br>
 * Data for the ItemStack, chance of it dropping (1/rollMaximum), minimum drop amount, and maximum drop amount are stored. The drop is determined by the following
 * process (this is done outside of MonsterDrop.java, in EntityEnemy.getDrops()):
 * <ol>
 * 	<li>See if the drop will actually occur
 *  <li>If the drop occurs, get the stack size where: minimum_drop_amount <= x <= maximum_drop_amount
 *  <li>Repeat for all drops
 *  <li>Return the applicable drops to be added to the itemsList in world
 * </ol>
 * Generally a declaration of this class is final, and will not change so there are only getters for the values, no setters. Values are assigned at creation through a constructor:
<pre>
	{@link #MonsterDrop(ItemStack)}
	
	{@link #MonsterDrop(ItemStack, int, int, int)}
</pre>
 * MonsterDrop(ItemStack) creates a guarenteed drop, of stack size 1. 
 * <br><br>
 * MonsterDrop(ItemStack, int, int, int) provides more customization. A minimum, maximum, and rollMax
 * are set, respectively. (Note: (1/rollMax) dictates the probability of an Item dropping). 
 * <br><br>
 * For varied chances of obtaining the same drop, but with a different quantity, use 2 separate MonsterDrops. For example, a 33% to drop 5 coal and 
 * a 66% chance to drop 2 coal on death must use 2 different drops. However, a 50% chance to drop 2-5 coal would not require this.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MonsterDrop
{	
	private int rollMaximum;
	private int minimum;
	private int maximum;
	private ItemStack drop;

	/**
	 * Constructs a new monster drop, with only the ItemStack. The chance of the drop is assumed to be 100% and maximum/minimum drop amount is 1. In other words, a 
	 * Guaranteed drop can be registered using this constructor.
	 * @param stack the stack to be dropped on death
	 */
	public MonsterDrop(ItemStack stack)
	{
		this.drop = stack;
		this.minimum = 1;
		this.maximum = 1;
		this.rollMaximum = 1;
	}
	
	/**
	 * Constructs a new monster drop, with more custom settings. Drop minimums, maximums, and maximum rolls can be registered using this constructor, 
	 * in addition to the ItemStack used to store the drop. 
	 * @param stack the stack to be dropped on death
	 * @param min minimum amount that has to be dropped
	 * @param max maximum amount that has to be dropped
	 * @param rollMax 1/rollMax is the chance the item drops
	 */
	public MonsterDrop(ItemStack stack, int min, int max, int rollMax)
	{
		this.drop = stack;
		this.minimum = min;
		this.maximum = max;
		this.rollMaximum = rollMax;
	}
	
	public int getRollMaximum()
	{
		return rollMaximum;
	}
	
	public int getMaximum()
	{
		return maximum;
	}
	
	public int getMinimum()
	{
		return minimum;
	}
	
	public ItemStack getDrop()
	{
		return new ItemStack(drop);
	}
}
