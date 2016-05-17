package libfs.files;

/*
	>>TODO:
		- Create methods to read/write binary files
		- 

		- Change to nio?
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TFile extends File{
	private boolean read, write, binary;
	private String name, path, type;
	private OutputStream __writer;
	private InputStream __reader;

	private static final long serialVersionUID = 1L;

	public TFile(String pathname, boolean create) throws IOException
	{
		this(pathname, true, create);
	}
	
	public TFile(String pathname, boolean read, boolean create) throws IOException
	{
		super(pathname);

		if (create)
			this.createNewFile();
		
		this.path = this.getAbsolutePath();
		this.name = super.getName();
		this.type = getExtension();
		
		if (!isDirectory())
			set(!read);
	}
	
	public String getParent()
	{
		return super.getParent() + "\\";
	}
	
	public void set(boolean write) throws IOException
	{
		this.read = !write;
		this.write = write;
	}
	
	public String getExtension()
	{
		return this.type = this.name.substring(this.name.lastIndexOf(".") + 1);
	}
	
	public long getSize()
	{
		return this.length();
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public OutputStream output() throws FileNotFoundException
	{
		return __writer = new FileOutputStream(this.path);
	}
	
	public InputStream input() throws FileNotFoundException
	{
		return __reader = new FileInputStream(this.path);
	}
	
	private ArrayList<String> cnv_map(Map<Integer, String> map)
	{
		ArrayList<String> converted = new ArrayList<String>();
		
		for (Integer index : map.keySet())
			converted.add(map.get(index));
		
		return converted;
	}
	
	public boolean write(String s, int line, boolean replace) throws IOException
	{
		ArrayList<String> __lines = cnv_map(lines());
		
		if (__lines.size() < line || !this.exists() || this.isDirectory())
			return false;
		
		if (replace && (__lines.get(line) != null))
			__lines.set(line, s);
		else
			__lines.add(line, s);
			
		this.delete();
		this.createNewFile();
		
		this.write(__lines, false);
		
		return true;
	}
	
	public void write(String s, boolean overwrite) throws IOException
	{
		if (!this.write || this.binary)
			return;
		
		__writer = new FileOutputStream(this.path);

		OutputStreamWriter w = new OutputStreamWriter(__writer);

		if (!overwrite)
			w.append(s);
		else
			w.write(s);
		
		w.flush();
		w.close();
		__writer.close();
	}
	
	public void write(ArrayList<String> lines, boolean overwrite) throws IOException
	{
		for (String line : lines)
			write(line, overwrite);
	}
	
	public Map<Integer, String> lines() throws IOException
	{	
		if (!this.read || this.binary)
			return null;
	
		__reader = new FileInputStream(this.path);
		
		Map<Integer, String> values = new HashMap<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(__reader));
		
		int index = 0;
		String __tmp;
		
		while ((__tmp = reader.readLine()) != null)
			values.put(index++, __tmp);
		
		reader.close();
		__reader.close();
		
		return values;
	}
	
	public ArrayList<String> read() throws IOException
	{
		if (!this.read || this.binary)
			return null;
		
		Map<Integer, String> values = lines();
		ArrayList<String> lines = new ArrayList<String>();
		
		for (int index = 0; index < values.size(); index++)
			lines.add(values.get(index));
		
		return lines;
	}
	
	public Map<Integer, String> find(String sub) throws IOException
	{
		if (this.binary || !this.read)
			return null;
		
		Map<Integer, String> lines = lines();
		Map<Integer, String> result = new HashMap<>();

		for (int __line = 0; __line < lines.size(); __line++)
		{
			if (lines.get(__line).contains(sub))
				result.put(__line, lines.get(__line));
		
			__line++;
		}
		
		return result;
	}
	
	public FileTime created() throws IOException
	{
		return Files.readAttributes(this.toPath(), BasicFileAttributes.class).creationTime();
	}
	
	public FileTime modified() throws IOException
	{
		return Files.readAttributes(this.toPath(), BasicFileAttributes.class).lastModifiedTime();
	}
	
	public TFile copy(String dest, String __name) throws IOException
	{
		//binary files not supported yet
		if (binary || (dest.equals("") && this.name.equals(__name)))
			return null;

		TFile __new = new TFile(dest + __name, false);
		boolean success = false;
		
		if (dest.equals("") && !__name.equals(this.name))
		{
			Files.copy(this.toPath(), __new.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.delete(this.toPath());

			return __new;
		}
		else
			success = Files.copy(this.toPath(), __new.toPath()) != null;

		return success ? __new : null;
	}
	
	public TFile rename(String __name) throws IOException
	{
		return copy("", __name);
	}

	public TFile convert(File file) throws IOException
	{
		return new TFile(file.getAbsolutePath(), false);
	}
	
	public ArrayList<TFile> convert(File[] listFiles) throws IOException 
	{
		ArrayList<TFile> converted = new ArrayList<TFile>();
		
		for (File f : listFiles)
			converted.add(this.convert(f));
		
		return converted;
	}

	public void recreate() throws IOException {
		this.delete();
		this.createNewFile();
	}
}
