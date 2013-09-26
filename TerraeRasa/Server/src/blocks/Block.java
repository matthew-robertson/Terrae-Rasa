package blocks;

import items.Item;

import java.util.Random;

import server.Log;
import utils.ActionbarItem;
import utils.ItemStack;


/**
 * Block defines methods and variables for the tiles that make up the world. Blocks can
 * be stored in the player's inventory as ItemStacks. Custom functionality can be added to 
 * a Block by extending Block, which will allow this more complicated Block to share a common
 * base and be a part of the blockList[], which contains all legal blocks.
 * <br> <br>
 * All Blocks except Block.air should be created in Block.java using {@link #Block(int)}. Air
 * can be created using {@link #Block()}, which will assign that block an ID of 0 and the unique 
 * properties of air.
 * <br> <br>
 * Block's contructors are protected to prevent random block redeclaration in the program, which will
 * cause an ID conflict and RuntimeException. 
 * 
 * @author Alec Sobeck
 * @author Matthew Robertson
 * @version 1.0
 * @since 1.0
 */
public class Block extends ActionbarItem 
{
	/** Defines one of the different tools that can be used to break a Block. */
	public static final short BLOCK_TYPE_UNBREAKABLE = -1,
								 BLOCK_TYPE_HAND = 0,
								 BLOCK_TYPE_PICKAXE = 1,
								 BLOCK_TYPE_AXE = 2;
	/** Defines one of the different tilemaps a Block can use when calculating its bitmap. */
	public static final byte TILEMAP_GENERAL = 0,
								TILEMAP_PILLAR = 1,
								TILEMAP_TREE_BRANCH = 2,
								TILEMAP_TREE = 3,
								TILEMAP_TREETOP = 4;
	/** Corresponds to the value of Render.BLOCK_SIZE. This is the width of a 1x1 Block in the world. */
	public static final int BLOCK_WIDTH = 6;
	/** Corresponds to the value of Render.BLOCK_SIZE. This is the height of a 1x1 Block in the world. */
	public static final int BLOCK_HEIGHT = 6;
	protected static final Random random = new Random();
	/** The ItemStack this Block drops when destroyed. This may be null, or inaccurate should {@link #getDroppedItem()} be overriden. */
	protected ItemStack droppedItem;
	/** The maximum possible quantity of {@link #droppedItem} that can be obtained. */
	protected int maximumDropAmount;
	/** The minimum possible quantity of {@link #droppedItem} that can be obtained. */
	protected int minimumDropAmount;
	/** Indicated whether or not another Block can be placed over this one. */
	protected boolean isOveridable;
    /** Indicated whether or not this block has metadata - if something has metadata it is greater than size 1x1. */
	protected boolean hasMetaData;
	/** This is a variable used to help calculate bitmaps. Different constants like {@link #TILEMAP_GENERAL} will change 
	 * the behaviour of bitmaps. */
	protected byte tileMap;
	
	
	
	/** The render width of a texture (in the world) */
	public double blockWidth; 
	/** The render height of a texture (in the world) */
	public double blockHeight; 
	/** The pixel width of a Block's texture (on a spritesheet) */
	public double textureWidth; 
	/** The pixel height of a Block's texture (on a spritesheet) */
	public double textureHeight; // 
	protected int gradeOfToolRequired;
	protected short blockType;
	protected int blockTier;
	protected boolean breakable;
	protected double hardness;
	protected boolean passable;
	protected boolean isMineable;
	public boolean isSolid;
	protected int hRange;
	protected int lRange;
	public double lightStrength;
	public int lightRadius;
	
