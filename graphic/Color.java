/**
 * Color.java
 *
 * @version 0.1 2011-04-21
 * @author Michael Thomas
 *
 * Some convenient methods to convert between different
 * colorspaces and representations
 *
 */

package graphic;
import java.lang.Math;

public class Color {

    /* integer reprentation of color */
    
    public int[] channels;

    /* convert to integer representation */
    static public int toInt(int red,int green, int blue) {

        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;

        return rgb;
    
    }

    // return gray value of the color
    public int gray() {
        int r,g,b,y;

        r = channels[0];
        g = channels[1];
        b = channels[2];

        // lumination
        y = (int)(r*0.299 + g * 0.587 + b * 0.114);

        return y;
    }
        

    public int toInt() {
        return toInt(channels[0],channels[1],channels[2]);
    }
    public Color(int c1, int c2, int c3) {

        this.channels = new int[3];
        this.channels[0] = Math.min(255,c1);
        this.channels[1] = Math.min(255,c2);
        this.channels[2] = Math.min(255,c3);
    }

    public Color(int[] channels) {
        this.channels = channels;
    }

    // create a gray value from a single parameter
    public Color(int gray) {
        this.channels = new int[3];
        this.channels[0] = gray;
        this.channels[1] = gray;
        this.channels[2] = gray;
    }
    
    /* convert from integer representation */
    /*static public Color fromInt(int rgb) {
        int c1,c2,c3,reminder;

        c1 = rgb / 0x00010000;
        reminder = rgb%0x00010000;
        c2 = reminder / 0x00000100;
        reminder = reminder%0x00000100;
        
        c3 = reminder;
        return new Color(c1,c2,c3);
    }*/

    /* convert from integer representation */
    static public Color fromInt(int rgb) {
        
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        
        return new Color(red,green,blue);
    }

   
    /* convert to gray */
    static public Color rgb2gray(Color c) {
        return new Color(c.gray());
    }

    /* convert rgb to yuv colorspace */
    static public Color rgb2yuv(Color c) {
        double y,u,v;
        double r,g,b;

        r = c.channels[0];
        g = c.channels[1];
        b = c.channels[2];


        y = (r*0.299 + g * 0.587 + b * 0.114);
        u = ((b - y) * 0.493);
        v = ((r - y) * 0.877);

//        System.out.printf("Too yuv:\n r = %.2f, g = %.2f, b = %.2f \n y = %.2f, u = %.2f, v = %.2f\n",r,g,b,y,u,v);

        // scale to 0-255 range
        return new Color((int)y+128,(int)u+128,(int)v+128);
    }

    static public Color yuv2rgb(Color c) {
        double r,g,b;
        double y,u,v; 

        y = c.channels[0]-128;
        u = c.channels[1]-128;
        v = c.channels[2]-128;


        r = y + v/0.877;
        b = y + u/0.493;
        g = 1.7*y - 0.509*r - 0.194*b;
  //      System.out.printf("Too rgb:\n r = %.2f, g = %.2f, b = %.2f \n y = %.2f, u = %.2f, v = %.2f\n",r,g,b,y,u,v);
        
        return new Color((int)r,(int)g,(int)b);

    }

    public Color rgb2yuv() {
        return rgb2yuv(this);
    }

    public Color yuv2rgb() {
        return yuv2rgb(this);
    }
    
    public boolean equals(Color c) {
        return(c.channels[0] == this.channels[0] &&
               c.channels[1] == this.channels[1] &&
               c.channels[2] == this.channels[2]);
    }
    
    public String toString() {
        String color = "";
        for(int c=0;c<3;c++) {
            color = color + "c["+ c +"]: " +this.channels[c]+"\n";
        }
        return color;
    }
            

}






