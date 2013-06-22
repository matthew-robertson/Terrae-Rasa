package utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * ImageLoader implements a method to load images from a jar's resource files. Use {@link #loadImage(String)} to do so.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class ImageLoader {
	
	/**
	 * Loads an image from disk using the given file path.
	 * @param ref the filepath to load the image from
	 * @return the BufferedImage returned by the filepath, or null if one does not exist
	 */
	public static BufferedImage loadImage(String ref) {
		try {
			URL url = ImageLoader.class.getClassLoader().getResource(ref);
			if (url == null) {
				throw new IOException("Cannot find: " + ref);
			}

			Image img = new ImageIcon(url).getImage();
			BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = bufferedImage.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