	/**
	 * Constructs a special Block - air. This constructor should only ever be used for the initial declaration of Block.air.
	 */
	private Block() 
	{
		super(0);
		name = "air";
		extraTooltipInformation = "";
		isSolid = false;
		isOveridable = true;
		blockWidth = 6;
		blockHeight = 6;
		textureWidth = 16;
		lightStrength = 0;
		lightRadius = 0;
		textureHeight = 16;
		tileMap = TILEMAP_GENERAL;
		blockType = BLOCK_TYPE_HAND;
		iconX = 0;
		iconY = 0;
		if (blocksList[id] != null) {
			Log.log("Conflict@ BlockID " + id);
			throw new RuntimeException(new StringBuilder().append("Conflict@ BlockID ").append(id).toString());
		}
		blocksList[id] = this;
	}

	/**
	 * Constructs a new Block that will be stored in the blocksList[] at the index of the blockID. This allows for 
	 * easy access to the Block created later. Block has a protected constructor, so that a Block can't redeclared 
	 * at a whim. Additional customization can be performed after creation of a Block through the use of setters. 
	 * @param id the unique blockID of the Block being created
	 */
	protected Block(int id)
	{
		super(id);
		this.id = id;
		breakable = true;
		blockWidth = 6;
		blockHeight = 6;
		textureWidth = 16;
		textureHeight = 16;
		passable = false;
		tileMap = ' ';
		maxStackSize = 250;
		gradeOfToolRequired = 0;
		blockTier = 0;
		droppedItem = new ItemStack(this);
		maximumDropAmount = 1;
		minimumDropAmount = 1;
		isOveridable = false;
		isSolid = true;
		isMineable = true;
		extraTooltipInformation = "";
		lightStrength = 0;
		lightRadius = 0;
		iconX = 0;
		iconY = 0;
		blockType = BLOCK_TYPE_HAND;
		tileMap = TILEMAP_GENERAL;
		if (blocksList[id] != null) {
			Log.log("Conflict@ BlockID " + id);
			throw new RuntimeException(new StringBuilder().append("Conflict@ BlockID ").append(id).toString());
		}
		blocksList[id] = this;
	}

	/**
	 * Creates a deep copy of a Block. Objects are copied, but will not have the same reference, though, Enums references are directly
	 * copied to the new Block.
	 * @param block the Block to be copied
	 * @return a new Block with the same properties as the copied one
	 */
	public Block(Block block) 
	{
		super(block);
		this.droppedItem = new ItemStack(block.droppedItem);
		this.maximumDropAmount = block.maximumDropAmount;
		this.minimumDropAmount = block.minimumDropAmount;
		this.isOveridable = block.isOveridable;
		this.hasMetaData = block.hasMetaData;
		this.tileMap = block.tileMap;
		this.blockWidth = block.blockWidth;
		this.blockHeight = block.blockHeight;
		this.textureWidth = block.textureWidth;
		this.textureHeight = block.textureHeight;
		this.gradeOfToolRequired = block.gradeOfToolRequired;
		this.blockType = block.blockType;
		this.blockTier = block.blockTier;
		this.breakable = block.breakable;
		this.hardness = block.hardness;
		this.passable = block.passable;
		this.isMineable = block.isMineable;
		this.isSolid = block.isSolid;
		this.hRange = block.hRange;
		this.lRange = block.lRange;
		this.lightRadius = block.lightRadius;
		this.lightStrength = block.lightStrength;
	}
	
	/**
	 * Set the light strength and radius.
	 * @param strength the max strength of the light. 1.0F is 100%, 0.0F is 0%
	 * @param radius the max distance of the light in blocks
	 * @return the updated block
	 */
	protected Block setLightStrengthAndRadius(double strength, int radius)
	{
		this.lightStrength = strength;
		this.lightRadius = radius;
		return this;
	}
	
	/**
	 * Overrides ActionbarItem.setName(String) to make Block creation more friendly.
	 */
	protected Block setName(String name)
	{
		this.name = name;
		return this;
	}
	
	/**
	 * Sets this Blocks' hardness
	 * @param f the new Block hardness
	 * @return a reference to this Block
	 */
	protected Block setBlockHardness(double f)
	{
		hardness = f;
		return this;
	}

	/**
	 * Sets this blocks breakability
	 * @param flag the new Block breakability
	 * @return a reference to this Block
	 */
	protected Block setBreakable(boolean flag) 
	{
		breakable = flag;
		return this;
	}

