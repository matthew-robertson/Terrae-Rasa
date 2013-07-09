package statuseffects;

import utils.Damage;
import world.World;
import entities.EntityLiving;
import enums.EnumDamageSource;
import enums.EnumDamageType;

/**
 * StatusEffectFatalWound is a brutal effect that will deal immense damage after a given amount of time. It is nerfed 
 * now, but will deal an (entity's maximum_health - 1) damage after it expires. No damage is dealt before expiring. This
 * damage ignores defense, armour, and absorbs.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class StatusEffectFatalWounds extends StatusEffect
{

	public StatusEffectFatalWounds(double durationSeconds, int tier, int power, int ticksBetweenEffect) 
	{
		super(durationSeconds, tier, power, ticksBetweenEffect);
		reapplicationSkipsRemovalEffect = true;
		this.isBeneficialEffect = false;
		iconX = 6;
		iconY = 2;
	}
	
	public void removeInitialEffect(World world, EntityLiving entity)
	{
		entity.damage(world, 
				new Damage(entity.maxHealth - 1, 
						new EnumDamageType[] { EnumDamageType.BLEED }, 
						EnumDamageSource.STATUS_EFFECT).setIsDodgeable(false)
						.setPenetratesArmor(true).setPiercesAbsorbs(true), 
				true);
	}
	
	public String toString()
	{
		return "Deals maximum health - 1 damage upon expiration";
	}
}
