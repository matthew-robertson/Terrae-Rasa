package render;

import hardware.Keys;
import hardware.LegalKeypresses;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import client.ClientsideCommands;

import transmission.ClientUpdate;
import utils.ColoredText;
import entities.EntityPlayer;
import enums.EnumColor;

public class GuiChatbox extends GuiComponent
{
	private final static int MAX_MESSAGE_LOG = 10;
	private Queue<ColoredText> values;
	private final static TrueTypeFont tooltipFont = new TrueTypeFont(((new Font("times", Font.PLAIN, 36)).deriveFont(36.0f)), true);
	private String temporaryValue;
	private boolean isChatOpen;
	private int ticksActive;
	private double width = 240;
	private double height = 125; 
	private int backspaceCooldown;
	private float textScale;

	public GuiChatbox()
	{
		super();
		if(trueTypeFont == null)
		{
			trueTypeFont = new TrueTypeFont(new Font("Agent Orange", Font.BOLD, 20), false/*DO NOT use antialiasing*/);
		}				
		ticksActive = 0;
		backspaceCooldown = 0;
		temporaryValue = "";
		textScale = 0.25F;
		this.values = new LinkedList<ColoredText>();
	}
	
	public void onClick(int x, int y) 
	{
		//No functionality.
	}

	public void update()
	{
		ticksActive++;
		backspaceCooldown--;
		for(ColoredText text : values)
		{
			text.ticksLeft--;
		}
	}
	
