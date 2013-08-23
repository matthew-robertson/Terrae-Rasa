package passivebonuses;

import entities.EntityPlayer;

/**
 * PassiveBonus is a fairly lightweight abstract base class for all PassiveBonus needs. A PassiveBonus is a static benefit to a stat
 * or something else in the player class that can be reversed. It does not respond to events. All subclasses of PassiveBonus
 * must implement {@link #apply(EntityPlayer)} and {@link #remove(EntityPlayer)} which will apply the set
 * bonus and then later remove it. These should directly counter each other and comply with stat-stacking rules.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class DisplayablePassiveBonus 
{
	/** The number of pieces required to activate this set bonus. */
	protected int piecesRequiredToActivate;
	protected String description;
	
	public DisplayablePassiveBonus(String description)
	{
		this.piecesRequiredToActivate = 1;
		this.description = description;
	}
	
	public DisplayablePassiveBonus(PassiveBonus bonus)
	{
		this.piecesRequiredToActivate = bonus.getPiecesRequiredToActivate();
		this.description = bonus.toString();
		if(description == null || description.equals(null))
		{
			description = "";
		}
	}
	
	public int getPiecesRequiredToActivate()
	{
		return piecesRequiredToActivate;
	}
	
	public DisplayablePassiveBonus setPiecesRequiredToActivate(int pieces)
	{
		this.piecesRequiredToActivate = pieces;
		return this;
	}
	
	public String toString()
	{
		return description;
	}
	
	public String getDescription()
	{
		return description;
	}
}
