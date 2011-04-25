/*
 * @(#)Painter.java 1.00 28.11.2008
 * 
 * Copyleft (c) 2008 - This software is for free use to the students of the
 * lecture Algorithmen und Programmieren V: Netzprogrammierung (Alp5),
 * Freie Universität Berlin, Germany.
 */
package graphic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import graphic.Image;

/**
 * This class creates a windows and provides a picture in it.
 * <p>
 * The picture is either give by an BufferedImage or created in the class. The
 * picture an be manupilated by the {@link #update(int, int, int)} methods.
 * 
 * @version 1.01 28 Apr 2011
 * @author Christian Grümme
 *
 * changed by Michael Thomas
 *
 */
public class Painter extends JScrollPane {

	/** ID to identify classes */
	private static final long serialVersionUID = 1181202812147000677L;

	/**
	 * A two-dimensional field of colors inVERTICAL_SCROLLBAR_ALWAYS RGB
	 * representation
	 */
	private BufferedImage image;

	/** The window itself */
	private JFrame frame;

	/**
	 * Constructor that expects a title of the window and dimension of the
	 * picture.
	 * 
	 * @param title
	 *            title of the window
	 * @param width
	 *            width of the picture
	 * @param height
	 *            height of the picture
	 */
	public Painter(String title, int width, int height) {
		this.image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// Set image to white
		int[] rgb = new int[1];
		rgb[0] = 0xFFFFFF;
		// this.update(0, 0, width, height, rgb, 0, 1);
		// Create new Windows
		this.frame = new JFrame(title);
		// Setting properties of the windows
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setContentPane(this);
		this.frame.setSize(image.getWidth(), image.getHeight());
		this.frame.setVisible(true);
	}

	/**
	 * Constructor that expects a title of the window and a picture
	 * 
	 * @param title
	 *            title of the window
	 * @param image
	 *            the image that should be drawn
	 */
	public Painter(String title, BufferedImage image) {
		if (image == null) {
			throw new NullPointerException("No BufferedImage given");
		}
		this.image = image;
		// Create new Windows
		this.frame = new JFrame(title);
		// Setting properties of the windows
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setContentPane(this);
		this.frame.setSize(image.getWidth(), image.getHeight());
		this.frame.setVisible(true);
	}

    public Painter(String title, Image image) {
        this(title,image.getImage());
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this);
	}

	/**
	 * Updates the color of one pixel of the drawn image.
	 * 
	 * @param x
	 *            x-coordinate of the pixel to change
	 * @param y
	 *            x-coordinate of the pixel to change
	 * @param color
	 *            integer RGB representation to the desired color
	 */
	public void update(int x, int y, int color) {
		this.image.setRGB(x, y, color);
		// Update image
		this.image.flush();
		this.frame.repaint();
	}

	/**
	 * Updates the color of a field of pixel of the drawn image.
	 * <p>
	 * It uses the setRGB method of the class ImageBuffer.
	 * 
	 * @param startX
	 *            the starting x-coordinate
	 * @param startY
	 *            the starting y-coordinate
	 * @param width
	 *            width of region
	 * @param height
	 *            height of region
	 * @param rgbArray
	 *            integer RGB representations of the desired colors
	 * @param offset
	 *            offset into the rgbArray
	 * @param scansize
	 *            scanline stride for the rgbArray (?)
	 */
	public void update(int startX, int startY, int width, int height,
			int[] rgbArray, int offset, int scansize) {
		this.image.setRGB(startX, startY, width, height, rgbArray, offset,
				scansize);
		// Update image
		this.image.flush();
		this.frame.repaint();
	}

	/**
	 * Calculates an integer RGB representation of the given color amounts.
	 * <p>
	 * Each amount that succeeds the value of 255 will be capped to 255.
	 * 
	 * @param red
	 *            requested amount of red
	 * @param green
	 *            requested amount of green
	 * @param blue
	 *            requested amount of blue
	 * @return integer RGB representation
	 */
	public static int calculateColor(int red, int green, int blue) {
		// Capping values to 255, if they are above
		if (red > 255) {
			red = 255;
		}
		if (green > 255) {
			green = 255;
		}
		if (blue > 255) {
			blue = 255;
		}
		// Shift them to the right place
		red = red * 0x00010000;
		green = green * 0x00000100;
		blue = blue * 0x00000001;

		return red + green + blue;
	}
}
