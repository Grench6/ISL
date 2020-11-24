package Compiled;

public class ClickCoordInstruction extends Compiled
{
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;

	@Override
	public String getInstructionID()
	{
		return "click_coordinate";
	}

	@Override
	public Compiled factory(String[] words)
	{
		ClickCoordInstruction factored = new ClickCoordInstruction();
		if (words.length != 3)
		{
			return new CompiledError(false, "Invalid syntax!");
		}
		if (!compare(words[0], getInstructionID()))
		{
			return new CompiledError(false, "This is not a click_coordinates instruction! " + words[0]);
		}

		try
		{
			factored.x = Integer.parseInt(words[1]);
			factored.y = Integer.parseInt(words[2]);
		} catch (NumberFormatException e)
		{
			return new CompiledError(true, "Wrong number!");
		}
		if (factored.x < 0 || factored.y < 0)
			return new CompiledError(true, "Coordinates must be a positive number!");
		return factored;
	}
}
