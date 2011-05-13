/**
 * HarrisCorner.java
 *
 * Finding interesting points using Harris Corner Detector
 * with aproximated Gaussian second order derivatives and
 * integral images
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package exercise04;
import Jama.Matrix;
import graphic.*;
import java.lang.Math;
import java.lang.IllegalArgumentException;

public class HarrisCorner {

    /** find corners
     *
     * @return -- local maxima of cornerness
     */

    private static int roundToUneven(double d) {
        int r = (int)Math.floor(d);
        if(r%2 == 0)
            return r+1;
        return r;
    }

    public static final int GRADIENT_XX = 0;
    public static final int GRADIENT_YY = 1;
    public static final int GRADIENT_XY = 2;

    // generate a second order gaussian derivative
    // kernel of size n x n 
    public static Matrix gradientKernel(int type, int size) { 
               
        Matrix kernel = new Matrix(size,size,0.0);;

        Matrix subblack,subwhite;
        switch(type) {
            case GRADIENT_XX:
               int subcols = roundToUneven(0.6*size);
               int subrows = size/3;
               int j0 = (size-subrows)/2;
               int j1 = j0+subrows-1;

               subwhite = new Matrix(subcols,subrows,1.0);
               subblack = new Matrix(subcols,subrows,-2.0);

               kernel.setMatrix(0,subrows-1,j0,j1,subwhite);
               kernel.setMatrix(subrows,(2*subrows)-1,j0,j1,subblack);
               kernel.setMatrix(2*subrows,size-1,j0,j1,subwhite);
               break;
            case GRADIENT_YY:
                  kernel = gradientKernel(GRADIENT_XX,size).transpose();
                  break;
            case GRADIENT_XY:
               int subdim = size/3;
               int padding = (2*subdim-1)/2; // distance from border

               subwhite = new Matrix(subdim,subdim,1.0);
               subblack = new Matrix(subdim,subdim,-2.0);

               kernel.setMatrix(padding,padding+subdim-1,padding,padding+subdim-1,subwhite);
               kernel.setMatrix(padding,padding+subdim-1,padding+subdim+1,padding+2*subdim,subblack);
               kernel.setMatrix(padding+subdim+1,padding+2*subdim,padding,padding+subdim-1,subblack);
               kernel.setMatrix(padding+subdim+1,padding+2*subdim,padding+subdim+1,padding+2*subdim,subwhite);
               break;
        }

        return kernel;
    }
               
    public short[][] findCorners(Image image) {

        int windowsize = 9;

        int w = image.width();
        int h = image.height();

        // derivative images
        float[][] imgDxx; 
        float[][] imgDxy; 
        float[][] imgDyy; 

        // integral images of the derivatives
        float[][] imgIntDxx; 
        float[][] imgIntDxy; 
        float[][] imgIntDyy; 


        return null;
    }
}


