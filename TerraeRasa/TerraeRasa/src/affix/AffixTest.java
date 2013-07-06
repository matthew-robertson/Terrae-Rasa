package affix;

import auras.AuraHeavensReprieve;

public class AffixTest extends Affix{
	public AffixTest(){
		super("Test");
		this.id = 49;
		addAura(new AuraHeavensReprieve());
	}
}