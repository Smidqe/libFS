package files;


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
import java.util.ArrayList;

public class TFile extends File{
	private boolean read, write, binary;
	private String name, path, type;
	private OutputStream __writer;
	private InputStream __reader;

	private static final long serialVersionUID = 1L;

	public TFile(String pathname) throws FileNotFoundException
	{
		this(pathname, true);
	}
	
	public TFile(String pathname, boolean read) throws FileNotFoundException
	{
		super(pathname);
		
		this.path = pathname;
		this.name = getName();
		this.type = getExtension();
		
		set_method(!read);
	}

	public void set_method(boolean write) throws FileNotFoundException
	{
		this.read = !write;
		this.write = write;
		
		System.out.println("Write: " + this.write + ", Read: " + this.read);
		if (this.read)
			__reader = new FileInputStream(this.path);
		else
			__writer = new FileOutputStream(this.path);
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
	
	public OutputStream output()
	{
		return (OutputStream) __writer;
	}
	
	public InputStream input()
	{
		return (InputStream) __reader;
	}
	
	public boolean write(String s, boolean overwrite) throws IOException
	{
		if (!this.write || this.binary)
			return false;
		
		OutputStreamWriter w = new OutputStreamWriter(__writer);

		if (!overwrite)
			w.append(s);
		else
			w.write(s);

		return false;
	}
	
	public ArrayList<String> read() throws IOException
	{
		if (!this.read || this.binary)
			return null;
		
		ArrayList<String> lines = new ArrayList<String>();
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String __tmp;
			
			while ((__tmp = r.readLine()) != null)
				lines.add(__tmp);
			
			r.close();
		
		return lines;
	}
	
	public ArrayList<String> find(String sub) throws IOException
	{
		if (this.binary)
			return null;
		
		ArrayList<String> lines = read();
		ArrayList<String> result = new ArrayList<String>();
		
		for (String line : lines)
			if (line.contains(sub))
				result.add(line);
		
		return result;
	}
	
	public TFile convert(File file) throws FileNotFoundException
	{
		return new TFile(file.getAbsolutePath());
	}
	
	public ArrayList<TFile> convert(File[] listFiles) throws FileNotFoundException 
	{
		ArrayList<TFile> converted = new ArrayList<TFile>();
		
		for (File f : listFiles)
			converted.add(this.convert(f));
		
		return converted;
	}
}
