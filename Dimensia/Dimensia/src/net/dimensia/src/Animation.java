package net.dimensia.src;

public class Animation 
{
	/** Defines the different directions to base individual frame texture coordinates on. Currently texture coordinates 
	 * per frame can only be calculated going horizontally and vertically, based on the TextureCoord object given in
	 * the constructor of the Animation. */
	public static final int ANIMATE_HORIZONTAL = 1,
								ANIMATE_VERTICALLY = 2;
	private Tessellator t = Tessellator.instance;
	private Texture texture;
	private TextureCoords animationBounds;
	private TextureCoords[] frameCoords;
	private int animationIndex;
	/** There are 20 ticks in a second. To get the actual time of this animation in ms, roughly multiply ticksDuration by 50. */
	private int ticksDuration; 
	private int frames;
	private int width;
	private int height;
	private int ticksIntoAnimation = 0;
	private int durationOfFrameTicks;
	
	/**
	 * Creates a new Animation object, in order to animate something. An animation should be based on a sprite sheet of some
	 * sort, and can be either the entire sprite sheet or a part of it. 
	 * @param texture the texture used to render the animation. This should be a sprite-sheet of some sort.
	 * @param animationBounds the texture coordinates of the entire animation on the sprite-sheet
	 * @param renderWidth the width of the texture when rendered to the screen
	 * @param renderHeight the height of the texture when rendered to the screen
	 * @param ticksDuration the duration of the animation (all frames) in game ticks (20 * seconds)
	 * @param frames the total number of frames in the animation
	 * @param animationDirection the direction of the animation on the sprite-sheet. EX. Horizontally means the frames are all on the same row.
	 */
	public Animation(Texture texture, TextureCoords animationBounds, int renderWidth, int renderHeight, int ticksDuration, int frames, int animationDirection)
	{
		this.frames = frames;
		this.ticksDuration = ticksDuration;
		this.texture = texture;
		this.animationBounds = animationBounds;
		animationIndex = 0;
		durationOfFrameTicks = (ticksDuration / frames > 0) ? (ticksDuration) / frames : 1;
		frameCoords = new TextureCoords[frames];
		
		for(int i = 0; i < frames; i++)
		{
			if(animationDirection == ANIMATE_HORIZONTAL)
			{
				frameCoords[i] = new TextureCoords(animationBounds.left + i*(animationBounds.width / frames),
						animationBounds.top, 
						animationBounds.left + (i+1)*(animationBounds.width / frames), 
						animationBounds.bottom);
			}
			else if(animationDirection == ANIMATE_VERTICALLY)
			{
				frameCoords[i] = new TextureCoords(animationBounds.left,
						animationBounds.top + (i) * animationBounds.height / frames, 
						animationBounds.right, 
						animationBounds.top + (i + 1) * animationBounds.height / frames);
			}			
		}		
	}

	/**
	 * Renders the Animation at the specified position, with the width/height given in the constructor.
	 * @param x the x position to render the animation at 
	 * @param y the y position to render the animation at
	 */
	public void render(double x, double y)
	{
		texture.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + height, 0, frameCoords[animationIndex].left, frameCoords[animationIndex].bottom);
        t.addVertexWithUV(x + width, y + height, 0, frameCoords[animationIndex].right, frameCoords[animationIndex].bottom);
        t.addVertexWithUV(x + width, y, 0, frameCoords[animationIndex].right, frameCoords[animationIndex].top);
        t.addVertexWithUV(x, y, 0, frameCoords[animationIndex].left, frameCoords[animationIndex].top);
        t.draw();      
        update();
	}
	
	/**
	 * Renders the Animation at the specified position, but overrides the width/height given in the constructor to render at
	 * a custom size.
	 * @param x the x position to render the animation at 
	 * @param y the y position to render the animation at
	 * @param width the width of the animation being rendered to the screen
	 * @param height the height of the animation being rendered to the screen
	 */
	public void render(double x, double y, double width, double height)
	{
		texture.bind(); 
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + height, 0, frameCoords[animationIndex].left, frameCoords[animationIndex].bottom);
        t.addVertexWithUV(x + width, y + height, 0, frameCoords[animationIndex].right, frameCoords[animationIndex].bottom);
        t.addVertexWithUV(x + width, y, 0, frameCoords[animationIndex].right, frameCoords[animationIndex].top);
        t.addVertexWithUV(x, y, 0, frameCoords[animationIndex].left, frameCoords[animationIndex].top);
        t.draw();      
        update();
	}
	
	/**
	 * Updates the ticks into the animation, moving the next frame is required.
	 */
	private void update()
	{
		ticksIntoAnimation++;
		
		if(ticksIntoAnimation >= durationOfFrameTicks)
		{
			animationIndex++;
			ticksIntoAnimation = 0;
			if(animationIndex >= frames)
				animationIndex = 0;
		}
	}
}