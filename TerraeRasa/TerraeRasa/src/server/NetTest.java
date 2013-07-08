package server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetTest 
{
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException
	{
		String ip = "localhost"; 
		int port = 48615;
		
		ServerConnect connect = new ServerConnect();
		String[] stuff = connect.getServerInformation(ip, port);
		for(String str : stuff)
		{
			System.out.println("[Server Info]: " + str);
		}
		 
		String[] message = { "" };
		Socket socket = connect.requestGameConnection(ip, port, message);
	//	ClientConnectionThread thread = new ClientConnectionThread(socket);
		if(socket != null) socket.close();
		
		System.out.println(message[0]);
		
	}

}
