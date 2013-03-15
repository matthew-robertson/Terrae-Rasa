package net.dimensia.client;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import net.dimensia.src.EntityLivingPlayer;
import net.dimensia.src.GuiMainMenu;
import net.dimensia.src.Render;
import net.dimensia.src.World;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Dimensia.java defines the entry class of the application. After calling the main method of the program,
 * a new instance of the Dimensia class is created in order to handle all the data and responsibilities of the
 * application. The constructor of this class is private, and an instance of Dimensia should only be created
 * in the main method of the application.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Dimensia
{
	public static boolean initInDebugMode = true;
	public static Dimensia dimensia;
	public static boolean done;
	public static boolean isMainMenuOpen;
	public static boolean areResourcesLoaded;
	public static boolean needsResized;
	public static int width;
	public static int height;
	public final GameEngine gameEngine;
	private Frame dimensiaFrame;
	private Canvas dimensiaCanvas;
	private static String osName;
	private final static String VERSION = "Alpha 1.0.23";	
	private final static String WINDOW_TITLE = "Dimensia " + VERSION;
	private final static String WINDOWS_BASE_PATH = new StringBuilder().append("C:/Users/").append(System.getProperty("user.name")).append("/AppData/Roaming/Dimensia").toString();
	private final static String MAC_BASE_PATH = new StringBuilder().append("/Users/").append(System.getProperty("user.name")).append("/Library/Application").append(" Support/Dimensia").toString();
	private final static String LINUX_BASE_PATH = new StringBuilder().append("/home/").append(System.getProperty("user.name")).append("/Dimensia").toString();
	private static String basePath;
	
	/**
	 * Creates a new instance of the container for all game objects, and a new GameEngine. 
	 * This is private to prevent destructive calls in other parts of the code. A new 
	 * instance of this class should only be created in the main method, implemented in Dimensia.java.
	 * @param w the width of the display at the start
	 * @param h the height of the display at the start
	 */
	private Dimensia(int w, int h)
	{
		width = w;
		height = h;
		this.gameEngine = new GameEngine();
	}
	
	public static void main(String[] args) 
	{		
		osName = System.getProperty("os.name").toLowerCase();
		//System.out.println("User Operating System is: " + osName);
		
		//Load the OpenGL libraries for rendering later on
		if(osName.contains("win")) //Windows
		{
			if(initInDebugMode)
			{
				System.setProperty("org.lwjgl.librarypath", "C:/lwjgl-2.8.4/native/windows");
			}
			else
			{
				System.setProperty("org.lwjgl.librarypath", WINDOWS_BASE_PATH + "/bin/native/windows");
			}
			basePath = WINDOWS_BASE_PATH;
		}
		else if(osName.contains("mac")) //Mac 
		{
			System.setProperty("org.lwjgl.librarypath", MAC_BASE_PATH + "/bin/native/macosx");
			basePath = MAC_BASE_PATH;
		}
		else if(osName.contains("ubuntu") || osName.contains("linux")) //Ubuntu/Linux. Ubuntu tested.
		{
			System.setProperty("org.lwjgl.librarypath", LINUX_BASE_PATH + "/bin/native/linux");
			basePath = LINUX_BASE_PATH;
		}
		else //Solaris and any other OS are out of luck for now
		{
			throw new RuntimeException("OS not supported");
		}
						
		dimensia = new Dimensia(900, 500/*784, 578/*854,480(widescreen)*/); //Init the game object with the specified resolution
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
			
			dimensiaFrame = new Frame(WINDOW_TITLE);
			dimensiaFrame.setBackground(Color.black);
			dimensiaFrame.setLayout(new BorderLayout());
			dimensiaFrame.add(dimensiaCanvas, "Center");
			dimensiaFrame.pack();
			dimensiaFrame.setLocationRelativeTo(null);
			dimensiaFrame.setVisible(true);
	
			//Opengl display
			Display.setParent(dimensiaCanvas); //The display is inside an awtCanvas to allow for resizing
	        Display.setTitle(WINDOW_TITLE); //sets the title of the window
		    Display.setDisplayMode(new DisplayMode(width, height)); //creates the window with the correct size
		    Display.setFullscreen(true);
		    Display.setResizable(true); //The display is resizable
			Display.create((new PixelFormat()).withDepthBits(24)); //finally creates the window
		    
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
	 * Resizes the JFrame and all components when applicable 
	 */
	public void resizeWindow()
	{
		width = (dimensiaFrame.getWidth() > 740) ? dimensiaFrame.getWidth() : 740; //740 = arbitrary value for things not to look bad
		height = (dimensiaFrame.getHeight() > 480) ? dimensiaFrame.getHeight() : 480; //same with 480
		
		int width_fix = (int)(width / Render.BLOCK_SIZE) * Render.BLOCK_SIZE + 3 * Render.BLOCK_SIZE;
		int height_fix = (int)(height / Render.BLOCK_SIZE) * Render.BLOCK_SIZE + 3 * Render.BLOCK_SIZE;
		
		if(osName.contains("Window")) //Windows Resize
		{
			dimensiaFrame.setSize(new Dimension(width, height));
			dimensiaCanvas.setSize(new Dimension(width, height));
		}
		else if(osName.toLowerCase().contains("linux"))
		{
			dimensiaCanvas.setSize(new Dimension(width_fix, height_fix));
			dimensiaFrame.setSize(new Dimension(width, height));
		}
		else //Mac resize
		{
			dimensiaFrame.setSize(new Dimension(width, height));
			dimensiaCanvas.setSize(new Dimension(width, height));
		    dimensiaCanvas.setLocation(0, 22);
			Display.setLocation(0, 22);
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION); 
        GL11.glLoadIdentity(); //Reset to the origin        
        GL11.glOrtho(0.0D, width_fix / 2, height_fix / 2, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); 
        GL11.glViewport(0, 0, width_fix, height_fix); //Sets up the second required element for 2D projection					
        
        System.out.println("Resizing window to: (" + width + ", " + height + ")");		
  	}
	
	/**
	 * Initializes the default openGL settings
	 */	
	public void initGL() 
	{		
		int width_fix = (int)(width / Render.BLOCK_SIZE) * Render.BLOCK_SIZE + Render.BLOCK_SIZE;
		int height_fix = (int)(height / Render.BLOCK_SIZE) * Render.BLOCK_SIZE + Render.BLOCK_SIZE;
		
		GL11.glClear(16640); //Clear the screen
        GL11.glMatrixMode(GL11.GL_PROJECTION); 
        GL11.glLoadIdentity(); //Reset to the origin
        GL11.glOrtho(0.0D, width_fix / 2, height_fix / 2, 0.0D, 1000D, 3000D); //initialize the 2D drawing area
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/); 
        GL11.glLoadIdentity(); //Reset position to the origin
        GL11.glTranslatef(0.0F, 0.0F, -2000F); //Move out on the screen so stuff is actually visible
        GL11.glViewport(0, 0, width_fix, height_fix); //Setup the second element for 2D projection
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F); //Clear the colour
        GL11.glDisable(GL11.GL_LIGHTING); //Ensure lighting isnt enabled
        GL11.glEnable(GL11.GL_TEXTURE_2D); //Allow flat textures to be drawn
        GL11.glDisable(GL11.GL_FOG); //Ensure there isnt fog enabled
	}	
	
	/**
	 * Attempts to load all resources- images, sound...
	 */
	public void loadResources()
 	{
		Render.initializeTextures(gameEngine.getWorld()); 
		/*
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
		*/
 	}
	
	/**
	 * Creates the window, loads resources, initializes the game engine,
	 * and then beings to run the game engine and main game loop. When quitting, 
	 * this method will destroy the display and then call System.exit(1) to ensure 
	 * the VM deallocates all resources and terminates.
	 */
	public void run()
	{
		createWindow(); //Create the display and embed it in an awtcanvas	
		loadResources(); //Load all graphical and sound resources
		
		isMainMenuOpen = true;       
		gameEngine.init();
		gameEngine.run();
	    
	    Display.destroy(); //Cleans up  
        //soundManager.destroy(); //Destroys the soundManager object
        System.exit(1); //remember to exit the system and release resources
	} 		
	
	/**
	 * Gets the file path to the root folder of the game save on Windows OS
	 * @return the root folder of the game save for Windows OS
	 */
	public final static String getWindowsBasePath()
	{
		return WINDOWS_BASE_PATH;
	}
	
	/**
	 * Gets the file path to the root folder of the game save on Mac OSX
	 * @return the root folder of the game save for Mac OSX
	 */
	public final static String getMacBasePath()
	{
		return MAC_BASE_PATH;
	}
	
	/**
	 * Gets the file path to the root folder of the game save on LINUS OS
	 * @return the root folder of the game save for LINUX OS
	 */
	public final static String getLinuxBasePath()
	{
		return LINUX_BASE_PATH;
	}
	
	/**
	 * Gets the file path to the root folder of the game, for the OS executing the game jar
	 * @return the root folder of the game save for the OS executing the game jar
	 */
	public final static String getBasePath()
	{
		return basePath;
	}
	
	/**
	 * Gets the game version for the current launch
	 * @return the game version for the current launch
	 */
	public final static String getVersion()
	{
		return VERSION;
	}
	
	/**
	 * Creates a new GuiMainMenu and assigns it to dimensia.gameEngine.mainMenu.
	 */
	public static void resetMainMenu()
	{
		dimensia.gameEngine.mainMenu = new GuiMainMenu();
	}
	
	/**
	 * Calls the startGame(World, EntityLivingPlayer) method of the GameEngine.
	 */
	public static void startGame(World world, EntityLivingPlayer player)
	{
		dimensia.gameEngine.startGame(world, player);
	}
}

