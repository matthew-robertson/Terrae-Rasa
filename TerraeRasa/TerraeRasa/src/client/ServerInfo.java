package client;

public class ServerInfo 
{
	private String name;
	private String ip;
	private String port;
	private String password;
	
	public ServerInfo(String name, String ip, String password, String port)
	{
		this.name = name;
		this.ip = ip;
		this.password = password;
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

	public void setPassword(String password)
	{
		this.password = password;
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
	
	public String getPassword()
	{
		return password;
	}
	
	public String getPort()
	{
		return port;
	}
	
	public String toString()
	{
		return "ServerName=" + name + " @IP=" + ip + " @Port=" + port + " Password=" + password;
	}
}
