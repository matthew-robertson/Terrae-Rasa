package affix;

import java.util.Random;
import java.util.Vector;

import passiveBonus.PassiveBonus;
import passiveBonus.PassiveBonusAttackSpeed;
import passiveBonus.PassiveBonusStrength;
import auras.Aura;

public class Affix{
	
	protected Random rng;
	private String name;
	private Vector<PassiveBonus> passives;
	private Vector<Aura> auras;
	private boolean prefix;
	
	public Affix(String name){
		this.name = name;
		rng = new Random();
		passives = new Vector<PassiveBonus>();
		auras = new Vector<Aura>();
		prefix = true;
	}
	
	public Affix addPassive(PassiveBonus bonus){
		passives.add(bonus);
		return this;
	}
	
	public Affix addAura(Aura aura){
		auras.add(aura);
		return this;
	}
	
	public Affix setName(String name){
		this.name = name;
		return this;
	}
	
	public Affix setPassives(Vector<PassiveBonus> passives){
		this.passives = passives;
		return this;
	}
	
	public Affix setAuras(Vector<Aura> auras){
		this.auras = auras;
		return this;
	}
	
	public Affix setPrefix(boolean flag){
		prefix = flag;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public PassiveBonus[] getPassives(){
		PassiveBonus[] bonus = new PassiveBonus[passives.size()];
		passives.copyInto(bonus);
		return bonus;
	}
	
	public Aura[] getAuras(){
		Aura[] aura = new Aura[auras.size()];
		auras.copyInto(aura);
		return aura;
	}
	
	public boolean getPrefix(){
		return prefix;
	}
}
