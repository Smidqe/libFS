package libfs.files;

import java.io.BufferedReader;
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

	TIni(TFile file) throws IOException
	{
		this.file = file;
		load();
	}
	
	TIni(String path, boolean create) throws IOException
	{
		this(new TFile(path, false));
	}
	
	private void load() throws IOException
	{
		if (!this.file.exists())
			return;

		BufferedReader __reader = new BufferedReader(new InputStreamReader(this.file.input()));
		String __line = null;
		String __section = null;
		
		while ((__line = __reader.readLine()) != null) 
		{
			Matcher __matcher = __ptn_section.matcher(__line);
			
			if (__matcher.matches())
				__section = __matcher.group(1).trim();
			
			if ((__section != null) && __ptn_key.matcher(__line).matches())
			{
				String key = __matcher.group(1).trim();
				String value = __matcher.group(2).trim();
				
				if (__entries.get(__section) == null)
					__entries.put(__section, new HashMap<>());
				
				__entries.get(__section).put(key, value);
			}
		}
	}
	
	public void add(String section, String key, String value, boolean write) throws IOException
	{
		if (__entries.get(section) == null)
			return;
		
		__entries.get(section).put(key, value);
		
		if (write)
			write(section, key, value, false);
	}
	
	public String entry(String section, String key)
	{
		return __entries.get(section).get(key);
	}

	public int readInt(String section, String key) throws NumberFormatException
	{
		return Integer.parseInt(entry(section, key));
	}
	
	public float readFloat(String section, String key) throws NumberFormatException
	{
		return Float.parseFloat(entry(section, key));
	}
	
	public boolean write(String section, String key, String value, boolean replace) throws IOException
	{
		if (replace)
		{
			file.write("", true);
			
			for (String __section : __entries.keySet())
			{
				file.write("[" + __section + "]", false);
				
				for (String __key : __entries.get(__section).keySet())
					file.write(__entries.get(__section).get(__key), false);
			}
		}
		
		return false;
	}
	
	public Map<String, Map<String, String>> entries()
	{
		return this.__entries;
	}
}
