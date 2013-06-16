package setbonus;

import java.io.Serializable;

import entities.EntityLivingPlayer;

/**
 * SetBonus is a fairly lightweight abstract base class for all SetBonus needs. A SetBonus is a static benefit to a stat
 * or something else in the player class that can be reversed. It does not respond to events. All subclasses of SetBonus
 * must implement {@link #apply(EntityLivingPlayer)} and {@link #remove(EntityLivingPlayer)} which will apply the set
 * bonus and then later remove it. These should directly counter each other and comply with stat-stacking rules.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public abstract class SetBonus 
		implements Serializable, ISetBonus
{
	private static final long serialVersionUID = 1L;
	/** A value that determines the strength of a set bonus. Effects vary by actual bonus. */
	protected double power;
	/** The number of pieces required to activate this set bonus. */
	protected int piecesRequiredToActivate;
	
	/**
	 * Only a subclass of SetBonus can use this constructor because this class requires subclassing.
	 * @param power the strength of the SetBonus, varying by effect
	 */
	protected SetBonus(double power)
	{
		this.power = power;
		this.piecesRequiredToActivate = 1;
	}

	/**
	 * Only a subclass of SetBonus can use this constructor because this class requires subclassing.
	 * @param power the strength of the SetBonus, varying by effect
	 * @param piecesRequiredToActivate the number of pieces required to activate this set bonus
	 */
	protected SetBonus(double power, int piecesRequiredToActivate)
	{
		this.power = power;
		this.piecesRequiredToActivate = piecesRequiredToActivate;
	}
	
	public int getPiecesRequiredToActivate()
	{
		return piecesRequiredToActivate;
	}
	
	public SetBonus setPiecesRequiredToActivate(int pieces)
	{
		this.piecesRequiredToActivate = pieces;
		return this;
	}

	public String toString()
	{
		return "SetBonus: Effect=None";
	}
}
