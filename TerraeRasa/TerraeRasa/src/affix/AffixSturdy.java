package affix;

import passivebonuses.PassiveBonusDefense;

public class AffixSturdy extends Affix{
	private int max = 10;
	private int min = 2;
	
	public AffixSturdy() {
		super("Sturdy");
		int power = (int) (rng.nextDouble() * (max - min) + min);
		this.addPassive(new PassiveBonusDefense(power));
	}
	
}