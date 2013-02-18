package net.dimensia.src;
import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * A custom (and very cool) GUI component. An instanceof GuiResizableText automatically resizes based on
 * whether or not the user's mouse is hovering over it. It will increase in size until reaching maxscale 
 * if the user's mouse is hovering over it, or it will also decrease in scale until reaching 
 * basescale if the user's mouse is not hovering over it. It has an approximated mouseover area based
 * on the message, and the scale sizes. This resize will only occur if mouse events are actually checked
 * against the component though.
 * 
 * By default, it also shades text grey at basescale, and white at maxscale. This makes it very clear when
 * a component is able to be pressed. This feature is overrideable, using overrideColor. This will make 
 * the text always be white, at any size. (Useful for visual effects). 
 * 
 * Additionally, by default this component will always center itself
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */

public class GuiResizableText 
{	
	private static TrueTypeFont trueTypeFont;
	private int screenX;
	private int screenY;
	private boolean overrideColor;
	private float maxScale;
	private float baseScale;
	private float scale;
	private int centery;
	private int mouseY;
	private int mouseWidth;
	private int mouseHeight;
	private int mouseX;	
	private String text;
	
	/**
	 * Constructs an instance of GuiResizableText. For a better description of what this class does, see 
	 * the class description.	 
	 * @param message the text of the GuiResizableText instance
 	 * @param base the basescale (minimum scale) of the component
	 * @param max hte maxscale (maximum scale) of the component
	 * @param cy the y position of the component 
	 * @param center whether or not to center the component (this is obsolete, value doesnt matter)
 	 */
	public GuiResizableText(String message, float base, float max, int cy, boolean center)
	{
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 24), false/*DO NOT use antialiasing*/);
		}		
		scale = base;
		baseScale = base;
		maxScale = max;
		text = message;
		overrideColor = false;
		screenX = Display.getWidth();
		screenY = Display.getHeight();
		
		float f = (float)(trueTypeFont.getWidth(text)) / 232;
		float w = (Display.getWidth() * 0.25f) - (f * scale);
		float x = (float)w - (f * 232 / (4 / scale));		
					
		centery = (int)cy;
		mouseY = (int)(cy - 30);
		mouseX = (int)x;
		mouseHeight = 22;
		mouseWidth = (int)(((float)(trueTypeFont.getWidth(text)) + 20.0f) / (2.0f / scale));		
	}
		
	/**
	 * Set bounds for the mouse and drawing (where to draw)
	 */
	public void setBounds(float cy/*CenterY*/, float x/*MouseX*/, float w/*MouseWidth*/) //Set the Mouse/Text Area Bounds
	{
		centery = (int)cy;
		mouseY = (int)(cy - 30);
		mouseX = (int)x;
		mouseHeight = 22;
		mouseWidth = (int)w;
	}
	
	/**
	 * Is the specified vertex inside the bounds of the text?
	 * @param x x location to check against text
	 * @param y y location to check against text
	 * @return whether the specified vector2f is inside the text
	 */
	public boolean inBounds(int x, int y) 
	{
		if(x > mouseX && x < mouseX + mouseWidth && y > mouseY && y < mouseY + mouseHeight) //In bounds
		{
			scale += 0.01f; //increase scale size (so text renders larger)
			if(scale < baseScale) scale = baseScale;
			if(scale > maxScale) scale = maxScale;		
			return true;
		}		
		scale -= 0.01f; //reduce scale size (so text renders smaller)
		if(scale < baseScale) scale = baseScale;
		if(scale > maxScale) scale = maxScale;		
		return false;
	}	
	
	/**
	 * Set bounds for a mouse hover
	 */
	public void setMouse()
	{
		//If the screensize has changed, the mouse listening positions need to be corrected
		float fw = (float)(trueTypeFont.getWidth(text)) / 232;
		float ac = (Display.getWidth() * 0.25f) - (fw * scale);
		float mx = (float)(trueTypeFont.getWidth(text)) + 20.0f;
		setBounds((float)(centery), (float)ac - (fw * 232 / 4), mx / 2); 
	}
	
	/**
	 * Render the text (with colour)
	 */
	public void draw()
	{	
		if(Display.getWidth() != screenX || Display.getHeight() != screenY) //Has the screen been resized?
		{
			setMouse(); //If so recalculate the mouse listening positions
			screenX = Display.getWidth();
			screenY = Display.getHeight();
		}
		
		if(overrideColor) //overrideColor signifies something should be displayed in full white colour
		{
			GL11.glColor4f(1, 1, 1, 1);
		}
		else
		{
			final float greyColor = 0.56078434f; //Base Colour 
			final float toWhiteModifier = 1.785714f; //Modifier to get from Base Colour to white
			float modifiedScale = scale; //Fixes things with a scale that can go below 1.0f 
			float scaleRatio = 1; 
			float f = (maxScale - baseScale) / baseScale;
			
			if(f >= 0.51f) //Is the text able to scale up too large? 
			{
				throw new RuntimeException("Illegal MaxScale Size!"); //If so break everything
			}			
			else if(f < 0.50f) //Is the temp variable 'f' less than 0.5f?
			{
				scaleRatio = 0.5f / f; //if so recalculate the scale ratio
			}			
			else
			{
				scaleRatio = 1.0f;//Otherwise the scaleRatio is 1 
			}
			
			if(baseScale < 1) //Is the baseScale less than 1?
			{
				modifiedScale = 1 + (scale - baseScale) * scaleRatio; //If so modifiedScale needs recalculated
			}
			
			//use of oneOrGreater ensures the colour is always at least the original grey or lighter
			float newColor = greyColor * MathHelper.oneOrGreater((float)((modifiedScale - 1) * scaleRatio * 2.0f) * toWhiteModifier);
						
			GL11.glColor4f(newColor, newColor, newColor, 1); //Set the recalculated colour
		}
		
		float y = centery - 22.0f * (1 - scale); //Adjusted Text Y Position
		float x = (Display.getWidth() * 0.25f) - ((trueTypeFont.getWidth(text) / 232) * scale); //Adjusted Text X position		
		trueTypeFont.drawString(x, y, text, scale, MathHelper.inverseValue(scale)/*Inverse for easier calculations*/, TrueTypeFont.ALIGN_CENTER); //Render the Text
	}
	
	/**
	 * Sets the scales of the text and sets the scale to baseScale
	 * Scale indicates how much to resize the text. This may or may not be 1.0f
	 * @param base the minimum scale
	 * @param max the maximum scale 
	 */
	public void setScales(float base, float max)
	{
		baseScale = base;
		scale = base;
		maxScale = max;
	}
			
	/**
	 * Resets scale to baseScale
	 */
	public void resetScale()
	{
		scale = baseScale;
	}	
}
