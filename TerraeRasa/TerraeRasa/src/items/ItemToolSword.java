package items;

public class ItemToolSword extends ItemTool
{
	protected ItemToolSword(int i)
	{
		super(i, "Sword Hit");
		swingTime = 2.1;
		setXBounds(new double[] { 11.0/16, 16.0/16, 16.0/16, 6.0/16, 1.0/16 });
		setYBounds(new double[] { 1.0/16, 1.0/16, 6.0/16, 16.0/16, 11.0/16 });	
		//Old hitbox
		//setXBounds(new double[] { 13.0/16, 16.0/16, 16.0/16, 4.0/16, 1.0/16 });
		//setYBounds(new double[] { 1.0/16, 1.0/16 ,  4.0/16, 16.0/16, 13.0/16 });
	}
}
