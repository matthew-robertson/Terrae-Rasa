package net.dimensia.src;

public class TemporaryTextStore 
{
	public TemporaryTextStore(String message, int x, int y, int ticks, char type)
	{
		this.x = x;
		this.y = y;
		this.message = message;
		this.ticksLeft = ticks;
		this.type = type;
	}
	
	public char type; //r->damage; g->healing
	public int x;
	public int y;
	public String message;
	public int ticksLeft;
}
