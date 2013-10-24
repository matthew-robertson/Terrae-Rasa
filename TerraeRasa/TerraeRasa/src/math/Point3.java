package math;

public class Point3 extends Point
{
	public Point3()
	{
		super(2);
	}
	
	public Point3(double x, double y, double z) 
	{
		super(new double[] { x, y, z });
	}

}
