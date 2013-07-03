package passiveBonus;

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
public abstract class PassiveBonus 
		 implements IPassiveBonus
{
	/** A value that determines the strength of a passive bonus. Effects vary by actual bonus. */
	protected double power;
	/** The number of pieces required to activate this passive bonus. */
	protected int piecesRequiredToActivate;
	
	/**
	 * Only a subclass of PassiveBonus can use this constructor because this class requires subclassing.
	 * @param power the strength of the PassiveBonus, varying by effect
	 */
	protected PassiveBonus(double power)
	{
		this.power = power;
		this.piecesRequiredToActivate = 1;
	}

	/**
	 * Only a subclass of PassiveBonus can use this constructor because this class requires subclassing.
	 * @param power the strength of the PassiveBonus, varying by effect
	 * @param piecesRequiredToActivate the number of pieces required to activate this passive bonus
	 */
	protected PassiveBonus(double power, int piecesRequiredToActivate)
	{
		this.power = power;
		this.piecesRequiredToActivate = piecesRequiredToActivate;
	}
	
	public int getPiecesRequiredToActivate()
	{
		return piecesRequiredToActivate;
	}
	
	public PassiveBonus setPiecesRequiredToActivate(int pieces)
	{
		this.piecesRequiredToActivate = pieces;
		return this;
	}

	public String toString()
	{
		return "PassiveBonus: Effect=None";
	}
}
