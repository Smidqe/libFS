package filesystem;

import java.io.IOException;
import java.util.ArrayList;


import files.*;
import folders.*;

/*
 	TODO:
 		- Finalise the methods and clean these functions (perhaps split something to elsewhere?)
 		
 
 
 
 
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

	public boolean add(String filename, boolean directory) throws IOException
	{
		TFile file = new TFile(filename, false);

		if (file.exists())
			files.add(file);
		
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
	
	public boolean add(ArrayList<String> filenames, boolean directory) throws IOException
	{
		int size = files.size();
		
		for (int i = 0; i < filenames.size(); i++)
			add(filenames.get(i), directory);
		
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

	public static TFileSystem instance() {
		return __self;
	}
}
