package affix;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusAttackSpeed;
import passivebonuses.PassiveBonusStrength;
import auras.Aura;

public class AffixFrenzied extends Affix{
	
	private int maxStr = 10;
	private int minStr = 2;
	private double maxSpd = 0.16;
	private double minSpd = 0.02;
	
	public AffixFrenzied(){
		super("Frenzied", 2, true);
		
		
	}

	//[strength_value{passive}, attack_speed_value{passive}
	public double[] rollPowers() {
		return new double[] { (int) (rng.nextDouble() * (maxStr - minStr) + minStr), rng.nextDouble() * (maxSpd - minSpd) + minSpd};
	}

	//[strength_value{passive}, attack_speed_value{passive}
	public PassiveBonus[] getPassives(double[] powers) {
		return new PassiveBonus[] { new PassiveBonusStrength(powers[0]), new PassiveBonusAttackSpeed(powers[1])};
	}

	//[strength_value{passive}, attack_speed_value{passive}
	public Aura[] getAuras(double[] powers) {
		return new Aura[] { };
	}

}