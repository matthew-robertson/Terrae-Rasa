package net.dimensia.src;

public class LightingEngine 
{
	/**
	 * Generates a circular light source at the specified point of specified radius and lightLevel(initial opacity). For the final parameter, lightLevel a value
	 * of 1f indicates full strength white light
	 */
	public static float[][] generateLightSource(World world, int x, int y, int strength, float lightLevel)
	{	
		final float powerValue = 2.0f; //sets the curve of the light source
		final float lightModifier = 1.0f / ((float)(strength)); //strength of the light source (radius)
		float lightMap[][] = new float[strength * 2 + 2][strength * 2 + 2];
		//y += 3;
		for(int i = strength; i < 2 * strength; i++) //Bottom Right
		{
			for(int k = strength; k < 2 * strength; k++)
			{
				float f = (float) (Math.pow(((i - strength) * lightModifier), powerValue) + Math.pow(((k - strength) * lightModifier), powerValue));
				if(f > 1) f = 1;
				else if(lightLevel != 1) f += (float)(1 - f) * lightLevel;
				
				lightMap[i][k] = f;					
				if(lightMap[i][k] > world.getLight(MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), MathHelper.returnIntegerInWorldMapBounds_Y(world, y + k - strength)))
					lightMap[i][k] = world.getLight(MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), MathHelper.returnIntegerInWorldMapBounds_Y(world, y + k - strength));
			}
		}		
		for(int i = strength; i < 2 * strength; i++) //Top Right
		{
			for(int k = strength; k >= 0; k--)
			{
				float f = (float)(Math.pow(Math.abs(((k - strength) * lightModifier)), powerValue) + Math.pow(Math.abs(((strength - i) * lightModifier)), powerValue));
				if(f > 1) f = 1;
				else if(lightLevel != 1) f = f + (float)(1 - f) * lightLevel;
				
				lightMap[i][k] = f;				
				if(lightMap[i][k] > world.getLight(MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength)))
					lightMap[i][k] = world.getLight(MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength));				
			}
		}		
		for(int i = strength; i >= 0; i--) //Top Left
		{
			for(int k = strength; k >= 0; k--)
			{
				float f = (float) (Math.pow(((strength - i) * lightModifier), powerValue) + Math.pow(((strength - k) * lightModifier), powerValue));
				if(f > 1) f = 1;
				else if(lightLevel != 1) f = f + (float)(1 - f) * lightLevel;
				
				lightMap[i][k] = f;				
				if(lightMap[i][k] > world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength)))
					lightMap[i][k] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength));
			}
		}
		for(int i = strength; i >= 0; i--) //Bottom Left
		{
			for(int k = strength; k < 2 * strength; k++)
			{
				float f = (float) (Math.pow(((strength - i) * lightModifier), powerValue) + Math.pow(((k - strength) * lightModifier), powerValue));
				if(f > 1) f = 1;
				else if(lightLevel != 1) f = f + (float)(1 - f) * lightLevel;

				lightMap[i][k] = f;
				if(lightMap[i][k] > world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength)))
					lightMap[i][k] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength));
			}
		}			
		
		int k = strength * 2;
		for(int i = 0; i < k; i++)
		{
			lightMap[i][k] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k - strength));
			lightMap[k][i] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + k - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + i - strength));

			lightMap[i][k + 1] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k + 1 - strength));
			lightMap[k + 1][i] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + i - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world,y + k + 1 - strength));
		}
		lightMap[k + 1][k + 1] = world.getLight((int) MathHelper.returnIntegerInWorldMapBounds_X(world, x + k + 2 - strength), (int) MathHelper.returnIntegerInWorldMapBounds_Y(world, y + k + 2 - strength));
		
		return lightMap;
	}
	
	
	public static void applySunlight(World world)
	{
		float lightLevel = (world.getLightLevel());
		float darknessBuffer = 0.0f;
		for(int i = 1; i < world.getWidth() - 1; i++)
		{
			lightLevel = (world.getLightLevel());
			darknessBuffer = 0.0f;
			for(int k = 2; k < world.getHeight() - 1; k++)
			{
				if (!world.getBlock(i, k-2).isPassable() && 
					!world.getBlock(i, k+1).isPassable() && 
					!world.getBlock(i-1, k).isPassable() &&
					!world.getBlock(i+1, k).isPassable()){
					darknessBuffer = 0.95f;
				}
				else
				{
					darknessBuffer = lightLevel;
				}
				
				if (k > world.getHeight() - 250) 
				{
					lightLevel += 0.006146923f;
				}
				if (darknessBuffer < 0)
				{
					darknessBuffer = 0;
				}
				if (darknessBuffer > 1f) 
				{
					darknessBuffer = 1f;				
				}
				world.setLight(MathHelper.oneOrLess(darknessBuffer), i, k);
			}
		}
		
		/**
		for(int i = 0; i < world.xsize; i++)
		{
			for(int k = 250; k < 255; k++)
			{
				world.lightMap[i][k] = (k - 250) * 0.2f + 0.2f;
			}
		}
		
		for(int i = 0; i < world.xsize; i++)
		{
			for(int k = 255; k < world.ysize; k++)
			{
				world.lightMap[i][k] = 1.0f;
			}
		}
		**/
	}	
}
