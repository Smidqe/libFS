package files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TIni {
	private TFile file;
	   private Pattern  __ptn_section  = Pattern.compile( "\\s*\\[([^]]*)\\]\\s*" );
	   private Pattern  __ptn_key = Pattern.compile( "\\s*([^=]*)=(.*)" );
	   private Map<String, Map<String, String>>  __entries  = new HashMap<>();

	TIni(TFile file)
	{
		this.file = file;
	}
	
	TIni(String path) throws IOException
	{
		this.file = new TFile(path);
		
		load();
	}
	
	private void load() throws IOException
	{
		if (!file.exists())
			return;

		BufferedReader __reader = new BufferedReader(new InputStreamReader(file.input()));
		String __line = null;
		String __section = null;
		
		while ((__line = __reader.readLine()) != null) 
		{
			Matcher __matcher = __ptn_section.matcher(__line);
			
			__section = __matcher.matches() ? __matcher.group(1).trim() : null;
			
			if (__section != null)
			{
				
			}
		}
	}
	
	public String read(String section, String key) throws IOException
	{
		return null;

	}

	public int readInt(String section, String key) throws NumberFormatException, IOException
	{
		return Integer.parseInt(read(section, key));
	}
	
	public float readFloat(String section, String key) throws NumberFormatException, IOException
	{
		return Float.parseFloat(read(section, key));
	}
	
	public boolean write(String section, String key, String value)
	{
		
		
		
		return false;
	}
	
	
}
