package audio;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

/**
 * Simple sound manager for OpenAL using n sources accessed in a round robin schedule. 
 * Source n is reserved for a single buffer and checking for whether it's playing.
 * TODO: Compatibility for .ogg sound format
 */
public class SoundManager
{
	private int[] buffers = new int[256]; // We support at most 256 buffers
	private int[] sources; //Number of sources is limited tby user (and hardware)
	private IntBuffer scratchBuffer = BufferUtils.createIntBuffer(256); //Our internal scratch buffer
	public boolean soundOutput; // Whether we're running in no sound mode
	private int bufferIndex; //Current index in our buffers 
	private int sourceIndex; //Current index in our source list
	public float volume; //0.0f - 1.0f indicating % of volume
	
	 /**
     * Initializes the SoundManager
     * @param channels Number of channels to create
     */
	public SoundManager(int channels, float volume)
	{
		try
		{
			AL.create();

			// allocate sources
		    scratchBuffer.limit(channels);
		    AL10.alGenSources(scratchBuffer);
		    scratchBuffer.rewind();
		    scratchBuffer.get(sources = new int[channels]);
		
		    // could we allocate all channels?
		    if(AL10.alGetError() != AL10.AL_NO_ERROR)
		    {
		    	throw new LWJGLException("Unable to allocate " + channels + " sources");
		    }
	
		    // we have sound
		    soundOutput = true;
		    this.volume = volume;
	    }
		catch (LWJGLException e) 
		{
	    	e.printStackTrace();
	    	System.out.println("Sound disabled");
	    }
	}
    /**
     * Plays a sound effect
     * @param buffer Buffer index to play gotten from addSound
     */
    public void playEffect(int buffer)
    {
    	if(soundOutput) 
    	{
    		// make sure we never choose last channel, since it is used for special sounds
    		int channel = sources[(sourceIndex++ % (sources.length-1))];
    		
    		// link buffer and source, and play it
    		AL10.alSourcef(channel, AL10.AL_GAIN, volume); //Volume control		
    		AL10.alSourcei(channel, AL10.AL_BUFFER, buffers[buffer]);
    		AL10.alSourcePlay(channel);
    	}
    }

    /**
     * Plays a sound on last source
     * @param buffer Buffer index to play gotten from addSound
     */
	public void playSound(int buffer)
	{
		if(soundOutput)
		{
			AL10.alSourcef(sources[sources.length-1], AL10.AL_GAIN, volume); //Volume Control         
    	    AL10.alSourcei(sources[sources.length-1], AL10.AL_BUFFER, buffers[buffer]);
      	   	AL10.alSourcePlay(sources[sources.length-1]);
		}
	}

    /**
     * Whether a sound is playing on last source
     * @return true if a source is playing right now on source n
     */
	public boolean isPlayingSound() 
	{
		return AL10.alGetSourcei(sources[sources.length-1], AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public byte[] getBytesFromFile(File file) throws IOException
	{
		InputStream is = new FileInputStream(file);
	    
		// Get the size of the file
	    long length = file.length();
	
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	
	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];
	
	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
	
	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }
	
	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}

	/**
	 * Adds a sound to the Sound Managers pool
	 * @param path Path to file to load
	 * @return index into SoundManagers buffer list
	 */
  	public int addSound(String path) 
  	{
  		// Generate 1 buffer entry
  		scratchBuffer.rewind().position(0).limit(1);
  		AL10.alGenBuffers(scratchBuffer);
    	buffers[bufferIndex] = scratchBuffer.get(0);
    	SoundEngine engine = new SoundEngine();
    	
    	// load wave data from buffer
    	//WaveData wavefile = WaveData.create(path);

    	if(AL10.alIsExtensionPresent("AL_EXT_vorbis"))
    	{
        	byte[] buffer = new byte[1];
        	try 
        	{
    			buffer = getBytesFromFile(new File(path));
    		} 
        	catch (IOException e)
    		{
    			e.printStackTrace();
    		}
        	
        	 ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 0x200000).order(ByteOrder.nativeOrder());
        	byteBuffer.put(buffer);
        	
        	// copy to buffersb
        	AL10.alBufferData(byteBuffer.get(0), AL10.AL_FORMAT_VORBIS_EXT, scratchBuffer, scratchBuffer.capacity());
        	//AL10.alBufferData(bytebuffer.get(0), AL10.AL_FORMAT_VORBIS_EXT, bytebuffer, bytebuffer.capacity());
        	//AL10.alBufferData(buffers[bufferIndex], wavefile.format, wavefile.data, wavefile.samplerate);

        	// unload file again
        	//wavefile.dispose();

        	byteBuffer = null; //This is actually required because there is no GC
    	}
    	else
    	{
    		throw new RuntimeException("No .ogg sound support! Your game will now explode.");
    	}
    	
    	// return index for this sound
  		return bufferIndex++;
  	}

  	/**
   	 * Destroy this SoundManager
   	 */
  	public void destroy() 
  	{
  		if(soundOutput) 
  		{
  			// stop playing sounds
  			scratchBuffer.position(0).limit(sources.length);
  			scratchBuffer.put(sources).flip();
	      	AL10.alSourceStop(scratchBuffer);
	
	      	// destroy sources
	      	AL10.alDeleteSources(scratchBuffer);
	
	      	// destroy buffers
	      	scratchBuffer.position(0).limit(bufferIndex);
	      	scratchBuffer.put(buffers, 0, bufferIndex).flip();
	      	AL10.alDeleteBuffers(scratchBuffer);

      		// destory OpenAL
    		AL.destroy();
    	}
  	}
  	
  	
  	/** Probably Store Loaded Sounds here **/
  	
  	//public static int SOUND_SHOT; //<<Sound declaration
	
  	
  	
}