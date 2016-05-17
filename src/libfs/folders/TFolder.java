package libfs.folders;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import libfs.debug.text.TTextDebug;
import libfs.debug.text.enums.e_TText;
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
	
	public TFolder(String path, boolean create) throws URISyntaxException, IOException
	{
		this();
		this.path = (new File(path).getAbsolutePath());
		this.exists = (new File(path).exists());
		this.name = (new File(path).getName());
		
		if (create && !exists)
			Files.createDirectory(new File(path).toPath());

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
	
	public Path path()
	{
		return new File(this.path).toPath();
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
				search(name, new TFolder(f.getAbsolutePath(), false), subfolders, found);
			
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

		return new TFolder(file.getAbsolutePath(), false);
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
				folders.add(new TFolder(file.getAbsolutePath(), false));
		
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
		information(showFiles);
	}
	
	public void print() throws IOException, URISyntaxException
	{
		print(false);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	public void contents(TFolder folder, boolean subfolders) throws URISyntaxException, IOException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("FOLDER: " + folder.name(), e_TText.HEADER, false);
		
		for (TFile file : folder.getFiles())
		{
			if (file.isDirectory() && subfolders)
				contents(new TFolder(file.getAbsolutePath(), false), subfolders);
			
			if (file.isDirectory())
				__debug.print("D: " + file.getName(), e_TText.SUBSTRING, false);
			else
				__debug.print("F: " + file.getName(), e_TText.SUBSTRING, false);
		}
		__debug.print("", e_TText.FOOTER, false);
	}
	
	public void information(boolean showFiles) throws IOException, URISyntaxException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("FOLDER INFORMATION: ", e_TText.HEADER, false);
		
		__debug.print("Name: " + this.name(), e_TText.SUBSTRING, false);
		__debug.print("Path: " + this.getPath(), e_TText.SUBSTRING, false);
		__debug.print("Files: " + this.count(true), e_TText.SUBSTRING, false);	
		__debug.print("Filetypes: " + this.filetypes().toString(), e_TText.SUBSTRING, false);
		
		if (showFiles)
		{
			__debug.print("FILES IN THE FOLDER: ", e_TText.HEADER, false);
			
			for (TFile file : this.getFiles())
				__debug.print(file.getName(), e_TText.SUBSTRING, false);
			
			__debug.print("END OF FILES", e_TText.FOOTER, false);
		}
		
		__debug.print("Directories: " + this.subfolders().toString(), e_TText.SUBSTRING, false);
		__debug.print("END OF INFORMATION", e_TText.FOOTER, false);
	}
}
