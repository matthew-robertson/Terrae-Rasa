package hardware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LegalKeypresses 
{
	private final static char[] legalLetters = { 
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'[', ']', '{', '}', '\'', '\"', ':', ';', '.', ',', '>', '<', '/', '?', '\\', '-', '_', '+', '=', '|', 
		'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '~', '`', ' '
	};
	
	public static boolean isLegalLetter(char character, int keyval)
	{	
		for(int i = 0; i < legalLetters.length; i++)
		{
			if(legalLetters[i] == character)
			{
				return true;
			}
		}
		return false;
	}

}
