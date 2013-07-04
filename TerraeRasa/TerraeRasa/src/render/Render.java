package render;

import io.TextureLoader;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import utils.MathHelper;
import utils.Texture;
import utils.TextureCoords;
import world.World;
import entities.EntityPlayer;

/**
 * Defines the base class for anything that draws something to screen in this application. Provides methods to appropriately 
 * update and move the camera, set it to the last position, render the background. Additionally, all Textures are stored here 
 * as public static fields.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Render 
{	
	public final static int TEXTURE_SHEET_ITEMS = 1,
			   TEXTURE_SHEET_TERRAIN_EARTH = 2;

	//Variables describing the size of the terrain_ground.png texture
	public final static int TEXTURE_SHEET_WIDTH = 256;
	public final static int TEXTURE_SHEET_HEIGHT = 512;
	public final static int TEXTURES_PER_ROW =  TEXTURE_SHEET_WIDTH / 16;
	public final static int TEXTURES_PER_COLUMN = TEXTURE_SHEET_HEIGHT / 16;
	public final static int BLOCK_SIZE = 6;
	public final static int BLOCK_SIZE_PIXELS = 12;
	
	//Variables describing the size  of the icons.png texture
	public final static int ICONS_SHEET_WIDTH = 256;
	public final static int ICONS_SHEET_HEIGHT = 256;
	public final static int ICONS_PER_ROW =  ICONS_SHEET_WIDTH / 16;
	public final static int ICONS_PER_COLUMN = ICONS_SHEET_HEIGHT / 16;
	
	//Items.png variables
	public final static int ITEMS_SHEET_WIDTH = 512;
	public final static int ITEMS_SHEET_HEIGHT = 256;
	public final static int ITEMS_PER_ROW =  ICONS_SHEET_WIDTH / 16;
	public final static int ITEMS_PER_COLUMN = ICONS_SHEET_HEIGHT / 16;
	
	public static Texture[] icons;
	
	public static Texture zombie;
	public static Texture goblin;
	public static Texture slime;
	
	//</menu textures>

	public static Texture dino;
	public static Texture ICONS;
	public static Texture snow_tex;
	public static Texture TERRAIN;
	public static Texture TERRAIN_GROUND;
	public static Texture PROJECTILES;
	public static RenderEntities renderEntities;
	public static RenderBlocks renderBlocks;
	public static RenderParticles renderParticles;
	public static TrueTypeFont trueTypeFont;
	public static TrueTypeFont trueTypeFont_AR;
	public static Tessellator t = Tessellator.instance;
	public static TextureLoader loader;
	public static Texture playerTexture;
	public static Texture actionbarSlot;
	public static Texture actionbarOutline;
	public static Texture tooltipBackground;
	public static Texture logo;
	public static Texture[] textures;
	public static Texture[] backgroundImages;
	//menu textures
	public static Texture buttonTexture;
	public static Texture[] moons;
	public static Texture sun; 
	public static Texture background_1; //Background (sky)
	
	public static Texture background_menu;
	public static Texture starParticleBackground;
	public static Texture starParticleForeground;
	public static Texture menuButtonBackground;
	public static Texture starAnimationSheet;
	
	/** 
	 * cameraX is a relatively unique variable. It's where the camera is moved to on the X axis, based on player location. 
	 * Therefore this value becomes very important to rendering user interface components. Using this value in addition 
	 * to another, allows something to always be seen by the player, and for that object not to shake or move randomly. 
	 * This value is always in 'ortho' units, for easy rendering and camera re-adjustment. <b>NOTE: cameraX is often 
	 * negative or inversed, use getCameraX() to correct this. </b>
	 **/
	private static int cameraX;
	/** 
	 * cameraY is a relatively unique variable. It's where the camera is moved to on the Y axis, based on player location. Therefore this value becomes
	 * very important to rendering user interface components. Using this value in addition to another, allows something to always be seen
	 * by the player, and for that object not to shake or move randomly. This value is always in 'ortho' units, for easy rendering and 
	 * camera readjustment. <b>NOTE: cameraY is often negative or inversed, use getCameraY() to correct this. </b>
	 **/
	private static int cameraY;
	public static Texture mainLogo;
	
	/**
	 * Renders a sky background image. This is a filler for now.
	 */
	protected void renderSkyBackgroundScene()
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
	 * Adjusts the camera, based on where the player is. This should only be called once (the first time) per render. 
	 * This is due to the large amount of math required. Subsequent camera adjustments (per rendered frame) can be made 
	 * much cheaper using {@link #adjustToLastCameraPosition()}.
	 */
	protected void adjustCamera(World world, EntityPlayer player) 
	{
		GL11.glLoadIdentity();// + seems to be on ----> this side of the Y axis, it behaves quite wierdly when comparing along the X axis however
		final int width = (int) (Display.getWidth() / 2) + 2;
		final int height = (int) (Display.getHeight() / 2) + 2;
		final int xAdjust = (int) (Display.getWidth() * 0.239);
		final int yAdjust = (int) (Display.getHeight() * 0.188);
		double sx = MathHelper.inverseValue((int)player.x) + xAdjust;
		double sy = MathHelper.inverseValue((int)player.y) + yAdjust;
		if(sx > 0) sx = 0; //Bounds checking
		if(sy > 0) sy = 0;				
		if(sx < MathHelper.inverseValue(world.getWidth()) * 6 + width) 
			sx = MathHelper.inverseValue(world.getWidth()) * 6 + width;
		if(sy < MathHelper.inverseValue(world.getHeight()) * 6 + height) 
			sy = MathHelper.inverseValue(world.getHeight()) * 6 + height;		
		
		cameraX = (int) sx;
		cameraY = (int) sy;
		GL11.glTranslated(sx, sy, -2000F);	//Adjust the camera	
	}
	
	/**
	 * Adjusts the camera to the last position it was at (cameraX, cameraY). This is useful for rotation, etc where
	 * GL11.glTranslatef has to be called to rotate properly; this is due to there be significant math required ONLY the
	 * first camera adjustment per frame. 
	 */
	protected void adjustCameraToLastPosition()
	{
		GL11.glLoadIdentity();
		GL11.glTranslatef(cameraX, cameraY, -2000F);
	}
	
	/**
	 * Loads all game textures in use by Render and all extensions. This cannot be threaded.
	 */
	public static void initializeTextures(World world)
	{
		trueTypeFont = new TrueTypeFont(new Font("Comic Sans MS", Font.BOLD, 32), false);
		loader = new TextureLoader();
		backgroundImages = new Texture[12];
		textures = loader.getItemTextureArray();
		
		//<menu textures>
		moons = loader.getMoonTextureArray();
		sun = loader.getTexture("Resources/sun.png");
		background_1 = loader.getTexture("Resources/background_1.png");		
		buttonTexture = loader.getTexture("Resources/gui_texture.png");		
		//</menu textures>
		
		goblin = loader.getTexture("Resources/goblin.png");
		zombie = loader.getTexture("Resources/zombie.png");
		slime = loader.getTexture("Resources/slime.png");
		dino = loader.getTexture("Resources/dino.png");
		
		snow_tex = loader.getTexture("Resources/snow_particle.png");
		ICONS = loader.getTexture("Resources/icons.png");
		TERRAIN = loader.getTexture("Resources/terrain.png");
		TERRAIN_GROUND = loader.getTexture("Resources/terrain_earth.png");
		PROJECTILES = loader.getTexture("Resources/projectiles.png");
		
		playerTexture = loader.getTexture("Resources/player_1.png");
		actionbarSlot = loader.getTexture("Resources/player_actionbar_slot.png");
		actionbarOutline = loader.getTexture("Resources/player_actionbar_outline.png");
		tooltipBackground = loader.getTexture("Resources/tooltip_background.png");
		
		icons = loader.getIcons();
		
		//
		zombie_test_animation = new Animation(loader.getTexture("Resources/test_animation.png"), 
				new TextureCoords(0, 0, 128F / 256F, 48.0F / 256F), 
				32, 
				48,
				160, 
				4, 
				Animation.ANIMATE_HORIZONTAL);
		background_menu = loader.getTexture("Resources/stars.png");
		logo = loader.getTexture("Resources/logo.png");
		starParticleBackground = loader.getTexture("Resources/shooting_star_background.png");
		starParticleForeground = loader.getTexture("Resources/shooting_star_foreground.png");
		menuButtonBackground = loader.getTexture("Resources/menubuttonbackground.png");
		//fivePointStar = loader.getTexture("Resources/five_point_star.png");
		starAnimationSheet = loader.getTexture("Resources/star_animation.png");
		renderParticles = new RenderParticles();
		renderEntities = new RenderEntities();
		renderBlocks = new RenderBlocks();
	}	
		
	static Animation zombie_test_animation;
	
	/**
	 * Gets the absolute value of cameraX, as it's generally negative. This value is useful for rendering the user interface and many
	 * other things, because it allows things to be rendered to screen at a static location the player can always see. See description
	 * of cameraX for more information.
	 * @return the absolute value of cameraX
	 */
	public static int getCameraX()
	{
		return Math.abs(cameraX);
	}
	
	/**
	 * Gets the absolute value of cameraY, as it's generally negative. This value is useful for rendering the user interface and many
	 * other things, because it allows things to be rendered to screen at a static location the player can always see. See description
	 * of cameraY for more information.
	 * @return the absolute value of cameraY
	 */
	public static int getCameraY()
	{
		return Math.abs(cameraY);
	}
}