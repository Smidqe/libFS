package folders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
		this.path = (new File(path).getAbsolutePath());
		this.exists = (new File(path).exists());
	}
	
	private void refresh() throws IOException
	{
		File[] files = (new TFile(this.path, false)).listFiles();

		if (this.files.size() > 0)
			this.files.clear();
		
		for (File s : files)
			this.files.add(new TFile(s.getAbsolutePath(), false));
	}
	
	public int count(boolean refresh) throws IOException
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
		return this.path;
	}
	
	public boolean exists()
	{
		return this.exists;
	}
	
	//not finished yet... needs a logic upgrade
	public Map<String, Map<String, String>> search(String name, TFolder current, boolean subfolders, Map<String, Map<String, String>> found) throws IOException, URISyntaxException
	{
		if (!exists() || current.count(true) == 0)
			return found;
		
		for (TFile f : current.files)
		{
			if (f.isDirectory() && subfolders)
				search(name, new TFolder(f.getAbsolutePath()), subfolders, found);
			
			if (f.getName().contains(name))
			{
				if (found.get(current.path) == null)
					found.put(current.path, new HashMap<>());
				
				found.get(current.path).put(f.getAbsolutePath(), f.getName());
			}
		}

		return found;
	}
	
	public Map<String, Map<String, String>> search(String name, boolean subfolders) throws IOException, URISyntaxException
	{
		return search(name, this, subfolders, new HashMap<>());
	}
	
	public ArrayList<TFolder> subfolders()
	{
		return null;
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
