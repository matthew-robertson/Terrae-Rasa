package affix;

import passivebonuses.PassiveBonus;
import auras.Aura;

public class AffixGenerator
{
	/**
	 * Sturdy->1
	 * Frenzied->2
	 * Destruction->3
	 */
//	IDs
	
	public static final Affix[] possibleAffixes = {
			new AffixSturdy(),
			new AffixDestruction(),
			new AffixFrenzied()			
	};
	
	public static PassiveBonus[] getPassiveBonuses(int affixID, double[] powers)
	{
		Affix affix = getByID(affixID);
		return affix.getPassives(powers);
	}
	
	public static Aura[] getAuras(int affixID, double[] powers)
	{
		Affix affix = getByID(affixID);
		return affix.getAuras(powers);		
	}
	
	public static double[] getPowers(int affixID)
	{
		Affix affix = getByID(affixID);
		return affix.rollPowers();
	}
	
	private static Affix getByID(int affixID)
	{
		for(Affix affix : possibleAffixes)
		{
			if(affix.getID() == affixID)
			{
				return affix;
			}
		}		
		return null;
	}
	
}
