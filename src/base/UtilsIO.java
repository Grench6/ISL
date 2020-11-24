package base;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class UtilsIO
{
	public static String[] readLinesFromFile(File file)
	{
		ArrayList<String> lines = new ArrayList<String>();
		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr))
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				lines.add(line);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (lines.size() == 0)
			return null;
		String[] ans_lines = new String[lines.size()];
		ans_lines = lines.toArray(ans_lines);
		return ans_lines;
	}

	public static File getExecutionPath()
	{
		String dir = System.getProperty("user.dir");
		return new File(dir);
	}
}
