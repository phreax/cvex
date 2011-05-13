/**
 * HarrisCorner.java
 *
 * Finding interesting points using Harris Corner Detector
 * with aproximated Gaussian second order derivatives and
 * integral images
 *
 * @version 0.1  2011-05-13
 * @author Michael Thomas, Jan Swoboda
 */

package exercise04;
import Jama.Matrix;
import graphic.*;
import java.lang.Math;
import java.lang.IllegalArgumentException;
import java.lang.ArrayIndexOutOfBoundsException;


public class HarrisCorner {


    // maxima of the blob response
    public int[][] blobmaxima;

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
               //System.out.printf("submatrix: (%d,%d)\n", subcols,subrows);
               int j0 = (size-subcols)/2;
               int j1 = j0+subcols-1;

               System.out.printf("j0,j1: (%d,%d)\n", j0,j1);
               subwhite = new Matrix(subrows,subcols,1.0);
               subblack = new Matrix(subrows,subcols,-2.0);

               //System.out.println("Submatrices:");
               subwhite.print(1,0);
                   
               kernel.setMatrix(0,subrows-1,j0,j1,subwhite);
               kernel.setMatrix(subrows,(2*subrows)-1,j0,j1,subblack);
               kernel.setMatrix(2*subrows,size-1,j0,j1,subwhite);
               break;
            case GRADIENT_YY:
                  kernel = gradientKernel(GRADIENT_XX,size).transpose();
                  break;
            case GRADIENT_XY:
               int subdim = size/3;
               int padding = (subdim-1)/2; // distance from border
               
               //System.out.printf("dim,padding: (%d,%d)\n", subdim,padding);

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
    
    /** sum up pixel intensities over
     *  a reagion, using integral images
     *
     *  @param image  -- integral image
     *  @param region -- rectangle defining the region
     *  @return       -- sum_{(x,y) in region)} I(x,y)
     **/
    private static int sumIntensities(int[][] image, Neighborhood region) {

        int w = image.length;
        int h = image[0].length;

        // corners of rectangular region 
        Point a,b,c,d;
        a = region.point(0,0);
        c = region.point(region.getSize()-1,region.getSize()-1);
        b = new Point(c.x,a.y);
        d = new Point(a.x,c.y);

        // check boundaries
        if(a.getX() <0) a.x = 0;
        if(a.getY() <0) a.y = 0;
        if(b.getX() >= w) b.x = w-1;
        if(b.getY() <0) b.y = 0;
        if(c.getX() >= w) c.x = w-1;
        if(c.getY() >= h) c.y = h-1;
        if(d.getX() <0) d.x = 0;
        if(d.getY() >=h) d.y = h-1;

        int val;
        try {
            val = image[a.getX()][a.getY()] + image[c.getX()][c.getY()] - image[b.getX()][b.getY()]
                                                                    - image[d.getX()][d.getY()];
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.printf("Image dimensions: %dx%d\n", w,h );
            System.out.printf("a = %s\n", a);
            System.out.printf("b = %s\n", b);
            System.out.printf("c = %s\n", c);
            System.out.printf("d = %s\n", d);
            throw(e);
        }
            
            

        return val;
     
    }
    

    public void findCorners(Image image, int windowsize) {

        int w = image.width();
        int h = image.height();

        // derivative images
        short[][] imgDxx; 
        short[][] imgDxy; 
        short[][] imgDyy; 

        // integral images of the derivatives
        int[][] imgIntDxx; 
        int[][] imgIntDxy; 
        int[][] imgIntDyy; 

        // blob response map
        int[][] blobresponses = new int[w][h];

        // determinante of hessian, refers
        // to blobresponse
        int det;

        // sum of gradients in point over the window 
        int sumDxx, sumDxy, sumDyy;

        // compute gradients
        imgDxx = Convolution.filter(image,gradientKernel(GRADIENT_XX,windowsize));
        imgDxy = Convolution.filter(image,gradientKernel(GRADIENT_XY,windowsize));
        imgDyy = Convolution.filter(image,gradientKernel(GRADIENT_YY,windowsize));

        // compute integral images of gradients
        imgIntDxx = Integral.compute(imgDxx);
        imgIntDxy = Integral.compute(imgDxy);
        imgIntDyy = Integral.compute(imgDyy);

        // window
        Neighborhood window;

        for(int i=0;i<w;i++) 
            for(int j=0;j<h;j++) {
    
                window = new Neighborhood(new Point(i,j),3);

                sumDxx = sumIntensities(imgIntDxx,window);
                sumDxy = sumIntensities(imgIntDxy,window);
                sumDyy = sumIntensities(imgIntDyy,window);

                // compute determinante of hessian
                det = (int) (sumDxx * sumDyy - (0.9*sumDxy*0.9*sumDxy));
                blobresponses[i][j] = det;
            }

        this.blobmaxima = MinMax.max(blobresponses,3);
    }
}


