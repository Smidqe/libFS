package libfs.debug.text;

import java.io.IOException;

import libfs.debug.text.enums.e_TText;
import libfs.files.TFile;

public class TLog {
	private TFile file;
	private TTextDebug __debug = TTextDebug.instance();
	private boolean bShowTimeStamp;
	
	
	public TLog(TFile file)
	{
		this.file = file;
	}
	
	public TFile getFile()
	{
		return this.file;
	}
	
	public void write(String text, e_TText method, boolean suppress) throws IOException
	{
		__debug.print(text, method, suppress);
		
		file.write(__debug.getString(), false);
	}
	
	public void write(String text) throws IOException
	{
		write(text, e_TText.SUBSTRING, true);
	}

	public boolean timestamp() 
	{
		return bShowTimeStamp;
	}

	public void useTimeStamp(boolean bShowTimeStamp) 
	{
		this.bShowTimeStamp = bShowTimeStamp;
	}
}
