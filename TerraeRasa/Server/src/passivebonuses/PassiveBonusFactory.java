package passivebonuses;

import items.Item;
import items.ItemArmor;

import java.util.Vector;

import utils.InventoryPlayer;
import utils.ItemStack;
import enums.EnumArmor;

/**
 * PassiveBonusFactory determines the PassiveBonuses for a given set of armour equipped on a player. PassiveBonusFactory 
 * exposes only the {@link #getPassiveBonuses(InventoryPlayer)} method which will return the PassiveBonuses for 
 * that given player inventory. PassiveBonuses found using PassiveBonusFactory are returned as a PassiveBonusContainer.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class PassiveBonusFactory 
{
	/** A list of all the EnumArmor tiers to consider. This is take directly from EnumArmor.getTiers(). */
	private static final EnumArmor[] ARMOR_TYPES = EnumArmor.getTiers();
	
	/**
	 * Gets the active set bonuses for an armor type. This is based on the total number of pieces required.
	 * @param inventory the player's inventory
	 * @param type the EnumArmor tier to get PassiveBonuses for
	 * @return a PassiveBonus[] of all the activate set bonuses for a given tier of gear
	 */
	private static PassiveBonus[] getBonusesByArmorType(InventoryPlayer inventory, EnumArmor type)
	{
		//Count the pieces of that tier active
		int piecesEquipped = 0;
		for(ItemStack stack : inventory.getArmorInventory())
		{
			if(stack != null && ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType() == type)
			{
				piecesEquipped++;
			}
		}		
		//Get the set bonuses based on piecesEquipped
		return type.getBonuses(piecesEquipped); 
	}
	
	/**
	 * Gets all the active PassiveBonuses for the player. This will consider all sets of gear, and can mix together 
	 * different set bonuses such as the gold2Piece, and silver2Piece - giving the effect of both.
	 * @param inventory the player's inventory
	 * @return a PassiveBonusContainer containing all the activate PassiveBonuses for the player
	 */
	public static PassiveBonusContainer getPassiveBonuses(InventoryPlayer inventory)
	{
		Vector<PassiveBonus> allPassiveBonuses = new Vector<PassiveBonus>();
		for(EnumArmor armorType : ARMOR_TYPES)
		{
			PassiveBonus[] PassiveBonuses = getBonusesByArmorType(inventory, armorType);
			for(PassiveBonus bonus : PassiveBonuses)
			{
				allPassiveBonuses.add(bonus);
			}
		}
		return new PassiveBonusContainer(allPassiveBonuses);
	}
}
