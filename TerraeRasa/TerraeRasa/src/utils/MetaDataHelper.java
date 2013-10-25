package utils;


/**
 * MetaDataHelper is a utility class to help deal with blocks of sizes greater than 1x1. Currently supporting blocks upto size 3x3.
 * 
 * <br><br>
 * 
 * It exposes only one method {@link #getMetaDataArray(int, int)}, taking arguments of width, and height. This returns a constant 
 * meta data array of size widthxheight. Using this metadata, it's possible to make easy calculations on which part of a block 
 * has been hit, and how to deal with it appropriately. Metadata array values are of type int.
 * 
 * <br><br>
 * 
 * An example of a metadata array, for size 2x3, is: <br>
 * {{ 1, 2 }, <br>
 * { 3, 4 }, <br>
 * { 5, 6 }} <br>
 * 
 * 
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class MetaDataHelper 
{	
	/**
	 * Returns a constant metadata array of size (w/6) by (h/6). This can be of any size from 0x0 to 100x100.
	 * Any value beyond 100 is denied based on the fact it is ridiculous.
	 * @param w the width of the metadata (block width)
	 * @param h the height of the metadata (block height)
	 * @return a metadata array of specified (width/6) and (height/6)
	 */
	public static int[][] getMetaDataArray(int w, int h)
	{
		if(w > 100) 
			w = 100;
		if(h > 100) 
			h = 100;
		
		int[][] metadata = new int[w][h];
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				metadata[i][j] = 1 + (i * w) + j;
			}
		}
		return metadata;
	}	
}
