package affix;

import passivebonuses.PassiveBonusAttackSpeed;
import passivebonuses.PassiveBonusStrength;

public class AffixFrenzied extends Affix{
	
	private int maxStr = 10;
	private int minStr = 2;
	private double maxSpd = 0.16;
	private double minSpd = 0.02;
	
	public AffixFrenzied(){
		super("Frenzied");
		int strength = (int) (rng.nextDouble() * (maxStr - minStr) + minStr);
		double speed = rng.nextDouble() * (maxSpd - minSpd) + minSpd;
		addPassive(new PassiveBonusStrength(strength));
		addPassive(new PassiveBonusAttackSpeed(speed));
		
	}
}