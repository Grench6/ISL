package Compiled;

public class WritelineInstruction extends Compiled
{
	private static final long serialVersionUID = 1L;
	public String writableString;

	@Override
	public String getInstructionID()
	{
		return "writeline";
	}

	@Override
	public Compiled factory(String[] words)
	{
		WritelineInstruction factored = new WritelineInstruction();
		if (words.length != 2)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a writeline instruction! " + words[0]);

		factored.writableString = words[1];
		if (!isStringWritable(factored.writableString))
			return new CompiledError(true, "Unwritable string! valid:(a-z A-Z 0-9)");
		return factored;
	}
}