	/**
	 * Sets this Blocks's icon position in form (x,y)
	 * @param x the new IconX value
	 * @param y the new IconY value
	 * @return a reference to this Block
	 */
	protected Block setIconIndex(int x, int y) 
	{
		iconY = y;
		iconX = x;
		return this;
	}

	/**
	 *  * Overrides ActionbarItem.setExtraTooltipInformation(String) to make Block creation more friendly.
	 */
	protected Block setExtraTooltipInformation(String info) 
	{
		this.extraTooltipInformation = info;
		return this;
	}

	/**
	 * Sets this Block's passability.
	 * @param flag the new Block passability
	 * @return a reference to this Block
	 */
	protected Block setPassable(boolean flag) 
	{
		passable = flag;
		return this;
	}

	/**
	 *  * Overrides ActionbarItem.setMaxStackSize(int) to make Block creation more friendly.
	 */
	protected Block setMaxStackSize(int i)
	{
		maxStackSize = i;
		return this;
	}

	/**
	 * Sets the grade of tool required to break this block.
	 * @param i the new grade of tool required to break this block
	 * @return a reference to this Block
	 */
	protected Block setGradeOfToolRequired(int i) 
	{
		gradeOfToolRequired = i;
		return this;
	}

	/**
	 * Sets both the world and texture width of a block.
	 * @param x the new width of this block, for the texture and world
	 * @param y the new height of this block, for the texture and world
	 * @return a reference to this Block
	 */
	protected Block setBothBlockWidthAndHeight(int x, int y)
	{
		setOnlyBlockWorldWidthAndHeight(x * 6, y * 6);
		setOnlyBlockTextureWidthAndHeight(x * 16, y * 16);
		return this;
	}

	/**
	 * Sets only the world width of a block. Note: this must be the actual size in ortho units.
	 * @param w the new width of this Block, for the world only
	 * @param h the new height of this Block, for the world only
	 * @return a reference to this Block
	 */
	protected Block setOnlyBlockWorldWidthAndHeight(int w, int h) 
	{
		blockWidth = w;
		blockHeight = h;
		hasMetaData = true;
		return this;
	}

	/**
	 * Sets the texture size of this block. Note: this must be the full texture size (ex. 16x16)
	 * @param w the new texture width of this Block
	 * @param h the new texture height of this Block
	 * @return a reference to this Block
	 */
	protected Block setOnlyBlockTextureWidthAndHeight(int w, int h) 
	{
		textureWidth = w;
		textureHeight = h;
		return this;
	}

	/**
	 * Sets this block's tier. 
	 * @param i the new Block tier
	 * @return a reference to this Block
	 */
	protected Block setBlockTier(int i)
	{
		blockTier = i;
		return this;
	}

	/**
	 * Sets this block's type, which can be a constant like {@link #BLOCK_TYPE_UNBREAKABLE}, to determine what tool breaks it.
	 * @param s the new Block type 
	 * @return a reference to this Block
	 */
	protected Block setBlockType(short s)
	{
		blockType = s;
		return this;
	}

	/**
	 * Sets whether or not the block allows blocks to be placed by it in the world.
	 * @param flag whether or not blocks can be placed next to this Block
	 * @return a reference to this Block
	 */
	protected Block setIsSolid(boolean flag)
	{
		isSolid = flag;
		return this;
	}

	/**
	 * Sets this block's hRange.
	 * @param i this block's new hRange
	 * @return a reference to this Block
	 */
	protected Block setHRange(int i) 
	{
		hRange = i;
		return this;
	}

	/**
	 * Sets this block's lRange.
	 * @param i this Block's new lRange
	 * @return a reference to this Block
	 */
	protected Block setLRange(int i) 
	{
		lRange = i;
		return this;
	}

