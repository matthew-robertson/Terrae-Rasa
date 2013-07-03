package affix;

import auras.AuraHeavensReprieve;

public class AffixTest extends Affix{
	public AffixTest(){
		super("Test");
		addAura(new AuraHeavensReprieve());
	}
}