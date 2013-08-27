package affix;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusDamageAll;
import auras.Aura;

public class AffixDestruction extends Affix{

	private double max = 0.06;
	private double min = 0.02;
	
	public AffixDestruction(){
		super("of destruction", 3, false);
		
	}

	//param powers [damage_bonus_value{passive}]
	public double[] rollPowers() {		
		return new double[] {  rng.nextDouble() * (max - min) + min };
	}

	//param powers [damage_bonus_value{passive}]
	public PassiveBonus[] getPassives(double[] powers) {
		return new PassiveBonus[] { new PassiveBonusDamageAll(powers[0]) };
	}

	//param powers [damage_bonus_value{passive}]
	public Aura[] getAuras(double[] powers) {
		return new Aura[] { };
	}
}