	public void drawPartially()
	{
		GL11.glEnable(GL11.GL_BLEND);
		double x = Render.getCameraX() + (Display.getWidth() * 0.5 * 0.025);
		double y = Render.getCameraY() + (Display.getHeight() * 0.5) - (height + 60);
		if(stopVerticalScaling)
		{
			y = this.y;
		}
		final int PADDING = 3;
		int maxLinesRendered = (int) (height / (tooltipFont.getHeight("") * textScale)) - 1;
		
		Iterator<ColoredText> it = values.iterator();
		List<ColoredText> values = new ArrayList<ColoredText>();
		while(it.hasNext())
		{
			ColoredText value = it.next();
			values.add(value);
		}
		
		List<ColoredText> lines = new ArrayList<ColoredText>();
		for(int i = values.size() - 1; i >= 0; i--)
		{
			String[] splitLine = splitMessage(values.get(i).text);
			if(lines.size() + splitLine.length <= maxLinesRendered)
			{
				for(int j = splitLine.length - 1; j >= 0; j--)
				{
					ColoredText coloredText = new ColoredText(values.get(j).color, splitLine[j], values.get(i).ticksLeft);
					lines.add(coloredText);
				}
			}
		}
		
		for(int i = 0; i < lines.size(); i++)
		{
			int invertedIndex = lines.size() - 1 - i;
			
			if(lines.get(invertedIndex).ticksLeft >= 0)
			{
				double backY = y + (tooltipFont.getHeight("") * textScale * i);
				double backHeight = (tooltipFont.getHeight("") * textScale);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator t = Tessellator.instance;
				GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 0.7);
				t.startDrawingQuads();		
			    t.addVertexWithUV(x, backY + backHeight, 0, 0, 1);
			    t.addVertexWithUV(x + width, backY + backHeight, 0, 1, 1);
			    t.addVertexWithUV(x + width, backY, 0, 1, 0);
			    t.addVertexWithUV(x, backY, 0, 0, 0);
				t.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				
				GL11.glColor4d(lines.get(invertedIndex).color.COLOR[0], 
						lines.get(invertedIndex).color.COLOR[1], 
						lines.get(invertedIndex).color.COLOR[2], 1.0);
				tooltipFont.drawString((float)(x + PADDING), 
						(float)(y + (tooltipFont.getHeight("") * textScale * (i + 1))), 
						lines.get(invertedIndex).text,
						textScale,
						-textScale, 
						TrueTypeFont.ALIGN_LEFT); 
			}
		}
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		GL11.glDisable(GL11.GL_BLEND);		
	}
	
	public void draw() 
	{
		GL11.glEnable(GL11.GL_BLEND);
		//0.025, 0.3, 0.5, 0.45
		
		double x = Render.getCameraX() + (Display.getWidth() * 0.5 * 0.025);
		double y = Render.getCameraY() + (Display.getHeight() * 0.5) - (height + 60);
		if(stopVerticalScaling)
		{
			y = this.y;
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator t = Tessellator.instance;
		GL11.glColor4d(EnumColor.GRAY.COLOR[0], EnumColor.GRAY.COLOR[1], EnumColor.GRAY.COLOR[2], 0.7);
		t.startDrawingQuads();		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		
		GL11.glColor4d(EnumColor.DARK_GRAY.COLOR[0], EnumColor.DARK_GRAY.COLOR[1], EnumColor.DARK_GRAY.COLOR[2], 0.8);
		t.startDrawingQuads();		
	    t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y + (height - tooltipFont.getHeight("") * textScale), 0, 1, 0);
	    t.addVertexWithUV(x, y + (height - tooltipFont.getHeight("") * textScale), 0, 0, 0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
				
		final int PADDING = 3;
		int maxLinesRendered = (int) (height / (tooltipFont.getHeight("") * textScale)) - 1;
		
		List<ColoredText> lines = new ArrayList<ColoredText>();
		Iterator<ColoredText> it = values.iterator();
		List<ColoredText> values = new ArrayList<ColoredText>();
		
		while(it.hasNext())
		{
			ColoredText value = it.next();
			values.add(value);
		}
		
		for(int i = values.size() - 1; i >= 0; i--)
		{
			String[] splitLine = splitMessage(values.get(i).text);
			if(lines.size() + splitLine.length <= maxLinesRendered)
			{
				for(int j = splitLine.length - 1; j >= 0; j--)
				{
					ColoredText coloredText = new ColoredText(values.get(j).color, splitLine[j]);
					lines.add(coloredText);
				}
			}
		}
		
		for(int i = 0; i < lines.size(); i++)
		{
			int invertedIndex = lines.size() - 1 - i;
			GL11.glColor4d(lines.get(invertedIndex).color.COLOR[0], 
					lines.get(invertedIndex).color.COLOR[1], 
					lines.get(invertedIndex).color.COLOR[2], 1.0);
			tooltipFont.drawString((float)(x + PADDING), 
					(float)(y + (tooltipFont.getHeight("") * textScale * (i + 1))), 
					lines.get(invertedIndex).text,
					textScale,
					-textScale, 
					TrueTypeFont.ALIGN_LEFT); 
		}

		String underscore = (ticksActive % 40 < 20) ? "_" : "";
				
		GL11.glColor4f(1, 1, 1, 1);
		tooltipFont.drawString((float)(x + PADDING), 
				(float)(y + (height)), 
				cropToSize() + underscore,
				textScale,
				-textScale, 
				TrueTypeFont.ALIGN_LEFT); 
				
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		GL11.glDisable(GL11.GL_BLEND);		
	}
	
	private String cropToSize()
	{
		if(trueTypeFont.getWidth(temporaryValue) * 0.25 > width)
		{
			String reversedVisibleText = "";
			int invertedIndex = temporaryValue.length() - 1;
			while(invertedIndex >= 0 && 
					((trueTypeFont.getWidth(reversedVisibleText + temporaryValue.charAt(invertedIndex))) * 0.25) < width)
			{
				reversedVisibleText += temporaryValue.charAt(invertedIndex); 
				invertedIndex--;
			}			
			String flipped = "";
			for(int i = reversedVisibleText.length() - 1; i >= 0; i--)
			{
				flipped += reversedVisibleText.charAt(i);
			}
			return flipped;
		}	
		return temporaryValue;	
	}
	
	/**
	 * Handles keyboard input. For the menu this means distributing typing to text boxes.
	 */
	public void keyboard(EntityPlayer player, int playerID, ClientUpdate update)
	{	
		if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && backspaceCooldown <= 0)
		{
			if(temporaryValue.length() >= 1)
			{
				backspaceCooldown = 3;
				temporaryValue = removeLastCharacter(temporaryValue);
			}
		}
		for( ; Keyboard.next(); handleKeyboardInput(player, playerID, update)) { } //Very hacky way of letting all keyboard input be recognized
	}
	
	/**
	 * Handles keyboard typing, such as that to text boxes
	 */
	private void handleKeyboardInput(EntityPlayer player, int playerID, ClientUpdate update)
	{
		if(Keyboard.getEventKeyState())
		{
			//If there're key events (Letters, numbers, etc typed to a textbox), update the temporary text appropriately
			append(player, playerID, update, Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		else //otherwise, throw away the input
		{
			Keyboard.getEventCharacter();
			Keyboard.getEventKey();
		}
	}
	
	private void append(EntityPlayer player, int playerID, ClientUpdate update, char c, int i)
	{
		if(i == Keyboard.KEY_ESCAPE)
		{
			isChatOpen = false;
			temporaryValue = "";
			Keys.ec = true; //prevents the settings menu from opening, by locking the escape key 
		}
		if(i == Keyboard.KEY_RETURN && !temporaryValue.equals(""))
		{
			String text = this.temporaryValue;
			if(text.startsWith("/"))
			{
				text = ClientsideCommands.fillOutChatCommand(player, text);
			}
			this.temporaryValue = "";
			String command = "/player " + playerID + " say " + EnumColor.WHITE.toString() + " " + text;
			update.addCommand(command);
			
			isChatOpen = false;
			temporaryValue = "";
		}
		else if(LegalKeypresses.isLegalLetter(c, i))
		{
			temporaryValue += c;
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
	
	public void log(ColoredText text)
	{
		addText(text);
	}

	public boolean isOpen()
	{
		return isChatOpen;
	}
	
	public void setIsOpen(boolean flag)
	{
		this.isChatOpen = flag;
		temporaryValue = "";
		//Empty the keyboard input backlog
		for( ; Keyboard.next(); ) { 
			Keyboard.getEventCharacter();
			Keyboard.getEventKey();
		}
	}
	
	private String[] splitMessage(String message)
	{
		String[] words = message.split(" ");
		Vector<String> renderLines = new Vector<String>();
		String line = "";
		double length = 0;		
		double spaceLength = textScale * trueTypeFont.getWidth(" ");
		if(message!= "")
		{
			for(int i = 0; i < words.length; i++)
			{
				double width = textScale * trueTypeFont.getWidth(words[i]);
				double d =length + width + spaceLength;
				if(d < this.width)
				{
					length += width + spaceLength;
					line += words[i] + " ";					
				}
				else
				{
				//	System.out.println(length);
					renderLines.add(line);
					line = "    " + words[i] + " ";
					length = width + (spaceLength * 5);
				}				
			}
			renderLines.add(line);
		}
		String[] temp = new String[renderLines.size()];
		renderLines.copyInto(temp);
		return temp;
	}
	
	public void addText(ColoredText text)
	{
		if(values.size() >= MAX_MESSAGE_LOG)
		{
			values.poll();
		}
		values.add(text);
	}
}
