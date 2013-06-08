package setbonus;

import enums.EnumArmor;
import items.Item;
import items.ItemArmor;
import utils.InventoryPlayer;
import utils.ItemStack;

public class SetBonusFactory 
{
	public static final EnumArmor[] ARMOR_TYPES = 
		{ 
			EnumArmor.COPPER, 
			EnumArmor.BRONZE, 
			EnumArmor.IRON, 
			EnumArmor.SILVER, 
			EnumArmor.GOLD 
		};
	
	private static SetBonus[] getByArmorType(InventoryPlayer inventory, EnumArmor type)
	{
		int piecesEquipped = 0;
		for(ItemStack stack : inventory.getArmorInventory())
		{
			if(stack != null && ((ItemArmor)(Item.itemsList[stack.getItemID()])).getArmorType() == type)
			{
				piecesEquipped++;
			}
		}		
		return type.getBonuses(piecesEquipped);
	}
	
	public static SetBonusContainer getSetBonuses(InventoryPlayer inventory)
	{
		SetBonus[] boni = getByArmorType(inventory, EnumArmor.GOLD);
		
		/**
		 * Start here - 
		 * DO this for all material grades, and then merge the results together somehow
		 * (Vector?)
		 * This should complete set bonuses and allow auras. 
		 * 
		 * 
		 * 
		 * 
		 */
		
		if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX) == null ||
			inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX) == null || 
			inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX) == null) //No set bonus possible
			
			{ 
				return new SetBonusContainer(new SetBonus[] { });
			}
			
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.copperHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.copperBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.copperPants.getID())
			{ //Copper Set Bonus
				//defense + 1... 
				return new SetBonusContainer(new SetBonus[] { new SetBonusDefense(1) });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.bronzeHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.bronzeBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.bronzePants.getID())
			{ //Bronze Set Bonus
				//defense + 2... 
				return new SetBonusContainer(new SetBonus[] { new SetBonusDefense(2) });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.ironHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.ironBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.ironPants.getID())
			{ //Iron Set Bonus
				//defense + 3... 
				return new SetBonusContainer(new SetBonus[] { new SetBonusDefense(3) });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.silverHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.silverBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.silverPants.getID())
			{ //Silver Set Bonus - Defense +4
				return new SetBonusContainer(new SetBonus[] { new SetBonusDefense(4) });
			}
			if (inventory.getArmorInventoryStack(InventoryPlayer.HELMET_INDEX).getItemID() == Item.goldHelmet.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.BODY_INDEX).getItemID() == Item.goldBody.getID() && 
				inventory.getArmorInventoryStack(InventoryPlayer.PANTS_INDEX).getItemID() == Item.goldPants.getID())
			{ //Gold Set Bonus - Defense +5
				return new SetBonusContainer(new SetBonus[] { new SetBonusDefense(5) });
			}
			
			return new SetBonusContainer(new SetBonus[] { }); //Nothing matched 
			
	}
		
		
}
