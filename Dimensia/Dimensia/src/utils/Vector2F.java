package utils;

public class Vector2F 
{
	public float x;
	public float y;
	
	public Vector2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2F multiplyScalar(float f){
		return new Vector2F(x * f, y * f);
	}
	
	public Vector2F addF(Vector2F vec)
	{
		return new Vector2F(x + vec.x, y + vec.y);
	}
	
	public Vector2F add(Vector2 vec)
	{
		return new Vector2F(x + vec.x, y + vec.y);
	}
	
	public Vector2F subtractF(Vector2F vec)
	{
		return new Vector2F(x - vec.x, y - vec.y);
	}
	
	public Vector2F subtract(Vector2 vec)
	{
		return new Vector2F(x - vec.x, y - vec.y);
	}
	
	public float length()
	{
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public String toString()
	{
		return "<" + x + "," + y + ">";
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
}