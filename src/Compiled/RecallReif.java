package Compiled;

public class RecallReif extends Compiled
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getInstructionID()
	{
		return "recall";
	}

	@Override
	public Compiled factory(String[] words)
	{
		RecallReif factored = new RecallReif();
		if (words.length < 1)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a end_reif structure! " + words[0]);
		return factored;
	}
}