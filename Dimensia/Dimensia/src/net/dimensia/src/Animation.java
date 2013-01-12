package net.dimensia.src;


public class Animation 
{
	public Animation(String fileLocation, int imageSubWidth, int imageSubHeight, int duration)
	{
		this.imageSubWidth = imageSubWidth;
		this.imageSubHeight = imageSubHeight;
		this.fileLocation = fileLocation;
		this.duration = duration;
		animationIndex = 0;
		loadAnimations();
		lastFrameDisplayed = System.currentTimeMillis();
		frameDelay = (int) ((float)(duration) / animationFrames.length);
	}
	
	public void loadAnimations()
	{
		TextureLoader loader = new TextureLoader();
		animationFrames = loader.getTextureArray(fileLocation, imageSubWidth, imageSubHeight);
	}
	
	public void bindNextTexture()
	{
		animationFrames[animationIndex].bind();			
		if((System.currentTimeMillis() - lastFrameDisplayed) > frameDelay)
		{
			animationIndex++;
		}	
		if(animationIndex >= animationFrames.length)
		{
			animationIndex = 0;
		}
	}
	
	public int getDuration() 
	{
		return duration;
	}
	
	public int getSubWidth() 
	{
		return imageSubWidth;
	}
	
	public int getSubHeight()
	{
		return imageSubHeight;
	}
	
	public String getFileLocation()
	{
		return fileLocation;
	}

	public int frameDelay;
	public long lastFrameDisplayed;
	public Texture[] animationFrames;
	public int animationIndex;
	private int duration;
	private int imageSubWidth;
	private int imageSubHeight;
	private String fileLocation;
}