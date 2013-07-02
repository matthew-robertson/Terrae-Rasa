package render;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiTitle extends GuiComponent
{
	/***/
	private String value;
	
	/**
	 * Constructs a new instance of GuiButton and initializes the button with the specified
	 * values.
	 * @param values the possible text strings the button can display (and return)
	 * @param x the x position of the button, as a percent of the screen, from 0.0F to 1.0F 
	 * @param y the y position of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param width the width of the button, as a percent of the screen, from 0.0F to 1.0F
	 * @param height the height of the button, as a percent of the screen, from 0.0F to 1.0F
	 */
	public GuiTitle(String value, double x, double y, double width, double height)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
	}
	
	public void onClick(int x, int y) 
	{
	}

	public void draw() 
	{
		//Texture:
		GL11.glColor4f(1, 1, 1, 1);		
		double width = Display.getWidth() * this.width * 0.5F;
		double height = 530 * this.height * 0.5F;
		double x = Display.getWidth() * this.x * 0.5F;
		double y = Display.getHeight() * this.y * 0.5F;
		if(stopVerticalScaling)
		{
			y = this.y;
		}
		//Text:
		float xOffset = (float) (x + width/2 * 0.95f);//trueTypeFont.getWidth(values[buttonIndex]) / 2;
		float yOffset = (float) (y + height - (height - trueTypeFont.getHeight(value)) / 2);
			
		trueTypeFont.drawString(xOffset, yOffset, value, 0.7f, -1, TrueTypeFont.ALIGN_CENTER); //Render the Text	
	}
}
