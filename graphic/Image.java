package graphic;

import java.lang.IllegalArgumentException;
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

    // underlying image buffer
    private BufferedImage image;

    public Image(String filename) {
        this.image = loadImage(filename);
    }

    public Image(int width, int height, String type) {
        int itype = BufferedImage.TYPE_INT_RGB;
        if(type.equals("rgb")) {
            itype = BufferedImage.TYPE_INT_RGB;
        }
        if(type.equals("gray")) {
            itype = BufferedImage.TYPE_BYTE_GRAY;
        }

        this.image = new BufferedImage(width,height,itype);
    }

    
    public Image(int width, int height, int type) {
        this.image = new BufferedImage(width,height,type);
    }

    public Image(int width, int height) {
        this(width,height,"rgb");
    }

    /** create gray image from signed int
     *
     * spread to gray 0-255
     **/

    public Image(short[][] shortimage ) {
        Image imgSpreaded = linearSpread(shortimage);
        this.image = imgSpreaded.getImage();
    }

    public Image(int[][] intimage ) {
        Image imgSpreaded = linearSpread(intimage);
        this.image = imgSpreaded.getImage();
    }
      

    // load image from file
    public static BufferedImage loadImage(String filename) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(filename));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bimg;
    }

    // convert to gray scale
    public static Image rgb2gray(Image img) {
        Image grayImg = new Image(img.width(),img.height(),"gray");
        for(int i=0;i<img.width();i++) 
            for(int j=0;j<img.height();j++) {
                Color c = img.getPixel(i,j);
                Color gray =  Color.rgb2gray(img.getPixel(i,j));
                grayImg.setPixel(i,j,gray);
            }

        return grayImg;
    }

    // get image dimensions
    public int width() {
        return this.image.getWidth();
    }

    public int height() {
        return this.image.getHeight();
    }
    /* return the underlying buffered image object */
    public BufferedImage getImage() {
        return this.image;
    }

    /* get image type */
    public int getType() {
        return image.getType();
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
        return Color.fromInt(this.image.getRGB(x,y));
    }

    public int getGray(int x,int y) {
        Color c = this.getPixel(x,y); 
        return c.gray();
    }

    // set/get pixel value using a Point
    public void setGray(Point p, int val) {
        Color c = new Color(val);
        this.setPixel(p.getX(),p.getY(),c);
    }
    
    public int getGray(Point p) {
        Color c = this.getPixel(p.getX(),p.getY()); 
        return c.gray();
    }

    public void setGray(int x, int y, int val) {
        Color c = new Color(val);
        this.setPixel(x,y,c);
    }
    public void setPixel(int x, int y, int c) {
        this.image.setRGB(x, y, c);
        // Update image
        this.image.flush();
    }

    public boolean checkBoundaries(int x, int y) {
        return ( x>=0 && y>=0 && x<width() && y < height());
    }

    // check boundaries using a point
    public boolean checkBoundaries(Point p) {
        int x = p.getX();
        int y = p.getY();
        return ( x>=0 && y>=0 && x<width() && y < height());
    }
    /** substract to images
     * @param minImg   -- minuend
     * @param subImage -- subtrahent
     *
     * @return signed int16 (short) image 
     **/
    public static short[][] sub(Image minImg, Image subImg) {

        int w = minImg.width();
        int h = minImg.height();
        if(h != subImg.height() || w != subImg.width()) 
            throw new IllegalArgumentException("Image Dimensions do not match!");

        // temporary buffer
        short[][] shortimage = new short[w][h];


        short val;

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (short) (minImg.getGray(i,j) - subImg.getGray(i,j));
                shortimage[i][j] = val;
            }
        return shortimage;
    }

    // substract image1 - image2
    // normalize result to 0..255
    public static Image subNorm(Image minImg, Image subImg ) {

        int w = minImg.width();
        int h = minImg.height();
        if(h != subImg.height() || w != subImg.width()) 
            throw new IllegalArgumentException("Image Dimensions do not match!");

        int max =  Integer.MIN_VALUE;
        int min =  Integer.MAX_VALUE;

        // temporary buffer
        short[][] shortimage = new short[w][h];


        short val;

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (short) (minImg.getGray(i,j) - subImg.getGray(i,j));

                shortimage[i][j] = val;

                if(val<min) min = val;
                if(val>max) max = val;
            }

        // coffecients for linear spread
        double b = 255.0;
        if((max-min) > 0)
            b = b / (max-min);
        double c = (-1)*min;

        // result image
        Image diffImg = new Image(w,h,"gray");
        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (short) ((shortimage[i][j] + c)*b);
                diffImg.setGray(i,j,val);
            }

        return diffImg;
    }


    public static Image linearSpread(short[][] shortimage) {

        int w = shortimage.length;
        int h = shortimage[0].length;

        int max =  Integer.MIN_VALUE;
        int min =  Integer.MAX_VALUE;

        Image imgSpreaded = new Image(w,h,"gray");

        short val;

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = shortimage[i][j]; 

                if(val<min) min = val;
                if(val>max) max = val;
            }

        // coffecients for linear spread
        double b = 255.0;
        if((max-min) > 0)
            b = b / (max-min);
        double c = (-1)*min;

        // result image
        Image diffImg = new Image(w,h,"gray");
        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (short) ((shortimage[i][j] + c)*b);
                imgSpreaded.setGray(i,j,val);
            }

        return imgSpreaded;

    }

    public static Image linearSpread(int[][] intimage) {

        int w = intimage.length;
        int h = intimage[0].length;

        int max =  Integer.MIN_VALUE;
        int min =  Integer.MAX_VALUE;

        Image imgSpreaded = new Image(w,h,"gray");

        int val;

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = intimage[i][j]; 

                if(val<min) min = val;
                if(val>max) max = val;
            }

        // coffecients for linear spread
        double b = 255.0;
        if((max-min) > 0)
            b = b / (max-min);
        double c = (-1)*min;

        // result image
        Image diffImg = new Image(w,h,"gray");
        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (int) ((intimage[i][j] + c)*b);
                imgSpreaded.setGray(i,j,val);
            }

        return imgSpreaded;

    }
}
