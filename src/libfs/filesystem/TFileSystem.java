package libfs.filesystem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import libfs.files.TFile;
import libfs.folders.TFolder;

/*
 	TODO:
 		- Finalise the methods and clean these functions (perhaps split something to elsewhere?)
 		- Other functionalities?
 			- Moving multiple files/folders
 			- Remove multiple files/folders
 			- Find certain folders/files
 			- 
 
 
 
 
 */

public class TFileSystem {
	private static TFileSystem __self = new TFileSystem();
	private ArrayList<TFile> files;
	private ArrayList<TFolder> folders;
	
	public TFileSystem()
	{
		files = new ArrayList<TFile>();
		folders = new ArrayList<TFolder>();
	}

	public boolean add(String filename, boolean directory, boolean create) throws IOException, URISyntaxException
	{
		TFile file = new TFile(filename, create);

		if (file.exists() && !create)
			files.add(file);
		else
			file.createNewFile();
		
		if (directory && create)
		{
			file.mkdir();
			folders.add(new TFolder(file.getAbsolutePath(), false));
		}

		return file.exists();
	}
	
	public ArrayList<TFile> getFiles()
	{
		return this.files;
	}
	
	public ArrayList<TFolder> getFolders()
	{
		return this.folders;
	}
	
	public TFolder copy(TFolder folder, String dest, boolean subfolders, String name) throws IOException, URISyntaxException
	{
		if ((dest.equals("") && folder.getName().equals(name)))
			return null;

		TFolder __new = new TFolder(dest + name, false);
		boolean success = false;
		
		if (dest.equals("") && !name.equals(folder.getName()))
		{
			Files.copy(folder.path(), __new.path(), StandardCopyOption.REPLACE_EXISTING);
			Files.delete(folder.path());

			return __new;
		}
		else
			success = Files.copy(folder.path(), __new.path()) != null;

		return success ? __new : null;
	}
	
	public TFolder rename(TFolder folder, String name) throws IOException, URISyntaxException
	{
		return this.copy(folder, "", false, name);
	}
	
	public boolean add(ArrayList<String> filenames, boolean directory, boolean create) throws IOException, URISyntaxException
	{
		int size = files.size();
		
		for (int i = 0; i < filenames.size(); i++)
			add(filenames.get(i), directory, create);
		
		if (!directory)
			return files.size() > size ? true : false;
		else
			return folders.size() > size ? true : false;
	}
	
	public TFolder getFolder(int index)
	{
		return folders.get(index);
	}

	public TFile getFile(int index)
	{
		return files.get(index);
	}
	
	public ArrayList<TFile> getFiles(int index, String extension)
	{
		ArrayList<TFile> files = getFiles();
		
		if (files == null || files.size() == 0)
			return null;
		
		ArrayList<TFile> filtered = new ArrayList<TFile>();
		for (TFile f : files)
			if (f.getType().equals(extension))
				filtered.add(f);
		
		return filtered;
	}

	public int fileCount()
	{
		return this.files.size();
	}
	
	public int folderCount()
	{
		return this.folders.size();
	}
	
	public static TFileSystem instance() {
		return __self;
	}
}
