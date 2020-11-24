package Compiled;

public class CompiledError extends Compiled
{
	private static final long serialVersionUID = 1L;
	public boolean sureSyntaxError = false;
	public String errorMessage;

	@Override
	public CompiledError getError()
	{
		return this;
	}

	public CompiledError(boolean sureSyntaxError, String errorMessage)
	{
		this.sureSyntaxError = sureSyntaxError;
		this.errorMessage = errorMessage;
	}

	@Override
	public String getInstructionID()
	{
		return null;
	}

	@Override
	public Compiled factory(String[] words)
	{
		return null;
	}

}
