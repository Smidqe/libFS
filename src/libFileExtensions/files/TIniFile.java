package libFileExtensions.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import libFileExtensions.debug.text.TTextDebug;
import libFileExtensions.debug.text.TTextDebug.TText;

public class TIniFile extends TTextFile{

	private static final long serialVersionUID = 1L;
	
	private Pattern  __ptn_section  = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
	private Pattern  __ptn_key = Pattern.compile("\\s*([^=]*)=(.*)");
	private Map<String, Map<String, String>>  __entries  = new LinkedHashMap<>();
	
	public TIniFile(String path, boolean create) throws IOException
	{
		super(path, create);
		load();
	}
	
	private void load() throws IOException
	{
		if (!this.exists() || !this.isReadable())
			return;

		BufferedReader __reader = new BufferedReader(new InputStreamReader(this.input()));
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
				
				__entries.putIfAbsent(__section, new LinkedHashMap<String, String>()); //we need to make sure that all the entries are in correct order and not shuffled (which happens with hashing)
				__entries.get(__section).put(key, value);
			}
		}
		
		__reader.close();
	}
	
	public Set<String> getSections()
	{
		return __entries.keySet();
	}
	
	public List<Set<String>> getEntries()
	{
		List<Set<String>> list = new ArrayList<Set<String>>();
		
		for (String value : __entries.keySet())
			list.add(__entries.get(value).keySet());
		
		return list;
	}
	
	public void set(String section, String key, String value) throws IOException
	{	
		if (__entries.get(section) == null)
			return;
		
		if (__entries.get(section).get(key) == null)
			return;
		
		__entries.get(section).replace(key, value);
	}
	
	public void set(String section, LinkedHashMap<String, String> sub)
	{
		__entries.replace(section, sub);
	}
	
	public void add(String section)
	{
		__entries.put(section, new LinkedHashMap<String, String>());
	}
	
	public void add(String section, String key, String value) throws IOException
	{
		if (__entries.get(section) == null)
			__entries.putIfAbsent(section, new LinkedHashMap<String, String>());
		
		__entries.get(section).put(key, value);
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
	
	public int getInt(String section, String key) throws NumberFormatException
	{
		return Integer.parseInt(entry(section, key));
	}
	
	public float getFloat(String section, String key) throws NumberFormatException
	{
		return Float.parseFloat(entry(section, key));
	}
	
	public void write() throws IOException
	{
		if (!this.isWritable())
			return;
		
		if (exists())
			recreate();
		
		for (String __section : __entries.keySet())
		{	
			write("[" + __section + "]\n", false);

			for (String __entry : __entries.get(__section).keySet())
				write(__entry + "=" + __entries.get(__section).get(__entry) + "\n", false);
		}
	}
	
	public int keys()
	{
		int result = 0;
		
		for (String section : __entries.keySet())
			result += __entries.get(section).size();
		
		return result;
	}

	public int sections()
	{
		return this.__entries.size();
	}
	
	public Map<String, Map<String, String>> get()
	{
		return this.__entries;
	}
	
	public void information() throws IOException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("INI file information: ", TText.HEADER);
		
		__debug.print("Name: " + getName());
		__debug.print("Path: " + getPath());
		__debug.print("Parent: " + getParent() + ", Folder: " + getParentFile().isDirectory());
		__debug.print("Created: " + created());
		__debug.print("Size: " + getSize());
		__debug.print("Last modified: " + modified());

		__debug.print("Sections(amount): " + sections());
		__debug.print("Keys(amount): " + keys());

		__debug.print("Sections: ", TText.HEADER);
		for (String key : __entries.keySet())
			__debug.print(key);
		__debug.print("", TText.FOOTER, true);
		
		__debug.print("Values: ", TText.HEADER);
		for (String section : __entries.keySet())
		{	
			__debug.print("Section: " + section, TText.HEADER);
			for (String key : __entries.get(section).keySet())
				__debug.print("Key: " + key + ", Value: " + __entries.get(section).get(key));
		
			__debug.print("", TText.FOOTER, true);
		}
		__debug.print("End of values", TText.FOOTER, false);	

		__debug.print("End of information.", TText.FOOTER);
	}
}
