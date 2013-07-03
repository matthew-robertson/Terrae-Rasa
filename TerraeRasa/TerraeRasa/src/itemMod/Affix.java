package itemMod;

import setbonus.SetBonus;
import setbonus.SetBonusAttackSpeed;
import setbonus.SetBonusDefense;
import setbonus.SetBonusStrength;
import auras.Aura;

public class Affix{
	
	private String name;
	private SetBonus[] passives;
	private Aura[] auras;
	
	public Affix(String name, SetBonus[] passives, Aura[] auras){
		this.name = name;
		this.passives = passives;
		this.auras = auras;
	}
	
	public Affix(String name, Aura[] auras){
		this.name = name;
		this.auras = auras;
	}
	
	public Affix(String name, SetBonus[] passives){
		this.name = name;
		this.passives = passives;
	}
	
	public Affix setName(String name){
		this.name = name;
		return this;
	}
	
	public Affix setPassives(SetBonus[] passives){
		this.passives = passives;
		return this;
	}
	
	public Affix setAuras(Aura[] auras){
		this.auras = auras;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public SetBonus[] getPassives(){
		return passives;
	}
	
	public Aura[] getAuras(){
		return auras;
	}
	
	public static Affix sturdy = new Affix("Sturdy"
			, new SetBonus[]{new SetBonusDefense(10)});
	
	public static Affix frenzied = new Affix("Frenzied"
			, new SetBonus[]{new SetBonusAttackSpeed(.5),
					new SetBonusStrength(10)});
}
