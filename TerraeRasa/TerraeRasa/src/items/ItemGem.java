package items;

import setbonus.SetBonus;
import auras.Aura;
import enums.EnumColor;

public class ItemGem extends Item
{
	protected EnumColor gemColor;
	protected SetBonus[] setBonuses;
	protected Aura[] auras;
	
	public ItemGem(int id)
	{
		super(id);
		this.setBonuses = new SetBonus[0];
		this.auras = new Aura[0];
		this.gemColor = EnumColor.WHITE;
	}
	
	public ItemGem setAuras(Aura[] auras)
	{
		this.auras = auras;
		return this;
	}
	
	public ItemGem setBonuses(SetBonus[] bonuses)
	{
		this.setBonuses = bonuses;
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
	
	public SetBonus[] getBonuses()
	{
		return setBonuses;
	}
}
