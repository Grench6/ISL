package Compiled;

public class EndReif extends Compiled
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getInstructionID()
	{
		return "end_reif";
	}

	@Override
	public Compiled factory(String[] words)
	{
		EndReif factored = new EndReif();
		if (words.length < 1)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a end_reif structure! " + words[0]);
		return factored;
	}
}