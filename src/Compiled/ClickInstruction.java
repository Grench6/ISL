package Compiled;

public class ClickInstruction extends Compiled
{
	private static final long serialVersionUID = 1L;
	public String varName;
	public boolean semicolon = false;

	@Override
	public String getInstructionID()
	{
		return "click";
	}

	@Override
	public Compiled factory(String[] words)
	{
		ClickInstruction factored = new ClickInstruction();
		if (!(words.length == 2 || words.length == 3))
			return new CompiledError(false, " Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a click with resources instruction! " + words[0]);
		if (words.length == 3)
			if (compare(words[2], ";"))
				factored.semicolon = true;
			else
				return new CompiledError(true, "Error! a semicolon was expected instead of " + words[2] + "");
		factored.varName = words[1];

		return factored;
	}
}
