package statuseffects;

import world.World;
import entities.EntityLiving;

/**
 * IStatusEffect defines the different methods that a StatusEffect must implement.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public interface IStatusEffect 
{
	public void applyInitialEffect(World world, EntityLiving entity);
	
	public void removeInitialEffect(World world, EntityLiving entity);
	
	public void applyPeriodicBonus(World world, EntityLiving entity);
	
	public boolean isExpired();
	
	public long getID();
}
