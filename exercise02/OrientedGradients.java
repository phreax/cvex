/**
 * OrientedGradients.java
 *
 * Compute Oriented Gradients of an Image 
 *
 * @version 0.1  2011-04-28
 * @author Michael Thomas, Jan Swoboda
 */
package exercise02;
import graphic.*;
import java.lang.Math;

public class OrientedGradients {


    // matrices for gradient magnitudes and orientations
    double[][] magnitudes;
    double[][] orientations;

    int w, h;
    /**
     * boundary safe image access
     */
    public static int getPixel(int x,int y, Image image) {
        int w,h;
        
        w = image.width();
        h = image.height();

        if(x>=w || x<0 || y>=h || y <0)
            return 0;

        return image.getGray(x,y);
    }

    /**
     * @param image -- grayscale image
     */
    public void compute(Image image) {
        int w,h;

        w = image.width();
        h = image.height();

        this.w = w;
        this.h = h;

        magnitudes   = new double[w][h];
        orientations = new double[w][h];

        for(int i = 0; i<w; i++) 
            for(int j = 0; j<h; j++) {
                double left,right,bottom,top;
                double m,theta; // magnitude, orientation

                left    = (double)getPixel(i+1,j,image);
                right   = (double)getPixel(i-1,j,image);
                bottom  = (double)getPixel(i,j-1,image);
                top     = (double)getPixel(i,j+1,image);
                
                m = Math.sqrt(((top-bottom)*(top-bottom)) + ((left-right)*(left-right))); 
                theta = Math.atan2((top-bottom),(left-right)); 

                // change range from (-PI;PI) to (0;2PI)
                theta += Math.PI;
                magnitudes[i][j] = m;
                orientations[i][j] = theta;
            }
    }


    boolean checkBoundaries(int x, int y)  {
        return (x>=0 && y>=0 && x< w && y< h);
    }


    public double getMagnitude(int x, int y) {
        if(!checkBoundaries(x,y)) return 0.0;
        return magnitudes[x][y];
    }

    public double getOrientation(int x, int y) {
        if(!checkBoundaries(x,y)) return 0.0;
        return orientations[x][y];
    }
}

