package libFileExtensions.debug.text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import libFileExtensions.debug.text.TTextDebug.TText;
import libFileExtensions.files.TTextFile;

public class TLog {
	private TTextFile file;
	
	private TTextDebug __debug = TTextDebug.instance();
	private boolean bShowTimeStamp, bDebugOnConsole;
	
	
	public TLog(TTextFile file)
	{
		this.file = file;
	}
	
	public TLog(String path, boolean create) throws IOException
	{
		this.file = new TTextFile(path, create);
	}
	
	public TLog(String path) throws IOException
	{
		this(path, true);
	}
	
	public TTextFile getFile()
	{
		return this.file;
	}
	
	public void write(String text, TText method, boolean suppress) throws IOException
	{
		__debug.print(bShowTimeStamp ? (new SimpleDateFormat("HH.mm.ss dd/MM/yyyy")).format(new Date()) + text : text, method, suppress);
		
		file.write(__debug.getString(), false);
	}
	
	public void write(String text) throws IOException
	{
		write(text, TText.SUBSTRING, bDebugOnConsole);
	}

	public boolean useTimestamp() 
	{
		return bShowTimeStamp;
	}

	public void setTimeStamp(boolean bShowTimeStamp) 
	{
		this.bShowTimeStamp = bShowTimeStamp;
	}
	
	public boolean debugOnConsole()
	{
		return this.bDebugOnConsole;
	}
	
	public void setDebugOnConsole(boolean bDebugOnConsole)
	{
		this.bDebugOnConsole = bDebugOnConsole;
	}
}
