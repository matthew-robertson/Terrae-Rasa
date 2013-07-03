package render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import utils.FileManager;
import utils.MainMenuHelper;
import utils.MathHelper;
import utils.Particle;
import utils.Vector2F;
import client.TerraeRasa;
import enums.EnumColor;
import enums.EnumPlayerDifficulty;
import enums.EnumWorldDifficulty;
import enums.EnumWorldSize;

public class MainMenu extends Render
{
	private final Random random = new Random();
	private String[] mainMenuFooter = { };
	private String[] mainMenuVarying = { "Single Player", "Multiplayer", "Settings", "Quit" };
	private String[] playerMenuFooter = { "New Player", "Delete Player", "Back" };
	private String[] worldMenuFooter = { "New World", "Delete World", "Back" };
	private String[] worldNames;
	private String[] playerNames;
	private String selectedPlayerName;
	private String selectedWorldName;
	private boolean isMainMenuOpen;
	private boolean isPlayerMenuOpen;
	private boolean isWorldMenuOpen;
	private boolean isDeleteWorldMenuOpen;
	private boolean isDeletePlayerMenuOpen;
	private boolean isNewPlayerMenuOpen;
	private boolean isNewWorldMenuOpen;
	private boolean isWaitingToDelete;
	private boolean flaggedForWorldGen;
	private boolean flaggedForGameStart;
	private List<Particle> stars;
	private List<Particle> starTrails;
	private EnumColor[] fieryColors = { EnumColor.FIERY1, EnumColor.FIERY2, EnumColor.FIERY3, EnumColor.FIERY4, EnumColor.FIERY5 };
	private MainMenuHelper menuHelper;
	private FileManager fileManager;
	private GuiMenu menu;
	private GuiTextboxScaling characterName;
	private GuiButtonImproved characterMode;
	private GuiTitle createNewCharacter;
	private GuiTitle stopCreatingCharacter;	
	private GuiTextboxScaling worldName;
	private GuiButtonImproved worldMode;
	private GuiButtonImproved worldSize;
	private GuiTitle createNewWorld;
	private GuiTitle stopCreatingWorld;		
	private GuiTitle deleteWorldBack;
	private GuiTitle deleteWorldConfirm;
	private GuiTitle deleteWorldMessage;
	private GuiTitle deletePlayerBack;
	private GuiTitle deletePlayerConfirm;
	private GuiTitle deletePlayerMessage;	
	
