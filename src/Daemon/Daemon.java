package Daemon;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Compiled.ClickCoordInstruction;
import Compiled.ClickInstruction;
import Compiled.ClickOffsetInstruction;
import Compiled.Compiled;
import Compiled.ContainsCondition;
import Compiled.NotContainsCondition;
import Compiled.RateDirective;
import Compiled.Reif;
import Compiled.SleepCommand;
import Compiled.WritelineInstruction;
import Compiler.Resource;
import Compiler.Script;

public class Daemon
{
	private final boolean DEBUG = true;

	private BufferedImage lastScreenShot;
	private Point foundPosition;

	Robot robot;
	private final int screen_width;
	private final int screen_height;
	private final int keyDelayTime = 25;
	private int loopDelayTime = 100;

	private Reif reif;
	private int reifIndex = -1;
	Script script;
	int currentIndex = 0;

	long startTime = 0;

	public Daemon(Script script)
	{
		this.script = script;
		Dimension tempvar = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		screen_width = tempvar.width;
		screen_height = tempvar.height;
		try
		{
			robot = new Robot();
		} catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	public void execute()
	{
		startTime = System.currentTimeMillis();
		printDebugMessage("START OF SCRIPT");
		while (currentIndex < script.fullCompiled.length)
		{
			Compiled current = script.fullCompiled[currentIndex];
			printDebugMessage("Next line! complied_number: " + currentIndex + "; line_number: " + current.lineNumberInPlainScript);
			takeFullScreenShot();

			String instructionID = current.getInstructionID();

			if (instructionID.compareTo("reif") == 0)
			{
				reif = (Reif) current;
				reifIndex = currentIndex;
				printDebugMessage("Saving reif point: " + reif.lineNumberInPlainScript + ")");
			} else if (instructionID.compareTo("recall") == 0)
			{
				if (reif != null)
				{
					printDebugMessage("Recall, going to reif line(" + reif.lineNumberInPlainScript + ")");
					currentIndex = reifIndex;
				}
			} else if (instructionID.compareTo("@") == 0)
			{
				// Do nothing, script has already taken care of it.
			} else if (instructionID.compareTo("sleep") == 0)
			{
				int millis = ((SleepCommand) current).millis;
				printDebugMessage("Sleeping... " + millis);
				sleep(millis);
			} else if (instructionID.compareTo("rate") == 0)
			{
				printDebugMessage("Changing screenshot rate");
				loopDelayTime = ((RateDirective) current).millis;
			} else if (instructionID.compareTo("writeline") == 0)
			{
				printDebugMessage("Writting line");
				writeString(((WritelineInstruction) current).writableString, true);
			} else if (instructionID.compareTo("click_coordinate") == 0)
			{
				printDebugMessage("Manual click with coords");
				manualClick(((ClickCoordInstruction) current).x, ((ClickCoordInstruction) current).y);
			} else if (instructionID.compareTo("click_offset") == 0)
			{
				printDebugMessage("Click with offset");
				autoClickWithOffset(((ClickOffsetInstruction) current).x, ((ClickOffsetInstruction) current).y);
			} else // Blockable instructions
			{
				if (instructionID.compareTo("contains") == 0)
				{
					Resource currentResource = Resource.getResourceByName(((ContainsCondition) current).varName, script.resources);
					if (((ContainsCondition) current).semicolon)
					{
						deadlockCheckImage(currentResource.image, true);
					} else
					{
						checkImage(currentResource.image);
					}
				} else if (instructionID.compareTo("not_contains") == 0)
				{
					Resource currentResource = Resource.getResourceByName(((NotContainsCondition) current).varName, script.resources);
					if (((NotContainsCondition) current).semicolon)
					{
						deadlockCheckImage(currentResource.image, false);
					} else
					{
						checkImage(currentResource.image);
					}
				} else if (instructionID.compareTo("click") == 0)
				{
					ClickInstruction parsed = (ClickInstruction) current;
					Resource currentResource = Resource.getResourceByName(parsed.varName, script.resources);
					boolean makeClick = false;
					if (parsed.semicolon)
					{
						makeClick = deadlockCheckImage(currentResource.image, true);
					} else
					{
						makeClick = checkImage(currentResource.image);
					}
					if (makeClick)
					{
						printDebugMessage("Click found resource");
						autoClickWithOffset(0, 0);
					}
				} else
				{
					System.err.println("Fatal error, the daemon could not execute the compiled instruction(Instruction not found/Corrupt script!): " + instructionID);
				}
			}
			currentIndex++;
		}
		printDebugMessage("END OF SCRIPT");
	}

	private void printDebugMessage(String msg)
	{
		if (DEBUG)
		{
			long currentMillis = System.currentTimeMillis() - startTime;
			long seconds = currentMillis / 1000;
			int centiSeconds = (int) ((currentMillis - (seconds * 1000)) / 10);
			if (centiSeconds >= 10)
				System.out.println("[" + seconds + "." + centiSeconds + " s] " + msg);
			else
				System.out.println("[" + seconds + ".0" + centiSeconds + " s] " + msg);
		}
	}

	private boolean checkImage(BufferedImage image)
	{
		takeFullScreenShot();
		foundPosition = ImageUtils.imageContained(lastScreenShot, image);
		if (foundPosition != null)
			return true;
		checkReif();
		return false;
	}

	private boolean deadlockCheckImage(BufferedImage image, boolean valueConditionForBreak)
	{
		while (true && !false)
		{
			takeFullScreenShot();
			foundPosition = ImageUtils.imageContained(lastScreenShot, image);
			if ((foundPosition != null ? true : false) == valueConditionForBreak)
			{
				printDebugMessage("Deadlock condition met! Returning to main thread... ");
				return true;
			} else if (checkReif())
				return false;
			printDebugMessage("Deadlock condition not met, sleeping... " + loopDelayTime);
			sleep(loopDelayTime);
		}
	}

	private boolean checkReif()
	{
		Point tempFoundPosition;
		if (reif != null)
		{
			Resource currentResource;
			boolean condition;
			if (reif.condition.getInstructionID().compareTo("contains") == 0)
			{
				currentResource = Resource.getResourceByName(((ContainsCondition) reif.condition).varName, script.resources);
				condition = true;
			} else if (reif.condition.getInstructionID().compareTo("not_contains") == 0)
			{
				currentResource = Resource.getResourceByName(((NotContainsCondition) reif.condition).varName, script.resources);
				condition = false;
			} else
				return false;

			if (currentResource != null)
			{
				tempFoundPosition = ImageUtils.imageContained(lastScreenShot, currentResource.image);
				if ((tempFoundPosition != null ? true : false) == condition)
				{
					printDebugMessage("Reif condition met! Changing current line of execution to " + reif.lineNumberInPlainScript);
					currentIndex = reifIndex;
					foundPosition = tempFoundPosition;
					return true;
				}
			}
		}
		return false;
	}

	private void writeString(String message, boolean sendIntro)
	{
		for (int i = 0; i < message.length(); i++)
		{
			char tempchar = message.charAt(i);
			try
			{
				if (Character.isUpperCase(tempchar))
					robot.keyPress(KeyEvent.VK_SHIFT);
				writeChar(tempchar);
				if (Character.isUpperCase(tempchar))
					robot.keyRelease(KeyEvent.VK_SHIFT);
			} catch (Exception e)
			{
				System.err.println("[!]Error while trying to write the message to the chat.");
				System.err.println("   Maybe a character was invalid? ");
				System.err.println("   Remember you cant send any special character, only numbers and letters(without accent)");
			}
		}
		if (sendIntro)
		{
			writeKeyCode(KeyEvent.VK_ENTER);
		}
	}

	private void writeChar(char charToWrite)
	{
		int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(charToWrite);
		robot.keyPress(keyCode);
		sleep(keyDelayTime);
		robot.keyRelease(keyCode);
		sleep(keyDelayTime);
	}

	private void writeKeyCode(int keyCode)
	{
		robot.keyPress(keyCode);
		sleep(keyDelayTime);
		robot.keyRelease(keyCode);
		sleep(keyDelayTime);
	}

	private void autoClickWithOffset(int ox, int oy)
	{
		if (foundPosition == null)
			return;
		robot.mouseMove(foundPosition.x + ox, foundPosition.y + oy);
		sleep(12);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		sleep(12);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		sleep(12);
	}

	private void manualClick(int x, int y)
	{
		robot.mouseMove(x, y);
		sleep(12);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		sleep(12);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		sleep(12);
	}

	private void takeFullScreenShot()
	{
		lastScreenShot = robot.createScreenCapture(new Rectangle(0, 0, screen_width, screen_height));
	}

	private static void sleep(int millis)
	{
		if (millis <= 0)
		{
			System.err.println("No negative values are allowed for sleep!");
			return;
		}
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
