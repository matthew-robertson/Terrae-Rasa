package savable;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import client.TerraeRasa;

import com.thoughtworks.xstream.XStream;

public class SaveManager 
{
	public void saveFile(String path, Object object) 
			throws IOException
	{
		XStream xstream = new XStream();
		String xml = xstream.toXML(object);
		System.out.println("len = " + xml.length());
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
	
	public void saveCompressedFile(String path, Object object) 
			throws IOException
	{		
		GZIPOutputStream fileWriter = new GZIPOutputStream(new FileOutputStream(new File(TerraeRasa.getBasePath() + path)));
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream s = new ObjectOutputStream(bos); 
		s.writeObject(object); 
		byte data[] = bos.toByteArray();
		fileWriter.write(data, 0, data.length);
		System.out.println("Saved to: " + path + " With Initial Size: " + data.length);
		s.close();
		bos.close();
		fileWriter.close();  
	}

	public Object loadCompressedFile(String path) 
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(new GZIPInputStream(new FileInputStream(TerraeRasa.getBasePath() + path)))); //Open an input stream
		Object obj = ois.readObject(); 
	    System.out.println("loaded from: " + path);
		ois.close();
		return obj;
	}


}