	public MainMenu()
	{
		menuHelper = new MainMenuHelper();
		fileManager = new FileManager();
		worldNames = menuHelper.getWorldFileNames();
		playerNames = menuHelper.getPlayerFileNames();
		isMainMenuOpen = true;
		selectedPlayerName = "";
		selectedWorldName = "";
		stars = new ArrayList<Particle>();
		starTrails = new ArrayList<Particle>();
		for(int i = 0; i < 5; i++)
		{
			Particle star = new Particle();
			star.active = true;
			star.life = 1.0;
			star.fade = 0.02;
			star.texture = Render.starParticleBackground;
		    star.r = 1;
		    star.g = 1;
		    star.b = 1;
		    star.a = 1;
		    star.x = (-1 * random.nextInt(200));
		    star.y = (-1 * random.nextInt(200));
		    star.zrot = random.nextInt(100);
		    star.velocity = new Vector2F(random.nextFloat() * 3, random.nextFloat() * 3);
		    star.acceleration = new Vector2F(random.nextFloat() / 20, random.nextFloat() / 20);
			
			stars.add(star);
		}
		
		//Character Creation Menu:
		characterName = (GuiTextboxScaling) new GuiTextboxScaling(0.225, 70, 0.55, 0.1).setStopVerticalScaling(true);
		characterMode = (GuiButtonImproved) new GuiButtonImproved(EnumPlayerDifficulty.getAllEnumAsStringArray(), 0.25, 100, 0.50, 0.1).setStopVerticalScaling(true);
		createNewCharacter = (GuiTitle) new GuiTitle("Create Character", .30, 130, .20, 0.1).setStopVerticalScaling(true);
		stopCreatingCharacter = (GuiTitle) new GuiTitle("Back", 0.60, 130, 0.2, 0.1).setStopVerticalScaling(true);
		//World Creation Menu:
		worldName = (GuiTextboxScaling) new GuiTextboxScaling(0.225, 70, 0.55, 0.1).setStopVerticalScaling(true);
		worldMode = (GuiButtonImproved) new GuiButtonImproved(EnumWorldDifficulty.getAllEnumAsStringArray(), 0.25, 100, 0.50, 0.1).setStopVerticalScaling(true);
		worldSize = (GuiButtonImproved) new GuiButtonImproved(EnumWorldSize.getAllEnumAsStringArray(), 0.25, 130, 0.50, 0.1).setStopVerticalScaling(true);
		createNewWorld = (GuiTitle) new GuiTitle("Create World", .30, 160, .20, 0.1).setStopVerticalScaling(true);
		stopCreatingWorld = (GuiTitle) new GuiTitle("Back", 0.60, 160, 0.2, 0.1).setStopVerticalScaling(true);
		//Delete Confirmation Menu(world):
		deleteWorldMessage = new GuiTitle("Are you sure?", 0.3, 0.25, 0.4, 0.1);
		deleteWorldBack = new GuiTitle("Back", .25, .4, .20, 0.1);
		deleteWorldConfirm = new GuiTitle("Confirm", 0.55, .4, 0.2, 0.1);
		//Delete Confirmation Menu(player):
		deletePlayerMessage = new GuiTitle("Are you sure?", 0.3, 0.25, 0.4, 0.1);
		deletePlayerBack = new GuiTitle("Back", .25, .4, .20, 0.1);
		deletePlayerConfirm = new GuiTitle("Confirm", 0.55, .4, 0.2, 0.1);
		menu = new GuiMenu(0.02, 0.05, 0.25, 0.90, "Main Menu", mainMenuVarying, mainMenuFooter);
	}
	
	public void render()
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -2000f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glEnable(GL11.GL_ALPHA_TEST);	

		if(flaggedForWorldGen)
		{
			flaggedForWorldGen = false;
			fileManager.generateAndSaveWorld(worldName.getText(), EnumWorldSize.getSize(worldSize.getValue()), EnumWorldDifficulty.getDifficulty(worldMode.getValue()));
			updateMenus();
			isNewWorldMenuOpen = false;
			worldName.setText("");
			worldName.freeFocused();
		}
		if(flaggedForGameStart)
		{
			try {
				playGame();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		keyboard();
		mouse();
		
		renderBackground();
		renderAnimation();
		renderComponents();
		renderVersion();
		
		if(flaggedForWorldGen)
		{
			overwriteWithLoadingScreen();
		}
		if(flaggedForGameStart)
		{
			overwriteWithGameLoadScreen();
		}
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
	
	private void renderAnimation()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); //Re-enable this so the lighting renders properly
		
		//Render and update the star trail particles, killing them if they've died
		double width = 2.6;
		double height = 2.6;
		Render.starParticleBackground.bind();
		t.startDrawingQuads();
		Iterator<Particle> iterator = starTrails.iterator();
		while(iterator.hasNext())
		{
			Particle starTrail = iterator.next();
			starTrail.integrate();
			t.setColorRGBA_F((float)starTrail.r, (float)starTrail.g, (float)starTrail.b, (float)starTrail.life);
			t.addVertexWithUV(starTrail.x, starTrail.y + height, 0, 0, 1);
		    t.addVertexWithUV(starTrail.x + width, starTrail.y + height, 0, 1, 1);
		    t.addVertexWithUV(starTrail.x + width, starTrail.y, 0, 1, 0);
		    t.addVertexWithUV(starTrail.x, starTrail.y, 0, 0, 0);
		    
		    if(starTrail.life <= 0)
		    {
		    	iterator.remove();
		    }
		    
		}
		t.draw();
		
		//The actual stars
		for(Particle star : stars)
		{
			star.integrate();
			width = 13;
			height = 13;
			double xoff = 0;
			double yoff = 0;
			
			//Back(1/4)
			Render.starParticleBackground.bind();
			t.startDrawingQuads();
			t.setColorRGBA_F((float)star.r, (float)star.g, (float)star.b, (float)star.a * 0.4F);
			t.addVertexWithUV(star.x + xoff, star.y + yoff + height, 0, 0, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff + height, 0, 1, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff, 0, 1, 0);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff, 0, 0, 0);
			t.draw();

		    //Three Quarters (3/4)
			Render.starParticleForeground.bind();
		    t.startDrawingQuads();
		    t.setColorRGBA_F((float)star.r, (float)star.g, (float)star.b, (float)star.a * 0.8F);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff + height, 0, 0, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff + height, 0, 1, 1);
		    t.addVertexWithUV(star.x + xoff + width, star.y + yoff, 0, 1, 0);
		    t.addVertexWithUV(star.x + xoff, star.y + yoff, 0, 0, 0);
			t.draw();
		    
