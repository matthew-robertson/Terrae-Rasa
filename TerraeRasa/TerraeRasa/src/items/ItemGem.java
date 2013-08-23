package items;

import passivebonuses.DisplayablePassiveBonus;
import auras.DisplayableAura;
import enums.EnumColor;

public class ItemGem extends Item
{
	protected EnumColor gemColor;
	protected DisplayablePassiveBonus[] PassiveBonuses;
	protected DisplayableAura[] auras;
	
	public ItemGem(int id)
	{
		super(id);
		this.PassiveBonuses = new DisplayablePassiveBonus[0];
		this.auras = new DisplayableAura[0];
		this.gemColor = EnumColor.WHITE;
	}
	
	public ItemGem setAuras(DisplayableAura[] auras)
	{
		this.auras = auras;
		return this;
	}
	
	public ItemGem PassiveBonuses(DisplayablePassiveBonus[] bonuses)
	{
		this.PassiveBonuses = bonuses;
		return this;		
	}
	
	public ItemGem setGemColor(EnumColor color)
	{
		this.gemColor = color;
		return this;
	}
	
	public EnumColor getColor()
	{
		return gemColor;
	}
	
	public DisplayableAura[] getAuras()
	{
		return auras;
	}
	
	public DisplayablePassiveBonus[] getBonuses()
	{
		return PassiveBonuses;
	}
}
