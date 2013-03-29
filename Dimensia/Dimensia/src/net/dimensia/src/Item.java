package net.dimensia.src;

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
	private static final long serialVersionUID = 1L;
	protected EnumToolMaterial material;
	protected EnumItemQuality itemQuality;	
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
	 * Creates a shallow copy, copying any references to non-primative objects to the new Item. 
	 * @param item the item to create a shallow copy of
	 */
	public Item(Item item)
	{
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
	protected Item setExtraTooltipInformation(String info)
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
		return new String[] { };
	}
		
	public static final Item[] itemsList = new Item[spellIndex];
	/** Item Declarations **/	

	public static Item copperAxe = new ItemToolAxe(1).setDamageDone(4).setName("Copper Axe").setIconPosition(0, 7).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperPickaxe = new ItemToolPickaxe(2).setDamageDone(3).setName("Copper Pickaxe").setIconPosition(0, 6).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperHammer = new ItemToolHammer(3).setDamageDone(3).setName("Copper Hammer").setIconPosition(0, 5).setToolMaterial(EnumToolMaterial.COPPER);
	public static Item copperSword = new ItemToolSword(4).setDamageDone(6).setName("Copper Sword").setIconPosition(0, 4).setToolMaterial(EnumToolMaterial.COPPER);	
	
	public static Item bronzeAxe = new ItemToolAxe(5).setDamageDone(5).setName("Bronze Axe").setIconPosition(0, 7).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzePickaxe = new ItemToolPickaxe(6).setDamageDone(4).setName("Bronze Pickaxe").setIconPosition(0, 6).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzeHammer = new ItemToolHammer(7).setDamageDone(4).setName("Bronze Hammer").setIconPosition(0, 5).setToolMaterial(EnumToolMaterial.BRONZE);
	public static Item bronzeSword = new ItemToolSword(8).setDamageDone(7).setName("Bronze Sword").setIconPosition(0, 4).setToolMaterial(EnumToolMaterial.BRONZE);	
	
	public static Item ironAxe = new ItemToolAxe(9).setDamageDone(7).setName("Iron Axe").setIconPosition(1, 7).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironPickaxe = new ItemToolPickaxe(10).setDamageDone(4).setName("Iron Pickaxe").setIconPosition(1, 6).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironHammer = new ItemToolHammer(11).setDamageDone(5).setName("Iron Hammer").setIconPosition(1, 5).setToolMaterial(EnumToolMaterial.IRON);
	public static Item ironSword = new ItemToolSword(12).setDamageDone(9).setName("Iron Sword").setIconPosition(1, 4).setToolMaterial(EnumToolMaterial.IRON);	
	
	public static Item silverAxe = new ItemToolAxe(13).setDamageDone(8).setName("Silver Axe").setIconPosition(2, 7).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverPickaxe = new ItemToolPickaxe(14).setDamageDone(5).setName("Silver Pickaxe").setIconPosition(2, 6).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverHammer = new ItemToolHammer(15).setDamageDone(6).setName("Silver Hammer").setIconPosition(2, 5).setToolMaterial(EnumToolMaterial.SILVER);
	public static Item silverSword = new ItemToolSword(16).setDamageDone(11).setName("Silver Sword").setIconPosition(2, 4).setToolMaterial(EnumToolMaterial.SILVER);	
	
	public static Item goldAxe = new ItemToolAxe(17).setDamageDone(9).setName("Gold Axe").setIconPosition(4, 7).setToolMaterial(EnumToolMaterial.GOLD);	
	public static Item goldPickaxe = new ItemToolPickaxe(18).setDamageDone(7).setName("Gold Pickaxe").setIconPosition(4, 6).setToolMaterial(EnumToolMaterial.GOD).setItemQuality(EnumItemQuality.LEGENDARY);	
	public static Item goldHammer = new ItemToolHammer(19).setDamageDone(8).setName("Gold Hammer").setIconPosition(4, 5).setToolMaterial(EnumToolMaterial.GOLD);
	public static Item goldSword = new ItemToolSword(20).setDamageDone(14).setName("Gold Sword").setIconPosition(4, 4).setToolMaterial(EnumToolMaterial.GOLD);	
	
	public static Item copperIngot = new Item(51).setIconPosition(3, 9).setName("Copper Ingot");
	public static Item tinIngot = new Item(52).setIconPosition(3, 10).setName("Tin Ingot");
	public static Item bronzeIngot = new Item(53).setIconPosition(3, 11).setName("Bronze Ingot");
	public static Item ironIngot = new Item(54).setIconPosition(3, 12).setName("Iron Ingot");
	public static Item silverIngot = new Item(55).setIconPosition(3, 13).setName("Silver Ingot");
	public static Item goldIngot = new Item(56).setIconPosition(3, 14).setName("Gold Ingot");
	
	public static Item copperOre = new Item(57).setIconPosition(0, 9).setName("Copper Ore");
	public static Item tinOre = new Item(58).setIconPosition(0, 10).setName("Tin Ore");
	public static Item bronzeOre = new Item(59).setIconPosition(0, 0).setName("Bronze Ore");
	public static Item ironOre = new Item(60).setIconPosition(0, 11).setName("Iron Ore");
	public static Item silverOre = new Item(61).setIconPosition(0, 12).setName("Silver Ore");
	public static Item goldOre = new Item(62).setIconPosition(0, 13).setName("Gold Ore");
	public static Item coal = new Item(63).setIconPosition(1, 9).setName("Coal");
	public static Item diamond = new Item(64).setIconPosition(0, 0).setName("Diamond");
	public static Item ruby = new Item(65).setIconPosition(0, 0).setName("Ruby");
	public static Item emerald = new Item(66).setIconPosition(0, 0).setName("Emerald");
	public static Item sapphire = new Item(67).setIconPosition(0, 0).setName("Sapphire");
	
	public static Item copperHelmet = new ItemArmorHelmet(100).setArmorType(EnumArmor.COPPER).setIconPosition(0, 0).setName("Copper Helmet");
	public static Item copperBody = new ItemArmorBody(101).setArmorType(EnumArmor.COPPER).setIconPosition(0, 1).setName("Copper Body");
	public static Item copperPants = new ItemArmorPants(102).setArmorType(EnumArmor.COPPER).setIconPosition(0, 2).setName("Copper Legguards");
	public static Item copperBoots = new ItemArmorBoots(103).setArmorType(EnumArmor.COPPER).setIconPosition(0, 2).setName("Copper Boots");
	public static Item copperGloves = new ItemArmorGloves(104).setArmorType(EnumArmor.COPPER).setIconPosition(0, 2).setName("Copper Gloves");
	public static Item copperBelt = new ItemArmorBelt(105).setArmorType(EnumArmor.COPPER).setIconPosition(0, 2).setName("Copper Belt");
	
	public static Item bronzeHelmet = new ItemArmorHelmet(106).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 0).setName("Bronze Helmet");
	public static Item bronzeBody = new ItemArmorBody(107).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 1).setName("Bronze Body");
	public static Item bronzePants = new ItemArmorPants(108).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 2).setName("Bronze Legguards");
	public static Item bronzeBoots = new ItemArmorBoots(109).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 2).setName("Bronze Boots");
	public static Item bronzeGloves = new ItemArmorGloves(110).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 2).setName("Bronze Gloves");
	public static Item bronzeBelt = new ItemArmorBelt(111).setArmorType(EnumArmor.BRONZE).setIconPosition(0, 2).setName("Bronze Belt");
	
	public static Item ironHelmet = new ItemArmorHelmet(112).setArmorType(EnumArmor.IRON).setIconPosition(1, 0).setName("Iron Helmet");
	public static Item ironBody = new ItemArmorBody(113).setArmorType(EnumArmor.IRON).setIconPosition(1, 1).setName("Iron Body");
	public static Item ironPants = new ItemArmorPants(114).setArmorType(EnumArmor.IRON).setIconPosition(1, 2).setName("Iron Legguards");
	public static Item ironBoots = new ItemArmorBoots(115).setArmorType(EnumArmor.IRON).setIconPosition(0, 2).setName("Iron Boots");
	public static Item ironGloves = new ItemArmorGloves(116).setArmorType(EnumArmor.IRON).setIconPosition(0, 2).setName("Iron Gloves");
	public static Item ironBelt = new ItemArmorBelt(117).setArmorType(EnumArmor.IRON).setIconPosition(0, 2).setName("Iron Belt");
	
	public static Item silverHelmet = new ItemArmorHelmet(118).setArmorType(EnumArmor.SILVER).setIconPosition(2, 0).setName("Silver Helmet");
	public static Item silverBody = new ItemArmorBody(119).setArmorType(EnumArmor.SILVER).setIconPosition(2, 1).setName("Silver Body");
	public static Item silverPants = new ItemArmorPants(120).setArmorType(EnumArmor.SILVER).setIconPosition(2, 2).setName("Silver Legguards");	
	public static Item silverBoots = new ItemArmorBoots(121).setArmorType(EnumArmor.SILVER).setIconPosition(0, 2).setName("Silver Boots");
	public static Item silverGloves = new ItemArmorGloves(122).setArmorType(EnumArmor.SILVER).setIconPosition(0, 2).setName("Silver Gloves");
	public static Item silverBelt = new ItemArmorBelt(123).setArmorType(EnumArmor.SILVER).setIconPosition(0, 2).setName("Silver Belt");
	
	public static Item goldHelmet = new ItemArmorHelmet(124).setArmorType(EnumArmor.GOLD).setIconPosition(4, 0).setName("Gold Helmet").setItemQuality(EnumItemQuality.RARE);
	public static Item goldBody = new ItemArmorBody(125).setArmorType(EnumArmor.GOLD).setIconPosition(4, 1).setName("Gold Body").setItemQuality(EnumItemQuality.RARE);
	public static Item goldPants = new ItemArmorPants(126).setArmorType(EnumArmor.GOLD).setIconPosition(4, 2).setName("Gold Legguards").setItemQuality(EnumItemQuality.RARE);	
	public static Item goldBoots = new ItemArmorBoots(127).setArmorType(EnumArmor.GOLD).setIconPosition(0, 2).setName("Gold Boots");
	public static Item goldGloves = new ItemArmorGloves(128).setArmorType(EnumArmor.GOLD).setIconPosition(0, 2).setName("Gold Gloves");
	public static Item goldBelt = new ItemArmorBelt(129).setArmorType(EnumArmor.GOLD).setIconPosition(0, 2).setName("Gold Belt");
	
	public static Item rocketBoots = new ItemArmorAccessory(130).setIconPosition(0, 3).setName("Rocket Boots");	
	public static Item ringOfVigor = new ItemArmorAccessory(131).setBonuses(new EnumSetBonuses[]{ 
			EnumSetBonuses.DAMAGE_DONE_10 
	}).setIconPosition(0, 3).setName("Ring of Vigor").setExtraTooltipInformation("This ring instills a sense of courage in its wearer.");	
	public static Item talismanOfWinds = new ItemArmorAccessory(132).setBonuses(new EnumSetBonuses[]{ 
			EnumSetBonuses.MOVEMENT_SPEED_10 
	}).setIconPosition(0, 3).setName("Talisman of Winds");	
	public static Item angelsSigil = new ItemArmorAccessory(133).setBonuses(new EnumSetBonuses[]{ 
			 EnumSetBonuses.HEAVENS_REPRIEVE
	}).setIconPosition(0, 3).setName("Angel's Sigil");	
	
	
	public static Item copperCoin = new ItemCoin(200).setIconPosition(0, 0).setName("Copper Coin");
	public static Item silverCoin = new ItemCoin(201).setIconPosition(0, 0).setName("Silver Coin");
	public static Item goldCoin = new ItemCoin(202).setIconPosition(0, 0).setName("Gold Coin");
	public static Item platinumCoin = new ItemCoin(203).setIconPosition(0, 0).setName("Platinum Coin");
	public static Item marblePillarEnd = new ItemPlacable(204, true).setIconPosition(4,9).setName("Marble Pillar");
	public static Item stonePillarEnd = new ItemPlacable(205, true).setIconPosition(4,10).setName("Stone Pillar");
	public static Item diamondPillarEnd = new Item(206).setIconPosition(4,11).setName("Diamond P");
	public static Item goldPillarEnd = new Item(207).setIconPosition(4,12).setName("Gold P");
	
	public static Item woodenArrow = new ItemAmmo(300).setProjectile(EntityProjectile.woodenArrow).setIconPosition(0, 8).setName("Wooden Arrow");
	
	public static Item snowball = new ItemRanged(301, 50).setAmmo(Item.woodenArrow).setCooldownTicks(10).setName("Snow Ball").setIconPosition(14, 0);
	public static Item magicMissileSpell = new ItemMagic(302, 1, 10, EntityProjectile.magicMissile).setName("Tome of Magic Missile").setIconPosition(6, 0);
	public static Item heartCrystal = new ItemHeartCrystal(340).setIconPosition(7, 0).setName("Heart Crystal");
	public static Item manaCrystal = new ItemManaCrystal(341).setIconPosition(6, 0).setName("Mana Crystal");
	
	public static Item manaPotion1 = new ItemPotionMana(342, 75).setIconPosition(7, 1).setName("Minor Mana Potion");
	public static Item manaPotion2 = new ItemPotionMana(343, 150).setIconPosition(7, 1).setName("Mana Potion");
	public static Item healthPotion1 = new ItemPotionHealth(344, 75).setIconPosition(6, 1).setName("Minor Healing Potion");
	public static Item healthPotion2 = new ItemPotionHealth(345, 150).setIconPosition(6, 1).setName("Healing Potion");
	public static Item manaStar = new ItemPotionMana(346, 25).setIconPosition(12, 3).setName("Mana Star");
		
	public static Item healingHerb1 = new Item(347).setIconPosition(0, 1).setName("Minor Healing Herb");
	public static Item healingHerb2 = new Item(348).setIconPosition(0, 1).setName("Healing Herb");
	public static Item magicHerb1 = new Item(349).setIconPosition(0, 1).setName("Minor Magic Herb");
	public static Item magicHerb2 = new Item(350).setIconPosition(0, 1).setName("Magic Herb");
	public static Item vialEmpty = new Item(351).setIconPosition(0, 1).setName("Empty Vial");
	public static Item vialOfWater = new Item(352).setIconPosition(0, 1).setName("Vial of Water");
	
	public static Item regenerationPotion1 = new ItemPotionRegeneration(359, 90, 1).setIconPosition(9, 1).setName("Minor Regen Potion");
	public static Item regenerationPotion2 = new ItemPotionRegeneration(360, 90, 2).setIconPosition(9, 2).setName("Regen Potion");
	public static Item regenerationPotion3 = new ItemPotionRegeneration(361, 90, 3).setIconPosition(0, 2).setName("regen potion 3");
	public static Item regenerationPotion4 = new ItemPotionRegeneration(362, 90, 4).setIconPosition(0, 2).setName("regen potion 4");
	public static Item attackSpeedPotion1 = new ItemPotionAttackSpeed(363, 180, 1).setIconPosition(9, 3).setName("Minor Attack Speed Potion");
	public static Item attackSpeedPotion2 = new ItemPotionAttackSpeed(364, 180, 2).setIconPosition(9, 4).setName("Attack Speed Potion");
	public static Item attackSpeedPotion3 = new ItemPotionAttackSpeed(365, 180, 3).setIconPosition(0, 2).setName("attack speed potion 3");
	public static Item attackSpeedPotion4 = new ItemPotionAttackSpeed(366, 180, 4).setIconPosition(0, 2).setName("attack speed potion 4");
	public static Item criticalChancePotion1 = new ItemPotionCriticalBuff(367, 180, 1).setIconPosition(10, 1).setName("Minor Critical Potion");
	public static Item criticalChancePotion2 = new ItemPotionCriticalBuff(368, 180, 2).setIconPosition(10, 2).setName("Critical Potion");
	public static Item criticalChancePotion3 = new ItemPotionCriticalBuff(369, 180, 3).setIconPosition(0, 2).setName("critical chance potion 3");
	public static Item criticalChancePotion4 = new ItemPotionCriticalBuff(370, 180, 4).setIconPosition(0, 2).setName("critical chance potion 4");
	public static Item damageBuffPotion1 = new ItemPotionDamageBuff(371, 75, 1).setIconPosition(12, 1).setName("Minor Damage Potion");
	public static Item damageBuffPotion2 = new ItemPotionDamageBuff(372, 75, 2).setIconPosition(12, 2).setName("Damage Potion");
	public static Item damageBuffPotion3 = new ItemPotionDamageBuff(373, 75, 3).setIconPosition(0, 2).setName("damage buff potion 3");
	public static Item damageBuffPotion4 = new ItemPotionDamageBuff(374, 75, 4).setIconPosition(0, 2).setName("damage buff potion 4");
	public static Item damageSoakPotion1 = new ItemPotionDamageSoak(375, 45, 1).setIconPosition(8, 3).setName("Minor Damage Soak Potion");
	public static Item damageSoakPotion2 = new ItemPotionDamageSoak(376, 45, 2).setIconPosition(8, 4).setName("Damage Soak Potion");
	public static Item damageSoakPotion3 = new ItemPotionDamageSoak(377, 45, 3).setIconPosition(0, 2).setName("damage soak potion 3");
	public static Item damageSoakPotion4 = new ItemPotionDamageSoak(378, 45, 4).setIconPosition(0, 2).setName("damage soak potion 4");
	public static Item dodgePotion1 = new ItemPotionDodge(379, 120, 1).setIconPosition(11, 1).setName("Minor Dodge Potion");
	public static Item dodgePotion2 = new ItemPotionDodge(380, 120, 2).setIconPosition(11, 2).setName("Dodge Potion");
	public static Item dodgePotion3 = new ItemPotionDodge(381, 120, 3).setIconPosition(0, 2).setName("dodge potion 3");
	public static Item dodgePotion4 = new ItemPotionDodge(382, 120, 4).setIconPosition(0, 2).setName("dodge potion 4");
	public static Item manaRegenerationPotion1 = new ItemPotionManaRegeneration(383, 150, 1).setIconPosition(6, 3).setName("Minor Mana Regen Potion");
	public static Item manaRegenerationPotion2 = new ItemPotionManaRegeneration(384, 150, 2).setIconPosition(6, 4).setName("Mana Regen Potion");
	public static Item manaRegenerationPotion3 = new ItemPotionManaRegeneration(385, 150, 3).setIconPosition(0, 2).setName("mana regen potion 3");
	public static Item manaRegenerationPotion4 = new ItemPotionManaRegeneration(386, 150, 4).setIconPosition(0, 2).setName("mana regen potion 4");
	public static Item steelSkinPotion1 = new ItemPotionSteelSkin(387, 150, 1).setIconPosition(7, 3).setName("Minor Steel Skin Potion");
	public static Item steelSkinPotion2 = new ItemPotionSteelSkin(388, 150, 2).setIconPosition(7, 4).setName("Steel Skin Potion");
	public static Item steelSkinPotion3 = new ItemPotionSteelSkin(389, 150, 3).setIconPosition(0, 2).setName("steel skin potion 3");
	public static Item steelSkinPotion4 = new ItemPotionSteelSkin(390, 150, 4).setIconPosition(0, 2).setName("steel skin potion 4");
	public static Item swiftnessPotion1 = new ItemPotionSwiftness(391, 360, 1).setIconPosition(13, 1).setName("Minor Swiftness Potion");
	public static Item swiftnessPotion2 = new ItemPotionSwiftness(392, 360, 2).setIconPosition(13, 2).setName("Swiftness Potion");
	public static Item swiftnessPotion3 = new ItemPotionSwiftness(393, 360, 3).setIconPosition(0, 2).setName("swiftness potion 3");
	public static Item swiftnessPotion4 = new ItemPotionSwiftness(394, 360, 4).setIconPosition(0, 2).setName("swiftness potion 4");
	public static Item absorbPotion1 = new ItemPotionAbsorb(395, 30, 1).setIconPosition(9, 1).setName("Minor Absorb Potion");
	
}
