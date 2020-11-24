package Compiled;

import java.util.Arrays;

public class Reif extends Compiled
{
	private static final long serialVersionUID = 1L;
	public Compiled condition;

	@Override
	public String getInstructionID()
	{
		return "reif";
	}

	@Override
	public Compiled factory(String[] words)
	{
		Reif factored = new Reif();
		if (words.length < 2)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a reif structure! " + words[0]);

		Compiled[] conditionsFactories = { new ContainsCondition(), new NotContainsCondition() };
		boolean foundMatch = false;
		for (Compiled currentFactory : conditionsFactories)
		{
			if (words[1].compareTo(currentFactory.getInstructionID()) == 0)
			{
				factored.condition = currentFactory.factory(Arrays.copyOfRange(words, 1, words.length));
				if (factored != null)
					if (factored.getError() == null)
					{
						foundMatch = true;
						break;
					} else if (factored.getError().sureSyntaxError)
						break;
			}
		}
		if (!foundMatch)
			if (factored.getError() == null)
				return new CompiledError(true, "\"" + words[1] + "\" is not a valid condition!");
			else if (factored.getError().sureSyntaxError)
				return new CompiledError(true, factored.getError().errorMessage);
			else
				return new CompiledError(true, "\"" + words[1] + "\" is not a valid condition!");
		return factored;
	}
}