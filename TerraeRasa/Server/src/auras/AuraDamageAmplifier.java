package auras;

import utils.Damage;
import world.World;
import entities.EntityPlayer;
import enums.EnumDamageType;

/**
 * AuraDamageAmplifier provides a damage increase to some damage type. This amplification can
 * actually be negative (IE a reduction), in addition to positive (IE an increase) depending on the given damageModifer. 
 * A damage modifier of 1 is normal, 0.5 is halved damage, 1.5 is 50% more damage done, etc. Only 1 damage type can be
 * amplified per aura. Damage with multiple types will still be amplified.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AuraDamageAmplifier extends Aura
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnumDamageType amplifiedType;
	private double damageModifier;
	private boolean percentileIncrease;
	
	/**
	 * Constructs a new Damage Amplification Aura.
	 * @param type the damage type to amplify
	 * @param damageModifier the modifier to apply, which can be percentile or flat
	 * @param percentileIncrease true if the aura multiplies the damage; false if it simply adds a flat amount
	 */
	public AuraDamageAmplifier(EnumDamageType type, double damageModifier, boolean percentileIncrease)
	{
		super();
		this.amplifiedType = type;
		this.damageModifier = damageModifier;
		this.percentileIncrease = percentileIncrease;
	}
	
	public void onDamageDone(World world, EntityPlayer player, Damage damage)
	{
		for(EnumDamageType type : damage.getType())
		{
			if(this.amplifiedType == type)
			{
				if(percentileIncrease)
				{
					damage.multiplyDamageValue(damageModifier);
				}
				else
				{
					damage.addDamage(damageModifier);
				}
				return;
			}
		}		
	}	
}
