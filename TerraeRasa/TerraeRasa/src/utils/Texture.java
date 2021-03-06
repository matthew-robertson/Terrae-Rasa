package utils;

import org.lwjgl.opengl.GL11;

/**
 * <code>Texture</code> implements a simple class for managing opengl textures. A texture object is created
 * with an integer value (the textureID), and can then be bound using {@link #bind()}. This will cause
 * images drawn to use this texture, until another Texture object's bind() method is called.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Texture 
{
	private int textureID; 
	
	/**
	 * Constructs a new Texture object, storing the specified textureID for binding
	 * @param textureID the Integer representing the texture that can be bound
	 */
	public Texture(int textureID)
	{
		this.textureID = textureID;
	}

	/**
	 * Binds the texture using GL11. The texture will remain bound until the next bind() call of a different
	 * texture object, or manual call to GL11.glBindTexture(...)
	 */
	public void bind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	}
}
