package items;

public class ItemToolPickaxe extends ItemTool
{
	protected ItemToolPickaxe(int i)
	{
		super(i, "Pick Hit");
		swingTime = 1.8; 
		setXBounds(new double[] { 6F/16, 15F/16, 16F/16, 3F/16 });
		setYBounds(new double[] { 0F/16, 3F/16 ,  9F/16, 15F/16 });	
	}
}