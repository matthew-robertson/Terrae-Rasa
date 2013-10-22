package passivebonuses;

import java.io.Serializable;

/**
 * DisplayablePassiveBonus is a data structure used to display a PassiveBonus client side. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DisplayablePassiveBonus 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** The number of pieces required to activate this set bonus. */
	protected int piecesRequiredToActivate;
	/** A description of this DisplayablePassiveBonus. */
	protected String description;
	
	/**
	 * Constructs a new DisplayablePassiveBonus given a description. This will set the pieces required to 1.
	 * @param description the description of this DisplayablePassiveBonus
	 */
	protected DisplayablePassiveBonus(String description)
	{
		this.piecesRequiredToActivate = 1;
		this.description = description;
	}
	
	/**
	 * Constructs a new DisplayablePassiveBonus given a PassiveBonus. This will copy both the number of pieces required and a description.
	 * @param bonus the PassiveBonus by which to create a DisplayablePassiveBonus
	 */
	protected DisplayablePassiveBonus(PassiveBonus bonus)
	{
		this.piecesRequiredToActivate = bonus.getPiecesRequiredToActivate();
		this.description = bonus.toString();
		if(description == null || description.equals(null))
		{
			description = "";
		}
	}
	
	/**
	 * Gets the number of pieces in a set required to activate this DisplayablePassiveBonus.
	 * @return the number of pieces required to activate this DisplayablePassiveBonus
	 */
	public int getPiecesRequiredToActivate()
	{
		return piecesRequiredToActivate;
	}
	
	/**
	 * Provides a description of this DisplayablePassiveBonus that is in plaintext.
	 */
	public String toString()
	{
		return description;
	}
	
	/**
	 * Gets the description of this DisplayablePassiveBonus.
	 * @return the description of this DisplayablePassiveBonus
	 */
	public String getDescription()
	{
		return description;
	}
}
