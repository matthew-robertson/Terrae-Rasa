package setbonus;

import items.Item;
import items.ItemArmor;

import java.util.Vector;

import utils.InventoryPlayer;
import utils.ItemStack;
import enums.EnumArmor;

/**
 * SetBonusFactory determines the SetBonuses for a given set of armor equipped on a player. SetBonusFactory 
 * exposes only the {@link #getSetBonuses(InventoryPlayer)} method which will return the SetBonuses for 
 * that given player inventory. SetBonuses found using SetBonusFactory are returned as a SetBonusContainer.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class SetBonusFactory 
{
	/** A list of all the EnumArmor tiers to consider. This is take directly from EnumArmor.getTiers(). */
	private static final EnumArmor[] ARMOR_TYPES = EnumArmor.getTiers();
	
	/**
	 * Gets the active set bonuses for an armor type. This is based on the total number of pieces required.
	 * @param inventory the player's inventory
	 * @param type the EnumArmor tier to get SetBonuses for
	 * @return a SetBonus[] of all the activate set bonuses for a given tier of gear
	 */
	private static SetBonus[] getBonusesByArmorType(InventoryPlayer inventory, EnumArmor type)
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
	 * Gets all the active SetBonuses for the player. This will consider all sets of gear, and can mix together 
	 * different set bonuses such as the gold2Piece, and silver2Piece - giving the effect of both.
	 * @param inventory the player's inventory
	 * @return a SetBonusContainer containing all the activate SetBonuses for the player
	 */
	public static SetBonusContainer getSetBonuses(InventoryPlayer inventory)
	{
		Vector<SetBonus> allSetBonuses = new Vector<SetBonus>();
		for(EnumArmor armorType : ARMOR_TYPES)
		{
			SetBonus[] setBonuses = getBonusesByArmorType(inventory, armorType);
			for(SetBonus bonus : setBonuses)
			{
				allSetBonuses.add(bonus);
			}
		}
		return new SetBonusContainer(allSetBonuses);
	}
}
