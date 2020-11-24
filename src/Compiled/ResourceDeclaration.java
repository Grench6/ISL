package Compiled;

import java.io.File;

public class ResourceDeclaration extends Compiled
{
	private static final long serialVersionUID = 1L;
	public String varName;
	public File file;

	@Override
	public String getInstructionID()
	{
		return "@";
	}

	public Compiled factory(String[] words)
	{
		ResourceDeclaration factored = new ResourceDeclaration();
		if (words.length != 4)
			return new CompiledError(false, "Invalid syntax!");
		if (!compare(words[0], getInstructionID()))
			return new CompiledError(false, "This is not a resource declaration! " + words[0]);
		if (!isVariableCorrect(words[1]))
			return new CompiledError(true, "Invalid resource name! " + words[1]);
		if (!compare(words[2], "="))
			return new CompiledError(true, "No assignation was made in a resource declaration! (Missing '=')");
		factored.varName = words[1];
		factored.file = new File(words[3]);
		return factored;
	}
}
