package client.audio;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Song extends Music{
	
	protected float savedPosition;
	protected String name;
	
	public Song(String ref, String s) throws SlickException {
		super(ref);
		savedPosition = 0f;
		name = s;
	}
	
	public void savePosition(){
		savedPosition = super.getPosition();
	}

	public float getSavedPosition(){
		return savedPosition;
	}
	
	public String getName(){
		return name;
	}
	
}