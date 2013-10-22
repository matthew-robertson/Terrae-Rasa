package utils;

import java.io.Serializable;

import entry.MPGameLoop;
import enums.EnumColor;

public class ColoredText
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public EnumColor color;
	public String text;
	public int ticksLeft;
	
	public ColoredText(EnumColor color, String text)
	{
		this.color = color;
		this.text = text;
		ticksLeft = 8 * MPGameLoop.TICKS_PER_SECOND;
	}
	
	public ColoredText(EnumColor color, String text, int ticksLeft)
	{
		this.color = color;
		this.text = text;
		this.ticksLeft = ticksLeft;
	}
}
