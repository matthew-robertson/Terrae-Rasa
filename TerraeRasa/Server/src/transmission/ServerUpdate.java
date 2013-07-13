package transmission;

import java.util.Vector;

public class ServerUpdate
{
	public Vector<String> values; 
	
	public ServerUpdate()
	{
		this.values = new Vector<String>();
	}
	
	public void addValue(String val)
	{
		this.values.add(val);
	}
	
	public String[] getValues()
	{
		String[] vals = new String[values.size()];
		values.copyInto(vals);
		return vals;
	}
}
