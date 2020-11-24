package Compiled;

import java.io.Serializable;

public abstract class Compiled implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int lineNumberInPlainScript = -1;
	
	public abstract String getInstructionID();
	public abstract Compiled factory(String[] words);

	public CompiledError getError()
	{
		return null;
	}

	protected static boolean compare(String a, String b)
	{
		if (a.compareTo(b) == 0)
			return true;
		return false;
	}

	protected static boolean compare(char a, char b)
	{
		if (a == b)
			return true;
		return false;
	}

	// private static char[] validChars =
	// "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_".toCharArray();
	private static String validVariableChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_";
	private static String validWritable = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
	private static String[] reservedNames = { "rate", "reif", "sleep", "writeline", "click_coordinate", "contains", "not_contains", "click", "click_offset" };
	@SuppressWarnings("unused")
	private static String[] specialCharacters = { "@", "#", ";" };

	protected static boolean isVariableCorrect(String varname)
	{
		for (int i = 0; i < varname.length(); i++)
			if (!validVariableChars.contains("" + varname.charAt(i)))
				return false;
		for (String current : reservedNames)
			if (compare(current, varname))
				return false;
		return true;
	}

	protected static boolean isStringWritable(String str)
	{
		for (int i = 0; i < str.length(); i++)
			if (!validWritable.contains("" + str.charAt(i)))
				return false;
		return true;
	}

	/*protected static void oldSemicolonApproach(String[] words)
	{
		//Very old, I never used this
		if (words.length == 0)
			return false;
		String lastWord = words[words.length - 1];
		if (compare(lastWord, ";") || compare(lastWord.charAt(lastWord.length() - 1), ';'))
			return true;
		return false;
		
		//This is the good one
		if (words.length == 3)
		{
			if (compare(words[2], ";"))
			{
				factored.semicolon = true;
			} else
			{
				return new CompiledError(true, "Error! a semicolon was expected instead of " + words[2] + "");
			}
		} else
		{
			if (compare(words[1].charAt(words[1].length() - 1), ';'))
			{
				factored.semicolon = true;
				words[1] = words[1].substring(0, words[1].length() - 1);
			}
		}
	}*/
}
