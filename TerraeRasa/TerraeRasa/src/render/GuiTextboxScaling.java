package render;
import java.awt.Font;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


import utils.MathHelper;

/**
 * GuiTextboxScaling is a rewrite of the original textbox intended to scale with screen size. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiTextboxScaling extends GuiComponent
{	
	private int MAX_TEXT_CHARACTERS;
	private static TrueTypeFont trueTypeFont; /*This has pretty heavy overhead to create. Should it be static?*/
	private double textScale;	
	private boolean isFocused;	
	private String text;
	
	/**
	 * Constructs an instance of GuiTextBox. Initializes the textbox with no text in it, at the specified
	 * position on the screen, focus is disabled by default.
	 * @param x the x position on the screen
	 * @param y the y position on the screen
	 * @param center whether or not to center the component (this currently doesn't work, this component will always be centered)
	 */
	public GuiTextboxScaling(double x, double y, double width, double height)
	{
		super();
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 20), false/*DO NOT use antialiasing*/);
		}		
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		text = "";
		textScale = 0.85f;
		MAX_TEXT_CHARACTERS = 20;
	}
	
	/**
	 * Renders the textbox to screen
	 */
	public void draw()
	{	
		double x = (Display.getWidth() * 0.5 * this.x);
		double y = Display.getHeight() * 0.5 * this.y;
		if(stopVerticalScaling)
		{
			y = this.y;
		}
		drawBounds();
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString((float)(x + 8),
				(float)(y + 27), 
				text + "_", 
				(float)textScale, 
				(float)MathHelper.inverseValue(textScale), 
				TrueTypeFont.ALIGN_LEFT); //Render the Text
	}
	
	/**
	 * Deals with keyboard events
	 * @param c character to be tested
	 * @param i integer value of the character (This may or may not be ascii)
	 */
	public void updateText(char c, int i)
	{
		if(i == 42 || i == 28) //garbage values (enter and caps lock, I believe)
		{
		}
		else if(i == 14) //backspace was pressed
		{
			if(text.length() >= 1)
			{
				text = removeLastCharacter(text);
			}
		}
		else if(text.length() < MAX_TEXT_CHARACTERS) //valid character, append to the end if there's space
		{
			if(trueTypeFont.getWidth((text + c)) < 330)
			{
				text += c;
			}
		}
	}
	
	/**
	 * Removes the last character of the textbox (deletes)
	 * @param str string to remove a character of.
	 * @return str without the last character
	 */
	public String removeLastCharacter(String str)
	{
		str = str.substring(0, str.length() - 1);
		return str;
	}
	
	/**
	 * Renders the bounds and background of the textbox
	 */
	public void drawBounds()
	{
		double x = (Display.getWidth() * 0.5 * this.x);
		double y = Display.getHeight() * 0.5 * this.y;
		double width = this.width * 0.5 * Display.getWidth();
		double height = 530 * this.height * 0.5F;
		if(stopVerticalScaling)
		{
			y = this.y;
		}
		
		GL11.glColor4f(0, 0, 0, 1);
		Tessellator t = Tessellator.instance;
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		t.startDrawingQuads();		
	    //Top Line
		t.addVertexWithUV(x, y + 1, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + 1, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		//Bottom Line
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y + height - 1, 0, 1, 0);
	    t.addVertexWithUV(x, y + height - 1, 0, 0, 0);
		//Left Line
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + 1, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + 1, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		//Right Line
	    t.addVertexWithUV(x + width - 1, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x + width - 1, y, 0, 0, 0);		
	    t.draw();	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		//Texture
		GuiMainMenu.buttonTexture.bind();
		GL11.glColor4f(1, 1, 1, 1);
		t.startDrawingQuads();		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
	}
	
	/**
	 * Sets the textbox's text to the specified String
	 * @param str the String to set the textbox's text to
	 */
	public void setText(String str)
	{
		text = str;
	}

	/**
	 * Gets the text currently held in the textbox
	 * @return the text currently in the textbox
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Gets whether or not the textbox is focused
	 * @return whether the textbox is focused or not
	 */
	public boolean isFocused()
	{
		return isFocused;
	}
	
	/**
	 * Sets the textbox as focused
	 */
	public void setFocused()
	{
		isFocused = true;
	}
	
	/**
	 * Frees the textbox's focus
	 */
	public void freeFocused()
	{
		isFocused = false;
	}

	public void onClick(int x, int y) {
		
	}
}
