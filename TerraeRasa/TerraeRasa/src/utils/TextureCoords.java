package utils;

/**
 * AnimationTextureCoords stores a set of texture coordinates. A set of texture coordinates
 * is considered to be a left, right, top, and bottom value for a given texture. Additionally, a width and height value
 * are calculated based on the given left, right, top and bottom values. No value in this class should ever exceed 1, or be
 * less than 0 and bounds checking is applied in the constructor. All values in a TextureCoords are final to prevent corruption
 * of the texture data.
 * <br><br>
 * Construct a new instance of TextureCoords using {@link #TextureCoords(double, double, double, double)}.
 */
public class TextureCoords 
{
	public final double left;
	public final double right;
	public final double top;
	public final double bottom;
	public final double width;
	public final double height;
	
	public TextureCoords(double left, double top, double right, double bottom)
	{
		this.left = (left < 0) ? 0 : (left > 1) ? 1 : left;
		this.right = (right < 0) ? 0 : (right > 1) ? 1 : right;
		this.top = (top < 0) ? 0 : (top > 1) ? 1 : top;
		this.bottom = (bottom < 0) ? 0 : (bottom > 1) ? 1 : bottom;
		this.width = (right - left < 0) ? 0 : (right - left > 1) ? 1 : right - left;
		this.height = (bottom - top < 0) ? 0 : (bottom - top> 1) ? 1 : bottom - top;
	}	
}
