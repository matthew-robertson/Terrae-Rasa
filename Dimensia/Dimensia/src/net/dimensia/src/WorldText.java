package net.dimensia.src;

import java.util.Random;

/**
 * WorldText is a data structure for text that is rendered from the world object. An example of this is text that shows 
 * from taking damage, or healing from a potion (which are red and green respectively). Each text piece is assigned a 
 * message, position, duration (in ticks), and a colour. If randomize is true, the position of each texture will change 
 * by a small amount to make it appear random.
 * @author alec
 *
 */
public class WorldText 
{
	private final static Random random = new Random();
	public EnumColor color; 
	public int x;
	public int y;
	public String message;
	public int ticksLeft;
	
	/**
	 * Constructs a new WorldText object, used to store a piece of text data to display in the world.
	 * @param message the textual value of this WorldText
	 * @param x the x position of the text, before randomization
	 * @param y the y position of the text, before randomization
	 * @param ticks the duration of the text, in ticks
	 * @param color the EnumColor which determines the render colour
	 * @param randomizePosition whether to randomly adjust the position of this text
	 */
	public WorldText(String message, int x, int y, int ticks, EnumColor color, boolean randomizePosition)
	{
		this.x = x;
		this.y = y;
		this.message = message;
		this.ticksLeft = ticks;
		this.color = color;
		
		if(randomizePosition)
		{
			x -= 10;
			x += random.nextInt(25);
			y -= 10;
			y += random.nextInt(20);
		}
	}
}
