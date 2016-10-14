package libFileExtensions.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class TBinaryFile extends TFile{
	//what variables if any?
	//encoding?
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1009927929269442689L;

	public TBinaryFile(String pathname, boolean create) throws IOException {
		super(pathname, create);
		// TODO Auto-generated constructor stub
	}
	
	public void write(OutputStream stream, Object object) throws IOException
	{
		ObjectOutputStream __stream = new ObjectOutputStream(stream);
		
		__stream.writeObject(object);
		__stream.close();
	}
	
	public Object read(InputStream stream) throws IOException, ClassNotFoundException
	{
		ObjectInputStream oos = new ObjectInputStream(stream);
		
		Object obj = oos.readObject();
		
		oos.close();
		
		return obj;
	}
}
