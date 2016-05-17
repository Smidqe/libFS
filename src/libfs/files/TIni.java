package libfs.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import libfs.debug.text.TTextDebug;
import libfs.debug.text.enums.e_TText;

public class TIni {
	private TFile file;
	
	private Pattern  __ptn_section  = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
	private Pattern  __ptn_key = Pattern.compile("\\s*([^=]*)=(.*)");
	private Map<String, Map<String, String>>  __entries  = new LinkedHashMap<>();

	public TIni(TFile file) throws IOException
	{
		this.file = file;
		load();
	}
	
	public TIni(String path, boolean create) throws IOException
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
			
			__matcher = __ptn_key.matcher(__line);
			if ((__section != null) && __matcher.matches())
			{
				String key = __matcher.group(1).trim();
				String value = __matcher.group(2).trim();
				
				__entries.putIfAbsent(__section, new LinkedHashMap<>());
				__entries.get(__section).put(key, value);
			}
		}
		
		__reader.close();
	}
	
	public void add(String section, String key, String value, boolean write) throws IOException
	{
		if (__entries.get(section) == null)
			__entries.putIfAbsent(section, new LinkedHashMap<String, String>());
		
		__entries.get(section).put(key, value);
		
		if (write)
			write();
	}
	
	public int line(String section, String key)
	{
		int index = 0;
		for (String s : __entries.keySet())
		{
			for (String ss : __entries.get(s).keySet())
			{
				if ((s.equals(section) && key.isEmpty()) || (ss.equals(key) && s.equals(section)))
					return index;

				index++;
			}
			
			index++;
		}
		
		return -1;
	}
	
	public String entry(String section, String key)
	{
		return __entries.get(section).get(key);
	}

	public String entry(String section, int index)
	{
		Map<String, String> values = __entries.get(section);
		
		String value = null;
		int i = 0;
		for (String key : values.keySet())
		{
			value = (index == i) ? values.get(key) : null;
					
			if (value != null)
				return value;
			
			i++;
		}
		
		return value;
	}
	
	public int readInt(String section, String key) throws NumberFormatException
	{
		return Integer.parseInt(entry(section, key));
	}
	
	public float readFloat(String section, String key) throws NumberFormatException
	{
		return Float.parseFloat(entry(section, key));
	}
	
	public void write() throws IOException
	{
		if (file.exists())
			file.recreate();
		
		for (String __section : __entries.keySet())
		{	
			file.write("[" + __section + "]", false);

			for (String __entry : __entries.get(__section).keySet())
				file.write(__entry + "=" + __entries.get(__section).get(__entry), false);
		}
	}
	
	public int count_keys()
	{
		int result = 0;
		
		for (String key : __entries.keySet())
			result += __entries.get(key).size();
		
		return result;
	}
	
	
	
	public int count_sections()
	{
		return this.__entries.size();
	}
	
	public Map<String, Map<String, String>> entries()
	{
		return this.__entries;
	}
	
	public void information() throws IOException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("INI file information: ", e_TText.HEADER);
		
		__debug.print("Name: " + file.getName());
		__debug.print("Path: " + file.getPath());
		__debug.print("Parent: " + file.getParent() + ", Folder: " + file.getParentFile().isDirectory());
		__debug.print("Created: " + file.created());
		__debug.print("Size: " + file.getSize());
		__debug.print("Last modified: " + file.modified());

		__debug.print("Sections(amount): " + count_sections());
		__debug.print("Keys(amount): " + count_keys());

		__debug.print("Sections: ", e_TText.HEADER);
		for (String key : __entries.keySet())
			__debug.print(key);
		__debug.print("", e_TText.FOOTER, true);
		
		__debug.print("Values: ", e_TText.HEADER);
		for (String section : __entries.keySet())
		{	
			__debug.print("Section: " + section, e_TText.HEADER);
			for (String key : __entries.get(section).keySet())
				__debug.print("Key: " + key + ", Value: " + __entries.get(section).get(key));
		
			__debug.print("", e_TText.FOOTER, true);
		}
		__debug.print("End of values", e_TText.FOOTER, false);	

		__debug.print("End of information.", e_TText.FOOTER);
	}
}
