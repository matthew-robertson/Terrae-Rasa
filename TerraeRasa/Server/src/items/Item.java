package items;

import passivebonuses.PassiveBonus;
import passivebonuses.PassiveBonusAttackSpeed;
import passivebonuses.PassiveBonusCriticalStrike;
import passivebonuses.PassiveBonusDamageAll;
import passivebonuses.PassiveBonusDefense;
import passivebonuses.PassiveBonusDodge;
import passivebonuses.PassiveBonusFallHeight;
import passivebonuses.PassiveBonusIntellect;
import passivebonuses.PassiveBonusJumpHeight;
import passivebonuses.PassiveBonusSpeed;
import passivebonuses.PassiveBonusStamina;
import passivebonuses.PassiveBonusStrength;
import utils.ActionbarItem;
import auras.Aura;
import auras.AuraHeavensReprieve;
import auras.AuraPeriodicModifier;
import auras.AuraSmartHeal;
import entities.EntityProjectile;
import enums.EnumArmor;
import enums.EnumItemQuality;
import enums.EnumToolMaterial;

/**
 * <code>Item extends ActionbarItem</code> and defines the class for most non-<code>Block</code> 
 * things within the player's inventory. 
 * All <code>Items</code> are declared in this class, and all possible <code>Items</code> must extend 
 * <code>Item</code>. This ensures general consistency, and the ability to easily access all the possible
 * <code>Items</code> through the use of <code>Item.itemsList[]</code>. <code>Item.itemsList[]</code> holds
 * all Items, and all extension of <code>Item</code>; however casting must be performed to access 
 * methods from a subclass of <code>Item</code>.
 * <br><br>
 * Items are all initialized with default settings, including an <code>EnumToolMaterial</code> of "FIST" and 
 * a name of "Unnamed". Through the use of setters, many method calls may be attached to the declaration such
 * as, for example, the declaration of a copperAxe:
 * <br>
 * <code>public static Item copperAxe = new ItemToolAxe(1).setDamageDone(4).setName("copper Axe").setIconPosition(0, 7).setToolMaterial(EnumToolMaterial.COPPER);</code>
 * <br> 
 * Here, <code>copperAxe</code> is customized significantly through the many possible setters, each changing 
 * a variable to suit the need of the specific <code>Item</code> (or <code>Item</code> Extension). Specifically,
 * <code>copperAxe</code> is declared as an <code>ItemToolAxe</code> with:
 * <li>An itemID of 1
 * <li>4 damage done to enemies
 * <li>A name of "copper Axe"
 * <li>An <code>EnumToolMaterial</code> of <code>COPPER</code>
 * <li>An icon position on items.png of (0,7)
 * <br>
 * This allows <code>Item</code> to be an extremely versatile class with many possible combinations of
 * variables, and possible <code>Items</code> even without extension. <b>NOTE: the name must be
 * set to something besides "Unnamed" or the <code>Item</code> will not be kept track of in the crafting 
 * manager or inventory totals. </b>
 * <br><br>
 * <code>Item</code>'s constructor is protected, to prevent a random declaration of <code>Item</code>. This 
 * could easily corrupt the <code>itemsList[]</code>, permanently damaging the save file or crashing the game.
 * With a protected constructor, all <code>Items</code> must be declared here upon creation and can never be 
 * redeclared (provided someone doesn't do something ridiculously stupid).
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Item extends ActionbarItem
{
	/** Each tier of gear has a material - this is used to find a set bonus */
	protected EnumToolMaterial material;
	public EnumItemQuality itemQuality;	
	protected int damage;
	protected int durability;
	protected boolean isContainer;
	protected int toolType;
	
	/**
	 * Constructs a new <code>Item</code> that will be stored in the <code>itemsList[]</code> at the index 
	 * of the itemID, allowing for easy access of the <code>Item</code> later. <code>Item</code> has a 
	 * protected constructor, so that <code>Items</code> can't be declared
	 * outside of <code>Item.java</code>. This should prevent random Items from overwriting random parts of
	 * the <code>itemsList[]</code>, possibly corrupting the save or crashing the game.
	 * <br><br>
	 * Additional customization can be performed after creation of an <code>Item</code> through the use of 
	 * setters (see the class comment for an example).
	 * @param i the unique itemID of the <code>Item</code> being created
	 */
	protected Item(int i)
	{
		super(i);
		this.id = i + itemIndex;
		isContainer = false;
		durability = -1;
		damage = 0;
		material = EnumToolMaterial.FIST;
		itemQuality = EnumItemQuality.COMMON;
		
		if(itemsList[id] != null)
		{
			System.out.println(new StringBuilder().append("Conflict@ itemsList").append(id).toString());
			throw new RuntimeException(new StringBuilder().append("Conflict@ itemsList").append(id).toString());
		}
		itemsList[id] = this;		
	}

	/**
	 * Creates a shallow copy, copying any references and primitive types to the new Item. 
	 * @param item the item to create a shallow copy of
	 */
	public Item(Item item)
	{
		super(item);
		this.material = item.material;
		this.itemQuality = item.itemQuality;
		this.damage = item.damage;
		this.durability = item.durability;
		this.isContainer = item.isContainer;
		this.toolType = item.toolType;
	}
	
	protected Item setContainerItem(boolean flag)
	{
		isContainer = flag;
		return this;
	}
	/**
	 * Overrides ActionbarItem.setMaxStackSize(String) to make Item declarations cleaner
	 */
	protected Item setMaxStackSize(int i)
	{
		maxStackSize = i;
		return this;
	}
	
	protected Item setDurability(int i)
	{
		durability = i;
		return this;
	}
	
	protected Item setItemQuality(EnumItemQuality quality)
	{
		this.itemQuality = quality;
		return this;
	}
	
	/**
	 * Overrides ActionbarItem.setExtraTooltipInformation(String) to make Item declarations cleaner
	 */
	public Item setExtraTooltipInformation(String info)
	{
		this.extraTooltipInformation = info;
		return this;
	}
	
	protected Item setToolType(int i){
		toolType = i;
		return this;
	}
	
	protected Item setToolMaterial(EnumToolMaterial mat){
		material = mat;
		return this;
	}

	/**
	 * Overrides ActionbarItem.setName(String) to make Item declarations cleaner
	 */
	protected Item setName(String name)
	{
		this.name = name;
		return this;
	}
	
	/**
	 * Overrides ActionbarItem.setIconPosition(int, int) to make Item declarations cleaner
	 */
	protected Item setIconPosition(int x, int y)
	{
		iconX = x;
		iconY = y;
		return this;
	}	
	
	public boolean getIsContainer()
	{
		return isContainer;
	}
	
	public int getDurability()
	{
		return durability;
	}
	
	public EnumItemQuality getItemQuality()
	{
		return itemQuality;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public int getToolType()
	{
		return toolType;
	}
	
	public EnumToolMaterial getToolMaterial()
	{
		return material;
	}
		
	/**
	 * Gets the stats of the given item. Basic Items shouldn't have stats, but other Items such as extensions of 
	 * ItemTool or ItemArmor can override this method and return an actual array of values.
	 * @return an array of this Item's stats
	 */
	public String[] getStats()
	{
		if(damage > 0)
		{
			return new String[] { "Damage: " + damage };
		}
		else
		{
			return new String[] { };
		}
	}
	
	/**
	 * Sets the number of sockets for this actionbar item.
	 * @param number the number of sockets
	 * @return a reference to this object
	 */
	protected Item setTotalSockets(int number)
	{
		this.totalSockets = number;
		return this;
	}
			
	public static final Item[] itemsList = new Item[spellIndex];
	/** Item Declarations **/	
	/**
	 * 000-199 -> tools and weapons (ammo too)
	 * 200-499 -> armour
	 * 500-599 -> Potions
	 * 600-699 -> Materials
	 */
	
	public static Item copperAxe = new ItemToolAxe(0).setDamageDone(4).setName("Copper Axe").setIconPosition(16 ,8).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperPickaxe = new ItemToolPickaxe(1).setDamageDone(3).setName("Copper Pickaxe").setIconPosition(16, 7).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperSword = new ItemToolSword(2).setDamageDone(6).setName("Copper Sword").setIconPosition(16, 6).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item bronzeAxe = new ItemToolAxe(3).setDamageDone(5).setName("Bronze Axe").setIconPosition(17, 8).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzePickaxe = new ItemToolPickaxe(4).setDamageDone(4).setName("Bronze Pickaxe").setIconPosition(17, 7).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzeSword = new ItemToolSword(5).setDamageDone(7).setName("Bronze Sword").setIconPosition(17, 6).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item ironAxe = new ItemToolAxe(6).setDamageDone(7).setName("Iron Axe").setIconPosition(18, 8).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironPickaxe = new ItemToolPickaxe(7).setDamageDone(4).setName("Iron Pickaxe").setIconPosition(18, 7).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironSword = new ItemToolSword(8).setDamageDone(9).setName("Iron Sword").setIconPosition(18, 6).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item silverAxe = new ItemToolAxe(9).setDamageDone(8).setName("Silver Axe").setIconPosition(19, 8).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverPickaxe = new ItemToolPickaxe(10).setDamageDone(5).setName("Silver Pickaxe").setIconPosition(19, 7).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverSword = new ItemToolSword(11).setDamageDone(11).setName("Silver Sword").setIconPosition(19, 6).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item goldAxe = new ItemToolAxe(12).setDamageDone(9).setName("Gold Axe").setIconPosition(20, 8).setToolMaterial(EnumToolMaterial.GOLD);	
	public static Item goldPickaxe = new ItemToolPickaxe(13).setDamageDone(7).setName("Gold Pickaxe").setIconPosition(20, 7).setToolMaterial(EnumToolMaterial.GOLD).setItemQuality(EnumItemQuality.LEGENDARY);	
	public static Item goldSword = new ItemToolSword(14).setDamageDone(14).setName("Gold Sword").setIconPosition(20, 6).setToolMaterial(EnumToolMaterial.GOLD);	
	public static Item woodenBow = new ItemRanged(15, 8).setCooldownTicks(10).setName("Wooden Bow").setIconPosition(16, 9);
	public static Item woodenArrow = new ItemAmmo(16).setProjectile(new EntityProjectile(7, 1, 1, 8f).setSpriteIndex(0, 1).setIsFriendly(true)).setIconPosition(4, 9).setName("Wooden Arrow");
	public static Item bronzeArrow = new ItemAmmo(17).setProjectile(new EntityProjectile(10, 1, 1, 8f).setSpriteIndex(0, 2).setIsFriendly(true)).setIconPosition(4, 10).setName("Bronze Arrow");
	public static Item ironArrow = new ItemAmmo(18).setProjectile(new EntityProjectile(13, 1, 1, 8f).setSpriteIndex(0, 3).setIsFriendly(true)).setIconPosition(4, 11).setName("Iron Arrow");
	public static Item silverArrow = new ItemAmmo(19).setProjectile(new EntityProjectile(16, 1, 1, 8f).setSpriteIndex(0, 4).setIsFriendly(true)).setIconPosition(4, 12).setName("Silver Arrow");
	public static Item snowball = new ItemThrown(20, 5).setProjectile(new EntityProjectile(5, 1, 1, 11f).setSpriteIndex(0, 0).setIsFriendly(true)).setCooldownTicks(10).setName("Snow Ball").setIconPosition(14, 0);
	
	public static Item godminiumPickaxe = new ItemToolPickaxe(156).setDamageDone(7).setName("God-Minium Pickaxe").setIconPosition(20, 7).setToolMaterial(EnumToolMaterial.GOD).setItemQuality(EnumItemQuality.LEGENDARY);	
	
	public static Item copperHelmet = new ItemArmorHelmet(200).setArmorType(EnumArmor.COPPER).setIconPosition(16, 0).setName("Copper Helmet");
	public static Item copperBody = new ItemArmorBody(201).setArmorType(EnumArmor.COPPER).setIconPosition(16, 1).setName("Copper Body");
	public static Item copperPants = new ItemArmorPants(202).setArmorType(EnumArmor.COPPER).setIconPosition(0, 0).setName("Copper Legguards");
	public static Item copperBoots = new ItemArmorBoots(203).setArmorType(EnumArmor.COPPER).setIconPosition(16, 3).setName("Copper Boots");
	public static Item copperGloves = new ItemArmorGloves(204).setArmorType(EnumArmor.COPPER).setIconPosition(16, 4).setName("Copper Gloves");
	public static Item copperBelt = new ItemArmorBelt(205).setArmorType(EnumArmor.COPPER).setIconPosition(16, 5).setName("Copper Belt");
	public static Item bronzeHelmet = new ItemArmorHelmet(206).setArmorType(EnumArmor.BRONZE).setIconPosition(17, 0).setName("Bronze Helmet");
	public static Item bronzeBody = new ItemArmorBody(207).setArmorType(EnumArmor.BRONZE).setIconPosition(17, 1).setName("Bronze Body");
	public static Item bronzePants = new ItemArmorPants(208).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 0).setName("Bronze Legguards");
	public static Item bronzeBoots = new ItemArmorBoots(209).setArmorType(EnumArmor.BRONZE).setIconPosition(17, 3).setName("Bronze Boots");
	public static Item bronzeGloves = new ItemArmorGloves(210).setArmorType(EnumArmor.BRONZE).setIconPosition(17, 4).setName("Bronze Gloves");
	public static Item bronzeBelt = new ItemArmorBelt(211).setArmorType(EnumArmor.BRONZE).setIconPosition(17, 5).setName("Bronze Belt");
	public static Item ironHelmet = new ItemArmorHelmet(212).setArmorType(EnumArmor.IRON).setIconPosition(18, 0).setName("Iron Helmet");
	public static Item ironBody = new ItemArmorBody(213).setArmorType(EnumArmor.IRON).setIconPosition(18, 1).setName("Iron Body");
	public static Item ironPants = new ItemArmorPants(214).setArmorType(EnumArmor.IRON).setIconPosition(0, 0).setName("Iron Legguards");
	public static Item ironBoots = new ItemArmorBoots(215).setArmorType(EnumArmor.IRON).setIconPosition(18, 3).setName("Iron Boots");
	public static Item ironGloves = new ItemArmorGloves(216).setArmorType(EnumArmor.IRON).setIconPosition(18, 4).setName("Iron Gloves");
	public static Item ironBelt = new ItemArmorBelt(217).setArmorType(EnumArmor.IRON).setIconPosition(18, 5).setName("Iron Belt");
	public static Item silverHelmet = new ItemArmorHelmet(218).setArmorType(EnumArmor.SILVER).setIconPosition(19, 0).setName("Silver Helmet");
	public static Item silverBody = new ItemArmorBody(219).setArmorType(EnumArmor.SILVER).setIconPosition(19, 1).setName("Silver Body");
	public static Item silverPants = new ItemArmorPants(220).setArmorType(EnumArmor.SILVER).setIconPosition(0, 0).setName("Silver Legguards");	
	public static Item silverBoots = new ItemArmorBoots(221).setArmorType(EnumArmor.SILVER).setIconPosition(19, 3).setName("Silver Boots");
	public static Item silverGloves = new ItemArmorGloves(222).setArmorType(EnumArmor.SILVER).setIconPosition(19, 4).setName("Silver Gloves");
	public static Item silverBelt = new ItemArmorBelt(223).setArmorType(EnumArmor.SILVER).setIconPosition(19, 5).setName("Silver Belt");
	public static Item goldHelmet = new ItemArmorHelmet(224).setArmorType(EnumArmor.GOLD).setIconPosition(20, 0).setName("Gold Helmet").setItemQuality(EnumItemQuality.RARE).setTotalSockets(1);
	public static Item goldBody = new ItemArmorBody(225).setArmorType(EnumArmor.GOLD).setIconPosition(20, 1).setName("Gold Body").setItemQuality(EnumItemQuality.RARE).setTotalSockets(1);
	public static Item goldPants = new ItemArmorPants(226).setArmorType(EnumArmor.GOLD).setIconPosition(0, 0).setName("Gold Legguards").setItemQuality(EnumItemQuality.RARE).setTotalSockets(1);	
	public static Item goldBoots = new ItemArmorBoots(227).setArmorType(EnumArmor.GOLD).setIconPosition(20, 3).setName("Gold Boots").setItemQuality(EnumItemQuality.RARE);
	public static Item goldGloves = new ItemArmorGloves(228).setArmorType(EnumArmor.GOLD).setIconPosition(20, 4).setName("Gold Gloves").setItemQuality(EnumItemQuality.RARE);
	public static Item goldBelt = new ItemArmorBelt(229).setArmorType(EnumArmor.GOLD).setIconPosition(20, 5).setName("Gold Belt").setItemQuality(EnumItemQuality.RARE);
	public static Item lunariumHelmet = new ItemArmorHelmet(230).setArmorType(EnumArmor.LUNARIUM).setIntellect(5).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.03)
	}).setIconPosition(22, 0).setName("Lunarium Crown").setTotalSockets(2);
	public static Item lunariumBody = new ItemArmorBody(231).setArmorType(EnumArmor.LUNARIUM).setIntellect(3).setIconPosition(22, 1).setName("Lunarum Robe").setTotalSockets(1);
	public static Item lunariumPants = new ItemArmorPants(232).setArmorType(EnumArmor.LUNARIUM).setIntellect(3).setIconPosition(22, 2).setName("Lunarium Pants").setTotalSockets(1);	
	public static Item lunariumBoots = new ItemArmorBoots(233).setArmorType(EnumArmor.LUNARIUM).setIntellect(3).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusSpeed(0.03)
	}).setIconPosition(22, 3).setName("Lunarium Boots").setTotalSockets(1);
	public static Item lunariumGloves = new ItemArmorGloves(234).setArmorType(EnumArmor.LUNARIUM).setIntellect(3).setIconPosition(22, 4).setName("Lunarium Gloves").setTotalSockets(1);
	public static Item lunariumBelt = new ItemArmorBelt(235).setArmorType(EnumArmor.LUNARIUM).setIntellect(3).setIconPosition(22, 5).setName("Lunarium Belt").setTotalSockets(1);
	
	public static Item rocketBoots = new ItemArmorAccessory(400).setIconPosition(0, 0).setName("Rocket Boots");	
	public static Item ringOfVigor = new ItemArmorAccessory(401).passiveBonuses(new PassiveBonus[]{ 
			new PassiveBonusDamageAll(0.1F)
	}).setIconPosition(15, 2).setName("Ring of Vigor").setExtraTooltipInformation("This ring instills a sense of courage in its wearer.");	
	public static Item talismanOfWinds = new ItemArmorAccessory(402).passiveBonuses(new PassiveBonus[]{ 
			new PassiveBonusSpeed(0.1F)
	}).setIconPosition(15, 3).setName("Talisman of Winds").setExtraTooltipInformation("The winds of fortune are at your back.");	
	public static Item ankh = new ItemArmorAccessory(403).setAuras(new Aura[]{ 
			new AuraHeavensReprieve()
	}).setIconPosition(14, 1).setName("Ankh").setExtraTooltipInformation("This relic will destroy itself to save the wearer.");	
	public static Item goddessesTear = new ItemArmorAccessory(404).passiveBonuses(new PassiveBonus[]{ 
			new PassiveBonusIntellect(8)
	}).setIconPosition(15, 4).setName("Goddess' Tear").setExtraTooltipInformation("This tear contains immense magical power.");	
	public static Item magicalCloud = new ItemArmorAccessory(405).passiveBonuses(new PassiveBonus[]{ 
			new PassiveBonusFallHeight(8)
	}).setIconPosition(15, 5).setName("Magical Cloud").setExtraTooltipInformation("As light as air itself.");	
	public static Item berserkersEssence = new ItemArmorAccessory(406).passiveBonuses(new PassiveBonus[]{ 
			new PassiveBonusStrength(3), new PassiveBonusAttackSpeed(0.1F)
	}).setIconPosition(15, 0).setName("Berserker's Essence").setExtraTooltipInformation("You sense deep inner rage within the essence.");	
	public static Item guardianAmulet = new ItemArmorAccessory(407).setDefense(6).setIconPosition(15, 6).setName("Amulet of the Guardian").setExtraTooltipInformation("This amulet contains great defensive power.");	
	public static Item sigilOfAsclepius = new ItemArmorAccessory(408).setAuras(new Aura[]{ 
			new AuraSmartHeal(25, 50, 0.5, false)
	}).setIconPosition(15, 7).setName("Sigil of Asclepius").setExtraTooltipInformation("This sigil radiates holy energy.");	
	public static Item heavyMedal = new ItemArmorAccessory(409).setAuras(new Aura[]{ 
			new AuraPeriodicModifier(0.75)
	}).setIconPosition(15, 1).setName("Heavy Medal").setExtraTooltipInformation("The power of this medal protects its wearer.");	
	public static Item eaglesFeather = new ItemArmorAccessory(410).passiveBonuses(new PassiveBonus[]{
		new PassiveBonusJumpHeight(4)	
	}).setIconPosition(14, 2).setName("Eagle's Feather").setExtraTooltipInformation("The eagle's feather almost lets take to the sky.");
	public static Item nimblefootCloak = new ItemArmorAccessory(411).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusDodge(0.05)
	}).setIconPosition(15, 9).setName("Nimblefoot Cloak").setExtraTooltipInformation("Grants the wearer great agility.");
	public static Item mastersBracer = new ItemArmorAccessory(412).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.04)
	}).setIconPosition(15, 10).setName("Master's Bracer").setExtraTooltipInformation("These bracers contain the spirit of a combat master.");
	public static Item heartyPendant = new ItemArmorAccessory(413).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusStamina(5)
	}).setIconPosition(14, 3).setName("Hearty Pendant").setExtraTooltipInformation("This pendant radiates life energy.");
	public static Item steadfastShield = new ItemArmorAccessory(414).setDefense(5).setIconPosition(15, 8).setName("Steadfast Shield").setExtraTooltipInformation("A symbol of unwaivering resolve.");
	public static Item sapphireRing = new ItemArmorAccessory(415).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.01)
	}).setIconPosition(14, 4).setName("Sapphire Ring").setExtraTooltipInformation("A basic sapphire ring.");
	public static Item emeraldRing = new ItemArmorAccessory(416).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusAttackSpeed(0.01)
	}).setIconPosition(14, 5).setName("Emerald Ring").setExtraTooltipInformation("A basic emerald ring.");
	public static Item rubyRing = new ItemArmorAccessory(417).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusStrength(1)
	}).setIconPosition(14, 6).setName("Ruby Ring").setExtraTooltipInformation("A basic ruby ring.");
	public static Item diamondRing = new ItemArmorAccessory(418).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusDodge(0.01)
	}).setIconPosition(14, 7).setName("Diamond Ring").setExtraTooltipInformation("A basic diamond ring.");
	public static Item opalRing = new ItemArmorAccessory(419).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusDefense(1)
	}).setIconPosition(14, 8).setName("Opal Ring").setExtraTooltipInformation("A basic opal ring.");
	public static Item jasperRing = new ItemArmorAccessory(420).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusStamina(1)
	}).setIconPosition(14, 9).setName("Jasper Ring").setExtraTooltipInformation("A basic jasper ring.");
	public static Item goldRing = new ItemArmorAccessory(421).setIconPosition(14, 10).setName("Gold Ring");
	
	public static Item opHelmet = new ItemArmorHelmet(454).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(1).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(21, 0).setName("TIER6 Helmet").setItemQuality(EnumItemQuality.RARE);
	public static Item opBody = new ItemArmorBody(455).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(2).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(21, 1).setName("TIER6 Body").setItemQuality(EnumItemQuality.RARE);
	public static Item opPants = new ItemArmorPants(456).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(3).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(0, 0).setName("TIER6 Legguards").setItemQuality(EnumItemQuality.RARE);	
	public static Item opBoots = new ItemArmorBoots(457).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(4).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(21, 3).setName("TIER6 Boots");
	public static Item opGloves = new ItemArmorGloves(458).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(4).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(21, 4).setName("TIER6 Gloves");
	public static Item opBelt = new ItemArmorBelt(459).setArmorType(EnumArmor.OP_TEST_SET).passiveBonuses(new PassiveBonus[]{
			new PassiveBonusCriticalStrike(0.1), new PassiveBonusAttackSpeed(0.1)
	}).setAuras(new Aura[]{ 
			new AuraSmartHeal(10, 600, 0.9, false)
	}).setTotalSockets(4).setIntellect(5).setStamina(10).setStrength(5).setDexterity(5).setIconPosition(21, 5).setName("TIER6 Belt");
	
	public static Item regenerationPotion1 = new ItemPotionRegeneration(500, 90, 1, 4, 40).setIconPosition(9, 1).setName("Minor Regen Potion");
	public static Item regenerationPotion2 = new ItemPotionRegeneration(501, 90, 2, 8, 40).setIconPosition(9, 2).setName("Regen Potion");
	public static Item regenerationPotion3 = new ItemPotionRegeneration(502, 90, 3, 12, 40).setIconPosition(0, 0).setName("regen potion 3");
	public static Item regenerationPotion4 = new ItemPotionRegeneration(503, 90, 4, 16, 40).setIconPosition(0, 0).setName("regen potion 4");
	public static Item attackSpeedPotion1 = new ItemPotionAttackSpeed(504, 180, 1, 0.1F, 1).setIconPosition(9, 3).setName("Minor Attack Speed Potion");
	public static Item attackSpeedPotion2 = new ItemPotionAttackSpeed(505, 180, 2, 0.2F, 1).setIconPosition(9, 4).setName("Attack Speed Potion");
	public static Item attackSpeedPotion3 = new ItemPotionAttackSpeed(506, 180, 3, 0.3F, 1).setIconPosition(0, 0).setName("attack speed potion 3");
	public static Item attackSpeedPotion4 = new ItemPotionAttackSpeed(507, 180, 4, 0.4F, 1).setIconPosition(0, 0).setName("attack speed potion 4");
	public static Item criticalChancePotion1 = new ItemPotionCriticalBuff(508, 180, 1, 0.1F, 1).setIconPosition(10, 1).setName("Minor Critical Potion");
	public static Item criticalChancePotion2 = new ItemPotionCriticalBuff(509, 180, 2, 0.2F, 1).setIconPosition(10, 2).setName("Critical Potion");
	public static Item criticalChancePotion3 = new ItemPotionCriticalBuff(510, 180, 3, 0.3F, 1).setIconPosition(0, 0).setName("critical chance potion 3");
	public static Item criticalChancePotion4 = new ItemPotionCriticalBuff(511, 180, 4, 0.4F, 1).setIconPosition(0, 0).setName("critical chance potion 4");
	public static Item damageBuffPotion1 = new ItemPotionDamageBuff(512, 75, 1, 0.1F, 1).setIconPosition(12, 1).setName("Minor Damage Potion");
	public static Item damageBuffPotion2 = new ItemPotionDamageBuff(513, 75, 2, 0.2F, 1).setIconPosition(12, 2).setName("Damage Potion");
	public static Item damageBuffPotion3 = new ItemPotionDamageBuff(514, 75, 3, 0.3F, 1).setIconPosition(0, 0).setName("damage buff potion 3");
	public static Item damageBuffPotion4 = new ItemPotionDamageBuff(515, 75, 4, 0.4F, 1).setIconPosition(0, 0).setName("damage buff potion 4");
	public static Item dodgePotion1 = new ItemPotionDodge(516, 120, 1, 0.12F, 1).setIconPosition(11, 1).setName("Minor Dodge Potion");
	public static Item dodgePotion2 = new ItemPotionDodge(517, 120, 2, 0.2F, 1).setIconPosition(11, 2).setName("Dodge Potion");
	public static Item dodgePotion3 = new ItemPotionDodge(518, 120, 3, 0.3F, 1).setIconPosition(0, 0).setName("dodge potion 3");
	public static Item dodgePotion4 = new ItemPotionDodge(519, 120, 4, 0.4F, 1).setIconPosition(0, 0).setName("dodge potion 4");
	public static Item manaRegenerationPotion1 = new ItemPotionManaRegeneration(520, 150, 1, 6, 40).setIconPosition(6, 3).setName("Minor Mana Regen Potion");
	public static Item manaRegenerationPotion2 = new ItemPotionManaRegeneration(521, 150, 2, 10, 40).setIconPosition(6, 4).setName("Mana Regen Potion");
	public static Item manaRegenerationPotion3 = new ItemPotionManaRegeneration(522, 150, 3, 15, 40).setIconPosition(0, 0).setName("mana regen potion 3");
	public static Item manaRegenerationPotion4 = new ItemPotionManaRegeneration(523, 150, 4, 21, 40).setIconPosition(0, 0).setName("mana regen potion 4");
	public static Item steelSkinPotion1 = new ItemPotionSteelSkin(524, 150, 1, 8, 1).setIconPosition(7, 3).setName("Minor Steel Skin Potion");
	public static Item steelSkinPotion2 = new ItemPotionSteelSkin(525, 150, 2, 16, 1).setIconPosition(7, 4).setName("Steel Skin Potion");
	public static Item steelSkinPotion3 = new ItemPotionSteelSkin(526, 150, 3, 24, 1).setIconPosition(0, 0).setName("steel skin potion 3");
	public static Item steelSkinPotion4 = new ItemPotionSteelSkin(527, 150, 4, 32, 1).setIconPosition(0, 0).setName("steel skin potion 4");
	public static Item swiftnessPotion1 = new ItemPotionSwiftness(528, 360, 1, 0.15F, 1).setIconPosition(13, 1).setName("Minor Swiftness Potion");
	public static Item swiftnessPotion2 = new ItemPotionSwiftness(529, 360, 2, 0.3F, 1).setIconPosition(13, 2).setName("Swiftness Potion");
	public static Item swiftnessPotion3 = new ItemPotionSwiftness(530, 360, 3, 0.45F, 1).setIconPosition(0, 0).setName("swiftness potion 3");
	public static Item swiftnessPotion4 = new ItemPotionSwiftness(531, 360, 4, 0.6F, 1).setIconPosition(0, 0).setName("swiftness potion 4");
	public static Item absorbPotion1 = new ItemPotionAbsorb(532, 30, 1, 50, 1).setIconPosition(9, 1).setName("Minor Absorb Potion");
	public static Item absorbPotion2 = new ItemPotionAbsorb(533, 30, 1, 100, 1).setIconPosition(9, 1).setName("Absorb Potion");
	public static Item manaPotion1 = new ItemPotionMana(534, 75).setIconPosition(7, 1).setName("Minor Mana Potion");
	public static Item manaPotion2 = new ItemPotionMana(535, 150).setIconPosition(7, 1).setName("Mana Potion");
	public static Item healthPotion1 = new ItemPotionHealth(536, 75).setIconPosition(6, 1).setName("Minor Healing Potion");
	public static Item healthPotion2 = new ItemPotionHealth(537, 150).setIconPosition(6, 1).setName("Healing Potion");
	
	public static Item copperIngot = new Item(600).setIconPosition(3, 9).setName("Copper Ingot");
	public static Item tinIngot = new Item(601).setIconPosition(3, 10).setName("Tin Ingot");
	public static Item bronzeIngot = new Item(602).setIconPosition(3, 11).setName("Bronze Ingot");
	public static Item ironIngot = new Item(603).setIconPosition(3, 12).setName("Iron Ingot");
	public static Item silverIngot = new Item(604).setIconPosition(3, 13).setName("Silver Ingot");
	public static Item goldIngot = new Item(605).setIconPosition(3, 14).setName("Gold Ingot");
	public static Item copperOre = new Item(606).setIconPosition(0, 9).setName("Copper Ore");
	public static Item tinOre = new Item(607).setIconPosition(0, 10).setName("Tin Ore");
	public static Item bronzeOre = new Item(608).setIconPosition(0, 0).setName("Bronze Ore");
	public static Item ironOre = new Item(609).setIconPosition(0, 11).setName("Iron Ore");
	public static Item silverOre = new Item(610).setIconPosition(0, 12).setName("Silver Ore");
	public static Item goldOre = new Item(611).setIconPosition(0, 13).setName("Gold Ore");
	public static Item coal = new Item(612).setIconPosition(1, 9).setName("Coal");
	public static Item diamond = new ItemGem(613).setIconPosition(2, 12).setName("Diamond");
	public static Item ruby = new ItemGem(614).setIconPosition(2, 11).setName("Ruby");
	public static Item emerald = new ItemGem(615).setIconPosition(2, 10).setName("Emerald");
	public static Item sapphire = new ItemGem(616).setIconPosition(2, 9).setName("Sapphire");
	public static Item opal = new ItemGem(617).setIconPosition(2, 13).setName("Opal");
	public static Item jasper = new ItemGem(618).setIconPosition(2, 14).setName("Jasper");
	public static Item copperCoin = new ItemCoin(619).setIconPosition(0, 1).setName("Copper Coin");
	public static Item silverCoin = new ItemCoin(620).setIconPosition(0, 2).setName("Silver Coin");
	public static Item goldCoin = new ItemCoin(621).setIconPosition(0, 3).setName("Gold Coin");
	public static Item platinumCoin = new ItemCoin(622).setIconPosition(0, 4).setName("Platinum Coin");
	public static Item heartCrystal = new ItemHeartCrystal(623).setIconPosition(7, 0).setName("Heart Crystal");
	public static Item manaCrystal = new ItemManaCrystal(624).setIconPosition(6, 0).setName("Mana Crystal");
	public static Item healingHerb1 = new Item(625).setIconPosition(0, 0).setName("Minor Healing Herb");
	public static Item healingHerb2 = new Item(626).setIconPosition(0, 0).setName("Healing Herb");
	public static Item magicHerb1 = new Item(627).setIconPosition(0, 0).setName("Minor Magic Herb");
	public static Item magicHerb2 = new Item(628).setIconPosition(0, 0).setName("Magic Herb");
	public static Item vialEmpty = new Item(629).setIconPosition(8, 1).setName("Empty Vial");
	public static Item vialOfWater = new Item(630).setIconPosition(8, 2).setName("Vial of Water");
}
