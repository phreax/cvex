/**
 * Convolution.java
 *
 * 2D convolution of an image 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package graphic;
import Jama.Matrix;
import graphic.*;
import java.lang.IllegalArgumentException;

public class Convolution {

    public static short[][] filter(Image image, Matrix kernel) {

        int w = image.width();
        int h = image.height();

        int colDim = kernel.getColumnDimension();
        int rowDim = kernel.getRowDimension();
       
        // kernel center
        Point kcenter = new Point(colDim/2,rowDim/2);

        // use a 16bit image for temporary result
        // to avoid under/overflow
        short[][] shortimage = new short[w][h];

        // result
        Image convimage = new Image(w,h,"gray");

        int frow,fcol; // indices for the flipped kernel
        int ii,jj;      // point in image that overlappes with the kernel 
        short val;

        // min,max for spreading
        short max = -32768; 
        short min =  32767; 

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                for(int row=0; row<rowDim; row++) {
                        
                    frow = rowDim-1-row;

                    for(int col=0; col<colDim; col++) {
                    
                        fcol = colDim-1-col;
                       
                        // indices of the image pixel under
                        // the current kernel element
                        ii = i - kcenter.getX() +fcol;
                        jj = j - kcenter.getY() +frow;

                        if(image.checkBoundaries(ii,jj)) {
                            //floatimage[i][j] += (double)image.getGray(ii,jj) * kernel.get(frow,fcol);
                            shortimage[i][j] += (short)(image.getGray(ii,jj)*kernel.get(frow,fcol));
                            
                        }
                    }
                }
                // find min/max
                val = shortimage[i][j];
                if(val<min) min = val;
                if(val>max) max = val;
            }

/*        // convert 16bit to 8bit image

        // coffecients for linear spread
        double b = 255.0;
        if((max-min) > 0)
            b = b / (max-min);
        double c = (-1)*min;

        for(int i = 0; i<w; i++)
            for(int j = 0; j<h; j++) {
                val = (short) ((shortimage[i][j] + c)*b);
                convimage.setGray(i,j,val);
            }
*/
        return shortimage; 
    }

    /** compute first derivative
     *
     * @param image -- input image
     * @param dir   -- direction x= 0, y= 1
     **/
    public static short[][] sobel(Image image, int dir) {
        
        if(dir > 1 || dir <0) 
            throw new IllegalArgumentException("Direction must be 0 or 1");

//        double[][] m = {{-1,0,1}};
        double[][] m = {{-1,0,1},{-2,0,2},{-1,0,1}};
        Matrix kernel = new Matrix(m);

        if(dir == 1) {
            kernel = kernel.transpose();
        }

        return filter(image,kernel);
    }

}








                

        
