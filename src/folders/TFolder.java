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
	
	public ArrayList<TFile> subfolders()
	{
		ArrayList<TFile> result = new ArrayList<TFile>();
		
		for (TFile f : files)
			if (f.isDirectory())
				result.add(f);
		
		return result;
	}
	
	public TFolder convert(TFile file) throws MalformedURLException, URISyntaxException
	{
		if (!file.isDirectory())
			return null;

		return new TFolder(file.getAbsolutePath());
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
	
	
	public String print()
	{	
		StringBuilder __builder = new StringBuilder();
		
		__builder.append("FOLDER: " + this.name + "\n");
		for (TFile file : files)
			__builder.append("  - " + file.getName() + "\n");
		
		__builder.append("");
		
		return __builder.toString();
	}
	
	public String print(boolean subfolders)
	{
		if (!subfolders)
			return print();
		
		return null;
	}
}
