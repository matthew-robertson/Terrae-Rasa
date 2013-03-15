package net.dimensia.src;

/**
 * The ambiguously named AnimationTextureCoords stores the coords for 1 frame of an animation.
 *
 */
public class AnimationTextureCoords 
{
	double left;
	double right;
	double top;
	double bottom;
	 
	public AnimationTextureCoords(double left, double top, double right, double bottom)
	{
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
}
