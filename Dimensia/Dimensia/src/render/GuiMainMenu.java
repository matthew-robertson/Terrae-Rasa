package render;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import client.Dimensia;


import enums.EnumDifficulty;
import enums.EnumWorldSize;

import utils.FileManager;
import utils.MathHelper;

/**
 * GuiMainMenu implements the Main Menu, which is seen immediately upon launching
 * the game jar. This class is self contained and handles its own keyboard,
 * mouse, and drawing. Requiring a simple call to the render() method in the main game loop to function properly.
 *
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class GuiMainMenu extends Render
{
	private FileManager fileManager;	
	private String[] worldNames;
	private String[] playerNames;
	private int totalPlayers;
	private int totalWorlds;		
	private GuiResizableText[] mainMenuTextAreas;
	private GuiResizableText[] playMenuTextAreas;
	private GuiResizableText[] worldMenuTextAreas;	
	private GuiTextbox characterName;
	private GuiButton characterMode;
	private GuiResizableText createNewCharacter;
	private GuiResizableText stopCreatingCharacter;	
	private GuiTextbox worldName;
	private GuiButton worldMode;
	private GuiButton worldSize;
	private GuiResizableText createNewWorld;
	private GuiResizableText stopCreatingWorld;		
	private GuiResizableText deleteWorldBack;
	private GuiResizableText deleteWorldConfirm;
	private GuiResizableText deleteWorldMessage;
	private GuiResizableText deletePlayerBack;
	private GuiResizableText deletePlayerConfirm;
	private GuiResizableText deletePlayerMessage;	
	private boolean isCreatingCharacter;
	private boolean isCreatingWorld;
	private boolean isPlayMenuOpen;
	private boolean isWorldMenuOpen;	
	private double logoAngle; //Angle of tilt for logo rotation
	private int logoModifier; //Direction modifier for logo rotation
	private boolean isDeletingCharacter;
	private boolean isDeletingWorld;
	private int deletingIndex;	
	private int playerIndex;
	private int worldIndex;	
	private boolean isWaitingToDelete;
	
	/**
	 * Constructs an instance of the main menu. This involves initializing the menu components and 
	 * the fileManager object used for I/O.
	 */
	public GuiMainMenu()
	{
		fileManager = new FileManager();
		initialize();
		totalPlayers = fileManager.getTotalPlayers();
		totalWorlds = fileManager.getTotalWorlds();
		onMenuOpen();
	}
	
	/**
	 * Handles everything: drawing, mouse events, and keyboard
	 */
	public void render()
	{	
		try
		{			
			GL11.glLoadIdentity();
			GL11.glTranslatef(0, 0, -2000f);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glEnable(GL11.GL_ALPHA_TEST);	
			
			renderBackground();
			renderLogo();
			renderComponents();
			renderVersion();
			
			keyboard();
			mouseEvents();
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads all keyboard input. (put key binds here), also passes input to text boxes 
	 */
	private void keyboard()
	{		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested())  
		{
			Dimensia.done = true;
		}
	
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
	
	/**
	 * Resets all menu scales, to correct a very wierd bug (scales weren't resetting very well); also resets to defaults in general
	 */
	private void onMenuOpen()
	{
		for(int i = 0; i < 4; i++) //Main Menu
		{
			mainMenuTextAreas[i].resetScale();
		}		
		for(int i = 0; i < 7; i++) //Player select
		{
			if(playMenuTextAreas[i] == null) continue; //things arent always filled, so ignore them if they arent
			playMenuTextAreas[i].resetScale();
		}	
		for(int i = 0; i < 7; i++) //World Select 
		{
			if(worldMenuTextAreas[i] == null) continue; //things arent always filled, so ignore them if they arent
			worldMenuTextAreas[i].resetScale();
		}	
		//Create character
		createNewCharacter.resetScale();
		characterMode.setButtonIndex(0);
		characterName.setText("");
		characterName.freeFocused();
		//Create World
		worldName.freeFocused();
		worldName.setText("");
		worldMode.setButtonIndex(0);
		worldSize.setButtonIndex(0);
		createNewWorld.resetScale();
		stopCreatingWorld.resetScale();	
	}

	/**
	 * Deletes the specified character in the Player Saves directory
	 * @param name the name of the player to delete
	 * @return whether or not the operation succeeded
	 */
	private boolean deleteCharacter(String name)
	{
		boolean success = fileManager.deletefile("/Player Saves/" + name); //delete the file
		totalPlayers = fileManager.getTotalPlayers(); //update player count
		//System.out.println("Total Players:" + totalPlayers);
		generatePlayerMenu(); //The player menu has changed, regenerate it
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
		totalWorlds = fileManager.getTotalWorlds(); //update world count
		//System.out.println("Total Worlds:" + totalWorlds);
		generateWorldMenu(); //the world menu has changed, so update it
		return success;
	}

	/**
	 * Due to the menu structure, this selects one of 30+ different mouse events based on what was clicked
	 * @param buttonPushed which button event to activate
	 */
	private void mouseClicks(int buttonPushed)
	{
		if(!Mouse.isButtonDown(0)) //Stupid Check
		{
			throw new RuntimeException("Stop failing at mouse events");
		}
		
		try
		{
			Mouse.destroy();
			Mouse.create();
		} 
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	
		//System.out.println("Mouse Clicked on Button: " + buttonPushed);
		
		//Button Events By ID:
		//Main Menu:
		if(buttonPushed == 0) //Play
		{
			isPlayMenuOpen = true;
			onMenuOpen();
		}
		if(buttonPushed == 1) //Multiplayer 
		{
			Sys.alert("Feature Not Available", "This feature is not available yet!"); 
		}
		if(buttonPushed == 2) //Options
		{
			Sys.alert("Feature Not Available", "This feature is not available yet!"); 
		}
		if(buttonPushed == 3) //Exit
		{
			Dimensia.done = true; //Essentially calls System.exit();
		}
		//Play Menu:
		if(buttonPushed == 4) //Player 1
		{		
			handlePlayerNameClicked(0);
		}
		if(buttonPushed == 5) //Player 2
		{		
			handlePlayerNameClicked(1);
		}
		if(buttonPushed == 6) //Player 3
		{	
			handlePlayerNameClicked(2);
		}
		if(buttonPushed == 7) //Player 4
		{
			handlePlayerNameClicked(3);
		}
		if(buttonPushed == 8) //Player 5
		{
			handlePlayerNameClicked(4);
		}
		if(buttonPushed == 9) //Create Character
		{
			isCreatingCharacter = true;
		}
		if(buttonPushed == 10) //Delete Character
		{
			isWaitingToDelete = true;			
		}
		if(buttonPushed == 11) //Back to Main
		{
			isPlayMenuOpen = false;
			onMenuOpen();
		}
		//World Menu:
		if(buttonPushed == 12) //World 1
		{		
			handleWorldNameClicked(0);
		}
		if(buttonPushed == 13) //World 2
		{		
			handleWorldNameClicked(1);
		}
		if(buttonPushed == 14) //World 3
		{	
			handleWorldNameClicked(2);
		}
		if(buttonPushed == 15) //World 4
		{
			handleWorldNameClicked(3);
		}
		if(buttonPushed == 16) //World 5
		{
			handleWorldNameClicked(4);
		}
		if(buttonPushed == 17) //Create World Menu open
		{
			isCreatingWorld = true;
		}
		if(buttonPushed == 18) //Delete World
		{
			isWaitingToDelete = true;
		}		
		if(buttonPushed == 19) //Back to Player
		{
			isWorldMenuOpen = false;
			onMenuOpen();
		}
		if(buttonPushed == 20) //Set Focus to world name
		{
			worldName.setFocused();
		}
		if(buttonPushed == 21) //Toggle World Mode
		{
			worldMode.onClick();
		}
		if(buttonPushed == 22) //Toggle World Size
		{
			worldSize.onClick();
		}
		if(buttonPushed == 23) //Create World
		{
			fileManager.generateAndSaveWorld(worldName.getText(), EnumWorldSize.getSize(worldSize.getValue()), EnumDifficulty.getDifficulty(worldMode.getValue()));
			generateWorldMenu();
			isCreatingWorld = false;
			onMenuOpen();
		}
		if(buttonPushed == 24) //Back 
		{
			isCreatingWorld = false;
			onMenuOpen();
		}
		/**
		if(buttonPushed == 25)
		{
			
		}
		*/
		if(buttonPushed == 26) //set Text Area Focused
		{
			characterName.setFocused();
		}
		if(buttonPushed == 27) //Character Mode
		{
			characterMode.onClick();
		}
		if(buttonPushed == 28) //Create Character
		{
			fileManager.generateAndSavePlayer(characterName.getText(), EnumDifficulty.getDifficulty(characterMode.getValue()));
			generatePlayerMenu();
			isCreatingCharacter = false;
			onMenuOpen();
		}
		if(buttonPushed == 29) //Back
		{
			isCreatingCharacter = false;
			onMenuOpen();
		}
		/**
		 * ID = 30;
		 * ID = 31;
		 * FILLER
		 */
		//World Delete Screen:
		if(buttonPushed == 32) //Delete World Confirm
		{
			deleteWorld(worldNames[deletingIndex]);
			onMenuOpen();
			isDeletingWorld = false;
			isWaitingToDelete = false;
		}
		if(buttonPushed == 33) //Delete World Back
		{
			isDeletingWorld = false;
			isWaitingToDelete = false;
			onMenuOpen();
		}
		//Player Delete Screen:
		if(buttonPushed == 34) //Delete Selected Player
		{
			deleteCharacter(playerNames[deletingIndex]);
			onMenuOpen();
			isDeletingCharacter = false;
			isWaitingToDelete = false;
		}
		if(buttonPushed == 35) //Back
		{
			isDeletingCharacter = false;
			isWaitingToDelete = false;
			onMenuOpen();
		}		
	}
	
	/**
	 * Reacts to the player menu names being clicked
	 * @param index which name was clicked (/5)
	 */
	private void handlePlayerNameClicked(int index)
	{
		if(isWaitingToDelete) //if delete was selected, verify the deletion
		{
			isDeletingCharacter = true;
			isWaitingToDelete = false;
			deletingIndex = index;			
		}
		else //otherwise show the world menu
		{
			isWorldMenuOpen = true;	
			playerIndex = index;
		}
	}
	
	/**
	 * Reacts to the world menu names being clicked
	 * @param index which world was selected (/5)
	 */
	private void handleWorldNameClicked(int index)
	{
		if(isWaitingToDelete) //if waiting to delete, verify the deletion
		{
			isDeletingWorld = true;
			isWaitingToDelete = false;
			deletingIndex = index;			
		}
		else //otherwise, try to start playing
		{
			worldIndex = index;
			try 
			{ //call the startGame method of Dimensia with the specified world and player, they of course have to be loaded though
				Dimensia.startGame(fileManager.loadWorld("Earth", worldNames[worldIndex]), fileManager.loadPlayer(playerNames[playerIndex]));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Goes hand in hand with mouseClicks, all mouse events go through here first and mouse clicks are send there to be handled.
	 */
	private void mouseEvents()
	{
		GL11.glColor4f(1, 1, 1, 1);
		
		int x = MathHelper.getCorrectMouseXPosition(); 
		int y = MathHelper.getCorrectMouseYPosition();	
		
		if(isDeletingCharacter) //Character Deletion screen
		{
			if(deletePlayerConfirm.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(34);
			}
			if(deletePlayerBack.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(35);
			}
		}
		else if(isDeletingWorld) //World Deletion Screen
		{
			if(deleteWorldConfirm.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(32);
			}
			if(deleteWorldBack.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(33);
			}
		}
		else if(isCreatingCharacter) //Character creation screen
		{
			if(characterName.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(26);
			}			
			if(characterMode.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(27);
			}		
			if(createNewCharacter.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(28);
			}
			if(stopCreatingCharacter.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(29);
			}
		}		
		else if(isCreatingWorld) //World creation screen
		{
			if(worldName.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(20);
			}		
			if(worldMode.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(21);
			}
			if(worldSize.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(22);
			}
			if(createNewWorld.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(23);
			}
			if(stopCreatingWorld.inBounds(x, y) && Mouse.isButtonDown(0))
			{
				mouseClicks(24);
			}
		}
		else if(isWorldMenuOpen) //World selection Screen
		{
			for(int i = 0; i < worldMenuTextAreas.length; i++)
			{	
				if(worldMenuTextAreas[i] == null || (i == 5 && totalWorlds >= 5) || (i == 6 && totalWorlds == 0))
				{ //there is a button the player shouldnt be able to use
					continue;				
				}
				if(worldMenuTextAreas[i].inBounds(x, y))
				{
					if(Mouse.isButtonDown(0))
					{
						mouseClicks(12 + i);
					}
				}
			}
		}
		else if(isPlayMenuOpen) //Player selection screen
		{
			for(int i = 0; i < playMenuTextAreas.length; i++)
			{	
				if(playMenuTextAreas[i] == null || (i == 6 && totalPlayers == 0) || (i == 5 && totalPlayers >= 5))
				{ //there is a button the player shouldnt be able to use
					continue;				
				}
				if(playMenuTextAreas[i].inBounds(x, y) && Mouse.isButtonDown(0))
				{
					mouseClicks(4 + i);
				}				
			}
		}
		else //Main Menu
		{
			for(int i = 0; i < mainMenuTextAreas.length; i++)
			{	
				if(mainMenuTextAreas[i].inBounds(x, y) && Mouse.isButtonDown(0))
				{				
					mouseClicks(i);
				}	
			}
		}
	}	
	
	/**
	 * Simply renders the components needed for whatever menu is open
	 */
	private void renderComponents()
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, -2000f);        
		
		if(isDeletingCharacter) //character deletion screen
		{
			deletePlayerBack.draw();
			deletePlayerConfirm.draw();
			deletePlayerMessage.draw(); 
		}
		else if(isDeletingWorld) //world deletion screen
		{
			deleteWorldBack.draw();
			deleteWorldConfirm.draw();
			deleteWorldMessage.draw();
		}
		else if(isCreatingCharacter) //Character creation
		{
			characterName.draw();
			characterMode.draw();		
			createNewCharacter.draw();
			stopCreatingCharacter.draw();	
		}
		else if(isCreatingWorld) //World creation
		{
			worldName.draw();
			worldMode.draw();
			worldSize.draw();
			createNewWorld.draw();
			stopCreatingWorld.draw();
		}
		else if(isWorldMenuOpen) //World selection menu
		{
			 for(int i = 0; i < worldMenuTextAreas.length; i++)
			 {
				 if(worldMenuTextAreas[i] == null || (i == 5 && totalWorlds >= 5) || (i == 6 && totalWorlds == 0)) 
				 {
					 continue; //dont render certain buttons if the player cant use them at all
				 }
				 worldMenuTextAreas[i].draw(); 
			 }			 
		}
		else if(isPlayMenuOpen) //Player selection menu
		{
			 for(int i = 0; i < playMenuTextAreas.length; i++)
			 {	
				 //5 create; 6 delete
				 if(playMenuTextAreas[i] == null || (i == 6 && totalPlayers == 0) || (i == 5 && totalPlayers >= 5)) 
				 { 
					 continue; //dont render certain buttons if the player cant use them at all
				 }
				 playMenuTextAreas[i].draw(); 
			 }			 
		}
		else //Main Menu
		{
			for(int i = 0; i < mainMenuTextAreas.length; i++)
			{
				mainMenuTextAreas[i].draw();
			}
		}	
	}
		
	/**
	 * Draws the background image
	 */
	private void renderBackground()
	{		
		background_1.bind();
		t.startDrawingQuads();
        t.setColorRGBA_F(1, 1, 1, 1);
        t.addVertexWithUV(0, Display.getHeight() / 2, 0, 0, 1);
        t.addVertexWithUV(Display.getWidth() / 2, Display.getHeight() / 2, 0, 1, 1);
        t.addVertexWithUV(Display.getWidth() / 2, 0, 0, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();
    }
	
	/**
	 * Draws the rotating logo
	 */
	private void renderLogo()
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(Display.getWidth() * 0.25f, 69, -2000f); //Move to the center of the image
		GL11.glRotated(logoAngle, 0, 0, 1.0f); //Rotate image back and forth
		
		int yoff = -64; //Where to draw on the Y
		int xoff = -128; //Where to draw on the X
		int height = 64; //Image Height in ortho
		int width = 256; //Image Width in ortho
		
		logo.bind();		
		t.startDrawingQuads(); //Draw the quad with full colour		
		t.setColorRGBA_F(1, 1, 1, 1); 
        t.addVertexWithUV(xoff, yoff + height, 0, 0, 1);
        t.addVertexWithUV(xoff + width, yoff + height, 0, 1, 1);
        t.addVertexWithUV(xoff + width, yoff, 0, 1, 0);
        t.addVertexWithUV(xoff, yoff, 0, 0, 0);        
        t.draw();
        
        logoAngle += logoModifier * 0.015f; //Modify image tilt
        if(logoAngle > 7.5f) //If it's too far up, send it back the down
        {
        	logoAngle = 7.5f;
        	logoModifier = -1;
        }
        if(logoAngle < -4f) //If it's too far down, send it up
        {
        	logoAngle = -4f;
        	logoModifier = 1;
        }
	}
	
	/**
	 * Due to everchanging player counts, recreates the player menu when needed
	 */
	private void generatePlayerMenu()
	{
		totalPlayers = fileManager.getTotalPlayers();
		playerNames = fileManager.getPlayerFileNames(); //Get player names
		
		playMenuTextAreas = new GuiResizableText[8];
		String[] playMessages = { "", "", "", "", "", "Create Character", "Delete", "Back" }; //some values are default, some arent on this menu
		for(int i = 0; i < playerNames.length; i++) //Fill in the player names (upto 5) in the array
		{
			playMessages[i] = playerNames[i];
		}		
		for(int i = 0; i < playerNames.length; i++) //recreate the components (upto the 5 player names)
		{
			playMenuTextAreas[i] = new GuiResizableText(playMessages[i], 0.7f, 0.77f, (110 + 20 * i), true);
		}		
		for(int i = 5; i < 8; i++) //recreate the bottom 3 components, which are slightly larger
		{
			playMenuTextAreas[i] = new GuiResizableText(playMessages[i], 0.85f, 0.935f, (230 + (i - 5) * 24), true);
		}
	}
	
	/**
	 * Due to everchanging world counts, recreates the world menu when needed
	 */
	private void generateWorldMenu()
	{
		totalWorlds = fileManager.getTotalWorlds();
		worldNames = fileManager.getWorldFileNames(); //Get world names
		
		worldMenuTextAreas = new GuiResizableText[8];
		String[] worldMessages = { "", "", "", "", "", "Create World", "Delete", "Back"}; //some values are default, some arent on this menu
		for(int i = 0; i < worldNames.length; i++) //Fill in the world names (upto 5) in the array
		{
			worldMessages[i] = worldNames[i];		
		}
		
		for(int i = 0; i < worldNames.length; i++) //recreate the components (upto the 5 world names)
		{
			worldMenuTextAreas[i] = new GuiResizableText(worldMessages[i], 0.7f, 0.77f, (110 + 20 * i), true );
		}
		for(int i = 5; i < 8; i++)//recreate the bottom 3 components, which are slightly larger
		{
			worldMenuTextAreas[i] = new GuiResizableText(worldMessages[i], 0.85f, 0.935f, (230 + (i - 5) * 24), true);
		}
	}
	
	/**
	 * Initializes all the componentes
	 */
	private void initialize()
	{
		//The following simply initializes all the components of the main menu's many (sub)menus
		//Main Main:
		mainMenuTextAreas = new GuiResizableText[4];
		String[] mainMenuMessages = { "Play", "Multiplayer", "Settings", "Exit" };
		for(int i = 0; i < 4; i++)
		{
			mainMenuTextAreas[i] = new GuiResizableText(mainMenuMessages[i], 1.0f, 1.5f, (110 + 50 * i), true);
		}		
		//Due to ever-changing settings, the player and world select menus have their own functions
		generatePlayerMenu();
		generateWorldMenu();
		//Character Creation Menu:
		characterName = new GuiTextbox(100, 70, true);
		characterMode = new GuiButton(new String[] { "Easy", "Normal", "HardCore" }, 100, 105, true);
		createNewCharacter = new GuiResizableText("Create Character", 0.85f, 0.935f, 175, true);
		stopCreatingCharacter = new GuiResizableText("Back", 0.85f, 0.935f, 210, true);
		//World Creation Menu:
		worldName = new GuiTextbox(100, 70, true);
		worldMode = new GuiButton(new String[] { "Easy", "Normal", "HardCore" }, 100, 105, true);
		worldSize = new GuiButton(new String[] { "Small", "Medium", "Large" }, 100, 140, true);
		createNewWorld = new GuiResizableText("Create World", 0.85f, 0.935f, 210, true);
		stopCreatingWorld = new GuiResizableText("Back", 0.85f, 0.935f, 245, true);
		//Delete Confirmation Menu(world):
		deleteWorldBack = new GuiResizableText("Back", 0.85f, 0.935f, 200, true);
		deleteWorldConfirm = new GuiResizableText("Confirm", 0.85f, 0.935f, 155, true);
		deleteWorldMessage = new GuiResizableText("Are you sure?", 1.0f, 1.0f, 110, true);
		//Delete Confirmation Menu(player):
		deletePlayerBack = new GuiResizableText("Back", 0.85f, 0.935f, 200, true);
		deletePlayerConfirm = new GuiResizableText("Confirm", 0.85f, 0.935f, 155, true);
		deletePlayerMessage = new GuiResizableText("Are you sure?", 1.0f, 1.0f, 110, true);
		//Resets the menu to the main menu (Play... Exit):
		isCreatingCharacter = false;
		isCreatingWorld = false;
		isWorldMenuOpen = false;
		isPlayMenuOpen = false;
		logoModifier = 1; //reset logo direction
		logoAngle = 0; //Reset logo tilt	
	}
	
	/**
	 * Renders the version played in the bottom left corner
	 */
	private void renderVersion()
	{
		GL11.glColor4f(1, 1, 1, 1);
		trueTypeFont.drawString(0, (Display.getHeight() / 2) - 20, Dimensia.getVersion(), 0.5f, -0.5f, TrueTypeFont.ALIGN_LEFT);
	}
}