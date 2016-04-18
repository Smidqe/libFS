package debug.file;

import java.io.IOException;

import files.TFile;
import debug.text.TTextDebug;
import debug.text.e_TText;

public class TFileDebug {
	private TFile file;

	public TFileDebug(String path) throws IOException
	{	
		this.file = new TFile(path, false);
	}

	public TFileDebug(TFile file)
	{
		this.file = file;
	}
	
	public void debug(String text, e_TText method) throws IOException
	{
		TTextDebug debug = TTextDebug.instance();
		
		debug.print(text, method, true);
		String str = debug.getString();
		
		file.write(str, false);
	}
	
	public void debug(String text) throws IOException
	{
		debug(text, e_TText.SUBSTRING);
	}
	
	public TFile getFile()
	{
		return this.file;
	}
}
