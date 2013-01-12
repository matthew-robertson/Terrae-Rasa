package net.dimensia.client;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import net.dimensia.src.Block;
import net.dimensia.src.EntityPlayer;
import net.dimensia.src.EnumDifficulty;
import net.dimensia.src.FileManager;
import net.dimensia.src.GuiMainMenu;
import net.dimensia.src.Item;
import net.dimensia.src.ItemStack;
import net.dimensia.src.Keys;
import net.dimensia.src.LightingEngine;
import net.dimensia.src.MouseInput;
import net.dimensia.src.Render;
import net.dimensia.src.RenderGlobal;
import net.dimensia.src.SoundEngine;
import net.dimensia.src.SoundManager;
import net.dimensia.src.StatusEffectDazed;
import net.dimensia.src.World;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Dimensia
{
	public Dimensia(int w, int h)
	{
		width = w;
		height = h;
	}
	
	public static void main(String[] args) throws Exception 
	{		
		osName = System.getProperty("os.name").toLowerCase();
		System.out.println("User Operating System is: " + osName);
		initInDebugMode = true;
		
		if(osName.contains("win")) //Load the windows opengl libraries
		{
			if(initInDebugMode)
			{
				System.setProperty("org.lwjgl.librarypath", "C:/lwjgl-2.8.4/native/windows");
			}
			else
			{
				System.setProperty("org.lwjgl.librarypath", WINDOWSBASEPATH + "/bin/native/windows");
			}		
		}
		else if(osName.contains("mac"))//Mac 
		{
			System.out.println(MACBASEPATH + "/bin/native/macosx");
			System.setProperty("org.lwjgl.librarypath", MACBASEPATH + "/bin/native/macosx");
			//Mac stuff here
		}
		else
		{
			throw new RuntimeException("OS not supported");
		}
						
		dimensia = new Dimensia(784, 578/*854,480(widescreen)*/); //Init the game object with the specified resolution
        dimensia.run(); //Start the game
	}	
	
	/**
	 * Initializes the canvas, display, and JFrame used by the game
	 */
	public void createWindow()
	{
		try
		{
			//Frame and canvas			
			dimensiaCanvas = new Canvas();
			dimensiaCanvas.setPreferredSize(new Dimension(width, height));
			
			dimensiaFrame = new Frame(windowTitle);
			dimensiaFrame.setBackground(Color.black);
			dimensiaFrame.setLayout(new BorderLayout());
			dimensiaFrame.add(dimensiaCanvas, "Center");
			dimensiaFrame.pack();
			//dimensiaFrame.setSize(new Dimension(width, height));
			dimensiaFrame.setLocationRelativeTo(null);
			dimensiaFrame.setVisible(true);
			
			//Opengl display
			Display.setParent(dimensiaCanvas); //The display is inside an awtCanvas to allow for resizing
	        Display.setTitle(windowTitle); //sets the title of the window
		    Display.setDisplayMode(new DisplayMode(width, height)); //creates the window with the correct size
		    Display.sync(60); //Sync the display so it attempts to maintain 60 fps
		    Display.setFullscreen(true);
		    Display.setResizable(true); //The display is resizable
			Display.create((new PixelFormat()).withDepthBits(24)); //finally creates the window
		    
			//dimensiaFrame.pack();
	    	
	        initGL(); //Initialize the generic 2D opengl settings
	    	resizeWindow(); //To remain consistent, resize the window to use slightly different calculations immediately
	    	
			Component myComponent = dimensiaFrame;
			myComponent.addComponentListener(new ComponentAdapter() //JFrame listener for a window resize event
			{
			    @Override
			    public void componentResized(ComponentEvent e)
			    {
		    		needsResized = true; //Flags the window for recalculations on the next game update
			    }
			});
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	
	
	/**
	//Frame and canvas
			Frame frame = new Frame(windowTitle);
			Canvas canvas = new Canvas();
			
			frame.setLayout(new BorderLayout());
			frame.add(canvas, "Center");
			canvas.setPreferredSize(new Dimension(width, height));
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			dimensiaCanvas = canvas;
			dimensiaFrame = frame;
			
			boolean fullscreen = false;
			
			if(dimensiaCanvas != null)
			{
				System.out.println("Creating frame");
				Graphics g = dimensiaCanvas.getGraphics();
				if(g != null)
				{
					g.setColor(Color.red);
					g.fillRect(0, 0, width, height);
					g.dispose();
				}
				Display.setParent(dimensiaCanvas);
			}
			else if(fullscreen)
			{
				Display.setFullscreen(true);
				width = Display.getDisplayMode().getWidth();
				height = Display.getDisplayMode().getHeight();
				if(width <= 0)
				{
					width = 1;
				}
				if(height <= 0)
				{
					height = 1;
				}
			}
			else
			{
				Display.setDisplayMode(new DisplayMode(width, height));
			}
			
			try
			{
				PixelFormat format = new PixelFormat();
				format = format.withDepthBits(24);
				Display.create(format);
			}
			catch(LWJGLException e)
			{
				e.printStackTrace();
				Thread.sleep(1000);
				Display.create();
			}
	 */
	
	/**
	 * Resizes the JFrame and all components when applicable 
	 */
	public void resizeWindow()
	{
		//dimensiaFrame.pack();
		
		width = (dimensiaFrame.getWidth() > 740) ? dimensiaFrame.getWidth() : 740; //740 = arbitrary value for things not to look bad
		height = (dimensiaFrame.getHeight() > 480) ? dimensiaFrame.getHeight() : 480; //same with 480
		
		if(osName.contains("Window")) //Windows Resize
		{
			dimensiaFrame.setSize(new Dimension(width, height));
			dimensiaCanvas.setSize(new Dimension(width, height));
			//dimensiaCanvas.setLocation(0, 22);
		}
		else //Mac resize
		{
			dimensiaFrame.setSize(new Dimension(width, height));
			dimensiaCanvas.setSize(new Dimension(width, height));
			dimensiaCanvas.setLocation(0, 22);
			Display.setLocation(0, 22);
			//dimensiaCanvas.setLocation(0, 0);
			//	dimensiaFrame.pack();
		}

		//dimensiaFrame.pack();
		
		GL11.glClear(16640); //Clear the screen
        GL11.glMatrixMode(GL11.GL_PROJECTION); 
        GL11.glLoadIdentity(); //Reset to the origin        
        GL11.glOrtho(0.0D, width / 2, height / 2, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/); 
        GL11.glViewport(0, 0, width, height); //Sets up the second required element for 2D projection					
        GL11.glLoadIdentity(); //Reset position to the origin
        GL11.glTranslatef(0.0F, 0.0F, -2000F); //Move out on the screen so stuff is actually visible
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F); //Clear the colour
        GL11.glDisable(GL11.GL_LIGHTING); //Ensure lighting isnt enabled
        GL11.glEnable(GL11.GL_TEXTURE_2D); //Allow flat textures to be drawn
        GL11.glDisable(GL11.GL_FOG); //Ensure there isnt fog enabled
        
        System.out.println("Resizing window to: (" + width + ", " + height + ")");		
  	}
	
	/**
	 * Initializes the default openGL settings
	 */	
	public void initGL() 
	{		
		GL11.glClear(16640); //Clear the screen
        GL11.glMatrixMode(GL11.GL_PROJECTION); 
        GL11.glLoadIdentity(); //Reset to the origin
        GL11.glOrtho(0.0D, width / 2, height / 2, 0.0D, 1000D, 3000D); //initialize the 2D drawing area
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/); 
        GL11.glLoadIdentity(); //Reset position to the origin
        GL11.glTranslatef(0.0F, 0.0F, -2000F); //Move out on the screen so stuff is actually visible
        GL11.glViewport(0, 0, width, height); //Setup the second element for 2D projection
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F); //Clear the colour
        GL11.glDisable(GL11.GL_LIGHTING); //Ensure lighting isnt enabled
        GL11.glEnable(GL11.GL_TEXTURE_2D); //Allow flat textures to be drawn
        GL11.glDisable(GL11.GL_FOG); //Ensure there isnt fog enabled
	}	
	
	/**
	 * Attempt to load all resources- images, sound...
	 */
	public void loadResources()
 	{
		Render.initializeTextures(world); 
		
		
		if(true)return;
		soundManager = new SoundManager(5, 0.5f);
		SoundEngine engine = new SoundEngine();
		SOUND_T = engine.addSound(soundManager, "calm2.ogg");
		soundManager.playEffect(SOUND_T);
		soundManager.playSound(SOUND_T);
        //Initialize Sound effects here, eventually
		//soundManager = new SoundManager(); //Creates the instance of SoundManager used for sounds
		//soundManager.initialize(5); //Initialize the soundManager object for 5 sounds
		//soundManager.volume = 0.5f; //Default Volume % is 50%
		//SOUND_EXPLODE = soundManager.addSound("resources/hit.wav");
		//Gui.Load textures //Load textures 
	}
	
	int SOUND_T;
	
	public void run()
	{
		createWindow(); //Create the display and embed it in an awtcanvas	
		loadResources(); //Load all graphical and sound resources
		
		isMainMenuOpen = true;
		init();        
		
		//Variables for the gameloop cap (20 times / second)
        final int TICKS_PER_SECOND = 20;
		final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
		final int MAX_FRAMESKIP = 5;
		long next_game_tick = System.currentTimeMillis();
		long start, end;
		int loops;
		
	    while(!done) //Main Game Loop
	    {
	    	start = System.currentTimeMillis();
	        loops = 0;
	        while(System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) //Update the game 20 times/second 
	        {
	        	if(!isMainMenuOpen) //Handle game inputs if the main menu isnt open (aka the game is being played)
	        	{
	        		MouseInput.mouse(world, world.player);
	        		Keys.keyboard(world, world.player);	            
	        		world.onWorldTick();
	        	}
	        	
				if(needsResized || width < 640 || height < 400) //If the window needs resized, resize it
				{
					resizeWindow();
					needsResized = false;
				}
	        	 
	        	next_game_tick += SKIP_TICKS;
	            loops++;
	        }
	        Display.update();
            
	        if(isMainMenuOpen) //if the main menu is open, render that, otherwise render the game
	        {
	        	mainMenu.render();
		    }
	        else
	        {
	        	RenderGlobal.render(world); //Renders Everything on the screen for the game
		    }
	        
        	end = System.currentTimeMillis();        	
       //     System.out.println(end - start);
	    }     
        
	    Display.destroy(); //Cleans up  
        //soundManager.destroy(); //Destroys the soundManager object
        System.exit(0); //remember to exit the system and release resources
	} 		
	
	/**
	 * Start a game from the main menu.
	 * @param world the world to play on.
	 * @param player the player to play on.
	 */
	public void startGame(World world, EntityPlayer player)
	{
		dimensia.world = world;
		dimensia.world.addPlayerToWorld(player);
		isMainMenuOpen = false;
		mainMenu = null;
	}
	
	/**
	 * Misc. things required to initalize the game
	 */
	public void init()
	{
		mainMenu = new GuiMainMenu();
		
		if(initInDebugMode)
		{
			isMainMenuOpen = false;
			FileManager fileManager = new FileManager();
			dimensia.world = fileManager.generateNewWorld("World", 1200, 800, EnumDifficulty.EASY);//EnumWorldSize.LARGE.getWidth(), EnumWorldSize.LARGE.getHeight());
			dimensia.world.addPlayerToWorld(new EntityPlayer("Alec", EnumDifficulty.EASY));
			dimensia.world.assessForAverageSky();
			
			world.player.inventory.pickUpItemStack(new ItemStack(Block.woodTable, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.torch, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.chest, 100));
			//*
			world.player.inventory.pickUpItemStack(new ItemStack(Item.healthPotion1, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.plank, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.manaPotion1, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.magicMissileSpell, 1));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.snowball, 1));
			/**/
			
			world.player.inventory.pickUpItemStack(new ItemStack(Item.woodenArrow, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.furnace, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.craftingTable, 6));
			world.player.inventory.pickUpItemStack(new ItemStack(Block.plank, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldPickaxe));
		
			world.player.inventory.pickUpItemStack(new ItemStack(Item.copperIngot, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.ironIngot, 6));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.copperCoin, 5));
			
			world.player.inventory.pickUpItemStack(new ItemStack(Item.woodenArrow, 5));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.woodenArrow, 5));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.woodenArrow, 5));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.woodenArrow, 5));
			
			world.player.inventory.pickUpItemStack(new ItemStack(Item.copperOre, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.tinOre, 100));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldOre, 15));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.tinIngot, 100));
			
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldHelmet));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldHelmet));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldBody));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.goldGreaves));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.bronzeHelmet));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.bronzeBody));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.bronzeGreaves));

			world.player.inventory.pickUpItemStack(new ItemStack(Item.rocketBoots));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.heartCrystal, 2));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.manaCrystal, 2));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.angelsSigil, 2));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.ringOfVigor, 5));
			world.player.inventory.pickUpItemStack(new ItemStack(Item.regenerationPotion2, 5));
			//world.player.registerStatusEffect(new StatusEffectDazed(500000, 1));
			LightingEngine.applySunlight(world);
		}		
	}
	
	public final static String getVersion()
	{
		return version;
	}
		
	public static boolean initInDebugMode;
	public static Dimensia dimensia;
	public static boolean done;
	public GuiMainMenu mainMenu;
	public boolean isMainMenuOpen;
	public SoundManager soundManager;	
	public World world;
	public static boolean areResourcesLoaded;
	private Frame dimensiaFrame;
	private Canvas dimensiaCanvas;
	private boolean needsResized;
	private int width;
	private int height;
	private static String osName;
	private final static String version = "Alpha 1.0.12";	
	private final static String windowTitle = "Dimensia " + version;
	private final static String WINDOWSBASEPATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/Dimensia").toString();
	private final static String MACBASEPATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application").append(" Support/Dimensia").toString();

}

