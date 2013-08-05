package utils;

/**
 * TerraeRasaException is an exception relating to something in the game that's gone wrong. This will likely be 
 * something illegal or risky in some way, but not a direct violation of anything in java.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class TerraeRasaException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new TerraeRasaException with the given error message.
	 * @param message the error message for this exception
	 */
	public TerraeRasaException(String message)
	{
        super(message);
	}
}
