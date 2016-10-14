package libFileExtensions.files;

/*
	>>TODO:
		- Create methods to read/write binary files
		- 

		- Change to nio?
 */

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TFile extends File{
	protected boolean read;
	protected boolean write;
	protected boolean binary;
	protected String name, path, type;
	protected OutputStream __output;
	protected InputStream __input;

	
	private List<FileChannel> __channels;
	
	private static final long serialVersionUID = 1L;

	public TFile(String pathname, boolean create) throws IOException
	{
		this(pathname, true, create);
	}
	
	public TFile(String pathname, boolean read, boolean create) throws IOException
	{
		super(pathname);

		if (create && !exists())
			this.createNewFile();
		
		this.path = this.getAbsolutePath();
		this.name = super.getName();
		this.type = getExtension();
		
		if (!isDirectory())
			mode(!read);
	}
	
	public String getParent()
	{
		return super.getParent() + "\\";
	}
	
	public void mode(boolean write) throws IOException
	{
		this.read = !write;
		this.write = write;
	}
	
	public boolean isReadable()
	{
		return this.read;
	}

	public boolean isWritable()
	{
		return this.write;
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
	
	public InputStream input() throws FileNotFoundException
	{
		System.out.println("TFILE - Input created");
		
		return new FileInputStream(this.path);
	}
	
	public OutputStream output(boolean overwrite) throws FileNotFoundException
	{
		return new FileOutputStream(this.path, !overwrite);
	}
	
	public OutputStream output() throws FileNotFoundException
	{
		return output(false);
	}

	//this is just horrible, fix this one!
	public void close() throws IOException
	{
		if (__output != null)
			__output.close();
		
		if (__input != null)
			__input.close();

		__input = null;
		__output = null;
	}

	public InputStream getInput()
	{
		return this.__input;
	}
	
	public OutputStream getOutput()
	{
		return this.__output;
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
		
		//check if we want to rename the file. 
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

	public static TFile convert(File file) throws IOException
	{
		return new TFile(file.getAbsolutePath(), false);
	}
	
	public static ArrayList<TFile> convert(File[] listFiles) throws IOException 
	{
		ArrayList<TFile> converted = new ArrayList<TFile>();
		
		for (File f : listFiles)
			converted.add(convert(f));
		
		return converted;
	}

	public void recreate() throws IOException {
		this.delete();
		this.createNewFile();
	}
}
