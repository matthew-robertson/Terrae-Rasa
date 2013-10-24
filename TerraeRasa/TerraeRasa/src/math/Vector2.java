package math;

/**
 * Vector2 is an extension of Vector that deals only with 2 dimensional Vectors in the Cartesian plane.
 * For all intents and purposes the values contained in this Vector can be referred to as x and y.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class Vector2 extends Vector
{
	private static final long serialVersionUID = 1L;

	public Vector2()
	{
		super(2);
	}
	
	public Vector2(double x, double y) 
	{
		super(new double[] { x, y });
	}
	
	public Vector2(Vector2 v)
	{
		super(2);
		this.values[0] = v.getX();
		this.values[1] = v.getY();
	}

	public void setX(double x)
	{
		this.values[0] = x;
	}
	
	public void setY(double y)
	{
		this.values[1] = y;
	}
	
	public double getX()
	{
		return this.values[0];
	}
	
	public double getY()
	{
		return this.values[1];
	}
}
