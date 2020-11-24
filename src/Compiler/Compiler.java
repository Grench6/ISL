package Compiler;

import java.util.ArrayList;

import Compiled.ClickCoordInstruction;
import Compiled.ClickInstruction;
import Compiled.ClickOffsetInstruction;
import Compiled.Compiled;
import Compiled.CompiledError;
import Compiled.ContainsCondition;
import Compiled.NotContainsCondition;
import Compiled.RateDirective;
import Compiled.RecallReif;
import Compiled.Reif;
import Compiled.ResourceDeclaration;
import Compiled.SleepCommand;
import Compiled.WritelineInstruction;

public class Compiler
{
	private static Compiled[] compileFactories = { new ClickCoordInstruction(), new ClickInstruction(), new ClickOffsetInstruction(), new ContainsCondition(),
			new NotContainsCondition(), new WritelineInstruction(), new RateDirective(), new Reif(), new ResourceDeclaration(), new RecallReif(), new SleepCommand() };

	public static Compiled compileLine(String line)
	{
		if (line == null)
			return null;
		if (line.length() == 0)
			return null;
		String[] words = getWordsInLine(line);
		if (words == null)
			return null;
		if (words.length == 0)
			return null;
		if (words[0].length() == 0)
			return null;
		Compiled compiled = null;
		for (Compiled current : compileFactories)
		{
			compiled = current.factory(words);
			if (compiled != null)
			{
				CompiledError error = compiled.getError();
				if (error == null)
					break;
				if (error.sureSyntaxError)
				{
					System.err.println(error.errorMessage);
					break;
				}
			}
		}
		return compiled;
	}

	private static String[] getWordsInLine(String line)
	{
		ArrayList<String> words = new ArrayList<String>();
		boolean isDoubleQuoted = false;
		String buffer = "";
		for (int i = 0; i < line.length(); i++)
		{
			char current = line.charAt(i);

			if (current == '"')
			{
				if (isDoubleQuoted)
				{
					isDoubleQuoted = false;
					if (buffer.compareTo("") != 0)
						words.add(buffer);
					buffer = "";
				} else
				{
					isDoubleQuoted = true;
				}
				continue;
			}

			if (isDoubleQuoted)
			{
				if (i == line.length() - 1)
					buffer = "\"" + buffer;
				buffer += current;
				continue;
			}

			if (isIgnorable(current) || isNewLine(current))
			{
				if (buffer.compareTo("") != 0)
					words.add(buffer);
				buffer = "";
				continue;
			}

			if (current == '=')
			{
				if (buffer.compareTo("") != 0)
					words.add(buffer);
				words.add("=");
				buffer = "";
				continue;
			} else if (current == '@')
			{
				if (buffer.compareTo("") != 0)
					words.add(buffer);
				words.add("@");
				buffer = "";
				continue;
			} else if (current == '#')
			{
				if (buffer.compareTo("") != 0)
					words.add(buffer);
				buffer = "";
				break;
			}

			buffer += current;

			if (i == line.length() - 1)
			{
				if (buffer.compareTo("") != 0)
					words.add(buffer);
				buffer = "";
			}
		}
		String[] answer_words = new String[words.size()];
		answer_words = words.toArray(answer_words);
		return answer_words;
	}

	private static boolean isNewLine(char current)
	{
		if (current == '\r' || current == '\n')
			return true;
		return false;
	}

	private static boolean isIgnorable(char current)
	{
		if (current == ' ' || current == '	' || current == '(' || current == ')' || current == ',')
			return true;
		return false;
	}

}
