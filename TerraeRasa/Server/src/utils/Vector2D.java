package utils;

public class Vector2D 
{
	public double x;
	public double y;
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D multiplyScalar(double f){
		return new Vector2D(x * f, y * f);
	}
	
	public Vector2D addF(Vector2D vec)
	{
		return new Vector2D(x + vec.x, y + vec.y);
	}
	
	public Vector2D add(Vector2 vec)
	{
		return new Vector2D(x + vec.x, y + vec.y);
	}
	
	public Vector2D subtractF(Vector2D vec)
	{
		return new Vector2D(x - vec.x, y - vec.y);
	}
	
	public Vector2D subtract(Vector2 vec)
	{
		return new Vector2D(x - vec.x, y - vec.y);
	}
	
	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public String toString()
	{
		return "<" + x + "," + y + ">";
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}