package math;

import java.io.Serializable;

/**
 * Vector is an implementation of many different vector operations that can be performed on nth 
 * dimensional vectors. Values are maintained as doubles and Vectors can be serialized if 
 * required. See the javadoc for the different operations a Vector or extension thereof.
 * can do.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class Vector
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** An array of values contained in this Vector, allowing for nth dimensional math. */
	protected double[] values;
	
	/**
	 * Constructs a new Vector with the specified number of elements. For example 3 will create a Vector
	 * of size 3 which can be represented as <x,y,z> in 3D math.
	 * @param size the number of elements contained in the freshly created Vector.
	 */
	public Vector(int size)
	{
		this.values = new double[size];
	}
	
	/**
	 * Constructs a Vector with the given initial values. The size of a Vector that is created
	 * cannot expand beyond the size of the initial double[] given.
	 * @param initialValues a double[] of values by which to initialize this Vector
	 */
	public Vector(double[] initialValues)
	{
		this.values = new double[initialValues.length];
		for(int i = 0; i < this.values.length; i++)
		{
			this.values[i] = initialValues[i];
		}
	}	
	
	/**
	 * Implements a copy constructor for Vector. This will copy over the values in a Vector 
	 * object and create a deep copy.
	 * @param vector the Vector to copy
	 */
	public Vector(Vector vector)
	{
		//TODO: implement this
	}
	
	/**
	 * Projects this Vector onto a provided Vector. This will yield a distance between two points if the length of
	 * the resulting Vector is taken. To get another Vector projected onto this one, simply call that Vector's 
	 * projectOnto(...) and provide this Vector as an argument. 
	 * @param vectorProjectedOnto the Vector to project this Vector onto
	 * @return this Vector projected onto the provided Vector
	 */
	public Vector projectOnto(Vector vectorProjectedOnto)
	{
		//Uses the following formula to project v onto u:
		//u dot v
		//-------  u = v projected onto u
		//u dot u
		//
		//Written another way, the formula is: 
		//v projected onto u = ((u dot v)/(u dot u))u 
		//
		Vector v = new Vector(vectorProjectedOnto.getValues());
		double scalar = this.dotProduct(v) / (v.dotProduct(v));
		v.scalarMultiply(scalar);
		return v;
	}
	
	/**
	 * Gets the dot product of this and another Vector. Given the properties of Vectors it should not matter
	 * the order by which Vectors are given to this method.
	 * @param v the Vector which will be used together with this Vector when taking a dot product
	 * @return the dot product of this Vector and the provided Vector v
	 */
	public double dotProduct(Vector v)
	{
		//Uses the formula: <x1,y1,z1> dot <x2,y2,z2> = <x1*x2,y1*y2,z1*z2> (et cetera)s
		double[] values = new double[this.values.length];
		for(int i = 0; i < values.length; i++)
		{
			values[i] = this.values[i] * v.values[i];
		}		
		double total = 0;
		for(int i = 0; i < values.length; i++)
		{
			total += values[i];
		}
		return total;
	}

	/**
	 * Multiplies a scalar onto this Vector, changing it's magnitude and possibly inverting its direction.
	 * @param scalar the real number to multiply onto this Vector
	 */
	public void scalarMultiply(double scalar)
	{
		for(int i = 0; i < values.length; i++)
		{
			values[i] = values[i] * scalar;
		}
	}
	
	/**
	 * Gets the length of this Vector assuming it is a Vector in standard position by taking the 
	 * square root of the dot product.
	 * @return the length of this Vector
	 */
	public double length()
	{
		//Uses the formula: length = sqrt(Vector dot Vector)
		return Math.sqrt(dotProduct(this));
	}
	
	/**
	 * Formats the Vector into plain-text form using triangle end brackets.
	 */
	public String toString()
	{
		String text = "<"; 
		for(int i = 0; i < values.length; i++)
		{
			text += values[i];
			if(i != values.length - 1)
			{
				text += ",";
			}
		}
		text += ">";
		return text;
	}
	
	/**
	 * Adds the values in the given Vector to the values in this Vector. 
	 * @param v the Vector to add to this Vector
	 */
	public void addVector(Vector v)
	{
		for(int i = 0; i < values.length; i++)
		{
			this.values[i] += v.getValues()[i];
		}
	}
	
	/**
	 * Subtracts the values in the given Vector from the values in this Vector. 
	 * @param v the Vector to subtract from this Vector
	 */
	public void subtractVector(Vector v)
	{
		for(int i = 0; i < values.length; i++)
		{
			this.values[i] -= v.getValues()[i];
		}
	}
	
	/**
	 * Gets a unit vector which has the same direction as this Vector. Unit Vectors are vectors of 
	 * length 1.
	 * @return a Vector of the same direction as this one, but of unit length
	 */
	public Vector getAsUnitVector()
	{
		double length = this.length();
		double[] newValues = new double[this.values.length];
		for(int i = 0; i < this.values.length; i++)
		{
			newValues[i] = this.values[i] / length;
		}
		return new Vector(newValues);
	}
	
	/**
	 * Gets the values contained in this Vector. This array's length for correspond to the dimension of the Vector. Ex.
	 * a 9th dimensional Vector will yield an array of size 9.
	 * @return the values contained in this Vector
	 */
	public double[] getValues()
	{
		return values;
	}
	
	/**
	 * Gets the angle between this vector and another Vector v. This yields an angle in radians following the 
	 * same return rules as Math.acos(double).
	 * @param v the Vector to use in finding the angle
	 * @return a double value indicating the angle between this Vector and Vector v.
	 */
	public double angleBetween(Vector v)
	{
		double top = v.dotProduct(this);
		double bottom = v.length() * this.length();
		return Math.acos(top/bottom);
	}
	
	public boolean equals(Vector v)
	{
		//TODO: implement this
		return false;
	}
	
	public boolean isParallel(Vector v)
	{
		//TODO: implement this
		return false;
	}
	
	public boolean isPerpendicular(Vector v)
	{
		//TODO: implement this
		return false;
	}
}
