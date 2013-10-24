package auras;

import server.entities.EntityPlayer;
import utils.Damage;
import world.World;

/**
 * AuraPeriodicModifer is intended to take advantage of the new Damage tech and modify periodic damage taken.
 * Damage done can be flagged as periodic. In this case, the damage will be multiplied by whatever modifier
 * is given - either an increase or decrease. Multiple auras stack multiplicatively. IE 2 0.75 modifier auras
 * provide a reduction of (0.75 * 0.75) = 0.5625, et cetera. 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraPeriodicModifier extends Aura 
{
	private static final long serialVersionUID = 1L;
	/** A double value indicating how much to modify the damage. 1 is normal; 0.5 is 50% reduced; 1.5 is 50% increased (et cetera) */
	private double damageModifier;
	
	/**
	 * Constructs a new AuraPeriodicModifier with the given damage modifier.
	 * @param damageModifier a double indicating the damage modifier of this aura; where 1 is normal damage (0 is complete reduction)
	 */
	public AuraPeriodicModifier(double damageModifier)
	{
		super();
		this.damageModifier = damageModifier;
	}
	
	public void onDamageTaken(World world, EntityPlayer player, Damage damage)
	{
		if(damage.isPeriodic())
		{
			damage.multiplyDamageValue(damageModifier);
		}		
		onPercentageHealth(world, player);
	}
	
	public String toString()
	{
		return (damageModifier < 1) ? "Reduces periodic damage by " + (int)(100.0 * (1.0 - damageModifier)) + "%" : "Increases periodic damage by " + (int)(100.0 * (1.0 - damageModifier)) + "%";
	}
}
