package server.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;

import entry.TerraeRasa;

/**
 *                                                     
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0 
 * @since       1.0
 */
public class SaveHelper
{
	public void saveFile(String path, Object object) 
			throws IOException
	{
		XStream xstream = new XStream();
		String xml = xstream.toXML(object);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TerraeRasa.getBasePath() + path)));
		writer.write(xml);
		writer.close();
	}
	
	public Object loadFile(String path) 
			throws FileNotFoundException
	{
		InputStream stream = new FileInputStream(new File(TerraeRasa.getBasePath() + path));
		XStream xstream = new XStream();
		return xstream.fromXML(stream);
	}
	
	/**
	 * Saves an object to file using GZIP compression. It must be Serializable or this will fail.
	 * @param path
	 * @param object
	 * @throws IOException
	 */
	public void saveCompressedFile(String path, Object object) 
			throws IOException
	{		
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(TerraeRasa.getBasePath() + path)));
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream s = new ObjectOutputStream(bos); 
		s.writeObject(object); 
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length);
		s.close();
		bos.close();
		fileWriter.close();  
	}
	
	/**
	 * Loads an object that was previously saved using GZIP compression.
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object loadCompressedFile(String path) 
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(new GZIPInputStream(new FileInputStream(TerraeRasa.getBasePath() + path)))); //Open an input stream
		Object obj = ois.readObject(); 
		ois.close();
		return obj;
	}
	
	public void saveFileXML(String path, String xml) 
			throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TerraeRasa.getBasePath() + path)));
		writer.write(xml);
		writer.close();
	}
	
	public String getFileXML(String path, boolean useClassloader) 
			throws URISyntaxException, IOException
	{
		File file = null;
		if(useClassloader)
		{
			// Load the directory as a resource
			URL dir_url = ClassLoader.getSystemResource(path);
			// Turn the resource into a File object
			file = new File(dir_url.toURI());
		}
		else
		{
			file = new File(TerraeRasa.getBasePath() + path);
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String xml = "";
		String line = "";
		while((line = reader.readLine()) != null)
		{
			xml += line;
			xml += '\n';
		}
		System.out.println("---- XML FILE START ----");
		System.out.println(xml);
		System.out.println("---- XML FILE END ----");
		reader.close();
		return xml;
	}
	
	public String convertToXML(Object obj)
	{
		XStream xstream = new XStream();
		return xstream.toXML(obj);
	}
	
	public Object xmlToObject(String xml)
	{
		XStream xstream = new XStream();
		return xstream.fromXML(xml);
	}
}
