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
public class GuiButtonImproved 
{	
	private static TrueTypeFont trueTypeFont;
	private Texture renderTexture;
	private String[] values;
	private int screenX;
	private int screenY;
	/** Determines whether the component will be centered on the X axis, which automatically updates the x position to be centered on screen size change*/
	//private boolean centerComponent;
	private int buttonIndex;
	/** The height of the button as a % of the screen, between 0.0F and 1.0F*/
	private float width;
	/** The height of the button as a % of the screen, between 0.0F and 1.0F*/
	private float height;
	/** The x position as a % of the screen, between 0.0F and 1.0F */
	private float x;
	/** The y position as a % of the screen, between 0.0F and 1.0F */
	private float y;
	
	/**
	 * Constructs a new instance of GuiButton. Initializes the button with the specified
	 * values, and currently can only center the button.
	 * @param values the possible text strings the button can display (and return)
	 * @param x the x position of the button
	 * @param y the y position of the button
	 * @param center whether the button should be centered. currently this value is not used.
	 */
	public GuiButtonImproved(String[] values, float x, float y, boolean center)
	{
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 20), false/*DO NOT use antialiasing*/);
		}		

		//centerComponent = center;
		this.y = y;
		width = 0.4F;
		height = 0.1F;
		buttonIndex = 0;
		this.values = values;
		screenX = Display.getWidth();
		screenY = Display.getHeight();
		renderTexture = GuiMainMenu.buttonTexture;
		
		/*
		if(centerComponent)
		{
			this.x = (int) ((Display.getWidth() * 0.25f) - (width * 0.5f));
		}
		else
		{
			
		}	
		*/
		this.x = x;
	}
	
	/**
	 * Sets the width of the button as a percent of the screen width
	 * @param width the new width of the width, in percent, between 0.0F and 1.0F
	 */
	public void setWidth(float width)
	{
		this.width = width;
	}
	
	/**
	 * Sets the height of the button as a percent of the screen height
	 * @param height the new height of the button, in percent, between 0.0F and 1.0F
	 */
	public void setHeight(float height)
	{
		this.height = height;
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
			screenX = Display.getWidth();
			screenY = Display.getHeight();
		}
		
		//Texture:
		GL11.glColor4f(1, 1, 1, 1);
		Tessellator t = Tessellator.instance;
		renderTexture.bind();
		t.startDrawingQuads();
		
		float width = Display.getWidth() * this.width * 0.5F;
		float height = Display.getHeight() * this.height * 0.5F;
		float x = Display.getWidth() * this.x * 0.5F;
		float y = Display.getHeight() * this.y * 0.5F;
		
		if(width < (trueTypeFont.getWidth(values[buttonIndex]) / 2))
		{
			width = trueTypeFont.getWidth(values[buttonIndex]) / 2;
		}
		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);		
		t.draw();
		
		
		//Text:
		float xOffset = x + width/2 * 0.95f;//trueTypeFont.getWidth(values[buttonIndex]) / 2;
		float yOffset = y + height - (height - trueTypeFont.getHeight(values[buttonIndex])) / 2;
			
		trueTypeFont.drawString(xOffset, yOffset, values[buttonIndex], 1, -1, TrueTypeFont.ALIGN_CENTER); //Render the Text	
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
	
	/**
	 * Gets the texture the button is currently rendering
	 * @return the texture currently used by this button
	 */
	public Texture getRenderTexture()
	{
		return renderTexture;
	}
	
	/**
	 * Assigns a new texture to the button
	 * @param texture the texture to assign to the button
	 */
	public void setRenderTexture(Texture texture)
	{
		this.renderTexture = texture;
	}
}

