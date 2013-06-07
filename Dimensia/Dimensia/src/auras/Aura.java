package auras;

/**
 * Cool effects - not passive stat bonuses. Those are set bonuses!
 *
 */
public abstract class Aura 
{
	public static final int TYPE_DAMAGE_DONE = 0,
							TYPE_DAMAGE_TAKEN = 1,
							TYPE_HEAL = 2,
							TYPE_ON_DEATH = 3,
							TYPE_PERCENT_HEALTH = 4;
	
	//In ticks
	protected int maxCooldown;
	
	//In ticks
	protected int remainingCooldown;
	
	//Chance to activate from 0->1, where 0 = 0%; 1 = 100%
	protected float activationChance;
	
	
	protected Aura()
	{
		remainingCooldown = 0;
	}

	public abstract void update();

	public abstract void onDamageDone();

	//This should probably call onPercentageHealth();
	public abstract void onDamageTaken();
	
	//This should also call onPercentageHealth();
	public abstract void onHeal();
	
	public abstract void onPercentageHealth();

	public abstract void onDeath();
}
