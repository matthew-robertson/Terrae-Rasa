package utils;

import java.io.Serializable;

import math.Vector2;
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
    public Vector2 velocity;
    public Vector2 acceleration;
    public EnumColor[] colors;
    public boolean rotateRight;
    public Texture texture;
    
    public Particle()
    {
    	 velocity = new Vector2(0, 0);
    	 acceleration = new Vector2(0, 0);
    	 colors = new EnumColor[0];
    }
    
    /**
	 * Applies dampening, then adds the acceleration vector to the velocity, then updated position.
	 * This is designed to move the particle throughout the world on each tick.
	 */
	public void integrate()
	{
		velocity.addVector(acceleration);
		this.x += velocity.getX();
		this.y += velocity.getY();
		life -= fade;
	}
}