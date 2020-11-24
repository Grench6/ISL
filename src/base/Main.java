package base;

import java.io.File;
import java.util.ArrayList;

import Compiled.Compiled;
import Compiler.Compiler;
import Compiler.Script;
import Daemon.Daemon;

//Simple Image Scripting language
public class Main
{

	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			printUsage();
			return;
		}

		if (args[0].compareTo("-compile") == 0)
		{
			if (args.length < 3)
			{
				printUsage();
				return;
			}
			File inputFile = new File(args[1]);
			Script script = compileScriptFromFile(inputFile);
			if (script != null && script.successfullyGenarated)
			{
				System.out.println("Script compiled successfully, saving it to a file...");
				script.preSerializeResources();
				boolean success = Script.writeScriptToFile(new File(args[2]), script);
				if (success)
					System.out.println("Script has been saved succesfully!");
				else
					System.out.println("Error while saving script!");
			} else
			{
				System.err.println("Error while compiling script: " + inputFile.getName());
			}
		} else if (args[0].compareTo("-execute") == 0)
		{
			File inputFile = new File(args[1]);
			Script script = compileScriptFromFile(inputFile);
			if (script != null && script.successfullyGenarated)
			{
				System.out.println("Script compiled successfully, running it...");
				Daemon daemon = new Daemon(script);
				daemon.execute();
			} else
			{
				System.err.println("Error while compiling script: " + inputFile.getName());
			}
		} else if (args[0].compareTo("-execute_compiled") == 0)
		{
			Script script = Script.readScriptFromFile(new File(args[1]));
			if (script != null && script.successfullyGenarated)
			{
				System.out.println("Compiled script has been loaded successfully, running it...");
				script.postDeserializeResources();
				Daemon daemon = new Daemon(script);
				daemon.execute();
			} else
			{
				System.err.println("Error while loading script: " + args[1]);
			}
		} else
			printUsage();
	}

	private static Script compileScriptFromFile(File file)
	{
		if (!file.exists())
			return null;
		String[] lines = UtilsIO.readLinesFromFile(file);
		ArrayList<Compiled> compiled = new ArrayList<Compiled>();
		for (int i = 0; i < lines.length; i++)
		{
			Compiled currentCompiled = Compiler.compileLine(lines[i]);
			if (currentCompiled != null)
			{
				currentCompiled.lineNumberInPlainScript = i+1;
				if (currentCompiled.getError() != null)
				{
					System.err.println("Error in line " + currentCompiled.lineNumberInPlainScript + " : " + lines[i]);
					System.err.println("    " + currentCompiled.getError().errorMessage);
					return null;
				}
				compiled.add(currentCompiled);
			}
		}
		Compiled[] ans_compiled = new Compiled[compiled.size()];
		ans_compiled = compiled.toArray(ans_compiled);

		Script script = new Script(ans_compiled);
		return script;
	}

	private static void printUsage()
	{
		System.out.println("java -jar lis.jar [-compile, -execute, -execute_compiled] [InputFile] [OutputFile]");
	}
}