	/**
	 * Sets the ItemStack, min, and max sizes of what's dropped by this Block when destroyed.
	 * @param stack the ItemStack dropped by this Block when destroyed (can be null)
	 * @param min the minimum quantity of said stack obtained
	 * @param max the maximum quantity of said stack obtained
	 * @return a reference to this Block
	 */
	protected Block setDroppedItem(ItemStack stack, int min, int max) 
	{
		droppedItem = stack;
		minimumDropAmount = min;
		maximumDropAmount = max;
		return this;
	}

	/**
	 * Sets this block's mineability - whether or not it is able to be mined.
	 * @param b this Block's new mineability
	 * @return a reference to this Block
	 */
	protected Block setIsMineable(boolean b)
	{
		isMineable = b;
		return this;
	}

	/**
	 * Sets this Block's icon position (x,y).
	 * @param x this Block's new IconX
	 * @param y this Block's new IconY
	 * @return a reference to this Block
	 */
	protected Block setIconIndex(double x, double y) 
	{
		iconY = y;
		iconX = x;
		return this;
	}

	/**
	 * Sets this Blokc's tilemap type to a constant.
	 * @param b this block's new tilemap constant
	 * @return a reference to this Block
	 */
	protected Block setTileMap(byte b)
	{
		tileMap = b;
		return this;
	}

	/**
	 * Sets whether or not a Block is Overidable. If a block is overidable it can have another block placed on it.
	 * @param flag this Block's new ability to be overwritten
	 * @return a reference to this Block
	 */
	protected Block setIsOveridable(boolean flag) 
	{
		isOveridable = flag;
		return this;
	}
	
	/**
	 * Overrides ActionbarItem.overrideItemIcon(int, int, int) to make block creation easier.
	 */
	protected Block overrideItemIcon(int x, int y)
	{
		iconOverrideX = x;
		iconOverrideY = y;
		iconOverriden = true;
		return this;
	}
	
	public boolean getHasMetaData()
	{
		return hasMetaData;
	}
	
	public double getLightStrength()
	{
		return lightStrength;
	}
	
	public double getLightRadius()
	{
		return lightRadius;
	}
	
	public double getBlockHardness()
	{
		return hardness;
	}

	public boolean getBreakable() 
	{
		return breakable;
	}

	public boolean getIsMineable(){
		return isMineable;
	}

	public boolean getPassable() 
	{
		return passable;
	}

	public double getBlockWidth()
	{
		return blockWidth;
	}

	public double getBlockHeight() 
	{
		return blockHeight;
	}

	public double getTextureWidth() 
	{
		return textureWidth;
	}

	public double getTextureHeight() 
	{
		return textureHeight;
	}

	public int getGradeOfToolRequired() 
	{
		return gradeOfToolRequired;
	}

	public int getBlockTier() 
	{
		return blockTier;
	}

	public int getBlockType()
	{
		return blockType;
	}

	public boolean getIsOveridable()
	{
		return isOveridable;
	}

	public int getHRange() 
	{
		return hRange;
	}

	public int getLRange() 
	{
		return lRange;
	}

	public byte getTileMap()
	{
		return tileMap;
	}

	/**
	 * Rolls for a blocks's dropped items, which can either be based on chance or a 100% drop.
	 * @return whatever destroying this block would yield as an ItemStack
	 */
	public ItemStack getDroppedItem() 
	{
		return (droppedItem != null) ? new ItemStack(droppedItem.getItemID(), (minimumDropAmount + (((maximumDropAmount - minimumDropAmount) > 0) ? random.nextInt(maximumDropAmount - minimumDropAmount) : 0))) : null;
	}

	public boolean getIsSolid() 
	{
		return isSolid;
	}

	/** Contains all the Blocks legally available in the game. */
	public static final Block[] blocksList = new Block[2048];

