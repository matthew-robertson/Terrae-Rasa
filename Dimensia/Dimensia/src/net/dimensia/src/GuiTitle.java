package net.dimensia.src;

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
	public GuiTitle(String value, float x, float y, float width, float height)
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
//		Tessellator t = Tessellator.instance;
//		renderTexture.bind();
//		t.startDrawingQuads();
		
		float width = Display.getWidth() * this.width * 0.5F;
		float height = Display.getHeight() * this.height * 0.5F;
		float x = Display.getWidth() * this.x * 0.5F;
		float y = Display.getHeight() * this.y * 0.5F;
		
		//if(width < (trueTypeFont.getWidth(values[buttonIndex]) / 2))
		//{
		//	width = trueTypeFont.getWidth(values[buttonIndex]) / 2;
		//}
//		
//	    t.addVertexWithUV(x, y + height, 0, 0, 1);
//	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
//	    t.addVertexWithUV(x + width, y, 0, 1, 0);
//	    t.addVertexWithUV(x, y, 0, 0, 0);		
//		t.draw();
//		
		
		//Text:
		float xOffset = x + width/2 * 0.95f;//trueTypeFont.getWidth(values[buttonIndex]) / 2;
		float yOffset = y + height - (height - trueTypeFont.getHeight(value)) / 2;
			
		trueTypeFont.drawString(xOffset, yOffset, value, 0.7f, -1, TrueTypeFont.ALIGN_CENTER); //Render the Text	
	}
}
