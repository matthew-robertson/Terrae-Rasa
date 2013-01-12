package net.dimensia.src;


public class MetaDataHelper 
{	
	/**
	 * Prefered way to get the metadata array of a block 
	 */
	public static int[][] getMetaDataArray(int w, int h)
	{
		return getMetaDataByType(getTypeBySize(w, h));
	}
	
	private static EnumBlockSize getTypeBySize(int w, int h)
	{
		return (w == 2 && h == 1) ? EnumBlockSize.TWOBYONE : (w == 3 && h == 1) ? EnumBlockSize.THREEBYONE : (w == 1 && h == 2) ? EnumBlockSize.ONEBYTWO : (w == 2 && h == 2) ? EnumBlockSize.TWOBYTWO : (w == 3 && h == 2) ? EnumBlockSize.THREEBYTWO : (w == 1 && h == 3) ? EnumBlockSize.ONEBYTHREE : (w == 2 && h == 3) ? EnumBlockSize.TWOBYTHREE : (w == 3 && h == 3) ? EnumBlockSize.THREEBYTHREE : EnumBlockSize.ONEBYONE;
	}
		
	private static int[][] getMetaDataByType(EnumBlockSize size)
	{
		return (size == EnumBlockSize.TWOBYONE) ? twoByOne : (size == EnumBlockSize.THREEBYONE) ? threeByOne : (size == EnumBlockSize.ONEBYTWO) ? oneByTwo : (size == EnumBlockSize.TWOBYTWO) ? twoByTwo : (size == EnumBlockSize.THREEBYTWO) ? threeByTwo : (size == EnumBlockSize.ONEBYTHREE) ? oneByThree : (size == EnumBlockSize.TWOBYTHREE) ? twoByThree : (size == EnumBlockSize.THREEBYTHREE) ? threeByThree : oneByOne;		
	}
	
	private static final int[][] oneByOne = 
	{ 
		{ 1 }
	};
	private static final int[][] twoByOne = 
	{
		{ 1 },
		{ 2 }
	};
	private static final int[][] threeByOne = 
	{
		{ 1 },
		{ 2 },
		{ 3 }
	};
	private static final int[][] oneByTwo = 
	{
		{ 1, 2 }
	};
	private static final int[][] twoByTwo = 		
	{ 
		{ 1, 2 },
		{ 3, 4 } 
	};
	private static final int[][] threeByTwo = 	
	{ 
		{ 1, 2 },
		{ 3, 4 },
		{ 5, 6 } 
	};
	private static final int[][] oneByThree = 	
	{ 
		{ 1, 2, 3 }
	};
	private static final int[][] twoByThree = 	
	{
		{ 1, 2, 3 },
		{ 4, 5, 6 }
	};
	private static final int[][] threeByThree = 
	{ 
		{ 1, 2, 3 },
		{ 4, 5, 6 },
		{ 7, 8, 9 } 
	};		     
}
