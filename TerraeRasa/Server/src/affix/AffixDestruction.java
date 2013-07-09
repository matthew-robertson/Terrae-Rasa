package affix;

import passivebonuses.PassiveBonusDamageAll;

public class AffixDestruction extends Affix{
	private double max = 0.06;
	private double min = 0.02;
	
	public AffixDestruction(){
		super("of destruction");
		this.id = 3;
		this.setPrefix(false);
		double power = rng.nextDouble() * (max - min) + min;
		this.addPassive(new PassiveBonusDamageAll(power));
	}
}