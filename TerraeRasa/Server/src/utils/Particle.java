package utils;

import java.io.Serializable;


import enums.EnumColor;


public class Particle 
		implements Serializable
{      
	private static final long serialVersionUID = 1L;
	public double index;
    public boolean active;               // Active (Yes/No)
    public double life;                   // Particle Life
    public double fade;                   // Fade Speed
    public double r;                      // Red Value
    public double g;                      // Green Value
    public double b;                      // Blue Value
    public double a;
    public double x;                      // X Position
    public double y;                      // Y Position
    public double zrot;					 // Rotation on the Z axis
    public Vector2F velocity;
    public Vector2F acceleration;
    public EnumColor[] colors;
    public boolean rotateRight;
    
    public Particle()
    {
    	 velocity = new Vector2F(0, 0);
    	 acceleration = new Vector2F(0, 0);
    	 colors = new EnumColor[0];
    }
    
    /**
	 * Applies dampening, then adds the acceleration vector to the velocity, then updated position.
	 * This is designed to move the particle throughout the world on each tick.
	 */
	public void integrate()
	{
		velocity = velocity.addF(acceleration);
		this.x += velocity.getX();
		this.y += velocity.getY();
		life -= fade;
	}
}