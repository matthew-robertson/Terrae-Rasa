package math;

public class Point 
{
	private double[] values;
	
	public Point(int size)
	{
		this.values = new double[size];
	}
	
	public Point(double[] initialValues)
	{
		this.values = new double[initialValues.length];
		for(int i = 0; i < this.values.length; i++)
		{
			this.values[i] = initialValues[i];
		}
	}
	
}
