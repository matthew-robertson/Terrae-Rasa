package math;

/**
 * Vector3 is an extension of the Vector class that provides specialised support for 3D space.
 * For all intents and purposes the values in Vector3 can be refered to as x, y, and z.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class Vector3 extends Vector
{
	private static final long serialVersionUID = 1L;

	public Vector3()
	{
		super(3);
	}
	
	public Vector3(double x, double y, double z) 
	{
		super(new double[] { x, y, z });
	}
	
	//this X v
		public Vector crossProduct(Vector v)
		{
			return new Vector(new double[] {
				this.values[1] * v.values[2] - this.values[2] * v.values[1],
				-1 * (this.values[0] * v.values[2] - this.values[2] * v.values[0]),
				this.values[0] * v.values[1] - this.values[1] * v.values[0] 
			});		
		}
		
		public static void main(String[]
				 args)
		{
			System.out.println(new Vector3(3, 0, 5).crossProduct(new Vector3(-1, -4, 4)));
		}

}
