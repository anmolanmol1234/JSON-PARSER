
import java.io.*;
import java.util.*;
import java.lang.*;
import java.nio.charset.Charset;

enum enumException
{
  Opening_Bracket_Expected,
  Closing_Bracket_Expected,
  Opening_Quotes_Expected,
  Closing_Quotes_Expected,
  Colon_Expected,
  Opening_Square_Bracket_Expected,
  Closing_Square_Bracket_Expected,
  Unexpected_end_of_file,
  Boolean_expected,
  Expected
}

public class JSONPARSER{	
	static char[] input;
	static int ptr;	
	static boolean object() throws CustomException 
	{	
		int fallback = ptr;
		if(input[ptr++] != '{') {
			enumException e=enumException.Opening_Bracket_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);	
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		if((member()==false))
		{
			ptr = fallback;
			return false;
		}	
		if(input[ptr++]!='}') {
			enumException e=enumException.Closing_Bracket_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);	
			//return false;
		}
		return true;	
	}	
	private static boolean member() throws CustomException
	{
		int fallback = ptr;
		if(input[ptr]=='}')
		{
			return true;
		}
			else
			{
				if( (pair()==false))
				{
					ptr = fallback;
					return false;
				}
				if(input[ptr]==',')
				{		
					ptr=ptr+1;
					if(ptr>=input.length)
					{
						enumException e=enumException.Unexpected_end_of_file;
						throw new CustomException(e,ptr,input);	
					}
					
					if(member()==false)
					{
						ptr = fallback;
						return false;
					}
					return true;
				}
				else if(input[ptr]=='}')
				{
					return true;
				}
				else
				{
					enumException e=enumException.Expected;
					ptr = ptr-1;
					throw new CustomException(e,ptr,input);	
				}
			}
	}	
	private static boolean pair() throws CustomException
	{
		int fallback = ptr;
		if(string()==false)
		{
			ptr = fallback;
			return false;
		}
		if(input[ptr++]!=':')
		{
			enumException e=enumException.Colon_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		if(value()==false)
		{
			ptr = fallback;
			return false;
		}	
	return true;		
	}
	private static boolean value() throws CustomException{
		int fallback=ptr;
		while( input[ptr]>= '0' && input[ptr]<= '9' )
		{
			if(number()==false)
			{
				ptr = fallback;
				return false;
			}
		}
		if(input[ptr]=='"')
		{
			if(string()==false)
			{
				ptr = fallback;
				return false;
			}
			if(ptr>=input.length)
			{
				enumException e=enumException.Unexpected_end_of_file;
				throw new CustomException(e,ptr,input);	
		    }
			return true;
		}
		else if(input[ptr]=='{')
		{
			if(object()==false)
			{
				ptr = fallback;
				return false;
			}
			if(ptr>=input.length)
			{
				enumException e=enumException.Unexpected_end_of_file;
				throw new CustomException(e,ptr,input);
		    }
			return true;
		}
		else if(input[ptr]=='[')
		{
			if(array()==false)
			{
				ptr = fallback;
				return false;
			}
			if(ptr>=input.length)
			{
				enumException e=enumException.Unexpected_end_of_file;
				throw new CustomException(e,ptr,input);	
		    }
			return true;
		}
		else if((input[ptr]=='t') || (input[ptr]=='f')||(input[ptr]=='n'))
		{
			if(bool()==false)
			{

				enumException e=enumException.Boolean_expected;
				ptr = ptr-1;
				throw new CustomException(e,ptr,input);	
				//return false;
			}
			if(ptr>=input.length)
			{
				enumException e=enumException.Unexpected_end_of_file;
				throw new CustomException(e,ptr,input);	
		    }
			return true;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		return true;
	}
	private static boolean bool() throws CustomException
	{
		String str="";
		Character c1[]= {input[ptr],input[ptr+1],input[ptr+2],input[ptr+3],input[ptr+4]};
		for (Character c : c1)
		str+=c.toString();	
        if(str.contains("true") || str.contains("null"))
        {
		ptr=ptr+4;
		return true;
	    }
        else if(str.contains("false"))
	    {
		ptr=ptr+5;
		return true;
	    }
        else
        	{return false;}
	}
	private static boolean number() throws CustomException
	{	
			if(input[ptr]>='0'&&input[ptr]<='9')
			{
				ptr=ptr+1;
				if(ptr>=input.length)
				{
					enumException e=enumException.Unexpected_end_of_file;
					throw new CustomException(e,ptr,input);	
			    }
				return true;
			}		
		return false;
	}
	private static boolean string() throws CustomException
	{
		int fallback = ptr;
		if(input[ptr++]!= '"')
		{	enumException e=enumException.Opening_Quotes_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);	
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		if(character()==false)
		{
			ptr = fallback;
			return false;
		}
		
		if(input[ptr++]!= '"')
		{	enumException e=enumException.Closing_Quotes_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);	
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		return true;	
	}
	private static boolean character() throws CustomException{
		while(input[ptr]!='"')
		{	
			ptr=ptr+1;
			if(ptr>=input.length)
			{
				enumException e=enumException.Unexpected_end_of_file;
				throw new CustomException(e,ptr,input);	
		    }
		}	
		return true;
	}
	private static boolean array() throws CustomException
	{
		int fallback = ptr;
		if(input[ptr++]!= '[')
		{	enumException e=enumException.Opening_Square_Bracket_Expected;
			ptr = ptr-1;
			throw new CustomException(e,ptr,input);	
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		if(elements()==false)
		{
			ptr = fallback;
			return false;
		}
		if(input[ptr++]!= ']')
		{	enumException e=enumException.Closing_Square_Bracket_Expected;
			ptr=ptr-1;
			throw new CustomException(e,ptr,input);
			//return false;
		}
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
		return true;	
	}
	
	private static boolean elements() throws CustomException
	{
		int fallback=ptr;
		if((value()==false))
		{
			ptr = fallback;
			return false;
		}
		else
		{
		if(input[ptr]==',')
		{	ptr=ptr+1;
		if(ptr>=input.length)
		{
			enumException e=enumException.Unexpected_end_of_file;
			throw new CustomException(e,ptr,input);	
	    }
			if(elements()==false)
			{
				ptr = fallback;
				return false;
			}
			else
				{return true;}
	    }
		else
			{return true;}
		}
	}
	private static char[] handleFile(File file, Charset encoding)
            throws IOException 
	{
        try (InputStream in = new FileInputStream(file);
             Reader reader = new InputStreamReader(in, encoding);
             Reader buffer = new BufferedReader(reader)) 
        {
              return handleCharacters(buffer);
        }
    }
	
	private static char[] handleCharacters(Reader reader)
            throws IOException
	{
        int r;
        StringBuilder fileData = new StringBuilder(100000);
        while ((r = reader.read()) != -1)
        {
        	
            char ch = (char) r;
            if(!Character.isWhitespace(ch))
            {fileData.append(ch);}
        }
        return  fileData.toString().toCharArray();
    }
	public static void main(String args[])
	{
		 Charset encoding = Charset.defaultCharset();
		 String filename="myFile1";
	   	 File file = new File(filename);
          try 
          {
			input=handleFile(file, encoding);
		  } 
          catch (IOException e)
         {
			
			e.printStackTrace();
		 }
        if(input.length < 2)
        {
  			System.out.println("The input string is invalid.The length of valid string must be greater than 2");
  			System.exit(0);
  		} 
        for(int i=0;i<input.length;i++)
        {
        	System.out.print(input[i]);
        	
        }
        System.out.println("\n");
		ptr = 0;
		try
		{
		boolean isValid = object();
		if((isValid) & (ptr == input.length)) 
		{
			System.out.println("\n THE INPUT STRING IS VALID.");
		}
		else 
		{
			System.out.println("\n THE INPUT STRING IS INVALID.");
		}
	}
	catch(Exception e){}
}

}
class CustomException extends Exception
{
	CustomException(enumException e,int ptr,char [] input)
	{
		for(int i=0;i<ptr;i++)
		{
			System.out.print(input[i]);
		}
		System.out.println("\n");
		for(int i=0;i<ptr-1;i++)
		{
			System.out.print(".");
		}
		System.out.println("^");
		 ptr=ptr+1;
	switch(e)
	{
	  case Opening_Bracket_Expected:
          System.out.println("Opening ({) brackets were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Closing_Bracket_Expected:
          System.out.println("Closing (})  brackets were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Opening_Quotes_Expected:
          System.out.println("Opening Quotes (\") were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Closing_Quotes_Expected:
          System.out.println("Closing Quotes (\")  were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Opening_Square_Bracket_Expected:
          System.out.println("Opening Square Brackets ([)  were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Closing_Square_Bracket_Expected:
          System.out.println("Closing Square Brackets (])  were expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Colon_Expected:
          System.out.println("Colon (:)  was expected at position " + ptr + " instead of " + input[ptr-1]);
	  break;
	  case Unexpected_end_of_file:
		  System.out.println("Unexpected end of file reached " );
		  break;
	  case Boolean_expected:
		  System.out.println(" True false or null expected ") ;
		  break;
	  case Expected:
		  System.out.println("  Expected \",\" or \"}\" but found "+ input[ptr-1]);
		  break;
	  default:
           System.out.println("no");
	}
	}
}
