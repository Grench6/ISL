package Compiled;

public class ClickOffsetInstruction extends Compiled
{
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public boolean semicolon = false;

	@Override
	public String getInstructionID()
	{
		return "click_offset";
	}

	@Override
	public Compiled factory(String[] words)
	{
		ClickOffsetInstruction factored = new ClickOffsetInstruction();
		if (!(words.length == 3 || words.length == 4))
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a click_offset instruction! " + words[0]);

		if (words.length == 4)
			if (compare(words[3], ";"))
				factored.semicolon = true;
			else
				return new CompiledError(true, "Error! a semicolon was expected instead of " + words[3] + "");

		try
		{
			factored.x = Integer.parseInt(words[1]);
			factored.y = Integer.parseInt(words[2]);
		} catch (NumberFormatException e)
		{
			return new CompiledError(true, "Wrong number!");
		}
		return factored;
	}
}
