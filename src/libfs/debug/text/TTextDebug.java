package libfs.debug.text;

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

		switch (method)
		{
			case WARNING:
			{
				txt = "WARNING: " + txt;
				break;
			}
			
			case ERROR:
			{
				txt = "ERROR: " + txt;
				break;
			}
			
			case CRITICAL:
			{
				txt = "CRITICAL: " + txt;
				break;
			}
			
			case FOOTER:
				indentation--; 
				break;

			default:
				break;
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < this.indentation; i++)
			builder.append("--");
		
		if (indentation > 0)
			builder.append(" ");
		
		if (method == e_TText.HEADER)
			indentation++;
		
		if (!suppress)
			System.out.println(this.previous = builder.append(txt).toString());
		else
			this.previous = builder.append(txt).toString();
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
