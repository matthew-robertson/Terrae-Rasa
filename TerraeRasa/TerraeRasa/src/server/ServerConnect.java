package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnect 
{
	public Socket requestGameConnection(String[] message, Socket socket, ObjectOutputStream os, ObjectInputStream is) throws IOException
	{
		os.writeUTF("/connect");
		os.flush();
		
		String response = is.readUTF();
		message[0] = response;

		if(!response.equals("connection accepted"))
		{
			socket.close();		
			return null;
		}
		return socket;
	}
	
	public String[] getServerInformation(String ip, int port)
	{
		try {
			return requestServerInformation(ip, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new String[] { };		
	}
	
	private String[] requestServerInformation(String ip, int port) throws UnknownHostException, IOException, ClassNotFoundException
	{
		Socket socket = new Socket(ip, port);
		ObjectOutputStream os = null;
		ObjectInputStream is = null;
		try {
			 os = new ObjectOutputStream(socket.getOutputStream());
			 is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		os.writeUTF("/serverinfo");
		os.flush();
		String[] stuff = (String[])is.readObject();		
		
		os.close();
		is.close();
		socket.close();		
		return stuff;
	}
}
