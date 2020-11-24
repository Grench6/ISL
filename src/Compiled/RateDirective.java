package Compiled;

public class RateDirective extends Compiled
{
	private static final long serialVersionUID = 1L;
	public int millis;

	@Override
	public String getInstructionID()
	{
		return "rate";
	}

	@Override
	public Compiled factory(String[] words)
	{
		RateDirective factored = new RateDirective();
		if (words.length != 3)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a rate command! " + words[0]);
		if (!compare(words[1], "="))
			return new CompiledError(true, "Directive without assignation! (missing '=')" + words[1]);
		try
		{
			factored.millis = Integer.parseInt(words[2]);
		} catch (NumberFormatException e)
		{
			return new CompiledError(true, "Wrong number!");
		}
		if (factored.millis < 0)
			return new CompiledError(true, "Sleep millis must be a positive number!");
		return factored;
	}

}
