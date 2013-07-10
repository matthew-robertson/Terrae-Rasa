package client;

public class ServerInfo 
{
	private String name;
	private String ip;
	private String port;
	
	public ServerInfo(String name, String ip, String port)
	{
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setIP(String ip)
	{
		this.ip = ip;
	}
	
	public void setPort(String port)
	{
		this.port = port;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getIP()
	{
		return ip;
	}
	
	public String getPort()
	{
		return port;
	}
	
	public String toString()
	{
		return name;
	}
}
