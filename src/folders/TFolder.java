package folders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import files.*;

public class TFolder {
	private ArrayList<TFile> files;
	private String path, name;
	private boolean exists;
	
	public TFolder()
	{
		files = new ArrayList<TFile>();
		name = "";
	}
	
	public TFolder(String path) throws MalformedURLException, URISyntaxException
	{
		this();
		this.path = (new URL(path)).toURI().toString();
		this.exists = (new File((new URL(path)).toURI())).exists();
	}
	
	private void refresh() throws FileNotFoundException
	{
		String files[] = new TFile(this.path).list();
		
		if (this.files.size() > 0)
			this.files.clear();
		
		for (String s : files)
			this.files.add(new TFile(s));
	}
	
	private int count(boolean refresh) throws FileNotFoundException
	{
		if (refresh)
			refresh();
	
		return this.files.size();
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public boolean exists()
	{
		return this.exists;
	}
	
	public boolean search(String name, String path, boolean recursive) throws IOException, URISyntaxException
	{
		if (!exists())
			return false;

		for (TFile f : files)
			if (f.isDirectory() && recursive)
				(new TFolder(f.getCanonicalPath())).search(name, recursive);
		
		return false;
	}
	
	public boolean search(String name, boolean recursive) throws IOException, URISyntaxException
	{
		return search(name, this.path, recursive);
	}
	
	public int count() throws FileNotFoundException
	{
		return count(false);
	}
	
	public String name()
	{
		return this.name;
	}
	
	public ArrayList<TFile> getFiles()
	{
		return this.files;
	}
	
/*	
	public void print()
	{
		TTextDebug debug = TTextDebug.instance();
		
		debug.print("Folder: ", e_TText.HEADER);
		
		for (TFile file : files)
			debug.print("- " + file.getName(), e_TText.SUBSTRING);
		
		debug.print("\n", e_TText.FOOTER);
	}
*/
}
