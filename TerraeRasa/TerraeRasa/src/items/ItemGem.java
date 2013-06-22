package items;

import setbonus.SetBonus;
import auras.Aura;
import enums.EnumColor;

public class ItemGem extends Item
{
	protected EnumColor gemColor;
	protected SetBonus[] setBonuses;
	protected Aura[] auras;
	
	public ItemGem(int id, EnumColor gemColor, SetBonus[] setBonuses, Aura[] auras)
	{
		super(id);
		this.setBonuses = setBonuses;
		this.auras = auras;
		this.gemColor = gemColor;
	}
	
	//... Getters and Setters!
}
