package net.dimensia.src;

public class Vector2F 
{
	public float x;
	public float y;
	
	public Vector2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2F subtract(Vector2 vec)
	{
		return new Vector2F(x - vec.x, y - vec.y);
	}
	
	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public String toString()
	{
		return "<" + x + "," + y + ">";
	}
}
