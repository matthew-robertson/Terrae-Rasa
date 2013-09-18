package utils;

import items.Item;
import items.ItemArmor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import affix.Affix;
import affix.AffixData;
import affix.AffixDestruction;
import affix.AffixFrenzied;
import affix.AffixSturdy;

public class RecipeResult 
{
	private double affixChance = 1.0;
	private ItemStack result;
	private List<Integer> possibleAffixIDs;
	
	public RecipeResult(ItemStack result)
	{
		possibleAffixIDs = new ArrayList<Integer>();
		this.result = result;
		addDefaultAffixes();
	}
	
	public RecipeResult(ItemStack result, boolean allowDefaultAffixes)
	{
		possibleAffixIDs = new ArrayList<Integer>();
		this.result = result;
		if(allowDefaultAffixes)
		{
			addDefaultAffixes();
		}
	}

	private void addDefaultAffixes()
	{
		int itemID = this.result.getItemID();
		if(itemID >= ActionbarItem.itemIndex && itemID < ActionbarItem.spellIndex)
		{
			Item item = Item.itemsList[itemID];
			if(item instanceof ItemArmor)
			{
				possibleAffixIDs.add(AffixSturdy.getAffixID());
				possibleAffixIDs.add(AffixDestruction.getAffixID());
				possibleAffixIDs.add(AffixFrenzied.getAffixID());
			}
		}		
	}
	
	public RecipeResult addAffix(int affixID)
	{
		possibleAffixIDs.add(affixID);
		return this;
	}
	
	public RecipeResult addAffix(Affix affix)
	{
		possibleAffixIDs.add(affix.getID());
		return this;
	}
	
	public RecipeResult addAffix(AffixData affix)
	{
		possibleAffixIDs.add(affix.getAffixID());
		return this;
	}
	
	public RecipeResult setAffixChance(double chance)
	{
		this.affixChance = chance;
		return this;
	}
	
	public ItemStack getResult()
	{
		ItemStack result = new ItemStack(this.result);
		rollAffixes(result);
		return result;
	}
	
	private void rollAffixes(ItemStack result)
	{
		Random random = new Random();
		double chanceRoll = random.nextDouble();
		if(chanceRoll <= affixChance && possibleAffixIDs.size() > 0)
		{
			int affixIndex = random.nextInt(possibleAffixIDs.size());
			result.rollAffixBonuses(possibleAffixIDs.get(affixIndex));
		}		
	}
}
