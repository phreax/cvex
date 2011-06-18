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

    
    /**
     * compute magintude image from
     * sobel filter
     *
     */
    public static short[][] sobelMagnitude(Image image) {

        short[][] sobelX = sobel(image,0);
        short[][] sobelY = sobel(image,1);

        short[][] imgMag = new short[image.width()][image.height()];

        for(int i=0;i<image.width();i++)
            for(int j=0;j<image.height();j++) {

                short gradX = sobelX[i][j];
                short gradY = sobelY[i][j];
                short val = (short) (Math.sqrt(gradX*gradX + gradY*gradY));

                imgMag[i][j] = val;

            }

        return imgMag;

    }

    /** sum up pixel intensities over
     *  a reagion, using integral images
     *
     *  @param image  -- integral image
     *  @param region -- rectangle defining the region
     *  @return       -- sum_{(x,y) in region)} I(x,y)
     **/
    private static int sumIntensities(int[][] image, Rectangle rect) {

        int w = image.length;
        int h = image[0].length;

        // corners of rectangular region 
        Point a,b,c,d;
        a = rect.p1;
        c = rect.p2; 
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

    /** compute approximized haar
     * wavelet responses in x or y direction
     * making efficient use of integral images
     *
     * @param image -- integral image 
     * @param size  -- window size 
     * @param dir   -- diretion x=0, y=1
     **/
    public static short[][] haar(int[][] image, int size, int dir) {
       
        // defining rectangles for black (-2) and
        // white areas of the haar wavelet

        if(dir <0 || dir > 1) 
            throw new IllegalArgumentException("direction must be 0 or 1");

        Rectangle whitewave, blackwave;

        // Rectangle defining black and white areas
        
        // x direction
        whitewave = new Rectangle(-size/2,-size/2,size/2,0);
        blackwave = new Rectangle(-size/2,0,size/2,size/2);
        
        if(dir == 1) {
            whitewave = new Rectangle(-size/2,-size/2,0,size/2);
            blackwave = new Rectangle(0,-size/2,size/2,size/2);
        }

        int w = image.length;
        int h = image[0].length;

        short[][] imgResponse = new short[w][h];

        Point current;
        int sumwhite,sumblack;
        for(int i=0;i<w;i++) 
            for (int j=0; j<h; j++) {
                current = new Point(i,j);

                // sum white and black areas
                sumblack = (-2) * sumIntensities(image, new Rectangle(
                            Point.add(blackwave.p1,current),
                            Point.add(blackwave.p2,current))
                        );

                sumwhite =  sumIntensities(image, new Rectangle(
                            Point.add(whitewave.p1,current),
                            Point.add(whitewave.p2,current))
                        );
                imgResponse[i][j] = (short)(sumblack + sumwhite);
            }

        return imgResponse;
    }

}








                

        
