package savable;

import java.util.Hashtable;

import utils.Cooldown;
import utils.ItemStack;
import enums.EnumPlayerDifficulty;

public class SavablePlayer 
{
	public String playerName;
	public EnumPlayerDifficulty difficulty;
	public double mana;
	public double health;
	public double specialEnergy;	
	public int baseSpecialEnergy;		
	public int baseMaxHealth;
	public int baseMaxMana;	
	public Hashtable<String, Cooldown> cooldowns;
	public ItemStack[] mainInventory;
	public ItemStack[] armorInventory;
	public ItemStack[] quiver;
	public ItemStack heldMouseItem;
	
	public SavablePlayer()
	{
		playerName = "";
		difficulty = EnumPlayerDifficulty.NORMAL;
		mana = 0;
		health = 0;
		specialEnergy = 0;
		baseSpecialEnergy = 0;
		baseMaxHealth = 0;
		baseMaxMana = 0;
		cooldowns = new Hashtable<String, Cooldown>();
		mainInventory = new ItemStack[0];
		armorInventory = new ItemStack[0];
		quiver = new ItemStack[0];
	}
}
