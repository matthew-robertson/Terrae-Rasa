package transmission;

import java.io.Serializable;

public class UpdateWithObject 
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String command;
	public Object object;
}
