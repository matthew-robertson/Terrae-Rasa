package net.dimensia.src;

import java.util.Vector;

/**
 * <code>Item</code> defines the class for most non-<code>Block</code> things within the player's inventory. 
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
 * <code>public static Item copperAxe = new ItemToolAxe(1).setDamageDone(4).setItemName("copper Axe").setIconIndex(0, 7).setToolMaterial(EnumToolMaterial.COPPER);</code>
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
public class Item 
{
	protected EnumToolMaterial material;
	protected EnumItemQuality itemQuality;
	protected static int shiftedIndex;
	protected int iconIndex;
	protected int damage;
	protected int maxStackSize;
	protected int itemID;
	protected int durability;
	protected boolean isContainer;
	protected String itemName;
	protected int toolType;
	protected int manaReq;
	protected Vector<ItemStack> ammo;
	protected EntityProjectile projectile;
	protected String extraTooltipInformation;
	
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
		shiftedIndex = 2048;
		itemID = i + shiftedIndex;
		maxStackSize = 250;
		isContainer = false;
		durability = -1;
		damage = 0;
		itemName = "Unnamed";
		material = EnumToolMaterial.FIST;
		itemQuality = EnumItemQuality.COMMON;
		extraTooltipInformation = "";
		
		if(itemsList[i + shiftedIndex] != null)
		{
			System.out.println(new StringBuilder().append("Conflict@ itemsList").append(i).toString());
			throw new RuntimeException(new StringBuilder().append("Conflict@ itemsList").append(i).toString());
		}
		itemsList[i + shiftedIndex] = this;		
	}
	
	protected Item setItemName(String s)
	{
		itemName = s;
		return this;
	}
	
	protected Item setContainerItem(boolean flag)
	{
		isContainer = flag;
		return this;
	}
	
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
	
	protected Item setIconIndex(int i, int j)
	{
		iconIndex = i * 16 + j;
		return this;
	}
	
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
	
	protected Item setManaReq(int i){
		manaReq = i;
		return this;
	}
		
	public static Item getItemByID(int id)
	{
		return itemsList[id];
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public boolean getIsContainer()
	{
		return isContainer;
	}
	
	public int getMaxStackSize()
	{
		return maxStackSize;
	}
	
	public int getDurability()
	{
		return durability;
	}
	
	public String getExtraTooltipInformation()
	{
		return extraTooltipInformation;
	}
	
	public int getIconIndex()
	{
		return iconIndex;
	}
	
	public EnumItemQuality getItemQuality()
	{
		return itemQuality;
	}
	
	public int getItemID()
	{
		return itemID;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public int getManaReq(){
		return manaReq;
	}
	
	public int getToolType(){
		return toolType;
	}
	
	public EnumToolMaterial getToolMaterial(){
		return material;
	}
	
	public EntityProjectile getProjectile(){
		return projectile;
	}
	
	public ItemStack[] getAmmoAsArray(){
		ItemStack[] ammunition = new ItemStack[ammo.size()];
		ammo.copyInto(ammunition);
		return ammunition;
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
	
	public void onRightClick(World world, EntityLivingPlayer entity)
	{
	}
	
	public static final Item[] itemsList = new Item[3200];
	/** Item Declarations **/
	

	public static Item copperAxe = new ItemToolAxe(1).setDamageDone(4).setItemName("Copper Axe").setIconIndex(0, 7).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperPickaxe = new ItemToolPickaxe(2).setDamageDone(3).setItemName("Copper Pickaxe").setIconIndex(0, 6).setToolMaterial(EnumToolMaterial.COPPER);	
	public static Item copperHammer = new ItemToolHammer(3).setDamageDone(3).setItemName("Copper Hammer").setIconIndex(0, 5).setToolMaterial(EnumToolMaterial.COPPER);
	public static Item copperSword = new ItemToolSword(4).setDamageDone(6).setItemName("Copper Sword").setIconIndex(0, 4).setToolMaterial(EnumToolMaterial.COPPER);	
	
	public static Item bronzeAxe = new ItemToolAxe(5).setDamageDone(5).setItemName("Bronze Axe").setIconIndex(0, 7).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzePickaxe = new ItemToolPickaxe(6).setDamageDone(4).setItemName("Bronze Pickaxe").setIconIndex(0, 6).setToolMaterial(EnumToolMaterial.BRONZE);	
	public static Item bronzeHammer = new ItemToolHammer(7).setDamageDone(4).setItemName("Bronze Hammer").setIconIndex(0, 5).setToolMaterial(EnumToolMaterial.BRONZE);
	public static Item bronzeSword = new ItemToolSword(8).setDamageDone(7).setItemName("Bronze Sword").setIconIndex(0, 4).setToolMaterial(EnumToolMaterial.BRONZE);	
	
	public static Item ironAxe = new ItemToolAxe(9).setDamageDone(7).setItemName("Iron Axe").setIconIndex(1, 7).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironPickaxe = new ItemToolPickaxe(10).setDamageDone(4).setItemName("Iron Pickaxe").setIconIndex(1, 6).setToolMaterial(EnumToolMaterial.IRON);	
	public static Item ironHammer = new ItemToolHammer(11).setDamageDone(5).setItemName("Iron Hammer").setIconIndex(1, 5).setToolMaterial(EnumToolMaterial.IRON);
	public static Item ironSword = new ItemToolSword(12).setDamageDone(9).setItemName("Iron Sword").setIconIndex(1, 4).setToolMaterial(EnumToolMaterial.IRON);	
	
	public static Item silverAxe = new ItemToolAxe(13).setDamageDone(8).setItemName("Silver Axe").setIconIndex(2, 7).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverPickaxe = new ItemToolPickaxe(14).setDamageDone(5).setItemName("Silver Pickaxe").setIconIndex(2, 6).setToolMaterial(EnumToolMaterial.SILVER);	
	public static Item silverHammer = new ItemToolHammer(15).setDamageDone(6).setItemName("Silver Hammer").setIconIndex(2, 5).setToolMaterial(EnumToolMaterial.SILVER);
	public static Item silverSword = new ItemToolSword(16).setDamageDone(11).setItemName("Silver Sword").setIconIndex(2, 4).setToolMaterial(EnumToolMaterial.SILVER);	
	
	public static Item goldAxe = new ItemToolAxe(17).setDamageDone(9).setItemName("Gold Axe").setIconIndex(4, 7).setToolMaterial(EnumToolMaterial.GOLD);	
	public static Item goldPickaxe = new ItemToolPickaxe(18).setDamageDone(7).setItemName("Gold Pickaxe").setIconIndex(4, 6).setToolMaterial(EnumToolMaterial.GOD).setItemQuality(EnumItemQuality.LEGENDARY);	
	public static Item goldHammer = new ItemToolHammer(19).setDamageDone(8).setItemName("Gold Hammer").setIconIndex(4, 5).setToolMaterial(EnumToolMaterial.GOLD);
	public static Item goldSword = new ItemToolSword(20).setDamageDone(14).setItemName("Gold Sword").setIconIndex(4, 4).setToolMaterial(EnumToolMaterial.GOLD);	
	
	public static Item copperIngot = new Item(51).setIconIndex(3, 9).setItemName("Copper Ingot");
	public static Item tinIngot = new Item(52).setIconIndex(3, 10).setItemName("Tin Ingot");
	public static Item bronzeIngot = new Item(53).setIconIndex(3, 11).setItemName("Bronze Ingot");
	public static Item ironIngot = new Item(54).setIconIndex(3, 12).setItemName("Iron Ingot");
	public static Item silverIngot = new Item(55).setIconIndex(3, 13).setItemName("Silver Ingot");
	public static Item goldIngot = new Item(56).setIconIndex(3, 14).setItemName("Gold Ingot");
	
	public static Item copperOre = new Item(57).setIconIndex(0, 9).setItemName("Copper Ore");
	public static Item tinOre = new Item(58).setIconIndex(0, 10).setItemName("Tin Ore");
	public static Item bronzeOre = new Item(59).setIconIndex(0, 0).setItemName("Bronze Ore");
	public static Item ironOre = new Item(60).setIconIndex(0, 11).setItemName("Iron Ore");
	public static Item silverOre = new Item(61).setIconIndex(0, 12).setItemName("Silver Ore");
	public static Item goldOre = new Item(62).setIconIndex(0, 13).setItemName("Gold Ore");
	public static Item coal = new Item(63).setIconIndex(1, 9).setItemName("Coal");
	public static Item diamond = new Item(64).setIconIndex(0, 0).setItemName("Diamond");
	public static Item ruby = new Item(65).setIconIndex(0, 0).setItemName("Ruby");
	public static Item emerald = new Item(66).setIconIndex(0, 0).setItemName("Emerald");
	public static Item sapphire = new Item(67).setIconIndex(0, 0).setItemName("Sapphire");
	
	public static Item copperHelmet = new ItemArmorHelmet(100).setArmorType(EnumArmor.COPPER).setIconIndex(0, 0).setItemName("Copper Helmet");
	public static Item copperBody = new ItemArmorBody(101).setArmorType(EnumArmor.COPPER).setIconIndex(0, 1).setItemName("Copper Body");
	public static Item copperGreaves = new ItemArmorGreaves(102).setArmorType(EnumArmor.COPPER).setIconIndex(0, 2).setItemName("Copper Greaves");
	public static Item bronzeHelmet = new ItemArmorHelmet(103).setArmorType(EnumArmor.BRONZE).setIconIndex(0, 0).setItemName("Bronze Helmet");
	public static Item bronzeBody = new ItemArmorBody(104).setArmorType(EnumArmor.BRONZE).setIconIndex(0, 1).setItemName("Bronze Body");
	public static Item bronzeGreaves = new ItemArmorGreaves(105).setArmorType(EnumArmor.BRONZE).setIconIndex(0, 2).setItemName("Bronze Greaves");
	public static Item ironHelmet = new ItemArmorHelmet(106).setArmorType(EnumArmor.IRON).setIconIndex(1, 0).setItemName("Iron Helmet");
	public static Item ironBody = new ItemArmorBody(107).setArmorType(EnumArmor.IRON).setIconIndex(1, 1).setItemName("Iron Body");
	public static Item ironGreaves = new ItemArmorGreaves(108).setArmorType(EnumArmor.IRON).setIconIndex(1, 2).setItemName("Iron Greaves");
	public static Item silverHelmet = new ItemArmorHelmet(109).setArmorType(EnumArmor.SILVER).setIconIndex(2, 0).setItemName("Silver Helmet");
	public static Item silverBody = new ItemArmorBody(110).setArmorType(EnumArmor.SILVER).setIconIndex(2, 1).setItemName("Silver Body");
	public static Item silverGreaves = new ItemArmorGreaves(111).setArmorType(EnumArmor.SILVER).setIconIndex(2, 2).setItemName("Silver Greaves");	
	public static Item goldHelmet = new ItemArmorHelmet(112).setArmorType(EnumArmor.GOLD).setIconIndex(4, 0).setItemName("Gold Helmet").setItemQuality(EnumItemQuality.RARE);
	public static Item goldBody = new ItemArmorBody(113).setArmorType(EnumArmor.GOLD).setIconIndex(4, 1).setItemName("Gold Body").setItemQuality(EnumItemQuality.RARE);
	public static Item goldGreaves = new ItemArmorGreaves(114).setArmorType(EnumArmor.GOLD).setIconIndex(4, 2).setItemName("Gold Greaves").setItemQuality(EnumItemQuality.RARE);	
	public static Item rocketBoots = new ItemArmorAccessory(115).setIconIndex(0, 3).setItemName("Rocket Boots");	
	public static Item ringOfVigor = new ItemArmorAccessory(116).setBonuses(new EnumSetBonuses[]{ 
			EnumSetBonuses.DAMAGE_DONE_10 
	}).setIconIndex(0, 3).setItemName("Ring of Vigor");	
	public static Item talismanOfWinds = new ItemArmorAccessory(117).setBonuses(new EnumSetBonuses[]{ 
			EnumSetBonuses.MOVEMENT_SPEED_10 
	}).setIconIndex(0, 3).setItemName("Talisman of Winds");	
	public static Item angelsSigil = new ItemArmorAccessory(118).setBonuses(new EnumSetBonuses[]{ 
			 EnumSetBonuses.HEAVENS_REPRIEVE
	}).setIconIndex(0, 3).setItemName("Angel's Sigil");	
	
	
	public static Item copperCoin = new ItemCoin(200).setIconIndex(0, 0).setItemName("Copper Coin");
	public static Item silverCoin = new ItemCoin(201).setIconIndex(0, 0).setItemName("Silver Coin");
	public static Item goldCoin = new ItemCoin(202).setIconIndex(0, 0).setItemName("Gold Coin");
	public static Item platinumCoin = new ItemCoin(203).setIconIndex(0, 0).setItemName("Platinum Coin");
	public static Item marblePillarEnd = new ItemPlacable(204, true).setIconIndex(4,9).setItemName("Marble Pillar");
	public static Item stonePillarEnd = new ItemPlacable(205, true).setIconIndex(4,10).setItemName("Stone Pillar");
	public static Item diamondPillarEnd = new Item(206).setIconIndex(4,11).setItemName("Diamond P");
	public static Item goldPillarEnd = new Item(207).setIconIndex(4,12).setItemName("Gold P");
	
	public static Item woodenArrow = new ItemAmmo(300).setProjectile(EntityProjectile.woodenArrow).setIconIndex(0, 8).setItemName("Wooden Arrow");
	
	public static Item snowball = new ItemRanged(301, 50).setAmmo(Item.woodenArrow).setItemName("Snow Ball").setIconIndex(14, 0);
	public static Item magicMissileSpell = new ItemMagic(302, 1, 10, EntityProjectile.magicMissile).setItemName("Tome of Magic Missile").setIconIndex(6, 0);
	public static Item heartCrystal = new ItemHeartCrystal(340).setIconIndex(7, 0).setItemName("Heart Crystal");
	public static Item manaCrystal = new ItemManaCrystal(341).setIconIndex(6, 0).setItemName("Mana Crystal");
	
	public static Item manaPotion1 = new ItemPotionMana(342, 75).setIconIndex(7, 1).setItemName("Minor Mana Potion");
	public static Item manaPotion2 = new ItemPotionMana(343, 150).setIconIndex(7, 1).setItemName("Mana Potion");
	public static Item healthPotion1 = new ItemPotionHealth(344, 75).setIconIndex(6, 1).setItemName("Minor Healing Potion");
	public static Item healthPotion2 = new ItemPotionHealth(345, 150).setIconIndex(6, 1).setItemName("Healing Potion");
	public static Item manaStar = new ItemPotionMana(346, 25).setIconIndex(12, 3).setItemName("Mana Star");
		
	public static Item healingHerb1 = new Item(347).setIconIndex(0, 1).setItemName("Minor Healing Herb");
	public static Item healingHerb2 = new Item(348).setIconIndex(0, 1).setItemName("Healing Herb");
	public static Item magicHerb1 = new Item(349).setIconIndex(0, 1).setItemName("Minor Magic Herb");
	public static Item magicHerb2 = new Item(350).setIconIndex(0, 1).setItemName("Magic Herb");
	public static Item vialEmpty = new Item(351).setIconIndex(0, 1).setItemName("Empty Vial");
	public static Item vialOfWater = new Item(352).setIconIndex(0, 1).setItemName("Vial of Water");
	
	public static Item regenerationPotion1 = new ItemPotionRegeneration(359, 90, 1).setIconIndex(9, 1).setItemName("Minor Regen Potion");
	public static Item regenerationPotion2 = new ItemPotionRegeneration(360, 90, 2).setIconIndex(9, 2).setItemName("Regen Potion");
	public static Item regenerationPotion3 = new ItemPotionRegeneration(361, 90, 3).setIconIndex(0, 2).setItemName("regen potion 3");
	public static Item regenerationPotion4 = new ItemPotionRegeneration(362, 90, 4).setIconIndex(0, 2).setItemName("regen potion 4");
	public static Item attackSpeedPotion1 = new ItemPotionAttackSpeed(363, 180, 1).setIconIndex(9, 3).setItemName("Minor Attack Speed Potion");
	public static Item attackSpeedPotion2 = new ItemPotionAttackSpeed(364, 180, 2).setIconIndex(9, 4).setItemName("Attack Speed Potion");
	public static Item attackSpeedPotion3 = new ItemPotionAttackSpeed(365, 180, 3).setIconIndex(0, 2).setItemName("attack speed potion 3");
	public static Item attackSpeedPotion4 = new ItemPotionAttackSpeed(366, 180, 4).setIconIndex(0, 2).setItemName("attack speed potion 4");
	public static Item criticalChancePotion1 = new ItemPotionCriticalBuff(367, 180, 1).setIconIndex(10, 1).setItemName("Minor Critical Potion");
	public static Item criticalChancePotion2 = new ItemPotionCriticalBuff(368, 180, 2).setIconIndex(10, 2).setItemName("Critical Potion");
	public static Item criticalChancePotion3 = new ItemPotionCriticalBuff(369, 180, 3).setIconIndex(0, 2).setItemName("critical chance potion 3");
	public static Item criticalChancePotion4 = new ItemPotionCriticalBuff(370, 180, 4).setIconIndex(0, 2).setItemName("critical chance potion 4");
	public static Item damageBuffPotion1 = new ItemPotionDamageBuff(371, 75, 1).setIconIndex(12, 1).setItemName("Minor Damage Potion");
	public static Item damageBuffPotion2 = new ItemPotionDamageBuff(372, 75, 2).setIconIndex(12, 2).setItemName("Damage Potion");
	public static Item damageBuffPotion3 = new ItemPotionDamageBuff(373, 75, 3).setIconIndex(0, 2).setItemName("damage buff potion 3");
	public static Item damageBuffPotion4 = new ItemPotionDamageBuff(374, 75, 4).setIconIndex(0, 2).setItemName("damage buff potion 4");
	public static Item damageSoakPotion1 = new ItemPotionDamageSoak(375, 45, 1).setIconIndex(8, 3).setItemName("Minor Damage Soak Potion");
	public static Item damageSoakPotion2 = new ItemPotionDamageSoak(376, 45, 2).setIconIndex(8, 4).setItemName("Damage Soak Potion");
	public static Item damageSoakPotion3 = new ItemPotionDamageSoak(377, 45, 3).setIconIndex(0, 2).setItemName("damage soak potion 3");
	public static Item damageSoakPotion4 = new ItemPotionDamageSoak(378, 45, 4).setIconIndex(0, 2).setItemName("damage soak potion 4");
	public static Item dodgePotion1 = new ItemPotionDodge(379, 120, 1).setIconIndex(11, 1).setItemName("Minor Dodge Potion");
	public static Item dodgePotion2 = new ItemPotionDodge(380, 120, 2).setIconIndex(11, 2).setItemName("Dodge Potion");
	public static Item dodgePotion3 = new ItemPotionDodge(381, 120, 3).setIconIndex(0, 2).setItemName("dodge potion 3");
	public static Item dodgePotion4 = new ItemPotionDodge(382, 120, 4).setIconIndex(0, 2).setItemName("dodge potion 4");
	public static Item manaRegenerationPotion1 = new ItemPotionManaRegeneration(383, 150, 1).setIconIndex(6, 3).setItemName("Minor Mana Regen Potion");
	public static Item manaRegenerationPotion2 = new ItemPotionManaRegeneration(384, 150, 2).setIconIndex(6, 4).setItemName("Mana Regen Potion");
	public static Item manaRegenerationPotion3 = new ItemPotionManaRegeneration(385, 150, 3).setIconIndex(0, 2).setItemName("mana regen potion 3");
	public static Item manaRegenerationPotion4 = new ItemPotionManaRegeneration(386, 150, 4).setIconIndex(0, 2).setItemName("mana regen potion 4");
	public static Item steelSkinPotion1 = new ItemPotionSteelSkin(387, 150, 1).setIconIndex(7, 3).setItemName("Minor Steel Skin Potion");
	public static Item steelSkinPotion2 = new ItemPotionSteelSkin(388, 150, 2).setIconIndex(7, 4).setItemName("Steel Skin Potion");
	public static Item steelSkinPotion3 = new ItemPotionSteelSkin(389, 150, 3).setIconIndex(0, 2).setItemName("steel skin potion 3");
	public static Item steelSkinPotion4 = new ItemPotionSteelSkin(390, 150, 4).setIconIndex(0, 2).setItemName("steel skin potion 4");
	public static Item swiftnessPotion1 = new ItemPotionSwiftness(391, 360, 1).setIconIndex(13, 1).setItemName("Minor Swiftness Potion");
	public static Item swiftnessPotion2 = new ItemPotionSwiftness(392, 360, 2).setIconIndex(13, 2).setItemName("Swiftness Potion");
	public static Item swiftnessPotion3 = new ItemPotionSwiftness(393, 360, 3).setIconIndex(0, 2).setItemName("swiftness potion 3");
	public static Item swiftnessPotion4 = new ItemPotionSwiftness(394, 360, 4).setIconIndex(0, 2).setItemName("swiftness potion 4");
	public static Item absorbPotion1 = new ItemPotionAbsorb(395, 30, 1).setIconIndex(9, 1).setItemName("Minor Absorb Potion");
	
}
