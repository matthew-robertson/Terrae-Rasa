package client.utils;

public class HeldLightMap {

	public double[][] lightmap;
	public int x;
	public int y;	
	public int radius;
	
	public HeldLightMap()
	{
		lightmap = new double[0][0];
		x = 0;
		y = 0;
		radius = 0;
	}
	
	public HeldLightMap(int x, int y, int radius, double[][] lightmap)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.lightmap = lightmap;
	}
}
