package libFileExtensions.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TTextFile extends TFile{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4388249078403402539L;

	public TTextFile(String pathname, boolean create) throws IOException {
		super(pathname, create);

	}
	
	public void write(String s, boolean overwrite) throws IOException
	{
		if (!this.write || this.binary)
			return;
		
		if (this.__output == null)
			this.__output = this.output(overwrite);

		OutputStreamWriter writer = new OutputStreamWriter(this.__output);

		writer.write(s);
		writer.close();
		
		this.close();
	}
	
	public void write(ArrayList<String> lines, boolean overwrite) throws IOException
	{
		for (String line : lines)
			write(line, overwrite);
	}
	
	public Map<Integer, String> lines() throws IOException
	{	
		if (!this.read || this.binary)
			return null;
	
		try {
			this.__input.close();
		} catch (Exception e) {
			System.out.println("Hello");
			this.__input = input();
		}
		
		Map<Integer, String> values = new HashMap<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(__input));
		
		int index = 0;
		String __tmp;
		
		while ((__tmp = reader.readLine()) != null)
			values.put(index++, __tmp);

		this.close();
		
		return values;
	}
	
	public ArrayList<String> read() throws IOException
	{
		if (!this.read || this.binary)
			return null;
		
		Map<Integer, String> values = lines();
		ArrayList<String> lines = new ArrayList<String>();
		
		for (int index = 0; index < values.size(); index++)
			lines.add(values.get(index));
		
		return lines;
	}
	
	public Map<Integer, String> find(String sub) throws IOException
	{
		if (this.binary || !this.read)
			return null;
		
		Map<Integer, String> lines = lines();
		Map<Integer, String> result = new HashMap<>();

		for (int __line = 0; __line < lines.size(); __line++)
		{
			if (lines.get(__line).contains(sub))
				result.put(__line, lines.get(__line));
		
			__line++;
		}
		
		return result;
	}
	
	@Override
	public TFile rename(String __name) throws IOException {
		// TODO Auto-generated method stub
		return super.rename(__name);
	}
	
	public boolean replaceLine(int line, String value) throws IOException
	{
		ArrayList<String> __lines = cnv_map(lines());
		
		if (__lines.size() < line || !this.exists() || this.isDirectory())
			return false;
		
		if (__lines.get(line) != null)
			__lines.set(line, value);
		else
			return false;
		
		this.delete();
		this.createNewFile();
		
		this.write(__lines, false);
		
		return true;
	}
	
	private ArrayList<String> cnv_map(Map<Integer, String> map)
	{
		ArrayList<String> converted = new ArrayList<String>();
		
		for (Integer index : map.keySet())
			converted.add(map.get(index));
		
		return converted;
	}
	

}
