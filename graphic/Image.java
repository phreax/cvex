package graphic;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import graphic.Color;

/** 
 * a wrapper for images in java with convenient methods to access an image.
 * 
 * @version 0.1 - 2011-04-20
 * @author Michael Thomas
 *
 */

public class Image {

    private BufferedImage image;

    public Image(String filename) {
        this.image = loadImage(filename);
    }

    public Image(int width, int height, String type) {
        int itype = BufferedImage.TYPE_INT_BGR;
        if(type.equals("rgb")) {
            itype = BufferedImage.TYPE_INT_BGR;
        }
        if(type.equals("gray")) {
            itype = BufferedImage.TYPE_BYTE_GRAY;
        }

        this.image = new BufferedImage(width,height,itype);
    }

    public Image(int width, int height) {
        this(width,height,"rgb");
    }
    public static BufferedImage loadImage(String filename) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(filename));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bimg;
    }

    public int height() {
        return this.image.getHeight();
    }

    public int width() {
        return this.image.getWidth();
    }

    /* return the underlying buffered image object */
    public BufferedImage getImage() {
        return this.image;
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
    public void setPixel(int x, int y, Color c) {
        this.image.setRGB(x, y, c.toInt());
        // Update image
        this.image.flush();
    }

    public Color getPixel(int x, int y) {
        return new Color(this.image.getRGB(x,y));
    }

    public int getGray(int x,int y) {
        return this.image.getRGB(x,y);
    }

    public void setPixel(int x, int y, int c) {
        this.image.setRGB(x, y, c);
        // Update image
        this.image.flush();
    }
}
