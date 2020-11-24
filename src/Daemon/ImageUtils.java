package Daemon;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImageUtils
{
	public static Point imageContained(BufferedImage bigone, BufferedImage contained)
	{
		for (int x = 0; x < bigone.getWidth(); x++)
		{
			if (x + contained.getWidth() >= bigone.getWidth())
				break;
			for (int y = 0; y < bigone.getHeight(); y++)
			{
				if (y + contained.getHeight() >= bigone.getHeight())
					break;
				if (bigone.getRGB(x, y) == contained.getRGB(0, 0))
				{
					BufferedImage subImage = bigone.getSubimage(x, y, contained.getWidth(), contained.getHeight());
					if (compareTwoImages(subImage, contained))
					{
						return new Point(x, y);
					}
				}
			}
		}
		return null;
	}

	// They MUST have the same dimensions.
	private static boolean compareTwoImages(BufferedImage img1, BufferedImage img2)
	{
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
		{
			System.out.println("ERROR: The two images to be compared dont have the same size");
			return false;
		}
		for (int x = 0; x < img1.getWidth(); x++)
		{
			for (int y = 0; y < img1.getHeight(); y++)
			{
				if (img1.getRGB(x, y) != img2.getRGB(x, y))
					return false;
			}
		}

		return true;
	}
}