			//Star Trail particles
			for(int i = 0; i < 6; i++)
			{
				Particle starTrail = new Particle();
				starTrail.active = true;
				starTrail.life = 1.0;
				starTrail.fade = random.nextDouble() / 25 + 0.04;
				starTrail.texture = Render.starParticleBackground;
				EnumColor color = fieryColors[random.nextInt(fieryColors.length)];
				starTrail.r = color.COLOR[0];
				starTrail.g = color.COLOR[1];
			    starTrail.b = color.COLOR[2];
			    starTrail.a = 1;
			    starTrail.x = star.x - 2 + random.nextInt(4) + (width / 2);
			    starTrail.y = star.y - 2 + random.nextInt(4) + (height / 2);
			    starTrail.zrot = random.nextInt(100);
			    starTrail.velocity = new Vector2F(star.velocity.x * -0.35F, star.velocity.y * -0.35F);
			    starTrail.acceleration = new Vector2F(star.acceleration.x * -0.35F, star.acceleration.y * -0.35F);
			    starTrails.add(starTrail);
			}

			//Reset the shooting star if it's off the screen
			if(star.x > Display.getWidth() * 0.6 || star.y > Display.getHeight() * 0.6)
			{
				star.active = true;
				star.life = 1.0;
				star.fade = 0.02;
				star.texture = Render.starParticleBackground;
			    star.r = 1;
			    star.g = 1;
			    star.b = 1;
			    star.a = 1;
			    star.x = (-1 * random.nextInt((int)(Display.getWidth() * 0.3)));
			    star.y = (-1 * random.nextInt((int)(Display.getHeight() * 0.3)));
			    star.zrot = random.nextInt(100);
			    star.velocity = new Vector2F(random.nextFloat() * 2 + 1, random.nextFloat() * 3);
			    star.acceleration = new Vector2F(random.nextFloat() / 15, random.nextFloat() / 15);
			}
		}	
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4d(1, 1, 1, 1);
	}
	
	private void renderBackground()
	{
		//1535, 1151
		int width = 450;
		int height = 250;

		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();
	}

	private void mouse()
	{
		if(isMainMenuOpen || isPlayerMenuOpen || isWorldMenuOpen)
		{
			int index = menu.getCellWithoutScroll(MathHelper.getCorrectMouseXPosition(), MathHelper.getCorrectMouseYPosition());
			menu.setHighlighted(index);
		}
		if(!Mouse.isButtonDown(0))
		{
			return;
		}

		int x = MathHelper.getCorrectMouseXPosition();
		int y = MathHelper.getCorrectMouseYPosition();
		
		try {
			Mouse.destroy();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		
		if(isDeletePlayerMenuOpen)
		{
			mouseMenuDeleteCharacter(x, y);
		}
		else if(isDeleteWorldMenuOpen)
		{
			mouseMenuDeleteWorld(x, y);
		}
		else if(isNewPlayerMenuOpen)
		{
			mouseMenuCreateCharacter(x, y);
		}
		else if(isNewWorldMenuOpen)
		{
			mouseMenuCreateWorld(x, y);
		}
		
		else if(isMainMenuOpen)
		{
			mouseMainMenu(x, y);
		}
		else if(isPlayerMenuOpen)
		{
			mousePlayerMenu(x, y);
		}
		else if(isWorldMenuOpen)
		{
			mouseWorldMenu(x, y);
		}
	}

	private void mouseMenuDeleteCharacter(int mouseX, int mouseY)
	{		
		if(deletePlayerConfirm.inBounds(mouseX, mouseY))
		{
			deleteCharacter(selectedPlayerName);
			updateMenus();
			isDeletePlayerMenuOpen = false;
			isWaitingToDelete = false;
		}
		if(deletePlayerBack.inBounds(mouseX, mouseY))
		{
			isDeletePlayerMenuOpen = false;
			isWaitingToDelete = false;
		}
	}
	
	private void mouseMenuCreateCharacter(int mouseX, int mouseY)
	{
		if(characterName.inBounds(mouseX, mouseY))
		{
			characterName.setFocused();
		}			
		if(characterMode.inBounds(mouseX, mouseY))
		{
			characterMode.onClick(mouseX, mouseY);
		}		
		if(createNewCharacter.inBounds(mouseX, mouseY))
		{
			fileManager.generateAndSavePlayer(characterName.getText(), EnumPlayerDifficulty.getDifficulty(characterMode.getValue()));
			updateMenus();
			isNewPlayerMenuOpen = false;
			characterName.setText("");
			characterName.freeFocused();
		}
		if(stopCreatingCharacter.inBounds(mouseX, mouseY))
		{
			isNewPlayerMenuOpen = false;
			characterName.setText("");
			characterName.freeFocused();
		}
	}
	
	private void mouseMenuDeleteWorld(int mouseX, int mouseY)
	{
		if(deleteWorldConfirm.inBounds(mouseX, mouseY))
		{
			deleteWorld(selectedWorldName);
			updateMenus();
			isDeleteWorldMenuOpen = false;
			isWaitingToDelete = false;
		}
		if(deleteWorldBack.inBounds(mouseX, mouseY))
		{
			isDeleteWorldMenuOpen = false;
			isWaitingToDelete = false;
		}
	}
	
	private void mouseMenuCreateWorld(int mouseX, int mouseY)
	{
		if(worldName.inBounds(mouseX, mouseY))
		{
			worldName.setFocused();
		}		
		if(worldMode.inBounds(mouseX, mouseY))
		{
			worldMode.onClick(mouseX, mouseY);
		}
		if(worldSize.inBounds(mouseX, mouseY))
		{
			worldSize.onClick(mouseX, mouseY);
		}
		if(createNewWorld.inBounds(mouseX, mouseY))
		{
			flaggedForWorldGen = true;
		}
		if(stopCreatingWorld.inBounds(mouseX, mouseY))
		{
			isNewWorldMenuOpen = false;
			worldName.setText("");
			worldName.freeFocused();
		}
	}
	
	private void mouseMainMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		
		int selectedMenuIndex = menu.getCellWithoutScroll(mouseX, mouseY);
		System.out.println(selectedMenuIndex);
		if(selectedMenuIndex == 1) //SP
		{
			isPlayerMenuOpen = true;
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
			isMainMenuOpen = false;
		}
		if(selectedMenuIndex == 2) //MP
		{
			Sys.alert("Feature Not Available", "This feature will be patched in soon. It is currently not available."); 
		}
		if(selectedMenuIndex == 3) //Settings
		{
			//This could actually launch the settings menu
		}
		if(selectedMenuIndex == 4) //Quit
		{
			TerraeRasa.done = true;
		}
	}
	
	private void mousePlayerMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		int selectedIndex = menu.getSelectedCell(mouseX, mouseY);
		if(selectedIndex == menu.getTotalMenuLength() - 3)
		{
			isNewPlayerMenuOpen = true;
		}
		else if(selectedIndex == menu.getTotalMenuLength() - 2)
		{
			isWaitingToDelete = true;
		}
		else if(selectedIndex == menu.getTotalMenuLength() - 1)
		{
			isPlayerMenuOpen = false;
			isMainMenuOpen = true;
			menu.updateLockedInComponents(mainMenuFooter);
			menu.updateTitle("Main Menu");
			menu.updateVaryingItems(mainMenuVarying);
		}
		else if(selectedIndex > 0 && selectedIndex < menu.getVaryingItems().length + 1)
		{
			if(isWaitingToDelete)
			{
				isDeletePlayerMenuOpen = true;
				selectedPlayerName = menu.getVaryingItems()[selectedIndex - 1];
			}
			else
			{
				selectedPlayerName = menu.getVaryingItems()[selectedIndex - 1];
				isPlayerMenuOpen = false;
				isWorldMenuOpen = true;
				menu.updateLockedInComponents(worldMenuFooter);
				menu.updateTitle("Worlds");
				menu.updateVaryingItems(worldNames);
			}
		}
	}
	
	private void mouseWorldMenu(int mouseX, int mouseY)
	{
		menu.onClick(mouseX, mouseY);
		
		int selectedIndex = menu.getSelectedCell(mouseX, mouseY);
		if(selectedIndex == menu.getTotalMenuLength() - 3)
		{
			isNewWorldMenuOpen= true;
		}
		else if(selectedIndex == menu.getTotalMenuLength() - 2)
		{
			isWaitingToDelete = true;			
		}
		else if(selectedIndex == menu.getTotalMenuLength() - 1)
		{
			isPlayerMenuOpen = true;
			isWorldMenuOpen = false;
			selectedPlayerName = "";
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
		}
		else if(selectedIndex > 0 && selectedIndex < menu.getVaryingItems().length + 1)
		{
			if(isWaitingToDelete)
			{
				selectedWorldName = menu.getVaryingItems()[selectedIndex - 1];
				isDeleteWorldMenuOpen = true;
			}
			else
			{
				selectedWorldName = menu.getVaryingItems()[selectedIndex - 1];
				isWorldMenuOpen = false;
				flaggedForGameStart = true;
			}
		}
	}
	
	private void playGame() 
			throws IOException, ClassNotFoundException
	{
		TerraeRasa.startGame(selectedWorldName, fileManager.loadWorld("Earth", selectedWorldName), fileManager.loadPlayer(selectedPlayerName));
	}
	
	private void keyboard()
	{	
		for( ; Keyboard.next(); handleKeyboardInput()) { } //Very hacky way of letting all keyboard input be recognized
	}
	
	/**
	 * Handles keyboard typing, such as that to text boxes
	 */
	private void handleKeyboardInput()
	{
		//If there're key events (Letters, numbers, etc typed to a textbox), update the textboxes appropriately
		if(Keyboard.getEventKeyState())
		{
			if(worldName.isFocused()) //World name text box
			{
				worldName.updateText(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			else if(characterName.isFocused()) //character name textbox
			{
				characterName.updateText(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			else //otherwise, throw away the input
			{
				Keyboard.getEventCharacter();
				Keyboard.getEventKey();
			}
		}
	}
	
	private void renderComponents()
	{		
		if(isDeletePlayerMenuOpen) //character deletion screen
		{
			deletePlayerBack.draw();
			deletePlayerConfirm.draw();
			deletePlayerMessage.draw(); 
		}
		else if(isDeleteWorldMenuOpen) //world deletion screen
		{
			deleteWorldBack.draw();
			deleteWorldConfirm.draw();
			deleteWorldMessage.draw();
		}
		else if(isNewPlayerMenuOpen) //Character creation
		{
			characterName.draw();
			characterMode.draw();		
			createNewCharacter.draw();
			stopCreatingCharacter.draw();	
		}
		else if(isNewWorldMenuOpen) //World creation
		{
			worldName.draw();
			worldMode.draw();
			worldSize.draw();
			createNewWorld.draw();
			stopCreatingWorld.draw();
		}
		else
		{
			menu.draw();
		}		
	}
	
	private void renderVersion()
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0, 0, 0, 1);
		double width = trueTypeFont.getWidth(TerraeRasa.getVersion()) * 0.5;
		double height = 20;
		double x = Display.getWidth() / 2 - 10 - width;
		double y = Display.getHeight() / 2 - 40;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0, 0, 1);
	    t.addVertexWithUV(x + width, y + height, 0, 1, 1);
	    t.addVertexWithUV(x + width, y, 0, 1, 0);
	    t.addVertexWithUV(x, y, 0, 0, 0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString(Display.getWidth() / 2 - 10, (Display.getHeight() / 2) - 20, TerraeRasa.getVersion(), 0.5f, -0.5f, TrueTypeFont.ALIGN_RIGHT);
	}
	
	private void updateMenus()
	{
		worldNames = menuHelper.getWorldFileNames();
		playerNames = menuHelper.getPlayerFileNames();
		if(isPlayerMenuOpen)
		{
			menu.updateLockedInComponents(playerMenuFooter);
			menu.updateTitle("Players");
			menu.updateVaryingItems(playerNames);
		}
		if(isWorldMenuOpen)
		{
			menu.updateLockedInComponents(worldMenuFooter);
			menu.updateTitle("Worlds");
			menu.updateVaryingItems(worldNames);
		}
	}

	/**
	 * Deletes the specified character in the Player Saves directory
	 * @param name the name of the player to delete
	 * @return whether or not the operation succeeded
	 */
	private boolean deleteCharacter(String name)
	{
		boolean success = fileManager.deletefile("/Player Saves/" + name); //delete the file
		updateMenus();
		return success;
	}
	
	/**
	 * Deletes the specified world in the World Saves directory
	 * @param name the name of the world to delete
	 * @return whether the operation succeeded or not
	 */
	private boolean deleteWorld(String name)
	{
		boolean success = fileManager.deleteWorldSave("/World Saves/" + name); //try to delete the file
		updateMenus();
		return success;
	}
	
	private void overwriteWithLoadingScreen()
	{
		int width = 450;
		int height = 250;
		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();

        trueTypeFont.drawString(Display.getWidth() * 0.25F, Display.getHeight() * 0.25F, "Generating the world...", 1F, -1F, TrueTypeFont.ALIGN_CENTER);
	
	}
	
	private void overwriteWithGameLoadScreen()
	{
		int width = 450;
		int height = 250;
		background_menu.bind();
		t.startDrawingQuads();
		for(int x = 0; x < (Display.getWidth() / width) + 1; x++)
		{
			for(int y = 0; y < (Display.getHeight() / height) + 1; y++)
			{
		        t.setColorRGBA_F(1, 1, 1, 1);
		        t.addVertexWithUV(x * width, (y + 1) * height, 0, 0, 1);
		        t.addVertexWithUV((x + 1) * width, (y + 1) * height, 0, 1, 1);
		        t.addVertexWithUV((x + 1) * width, y * height, 0, 1, 0);
		        t.addVertexWithUV(x * width, y * height, 0, 0, 0);
			}
		}
        t.draw();
		
        width = 300;
        height = 63;
        int xoff = (int) (0.15 * Display.getWidth());
        int yoff = 0;
        logo.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);
        t.draw();

        trueTypeFont.drawString(Display.getWidth() * 0.25F, Display.getHeight() * 0.25F, "Loading...", 1F, -1F, TrueTypeFont.ALIGN_CENTER);
		
	}
	
}
