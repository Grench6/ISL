package Compiled;

public class SleepCommand extends Compiled
{
	private static final long serialVersionUID = 1L;
	public int millis;

	@Override
	public String getInstructionID()
	{
		return "sleep";
	}

	@Override
	public Compiled factory(String[] words)
	{
		SleepCommand factored = new SleepCommand();
		if (words.length != 2)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a sleep command! " + words[0]);
		try
		{
			factored.millis = Integer.parseInt(words[1]);
		} catch (NumberFormatException e)
		{
			return new CompiledError(true, "Wrong number!");
		}
		if (factored.millis < 0)
			return new CompiledError(true, "Sleep millis must be a positive number!");
		return factored;
	}

}
