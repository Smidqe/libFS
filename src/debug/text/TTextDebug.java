package debug.text;


/*
	- TTextDebug
		- Will be a singleton, since it will be used everywhere.
		- Will probably connect to multiple text sources.
		- Handles indentation
		- Handles writing to file if necessary. ???





*/
public class TTextDebug {
	private static TTextDebug __self = new TTextDebug();
	private int indentation;
	private String previous;
	
	public TTextDebug()
	{
		this.indentation = 0;
	}
	
	public void print(String txt, e_TText method, boolean suppress)
	{
		if (method == e_TText.SUBSTRING)
			indentation++;
		
		if (method == e_TText.FOOTER)
			indentation--;

		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < this.indentation; i++)
			builder.append("--");
		
		switch (method)
		{
			case WARNING:
			{
				builder.append("WARNING:");
				break;
			}
			
			case ERROR:
			{
				builder.append("ERROR:");
				break;
			}
			
			case CRITICAL:
			{
				builder.append("CRITICAL:");
				break;
			}
			default:
				break;
		}
		
		if (!suppress)
			System.out.println(this.previous = builder.append(" ").append(txt).toString());
		
		if (method == e_TText.HEADER)
			indentation++;
		
		if (method == e_TText.SUBSTRING)
			indentation--;
	}

	public String getString()
	{
		return this.previous;
	}

	public int getIndention()
	{
		return this.indentation;
	}

	public static TTextDebug instance() {
		return __self;
	}
}
