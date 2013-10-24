package affix;

import passivebonuses.PassiveBonus;
import auras.Aura;

/**
 * AffixGenerator contains static methods to reconstruct an affix from AffixData or create a new affix. This is done for security reasons 
 * such that affixes cant be stupidly overpowered. Reconstructing an affix should first check its powers are legal with:
 * {@link #verifyPowers(int, double[])}, then get the relevant auras or passives with {@link #getPassiveBonuses(int, double[])},
 * or {@link #getAuras(int, double[])}. 
 * <br> <br>
 * Additionally, it's possible to retrieve an Affix, by ID, using {@link #getAffix(int)} and to roll powers for a fresh affix
 * using {@link #getPowers(int)}.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class AffixGenerator
{
	/**
	 * An array of the affixes allowed ingame. If an affix class exists but is not in this list than it
	 * cannot legally be used in game. (This is a debug feature).
	 * <ul>
	 *  <li> Sturdy has ID#1</li>
	 *  <li> Frenzied has ID#2</li>
	 *  <li> Destruction has ID#3</li>
	 * </ul>
	 */
	private static final Affix[] possibleAffixes = 
	{
			new AffixSturdy(),
			new AffixDestruction(),
			new AffixFrenzied()			
	};
	
	/**
	 * Gets an affix by it's unique affixID
	 * @param affixID a unique ID belonging to an affix
	 * @return the affix that corresponds to the given ID, or null if one does not exist
	 */
	public static Affix getAffix(int affixID)
	{
		return getByID(affixID);
	}
	
	/**
	 * Verifies a power[] does not contain any illegal values.
	 * @param affixID a unique ID belonging to an affix
	 * @param powers the array of powers belonging to an affix
	 */
	public static void verifyPowers(int affixID, double[] powers)
	{
		Affix affix = getByID(affixID);
		if(affix == null && powers != null)
		{
			for(int i = 0; i < powers.length; i++)
			{
				powers[i] = 0.0;
			}
			throw new RuntimeException("This is illegal - forcing all powers to 0.");
		}
		else if(powers == null)
		{
			powers = new double[0];
			throw new RuntimeException("This is illegal - generating fresh power[].");
		}
		affix.verifyPowers(powers);
	}

	/**
	 * Gets the PassiveBonuses for a given affix, given a power[] and affixID.
	 * @param affixID a unique ID belonging to an affix
	 * @param powers the array of powers belonging to an affix
	 * @return a PassiveBonus[] containing all the bonuses for this affix - this can be of size 0
	 */
	public static PassiveBonus[] getPassiveBonuses(int affixID, double[] powers)
	{
		Affix affix = getByID(affixID);
		return (affix == null) ? new PassiveBonus[]{ } : affix.getPassives(powers);
	}

	/**
	 * Gets the Auras for a given affix, given a power[] and affixID.
	 * @param affixID a unique ID belonging to an affix
	 * @param powers the array of powers belonging to an affix
	 * @return a Aura[] containing all the auras for this affix - this can be of size 0
	 */
	public static Aura[] getAuras(int affixID, double[] powers)
	{
		Affix affix = getByID(affixID);
		return (affix == null) ? new Aura[]{ } : affix.getAuras(powers);		
	}
	
	/**
	 * Rolls a fresh power[] for a given affix(ID)
	 * @param affixID a unique ID belonging to an affix
	 * @return a power[] for the given affix - it's possible this could be size 0
	 */
	public static double[] getPowers(int affixID)
	{
		Affix affix = getByID(affixID);
		return (affix == null) ? new double[]{ } : affix.rollPowers();
	}
	
	/**
	 * Gets an affix by it's static, final id. This will yield null if no matching ID is found.
	 * @param affixID the ID of the affix to get
	 * @return the affix corresponding to that ID, or null if none is found.
	 */
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
