package utils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import client.GameEngine;

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
	public static double floorOne(double value)
	{
		return value > 1.0F ? value : 1.0F;
	}
	
	/**
	 * Converts a radian measurement into a degree measurement
	 * @param radian the radian to convert
	 * @return the degree value representing the given radian
	 */
	public static double radianToDegree(double radian)
	{
		return (double) (radian * (180.0F / Math.PI));
	}
	
	/**
	 * Converts a degree measurement into a radian measurement.
	 * @param degree the degree to convert 
	 * @return the radian value representing the given degree
	 */
	public static double degreeToRadian(double degree)
	{
		return (double) (degree / (180.0F / Math.PI));
	}
	
	/**
	 * Converts polar co-ordinates(magnitudes, radians) to rectangular (x,y).
	 * @param magnitude magnitude of the vector
	 * @param angle the radian value to convert using
	 * @return rectangular co-ordinates for the vector
	 */
	public static Vector2F toRectangular(double magnitude, double angle){		
		return new Vector2F((float) (magnitude * Math.cos(angle)), (float) (magnitude * Math.sin(angle)));
	}
	
	/**
	 * Rounds down a double value to the nearest 20th (0.05). For example, the value 6.7754f will be rounded down to 6.75f;
	 * @param f the value to round down to the nearest 20th
	 * @return the rounded down value (nearest 20th)
	 */
	public static double roundDowndouble20th(double f)
	{
		return (double)(Math.floor(f * 20 + 0.5) / 20);
	}

	/**
	 * Rounds down a double value to the nearest 10th (0.1). For example, the value 6.7233f will be rounded down to 6.7f.
	 * @param f the value to round down to the nearest 10th
	 * @return the rounded down value (nearest 10th)
	 */
	public static double roundDowndouble10th(double f)
	{
		return (double)(Math.floor(f * 10 + 0.5) / 10);
	}
	
	/**
	 * Returns a value between 0.0F and 1.0F, preferring the original value given if possible
	 * @param f the value to check
	 * @return the original value, if between 0.0F and 1.0F; 0.0F if the value is below 0.0F; 1.0F if the value is above 1.0F
	 */ 
	public static double sat(double f)
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
	 * Inverses the value of the given double. If it's negative then return a positive number; if it's positive return a negative number.
	 * @return the reversed value of the given double
	 */	
	public static double inverseValue(double d)
	{
		return d * -1;
	}
	
	/**
	 * A method designed to find the angle between the player and the mouse
	 * @param mouseX - location of the mouse on the X axis, with respect to the origin
	 * @param mouseY - location of the mouse on the Y axis, with respect to the origin
	 * @param playerX - location of the player on the X axis, with respect to the origin
	 * @param playerY - location of the player on the Y axis, with respect to the origin
	 * @return
	 */
	public static int angleMousePlayer(double mouseX, double mouseY, double playerX, double playerY){
		double deltaX = mouseX - playerX;
		double deltaY = mouseY - playerY;
		int degrees = (int) Math.toDegrees(Math.atan2(deltaX, deltaY));
		if (degrees < 0){
			degrees += 360;
		}
		return degrees;
	}
	
	/**
	 * Returns a double value of at least value 0
	 * @return a double value of at least 0
	 */
	public static double zeroOrGreater(double f)
	{
		return (f < 0) ? 0: f;
	}
	
	/**
	 * Returns a value of at most 1.0f. Mostly of use for calculating light values.
	 * @return a double value of at most 1.0f
	 */
	public static double oneOrLess(double f)
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
		return (int) ((double)(Display.getHeight() - Mouse.getY()) / 2);
	}

	/**
	 * Returns a X mouse position based off the origin, in ortho units
	 * @return a mouse X position that actually makes sense, based on (0,0)
	 */
	public static int getCorrectMouseXPosition()
	{
		return (int) ((double)(Mouse.getX()) / 2);
	}
	
	/**
	 * Returns a double value of at least 1.0f
	 * @return a double value of at least 1.0f
	 */
	public static double oneOrGreater(double f)
	{
		return (f < 1) ? 1 : f;
	}
	
	/**
	 * Returns a double value from 0.0f-1.0f, reversed. This is used for lighting and colour calculations
	 * that are simply evil without this type of math. An example of this method is: 
	 * <br>
	 * 0.8f becomes 0.2f
	 * @return a double value from 0.0f-1.0f, reversed
	 */
	public static double inversedZeroToOneValue(double f)
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
	 * Returns a double inside the X world drawing bounds 
	 * @return a double value inside world X ortho bounds
	 */
	public static double returndoubleInWorldOrthoBounds_X(World world, double f)
	{
		return (f < 0) ? 0: (f >= (world.getWidth() * 6)) ? ((world.getWidth() - 1) * 6) : f;
	}
	
	/**
	 * Returns a double inside the Y world drawing bounds 
	 * @return a double value inside world Y ortho bounds
	 */
	public static double returndoubleInWorldOrthoBounds_Y(World world, double f)
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
	 * Gets fall speed based on height fallen. Greater height does more damage.
	 * @param Vi the initial upward velocity before gravity began applying
	 * @param g the g value for a given world
	 * @param ticksFallen the time spent falling, in game ticks
	 * @return damage done by the fall
	 */	
	public static double getFallSpeed(double Vi, double g, double ticksFallen)
	{
		//Vf = Vi + at
		double d = Vi + g * (ticksFallen / GameEngine.TICKS_PER_SECOND);
		return d;
	}	
	
	/**
	 * Determines the fall damage based on an exponential curve. generally death is certain after 40 blocks of falling
	 * @return the fall damage for the given distance fallen
	 */
	public static double getFallDamage(double Vi, double g, double ticksFallen)
	{
		return Math.pow((getFallSpeed(Vi, g, ticksFallen) - 4), 3);
	}

	/**
	 * Gets the 2D distance between 2 points. Uses the formula of: SQRT((x2-x1)^2 + (y2 - y1)^2)
	 * @return the distance between 2 points, in 2D space
	 */
	public static double distanceBetweenTwoPoints(double x1, double y1, double x2, double y2)
	{
		return (double) Math.sqrt(((Math.pow((x2 - x1), 2)) + (Math.pow((y2 - y1), 2))));
	}
}