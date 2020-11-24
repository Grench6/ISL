package Compiler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Resource implements Serializable
{
	private static final long serialVersionUID = 1L;
	public File file;
	public String name;
	public byte[] bytes;

	public transient BufferedImage image;

	public Resource(String name, File file)
	{
		this.name = name;
		this.file = file;
	}

	public boolean loadResourceInRAM()
	{
		if (file == null)
			if (image == null)
				return false;
			else
				return true;
		if (!file.exists())
			return false;
		try
		{
			image = ImageIO.read(file);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage)
	{
		try (ByteArrayOutputStream BAOS = new ByteArrayOutputStream())
		{
			ImageIO.write(bufferedImage, "png", BAOS);
			byte[] bytes = BAOS.toByteArray();
			return bytes;
		} catch (Exception e)
		{
			return null;
		}
	}

	public static BufferedImage byteArrayToBufferedImage(byte[] bytes)
	{
		try (InputStream IS = new ByteArrayInputStream(bytes))
		{
			BufferedImage BI = ImageIO.read(IS);
			return BI;
		} catch (Exception e)
		{
			return null;
		}
	}

	public static Resource getResourceByName(String name, ArrayList<Resource> resources)
	{
		for (Resource current : resources)
		{
			if (current.name.compareTo(name) == 0)
				return current;
		}
		return null;
	}

	public static boolean addResourceToList(Resource newResource, ArrayList<Resource> resources)
	{
		for (Resource current : resources)
		{
			if (current.name.compareTo(newResource.name) == 0)
				return false;
		}
		resources.add(newResource);
		return true;
	}
}