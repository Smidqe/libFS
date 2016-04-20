package libfs.folders;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libfs.debug.folder.TFolderDebug;
import libfs.files.TFile;

public class TFolder {
	private ArrayList<TFile> files;
	private String path, name;
	private boolean exists;
	
	public TFolder()
	{
		files = new ArrayList<TFile>();
		name = "";
	}
	
	public TFolder(String path) throws URISyntaxException, IOException
	{
		this();
		this.path = (new File(path).getAbsolutePath());
		this.exists = (new File(path).exists());
		this.name = (new File(path).getName());
		
		refresh();
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
	
	public TFolder convert(TFile file) throws URISyntaxException, IOException
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
	
	public ArrayList<TFolder> getSubfolders() throws URISyntaxException, IOException
	{
		ArrayList<TFolder> folders = new ArrayList<TFolder>();
		for (TFile file : files)
			if (file.isDirectory())
				folders.add(new TFolder(file.getAbsolutePath()));
		
		return folders;
	}
	
	public ArrayList<String> subfolders() throws URISyntaxException, IOException
	{
		ArrayList<TFolder> folders = getSubfolders();
		ArrayList<String> names = new ArrayList<String>();
		
		for (TFolder folder : folders)
			names.add(folder.name);
		
		return names;
	}
	
	public ArrayList<String> filetypes()
	{
		if (files.size() == 0)
			return null;
		
		ArrayList<String> extensions = new ArrayList<String>();
		for (TFile file : files)
		{
			if (file.isDirectory())
				continue;
			
			if (extensions.indexOf(file.getExtension()) == -1)
				extensions.add(file.getExtension());
		}
		return extensions;
	}
	
	public void print(boolean showFiles) throws IOException, URISyntaxException
	{
		(new TFolderDebug()).information(this, showFiles);
	}
	
	public void print() throws IOException, URISyntaxException
	{
		print(false);
	}
}
