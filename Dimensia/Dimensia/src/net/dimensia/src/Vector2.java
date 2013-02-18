package net.dimensia.src;

public class Vector2 
{
	public int x;
	public int y;
	
	public Vector2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2 subtract(Vector2 vec)
	{
		return new Vector2(x - vec.x, y - vec.y);
	}
	
	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
