package Compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Compiled.Compiled;
import Compiled.CompiledError;
import Compiled.ResourceDeclaration;

public class Script implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ArrayList<Resource> resources = new ArrayList<Resource>();
	public boolean successfullyGenarated = false;
	public Compiled[] fullCompiled;
	private int currentLine = 0;

	public Script(Compiled[] fullCompiled)
	{
		this.fullCompiled = fullCompiled;
		CompiledError error = verifyForLogicErrors();
		if (error != null)
		{
			successfullyGenarated = false;
			System.err.println("Error while generating script!");
			System.err.println(error.errorMessage);
		} else
			successfullyGenarated = true;
	}

	private CompiledError verifyForLogicErrors()
	{
		Compiled current;
		boolean foundReif = false;
		boolean declarationPhase = true;
		while ((current = nextCompiled()) != null)// Add resources
		{
			if (declarationPhase)
			{
				if (current.getInstructionID().compareTo("@") == 0)
				{
					String varName = ((ResourceDeclaration) current).varName;
					File file = ((ResourceDeclaration) current).file;
					Resource newResource = new Resource(varName, file);
					if (!Resource.addResourceToList(newResource, resources))
						return new CompiledError(true, "Resource was declared twice!");
					if (!newResource.loadResourceInRAM())
						return new CompiledError(true, "Unable to load resource!");
					continue;
				} else
					declarationPhase = false;
			}
			if (current.getInstructionID().compareTo("@") == 0)
				return new CompiledError(true, "Declarations must be only at the very begining of the script!");
			if (current.getInstructionID().compareTo("reif") == 0)
			{
				if (!foundReif)
					foundReif = true;
				else
					return new CompiledError(true, "There can only be one reif per script!");
			}
			if (current.getInstructionID().compareTo("recall") == 0)
			{
				if (!foundReif)
					return new CompiledError(true, "The recall is not inside a reif!");
			}
		}
		return null;
	}

	private Compiled nextCompiled()
	{
		if (currentLine >= fullCompiled.length)
			return null;
		return fullCompiled[currentLine++];
	}
	
	public void preSerializeResources() 
	{
		for (Resource current:resources) 
			current.bytes = Resource.bufferedImageToByteArray(current.image);
	}
	
	public void postDeserializeResources() 
	{
		for (Resource current:resources) 
			current.image = Resource.byteArrayToBufferedImage(current.bytes);
	}

	public static Script readScriptFromFile(File file)
	{
		try (ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(file)))
		{
			Script script = (Script) OIS.readObject();
			return script;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static boolean writeScriptToFile(File file, Script script)
	{
		try (ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(file)))
		{
			OOS.writeObject(script);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