	public static final Block none = new Block(2047).setName("None");
	public static final Block air = new Block();
	public static final Block dirt = new Block(1).setName("Dirt").setBlockHardness(40.0f).setIconIndex(0, 3).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block stone = new Block(2).setName("Stone").setBlockHardness(70.0f).setIconIndex(0, 0).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block grass = new BlockGrass(3).setName("Grass").setBlockHardness(40.0f).setIconIndex(0, 4).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block sand = new Block(6).setName("Sand").setBlockHardness(30.0f).setIconIndex(0, 2).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block sandstone = new Block(7).setName("Sandstone").setIconIndex(0, 1).setBlockHardness(70.0f).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block cactus = new Block(8).setName("Cactus").setBlockHardness(1.0f).setIconIndex(2, 3).setPassable(true).setBlockType(BLOCK_TYPE_AXE).setIsSolid(false);
	public static final Block tree = new BlockWood(9).setName("Tree").setTileMap(TILEMAP_TREE).setBlockHardness(60.0f).setIconIndex(1, 27).setPassable(true).setBlockType(BLOCK_TYPE_AXE).setIsSolid(false);
	public static final Block treebase = new BlockWood(10).setName("tree base").setTileMap(TILEMAP_TREE).setBlockHardness(60.0f).setIconIndex(0, 27).setPassable(true).setBlockType(BLOCK_TYPE_AXE).setIsSolid(false);
	public static final Block treebranch = new BlockWood(12).setName("tree branch").setTileMap(TILEMAP_TREE_BRANCH).setBlockHardness(5.0f).setIconIndex(4, 25).setPassable(true).setIsSolid(false);
	public static final Block treetop = new BlockLeaves(20).setName("tree top left top").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(4, 27).setPassable(true).setIsSolid(false);
	public static final Block treetopl2 = new BlockLeaves(21).setName("tree top left bottom").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(4, 28).setPassable(true).setIsSolid(false);
	public static final Block treetopc1 = new BlockLeaves(22).setName("tree top center top").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(5, 27).setPassable(true).setIsSolid(false);
	public static final Block treetopc2 = new BlockLeaves(23).setName("tree top center bottom").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(5, 28).setPassable(true).setIsSolid(false);
	public static final Block treetopr1 = new BlockLeaves(24).setName("tree top right top").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(6, 27).setPassable(true).setIsSolid(false);
	public static final Block treetopr2 = new BlockLeaves(25).setName("tree top right bottom").setTileMap(TILEMAP_TREETOP).setBlockHardness(5.0f).setIconIndex(6, 28).setPassable(true).setIsSolid(false);
	public static final Block gold = new BlockOre(26).setName("Gold Ore Block").setBlockHardness(10.0f).setHRange(0).setLRange(3).setIconIndex(15, 11).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.goldOre), 1, 1);
	public static final Block iron = new BlockOre(27).setName("Iron Ore Block").setBlockHardness(10.0f).setHRange(5).setLRange(10).setIconIndex(15, 8).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.ironOre), 1, 1);
	public static final Block coal = new BlockOre(28).setName("Coal Ore Block").setHRange(40).setLRange(10).setBlockHardness(10.0f).setIconIndex(15, 6).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.coal), 1, 1);
	public static final Block diamond = new BlockOre(29).setName("Diamond Ore Block").setBlockHardness(10.0f).setLRange(10).setIconIndex(15, 14).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.diamond), 1, 1);
	public static final Block ruby = new BlockOre(30).setName("Ruby Ore Block").setBlockHardness(10.0f).setLRange(5).setIconIndex(15, 13).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.ruby), 1, 1);
	public static final Block sapphire = new BlockOre(31).setName("Sapphire Ore Block").setBlockHardness(10.0f).setLRange(5).setIconIndex(15, 12).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.sapphire), 1, 1);
	public static final Block emerald = new BlockOre(32).setName("Emerald Ore Block").setBlockHardness(10.0f).setLRange(5).setIconIndex(15, 15).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.emerald), 1, 1);
	public static final Block opal = new BlockOre(33).setName("Opal Ore Block").setBlockHardness(10.0f).setLRange(5).setIconIndex(15, 16).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.opal), 1, 1);
	public static final Block jasper = new BlockOre(34).setName("Jasper Ore Block").setBlockHardness(10.0f).setLRange(5).setIconIndex(15, 17).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.jasper), 1, 1);
	public static final Block copper = new BlockOre(35).setName("Copper Ore Block").setBlockHardness(55.0f).setHRange(25).setLRange(0).setIconIndex(15, 9).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.copperOre), 1, 1);
	public static final Block silver = new BlockOre(36).setName("Silver Ore Block").setBlockHardness(80.0f).setHRange(0).setLRange(10).setIconIndex(15, 10).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.silverOre), 1, 1);
	public static final Block tin = new BlockOre(37).setName("Tin Ore Block").setBlockHardness(55.0f).setHRange(20).setLRange(0).setIconIndex(15, 8).setBlockType(BLOCK_TYPE_PICKAXE).setDroppedItem(new ItemStack(Item.tinOre), 1, 1);
	public static final Block redflower = new Block(38).setName("Red Flower").setBlockHardness(10.0f).setIconIndex(0, 1).setPassable(true).setIsOveridable(true).setIsSolid(false);
	public static final Block yellowflower = new Block(39).setName("Yellow Flower").setBlockHardness(10.0f).setIconIndex(0, 1).setPassable(true).setIsOveridable(true).setIsSolid(false);
	public static final Block tallgrass = new Block(40).setName("Tall Grass").setBlockHardness(10.0f).setIconIndex(0, 1).setPassable(true).setIsOveridable(true).setIsSolid(false);
	public static final Block snowCover = new Block(44).setName("Snow Cover").setBlockHardness(10.0f).setIconIndex(9, 10).setPassable(true).setIsOveridable(true).setDroppedItem(new ItemStack(Item.snowball), 1, 1).setIsSolid(false);
	public static final Block torch = new Block(48).setLightStrengthAndRadius(1.0F, 10).setName("Torch").setBlockHardness(0.0f).overrideItemIcon(5, 0).setOnlyBlockTextureWidthAndHeight(32, 32).setPassable(true).setIsSolid(false);
	public static final Block adminium = new Block(49).setName("Adminium").setIsMineable(false).setBlockHardness(8000.0f).setIconIndex(0, 1);
	public static final Block plank = new Block(50).setName("Plank").setBlockHardness(30.0f).setIconIndex(0, 20).setBlockType(BLOCK_TYPE_AXE);
	public static final Block sapling = new Block(51).setName("Sapling").setBlockHardness(5.0f).setIconIndex(10, 4).setBlockType(BLOCK_TYPE_AXE).setIsOveridable(true).setIsSolid(false);
	public static final Block furnace = new Block(52).setName("Furnace").setBlockHardness(50.0f).setIconIndex(10, 23).setBlockType(BLOCK_TYPE_PICKAXE).setBothBlockWidthAndHeight(2, 2);
	public static final Block craftingTable = new Block(53).setName("Crafting table").setBlockHardness(50.0f).setIconIndex(14, 23).setBlockType(BLOCK_TYPE_AXE).setBothBlockWidthAndHeight(2, 2);
	public static final Block snow = new Block(54).setName("Snow Block").setBlockHardness(10.0f).setIconIndex(4, 1).setBlockType(BLOCK_TYPE_PICKAXE).setIsOveridable(true);
	public static final Block heartCrystal = new Block(55).setName("Heart Crystal Block").setBlockHardness(50.0f).setIconIndex(1, 1).setBlockType(BLOCK_TYPE_PICKAXE).setBothBlockWidthAndHeight(2, 2).setDroppedItem(new ItemStack(Item.heartCrystal), 1, 1);
	public static final Block chest = new BlockChest(56, 20).setName("Chest").setBothBlockWidthAndHeight(2, 2).setBlockHardness(40.0f).setIconIndex(11, 1).setBlockType(BLOCK_TYPE_AXE).setIsOveridable(false).setIsSolid(true);
	
	// Needs recipe
	public static final Block bookshelf = new Block(57).setName("Bookshelf").setIconIndex(4, 3).setBlockHardness(20.0f).setBlockType(BLOCK_TYPE_AXE);
	public static final Block woodTable = new Block(58).setName("Wooden Table").setPassable(true).setBlockHardness(50.0f).setIconIndex(5, 24).setBlockType(BLOCK_TYPE_AXE).setBothBlockWidthAndHeight(2, 1);
	public static final Block stoneTable = new Block(59).setName("Stone Table").setPassable(true).setBlockHardness(50.0f).setIconIndex(5, 23).setBlockType(BLOCK_TYPE_PICKAXE).setBothBlockWidthAndHeight(2, 1);
	public static final Block fence = new Block(61).setName("Fence").setPassable(true).setBlockHardness(50.0f).setIconIndex(3, 4).setBlockType(BLOCK_TYPE_AXE);
	public static final Block woodChairRight = new Block(63).setName("Right facing wood chair").setPassable(true).setBlockHardness(50.0f).setIconIndex(5, 24).setBlockType(BLOCK_TYPE_AXE);
	public static final Block woodChairLeft = new Block(64).setName("Left facing wood chair").setPassable(true).setBlockHardness(50.0f).setIconIndex(4, 24).setBlockType(BLOCK_TYPE_AXE);
	public static final Block stoneChairRight = new Block(65).setName("Right facing stone chair").setPassable(true).setBlockHardness(50.0f).setIconIndex(5, 23).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block stoneChairLeft = new Block(66).setName("Left facing stone chair").setPassable(true).setBlockHardness(50.0f).setIconIndex(4, 23).setBlockType(BLOCK_TYPE_PICKAXE);

	public static final Block stonePillar = new BlockPillar(68).setName("Stone Pillar Block").setTileMap(TILEMAP_PILLAR).setPassable(true).setBlockHardness(75.0f).setIconIndex(0, 23).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block marblePillar = new BlockPillar(71).setName("Marble Pillar Block").setTileMap(TILEMAP_PILLAR).setPassable(true).setBlockHardness(75.0f).setIconIndex(0, 24).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block goldPillar = new BlockPillar(74).setName("Gold Pillar Block").setTileMap(TILEMAP_PILLAR).setPassable(true).setBlockHardness(75.0f).setIconIndex(0, 25).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block diamondPillar = new BlockPillar(77).setName("Diamond Pillar Block").setTileMap(TILEMAP_PILLAR).setPassable(true).setBlockHardness(75.0f).setIconIndex(0, 26).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block glass = new Block(79).setName("Glass Block").setBlockHardness(10.0f).setIconIndex(1, 3).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block ironChest = new BlockChest(80, 40).setName("Iron Chest").setBothBlockWidthAndHeight(2, 2).setBlockHardness(40.0f).setIconIndex(11, 1).setBlockType(BLOCK_TYPE_AXE).setIsOveridable(false).setIsSolid(true);
	public static final Block gemcraftingBench = new Block(81).setName("Gemcrafting table").setBlockHardness(50.0f).setIconIndex(0, 0).setBlockType(BLOCK_TYPE_PICKAXE).setBothBlockWidthAndHeight(2, 2);
	public static final Block alchemyStation = new Block(82).setName("Alchemy station").setBlockHardness(50.0f).setIconIndex(12, 23).setBlockType(BLOCK_TYPE_AXE).setBothBlockWidthAndHeight(2, 2);
	
	// Backwalls
	public static final Block backAir = new BlockBackWall(128).setIsSolid(false).setName("backwall air");
	public static final Block backDirt = new BlockBackWall(129).setIconIndex(0, 21).setName("backwall dirt").setBlockHardness(40.0f).setBlockType(BLOCK_TYPE_PICKAXE);
	public static final Block backStone = new BlockBackWall(130).setIconIndex(0, 22).setName("backwall stone").setBlockHardness(70.0f).setBlockType(BLOCK_TYPE_PICKAXE);
}
