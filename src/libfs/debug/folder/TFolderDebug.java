package libfs.debug.folder;

import java.io.IOException;
import java.net.URISyntaxException;

import libfs.debug.text.TTextDebug;
import libfs.debug.text.e_TText;
import libfs.files.TFile;
import libfs.folders.TFolder;

public class TFolderDebug {
	public void contents(TFolder folder, boolean subfolders) throws URISyntaxException, IOException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("FOLDER: " + folder.name(), e_TText.HEADER, false);
		
		for (TFile file : folder.getFiles())
		{
			if (file.isDirectory() && subfolders)
				contents(new TFolder(file.getAbsolutePath()), subfolders);
			
			if (file.isDirectory())
				__debug.print("D: " + file.getName(), e_TText.SUBSTRING, false);
			else
				__debug.print("F: " + file.getName(), e_TText.SUBSTRING, false);
		}
		__debug.print("", e_TText.FOOTER, false);
	}
	
	public void information(TFolder folder, boolean showFiles) throws IOException, URISyntaxException
	{
		TTextDebug __debug = TTextDebug.instance();
		
		__debug.print("FOLDER INFORMATION: ", e_TText.HEADER, false);
		
		__debug.print("Name: " + folder.name(), e_TText.SUBSTRING, false);
		__debug.print("Path: " + folder.getPath(), e_TText.SUBSTRING, false);
		__debug.print("Files: " + folder.count(true), e_TText.SUBSTRING, false);	
		__debug.print("Filetypes: " + folder.filetypes().toString(), e_TText.SUBSTRING, false);
		
		if (showFiles)
		{
			__debug.print("FILES IN THE FOLDER: ", e_TText.HEADER, false);
			
			for (TFile file : folder.getFiles())
				__debug.print(file.getName(), e_TText.SUBSTRING, false);
			
			__debug.print("END OF FILES", e_TText.FOOTER, false);
		}
		
		__debug.print("Directories: " + folder.subfolders().toString(), e_TText.SUBSTRING, false);
		__debug.print("END OF INFORMATION", e_TText.FOOTER, false);
	}
}
