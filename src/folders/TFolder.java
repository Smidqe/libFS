package folders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	private void refresh() throws IOException
	{
		String files[] = new TFile(this.path, false).list();
		
		if (this.files.size() > 0)
			this.files.clear();
		
		for (String s : files)
			this.files.add(new TFile(s, false));
	}
	
	private int count(boolean refresh) throws IOException
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
	
	public ArrayList<Map<String, Map<TFile, String>>> search(String name, boolean recursive, ArrayList<URI> folders, ArrayList<Map<String, Map<TFile, String>>> found, int layer) throws IOException, URISyntaxException
	{
		if (!exists())
			return null;
		
		if ((folders == null) && recursive)
			folders = new ArrayList<URI>();
			
		for (TFile f : files)
		{
			if (f.isDirectory() && recursive)
				folders.add(f.toURI());
			else
				continue;
			
			if (f.getName().matches(name))
			{
				if (found.get(layer).get(this.name) == null)
					found.add(new HashMap<String, Map<TFile, String>>());
				
				found.get(layer).get(this.name).put(f, f.getAbsolutePath());
			}
		}

		if (recursive)
			return search(name, recursive, folders, found, layer++);
		else
			return found;
	}
	
	public ArrayList<Map<String, Map<TFile, String>>> search(String name, boolean recursive) throws IOException, URISyntaxException
	{
		return search(name, recursive, null, new ArrayList<>(), 0);
	}
	
	public int count() throws IOException
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
