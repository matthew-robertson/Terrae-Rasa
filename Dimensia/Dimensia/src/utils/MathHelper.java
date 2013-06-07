package utils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import world.World;

/**
 * <code>MathHelper</code> defines many miscellaneous methods to help with lighting, world bounds math, fall damage,
 * and anything else in the entire project. All methods are static, so they may be directly referenced from 
 * anywhere within the project. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MathHelper 
{	
	/**
	 * Ensures a value is never less than 1.0F
	 * @param value the value to verify
	 * @return the value given, or 1.0F if that was less than 1.0F
	 */
	public static float floorOne(float value)
	{
		return value > 1.0F ? value : 1.0F;
	}
	
	/**
	 * Converts a radian measurement into a degree measurement
	 * @param radian the radian to convert
	 * @return the degree value representing the given radian
	 */
	public static float radianToDegree(float radian)
	{
		return (float) (radian * (180.0F / Math.PI));
	}
	
	/**
	 * Converts a degree measurement into a radian measurement.
	 * @param degree the degree to convert 
	 * @return the radian value representing the given degree
	 */
	public static float degreeToRadian(float degree)
	{
		return (float) (degree / (180.0F / Math.PI));
	}
	
	/**
	 * Converts polar co-ordinates(magnitudes, radians) to rectangular (x,y).
	 * @param magnitude magnitude of the vector
	 * @param angle the radian value to convert using
	 * @return rectangular co-ordinates for the vector
	 */
	public static Vector2F toRectangular(float magnitude, float angle){		
		return new Vector2F((float) (magnitude * Math.cos(angle)), (float) (magnitude * Math.sin(angle)));
		
	}
	
	/**
	 * Rounds down a float value to the nearest 20th (0.05). For example, the value 6.7754f will be rounded down to 6.75f;
	 * @param f the value to round down to the nearest 20th
	 * @return the rounded down value (nearest 20th)
	 */
	public static float roundDownFloat20th(float f)
	{
		return (float)(Math.floor(f * 20 + 0.5) / 20);
	}

	/**
	 * Rounds down a float value to the nearest 10th (0.1). For example, the value 6.7233f will be rounded down to 6.7f.
	 * @param f the value to round down to the nearest 10th
	 * @return the rounded down value (nearest 10th)
	 */
	public static float roundDownFloat10th(float f)
	{
		return (float)(Math.floor(f * 10 + 0.5) / 10);
	}
	
	/**
	 * Returns a value between 0.0F and 1.0F, preferring the original value given if possible
	 * @param f the value to check
	 * @return the original value, if between 0.0F and 1.0F; 0.0F if the value is below 0.0F; 1.0F if the value is above 1.0F
	 */ 
	public static float sat(float f)
	{
		return (f > 1.0F) ? 1.0F : (f < 0.0F) ? 0.0F : f;
	}
	
	/**
	 * Inverses the value of the given integer. If it's negative then return a positive number; if it's positive return a negative number.
	 * @return the reversed value of the given Integer
	 */	
	public static int inverseValue(int i)
	{
		return i * -1;		
	}

	/**
	 * Inverses the value of the given float. If it's negative then return a positive number; if it's positive return a negative number.
	 * @return the reversed value of the given float
	 */	
	public static float inverseValue(float f)
	{
		return f * -1;
	}
	
	/**
	 * A method designed to find the angle between the player and the mouse
	 * @param mouseX - location of the mouse on the X axis, with respect to the origin
	 * @param mouseY - location of the mouse on the Y axis, with respect to the origin
	 * @param playerX - location of the player on the X axis, with respect to the origin
	 * @param playerY - location of the player on the Y axis, with respect to the origin
	 * @return
	 */
	public static int angleMousePlayer(float mouseX, float mouseY, float playerX, float playerY){
		float deltaX = mouseX - playerX;
		float deltaY = mouseY - playerY;
		int degrees = (int) Math.toDegrees(Math.atan2(deltaX, deltaY));
		if (degrees < 0){
			degrees += 360;
		}
		return degrees;
	}
	
	/**
	 * Returns a float value of at least value 0
	 * @return a float value of at least 0
	 */
	public static float zeroOrGreater(float f)
	{
		return (f < 0) ? 0: f;
	}
	
	/**
	 * Returns a value of at most 1.0f. Mostly of use for calculating light values.
	 * @return a float value of at most 1.0f
	 */
	public static float oneOrLess(float f)
	{
		return (f > 1) ? 1 : f;
	}
	
	/**
	 * Returns an integer value of at least 0
	 * @return an integer value of at least 0
	 */
	public static int zeroOrGreater(int i)
	{
		return (i < 0) ? 0 : i;
	}
	
	/**
	 * Returns the closest (lower) multiple of 6. Mostly used to correct block rounding errors
	 * @return the closest (lower) multiple of 6, for the given Integer
	 */
	public static int multipleOfSix(int i)
	{
		return (i / 6) * 6;
	}
	
	/**
	 * Returns a Y mouse position based off the origin, in ortho units
	 * @return a mouse Y position that actually makes sense, based on (0,0)
	 */
	public static int getCorrectMouseYPosition()
	{
		return (int) ((float)(Display.getHeight() - Mouse.getY()) / 2);
	}

	/**
	 * Returns a X mouse position based off the origin, in ortho units
	 * @return a mouse X position that actually makes sense, based on (0,0)
	 */
	public static int getCorrectMouseXPosition()
	{
		return (int) ((float)(Mouse.getX()) / 2);
	}
	
	/**
	 * Returns a float value of at least 1.0f
	 * @return a float value of at least 1.0f
	 */
	public static float oneOrGreater(float f)
	{
		return (f < 1) ? 1 : f;
	}
	
	/**
	 * Returns a float value from 0.0f-1.0f, reversed. This is used for lighting and colour calculations
	 * that are simply evil without this type of math. An example of this method is: 
	 * <br>
	 * 0.8f becomes 0.2f
	 * @return a float value from 0.0f-1.0f, reversed
	 */
	public static float inversedZeroToOneValue(float f)
	{
		return 1 - f;
	}
	
	/**
	 * Returns an integer inside the worldmap X bounds 
	 * @return an X value Integer safe for use in the 'world map'
	 */
	public static int returnIntegerInWorldMapBounds_X(World world, int i)
	{
		return (i < 0) ? 0 : (i >= world.getWidth()) ? world.getWidth() - 1 : i;
	}
	
	/**
	 * Returns an integer inside the worldmap Y bounds 
	 * @return a Y value Integer safe for use in the 'world map'
	 */	
	public static int returnIntegerInWorldMapBounds_Y(World world, int i)
	{
		return (i < 0) ? 0 : (i >= world.getHeight()) ? world.getHeight() - 1 : i;
	}
	
	/**
	 * Returns a float inside the X world drawing bounds 
	 * @return a float value inside world X ortho bounds
	 */
	public static float returnFloatInWorldOrthoBounds_X(World world, float f)
	{
		return (f < 0) ? 0: (f >= (world.getWidth() * 6)) ? ((world.getWidth() - 1) * 6) : f;
	}
	
	/**
	 * Returns a float inside the Y world drawing bounds 
	 * @return a float value inside world Y ortho bounds
	 */
	public static float returnFloatInWorldOrthoBounds_Y(World world, float f)
	{
		return (f < 0) ? 0: (f >= (world.getHeight() * 6)) ? ((world.getHeight() - 1) * 6) : f;
	}
	
	/**
	 * Determines whether the specified integer inside the world map X bounds
	 * @return whether the integer is inside world map X bounds
	 */
	public static boolean inWorldBounds_X(World world, int i)
	{
		if(i < 0 || i >= world.getWidth())
			return false;
		return true;
	}
	
	/**
	 * Determines whether the specified integer inside the world map Y bounds
	 * @return whether the integer is inside world map Y bounds
	 */	
	public static boolean inWorldBounds_Y(World world, int i)
	{
		if(i < 0 || i >= world.getHeight())
			return false;
		return true;
	}
	
	/**
	 * Gets fall damage based on height fallen. Greater height does more damage.
	 * @param i fall distance in pixels
	 * @return damage done by the fall
	 */	
	public static float getFallSpeed(float speed, float tick)
	{
		final float BLOCKS_OF_THREE_SPEED_AT_START_MINUS_ONE = 5.0f; //blocks before speed increases exponentially 
		float f = (float) (Math.pow(speed, (1.0f + ((tick - BLOCKS_OF_THREE_SPEED_AT_START_MINUS_ONE) / 45.0f))) 
				+ ((tick - BLOCKS_OF_THREE_SPEED_AT_START_MINUS_ONE) / 9.5f));
		return (f > 20) ? 20 : (tick >= BLOCKS_OF_THREE_SPEED_AT_START_MINUS_ONE) ? f : 3;
	}	
	
	/**
	 * Determines the fall damage based on an exponential curve. generally death is certain after 40 blocks of falling
	 * @return the fall damage for the given distance fallen
	 */
	public static float getFallDamage(float distanceFallen, float maxHeightFallenSafely)
	{
		float f = (distanceFallen - maxHeightFallenSafely) / 6;
		float f1 = (float) ((Math.pow(f, 1.5f)) + (f * 0.8f));
		return (f >= 0) ? f1 : 0.0f; 
	}

	/**
	 * Gets the 2D distance between 2 points. Uses the formula of: SQRT((x2-x1)^2 + (y2 - y1)^2)
	 * @return the distance between 2 points, in 2D space
	 */
	public static float distanceBetweenTwoPoints(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt(((Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2))));
	}
}