package net.dimensia.src;
import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * A custom (and very cool) GUI component. An instanceof GuiResizableTextUncentered automatically resizes based on
 * whether or not the user's mouse is hovering over it. It will increase in size until reaching maxscale 
 * if the user's mouse is hovering over it, or it will also decrease in scale until reaching 
 * basescale if the user's mouse is not hovering over it. It has an approximated mouseover area based
 * on the message, and the scale sizes. This resize will only occur if mouse events are actually checked
 * against the component though.
 * <br> <br>
 * Unlike GuiResizableText, this currently does not support different shades of color based on size,
 * and will always remain white. This may or may not change in the future.
 * <br> <br>
 * Regarding Alignment, certain alignments don't actually work. If alignment completely fails, assume
 * the currently used alignment is not supported. Alignments that are guarenteed not to work currently 
 * include:
 * <li> H_CENTER
 * <li> V_CENTER
 * <br>
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiResizableTextUncentered 
{	
	private final int RIGHT_SIDE_OFFSET = 20;
	private final int BUTTON_HEIGHT = 20;
	private ALIGN hAlignment;
	private ALIGN vAlignment;
	private static TrueTypeFont trueTypeFont; /*This has pretty heavy overhead to create. Should it be static?*/
	private int screenX;
	private int screenY;
	private float maxScale;
	private float baseScale;
	private float scale;
	private float x;
	private float y;
	private float mouseY;
	private float mouseWidth;
	private float mouseHeight;
	private float mouseX;	
	private String text;
	
	/**
	 * V, H are for vertical and horizontal
	 * If align_right the x,y are how far from the right of the screen to draw the button, same with align bottom
	 */
	public enum ALIGN
	{
		H_ALIGN_LEFT(),
		H_ALIGN_RIGHT(),
		H_ALIGN_CENTER(),
		V_ALIGN_TOP(),
		V_ALIGN_CENTER(),
		V_ALIGN_BOTTOM();
	}
	
	/**
	 * Constructs an instance of GuiResizableTextUncentered. For a better description of what this class does, see 
	 * the class description.
	 * @param message the text of the GuiResizableTextUncentered instance
	 * @param base the basescale (minimum scale) of the component
	 * @param max the maxscale (maximum scale) of the component
	 * @param x the x position relative to the alignment
	 * @param y the y position relative to the alignment
	 * @param halign the horizontal alignment (Align enum) of the component
	 * @param valign the vertical alignment (Align enum) of the component
	 */
	public GuiResizableTextUncentered(String message, float base, float max, int x, int y, ALIGN halign, ALIGN valign)
	{
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Comic Sans MS", Font.BOLD, 32), false);
		}		
		
		scale = base;
		baseScale = base;
		maxScale = max;
		text = message;
		screenX = Display.getWidth();
		screenY = Display.getHeight();		
		this.x = x;
		this.y = y + BUTTON_HEIGHT;
		hAlignment = halign;
		vAlignment = valign;
		
		setMouse();
	}
	
	/**
	 * This is a special extension, used for the inventory. This saves and quits to the main menu
	 */
	public void saveAndQuit(World world, EntityLivingPlayer player)
	{
		new FileManager().saveAndQuitGame();
	}
		
	/**
	 * Set the mouse bounds
	 */
	public void setBounds(float x, float y, float w, float h)
	{
		mouseY = y;
		mouseX = x;
		mouseHeight = h;
		mouseWidth = w;		
	}	
	
	/**
	 * Is the specified vertex inside the bounds of the text?
	 * @param x x location to check against text
	 * @param y y location to check against text
	 * @return whether the specified vector2f is inside the text
	 */
	public boolean inBounds(int x, int y) //Is the specified vertex inside the bounds of the text?
	{
		if(x > mouseX && x < mouseX + mouseWidth && y < mouseY && y > mouseY - mouseHeight) //In bounds
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
	 * Does math for setBounds(float, float, float, float), based on alignment, then hands off to that
	 */
	public void setMouse()
	{
		float newX = this.x;
		float newY = this.y;
		
		if(hAlignment == ALIGN.H_ALIGN_RIGHT)
		{
			newX = (Display.getWidth() * 0.5f) - x - (trueTypeFont.getWidth(text) / 3.6f) - BUTTON_HEIGHT;
		}
		if(vAlignment == ALIGN.V_ALIGN_BOTTOM)
		{
			newY = (Display.getHeight() * 0.5f) - y;
		}
		
		setBounds(newX, newY, ((trueTypeFont.getWidth(text)) / 3.6f), BUTTON_HEIGHT); 		
	}
	
	/**
	 * Renders the text (no colour)
	 */
	public void draw()
	{	
		if(Display.getWidth() != screenX || Display.getHeight() != screenY) //Has the screen been resized?
		{
			setMouse(); //If so recalculate the mouse listening positions
			screenX = Display.getWidth();
			screenY = Display.getHeight();
		}
				
		GL11.glColor4f(1, 1, 1, 1);		
		float x = this.x;
		float y = this.y;
		int textAlign = TrueTypeFont.ALIGN_LEFT;
		
		if(hAlignment == ALIGN.H_ALIGN_RIGHT) //Right alignment 
		{
			textAlign = TrueTypeFont.ALIGN_RIGHT;
			x = (Display.getWidth() * 0.5f) - x - RIGHT_SIDE_OFFSET;
		}
		else if(hAlignment == ALIGN.H_ALIGN_LEFT) //Left alignment
		{
			textAlign = TrueTypeFont.ALIGN_LEFT;
			x = 3;
		}
		if(vAlignment == ALIGN.V_ALIGN_BOTTOM) //Bottom alignment
		{
			y = (Display.getHeight() * 0.5f) - y;
		}		
		if(vAlignment == ALIGN.V_ALIGN_TOP) //Top alignment
		{
			y = 3 + trueTypeFont.getLineHeight() * scale;
		}
		
		//Text
		trueTypeFont.drawString(Render.getCameraX() + x, Render.getCameraY() + y, text, scale, MathHelper.inverseValue(scale)/*Inverse for easier calculations*/, textAlign); //Render the Text		
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
}