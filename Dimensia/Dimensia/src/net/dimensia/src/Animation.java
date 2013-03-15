package net.dimensia.src;


public class Animation 
{
	public Texture texture;
	public AnimationTextureCoords animationBounds;
	public AnimationTextureCoords[] frameCoords;
	public int animationIndex;
	private int ticksDuration; 
	int duration;//ms
	int frames;
	//private int imageSubWidth; 
	//private int imageSubHeight;
	
	public Animation(Texture texture, AnimationTextureCoords animationBounds, int ticksDuration, int frames)
	{
		this.frames = frames;
		this.ticksDuration = ticksDuration;
		duration = ticksDuration * 50;
		animationIndex = 0;
		
	}

	public void bind()
	{
		texture.bind();
	}
	
	public AnimationTextureCoords getFrameBounds()
	{
		return frameCoords[animationIndex];
	}
	
	public void update()
	{
		
	}
}