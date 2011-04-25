/**
 * Demosaicing.java
 *
 * Input an color imager, apply Bayer Filter and do
 * demosaicing using bilinear interpolation
 *
 * @version 0.1 2011-04-21
 * @author Michael Thomas, Jan Swoboda
 */

package uebung01;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import graphic.Color;
import graphic.Image;
import graphic.Painter;

class Pixel {
    public int x,y;

    Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Pixel() {
        x=0;
        y=0;
    }

}

// class pixel in the  a 8 Neighborhood
class Neigh_8 {

    // neighorhood pixels starting from the left top corner
    // in anti clockwise direction
    Pixel[] pixels;
    
    public Neigh_8(Image img,int x,int y) {
        pixels = new Pixel[8];
        pixels[0] = new Pixel(x-1,y-1);
        pixels[1] = new Pixel(x,y-1);
        pixels[2] = new Pixel(x+1,y-1);
        pixels[3] = new Pixel(x+1,y);
        pixels[4] = new Pixel(x+1,y+1);
        pixels[5] = new Pixel(x,y+1);
        pixels[6] = new Pixel(x-1,y+1);
        pixels[7] = new Pixel(x-1,y);

        // image dims
        int w = img.width();
        int h = img.height();

        
        for(int i=0; i<8;i++) {
       
            Pixel p = pixels[i];

            // check boundaries
            if(p.x >= w) pixels[i].x = w-1;
            if(p.y >= h) pixels[i].y = h-1;

            if(p.x < 0) pixels[i].x = 0;
            if(p.y < 0) pixels[i].y = 0;
        }

    }
}


class Democaicing {

    // pixel typtes in Bayer Pattern
    static final int RED = 0;
    static final int BLUE = 2;
    static final int GREEN_BLUE = 1; // bgb
    static final int GREEN_RED = 4; // rgr

    // determin bayer pixel type from coordinates
    public static int bayerPixelType(int x, int y) {
        if((x%2) == 0 && (y%2) == 1) 
                return BLUE; // blue
       
        if((x%2) == 1 && (y%2) == 0) 
            return RED; // red
        
        if((x%2) == 0 && (y%2) == 0) 
            return GREEN_RED;

        return GREEN_BLUE;
    }

    // compute the the bilinear interpolated value at x,y
    public static Color bilinearValue(Image img,int x,int y) {
        int type = bayerPixelType(x,y);
        int r,g,b;
        r=0;
        g=0;
        b=0;
        Neigh_8 neigh = new Neigh_8(img,x,y);

        // switch pixel type and interpolate values by neighbourhood values
        switch(type) {
            case BLUE:
                b = img.getPixel(x,y).channels[0];
                // sum up red and green pixels in neighborhood
                for(int i=0;i<8;i++) {
                    if(i%2==1)
                        g += img.getPixel(neigh.pixels[i].x,neigh.pixels[i].y).channels[0];
                    else
                        r += img.getPixel(neigh.pixels[i].x,neigh.pixels[i].y).channels[0];
                }

                g /=4;
                r /=4;
                break;
            case RED:

                r = img.getPixel(x,y).channels[0];
                // sum up blue and green pixels in neighborhood
                for(int i=0;i<8;i++) {
                    if(i%2==1)
                        g += img.getPixel(neigh.pixels[i].x,neigh.pixels[i].y).channels[0];
                    else
                        b += img.getPixel(neigh.pixels[i].x,neigh.pixels[i].y).channels[0];
                }

                g /=4;
                b /=4;
                break;
            case GREEN_BLUE:

                g = img.getPixel(x,y).channels[0];
                // sum up red and blue pixels in neighborhood
                b = img.getPixel(neigh.pixels[3].x,neigh.pixels[3].y).channels[0];
                b += img.getPixel(neigh.pixels[7].x,neigh.pixels[7].y).channels[0];
                
                r = img.getPixel(neigh.pixels[1].x,neigh.pixels[1].y).channels[0];
                r += img.getPixel(neigh.pixels[5].x,neigh.pixels[5].y).channels[0];

                b /= 2;
                r /= 2;
                break;

            case GREEN_RED:

                g = img.getPixel(x,y).channels[0];
                // sum up red and blue pixels in neighborhood
                r = img.getPixel(neigh.pixels[3].x,neigh.pixels[3].y).channels[0];
                r += img.getPixel(neigh.pixels[7].x,neigh.pixels[7].y).channels[0];
                
                b = img.getPixel(neigh.pixels[1].x,neigh.pixels[1].y).channels[0];
                b += img.getPixel(neigh.pixels[5].x,neigh.pixels[5].y).channels[0];

                b /= 2;
                r /= 2;
                break;

        }

        return new Color(r,g,b); // return interpolated color

    }


    // do actual interpolation
    public static Image interpolateBilinear(Image bayer) {

        Image img = new Image(bayer.width(),bayer.height());

        for(int i=0;i<img.width();i++)
            for(int j=0;j<img.height();j++) 
                img.setPixel(i,j,bilinearValue(bayer,i,j));

        return img;
    }




    // determin color channel from coordinates
    public static int bayerChannel(int x, int y) {
        if((x%2) == 0 && (y%2) == 1) 
                return 2; // blue
       
        if((x%2) == 1 && (y%2) == 0) 
            return 0; // red
    
        return 1; // green
    }   
    
    public static Image bayerFilter(Image img) {

        Image bayer = new Image(img.width(),img.height(),"gray");
        for(int i=0;i<img.width();i++)
            for(int j=0;j<img.height();j++) {
                Color color = img.getPixel(i,j);
                int c = bayerChannel(i,j);
                /*bayer.setPixel(i,j,512);
                if(i<400&&j<500) 
                    bayer.setPixel(i,j,0);*/
                int gray = color.channels[c];
                bayer.setPixel(i,j,new Color(gray,gray,gray));
                /*if(bayer.getGray(i,j) != color.channels[c]) {
                    System.out.println("unequal gray scale values!");
                    System.out.printf("%d != %d\f",bayer.getGray(i,j),color.channels[c]);
                }*/
            }
        return bayer;
    }
        
    public static void main(String[] args) {

        String file = "uebung01/wallpaper.jpg";

        if(args.length>= 1) {
                
            file = args[0];
            return;
        }

        Image imgPlain = new Image(file);
        if(imgPlain == null) {
            System.err.println("failed loading Image");
            return;
        }
        Painter painterPlain = new Painter("Plain " + file,imgPlain);

        Image imgBayer =  bayerFilter(imgPlain);
        Painter painterBayer = new Painter("Bayer Filter " + file,imgBayer);

        Image imgDemo =  interpolateBilinear(imgBayer);
        Painter painterDemo = new Painter("Demosacaiced " + file,imgDemo);

    }
}

    






