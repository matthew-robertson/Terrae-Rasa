package items;

import passivebonuses.PassiveBonus;
import auras.Aura;
import enums.EnumColor;

public class ItemGem extends Item
{
	protected EnumColor gemColor;
	protected PassiveBonus[] PassiveBonuses;
	protected Aura[] auras;
	
	public ItemGem(int id)
	{
		super(id);
		this.PassiveBonuses = new PassiveBonus[0];
		this.auras = new Aura[0];
		this.gemColor = EnumColor.WHITE;
	}
	
	public ItemGem setAuras(Aura[] auras)
	{
		this.auras = auras;
		return this;
	}
	
	public ItemGem PassiveBonuses(PassiveBonus[] bonuses)
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
	
	public Aura[] getAuras()
	{
		return auras;
	}
	
	public PassiveBonus[] getBonuses()
	{
		return PassiveBonuses;
	}
}
