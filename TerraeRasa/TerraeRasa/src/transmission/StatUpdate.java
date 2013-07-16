package transmission;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import statuseffects.StatusEffect;
import statuseffects.StatusEffectAbsorb;
import utils.Cooldown;

/**
 * a data structure that sends everything relating to stats back to the player.
 * Well, anything they need to see anyway. Turns out the client doesnt actually need 
 * to know that much.
 *
 */
public class StatUpdate
		implements Serializable
{
	private static final long serialVersionUID = 1L;

	public int entityID;
	
	public boolean isStunned;
	public double defense;
	public double mana;
	public double maxMana;
	public double health;
	public double maxHealth;
	public double specialEnergy;
	public double maxSpecialEnergy;
	
	public boolean isSwingingRight;
	public boolean hasSwungTool;
	public double rotateAngle;

	public List<StatusEffect> statusEffects;
	public List<StatusEffectAbsorb> absorbs;
	public Hashtable<String, Cooldown> cooldowns;
//	public PassiveBonusContainer currentBonuses; 
//	public AuraTracker auraTracker;
				
//	public boolean isFacingRight;
	
	public boolean defeated;
	
}
