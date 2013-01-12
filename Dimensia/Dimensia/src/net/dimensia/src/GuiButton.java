package net.dimensia.src;
import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * GuiButton implements a simple button, with a single background texture. Contains
 * methods to draw and check for mouse clicks as well as
 * a method to get the current String value of the button (for external use)
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */

public class GuiButton 
{	
	/**
	 * Constructs a new instance of GuiButton. Initializes the button with the specified
	 * values, and currently can only center the button.
	 * @param values the possible text strings the button can display (and return)
	 * @param x the x position of the button
	 * @param y the y position of the button
	 * @param center whether the button should be centered. currently this value is not used.
	 */
	public GuiButton(String[] values, int x, int y, boolean center)
	{
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 20), false/*DO NOT use antialiasing*/);
		}		

		centerComponent = center;
		this.y = y;
		width = 120;
		height = 30;
		buttonIndex = 0;
		this.values = values;
		screenX = Display.getWidth();
		screenY = Display.getHeight();
		
		if(centerComponent)
		{
			this.x = (int) ((Display.getWidth() * 0.25f) - (width * 0.5f));
		}
		else
		{
			this.x = x;
		}		
	}
	
	/**
	 * Returns the String value currently selected in values[]
	 * @return the String value of the currently selected button value
	 */
	public String getValue()
	{
		return values[buttonIndex];
	}
	
	/**
	 * Gets whether or not a point is inside the button
	 * @param x the x value of the point to compare
	 * @param y the y value of the point to compare
	 * @return whether the point is in bounds or not
	 */
	public boolean inBounds(int x, int y) //Is the specified vertex inside the bounds of the text?
	{
		return (x > this.x && x < this.x + width && y > this.y && y < this.y + height);
	}	
	
	/**
	 * Draws the button, and fixes the position if the screen has been resized
	 */
	public void draw()
	{	
		if(Display.getWidth() != screenX || Display.getHeight() != screenY) //Has the screen been resized?
		{
			if(centerComponent)
			{
				this.x = (int) ((Display.getWidth() * 0.25f) - (width * 0.5f));
			}
			screenX = Display.getWidth();
			screenY = Display.getHeight();
		}
		
		//Texture:
		GL11.glColor4f(1, 1, 1, 1);
		Tessellator t = Tessellator.instance;
		GuiMainMenu.buttonTexture.bind();
		t.startDrawingQuads();		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);		
		t.draw();
		
		//Text:
		float xOffset = (width - ((trueTypeFont.getWidth(values[buttonIndex]) / 2) * 1.125f)) / 2;
		trueTypeFont.drawString(x + xOffset, y + 30, values[buttonIndex], 1, -1, TrueTypeFont.ALIGN_LEFT); //Render the Text	
	}
	
	/**
	 * Sets the button's value to the given value
	 * @param i the number to set buttonIndex to
	 */
	public void setButtonIndex(int i)
	{
		buttonIndex = i;
	}
	
	/**
	 * Increments the value of the button (increases by 1)
	 */
	public void onClick()
	{
		buttonIndex++;
		if(buttonIndex >= values.length)
		{
			buttonIndex = 0;
		}
	}
	
	private static TrueTypeFont trueTypeFont;
	private String[] values;
	private int screenX;
	private int screenY;
	private boolean centerComponent;
	private int buttonIndex;
	private float width;
	private float height;
	private float x;
	private float y;
}
