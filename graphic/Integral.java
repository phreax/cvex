/**
 * Integral.java
 *
 * Compute the integral image. 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package graphic;
import Jama.Matrix;
import graphic.*;
import java.lang.IllegalArgumentException;


public class Integral {

    public static int[][] compute( Image image ) {

        int w = image.width();
        int h = image.height();

        int[][] imgIntegral = new int[w][h];

        // integral at current position:
        int integral = 0;

        // last values of integral image
        int lx, ly, lxy;

        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {
               
                // sum up pixel values
                // check lower boundary
                lx = i>0 ? imgIntegral[i-1][j] : 0;
                ly = j>0 ? imgIntegral[i][j-1] : 0;
                lxy = i>0 && j>0 ? imgIntegral[i-1][j-1] : 0;

                imgIntegral[i][j] = image.getGray(i,j) + lx + ly - lxy; 
            }

        return imgIntegral;
    }
    
    // awefull hack to avoid generics
    public static int[][] compute( short[][] image ) {

        int w = image.length;
        int h = image[0].length;

        int[][] imgIntegral = new int[w][h];

        // integral at current position:
        int integral = 0;

        // last values of integral image
        int lx, ly, lxy;

        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {
               
                // sum up pixel values
                // check lower boundary
                lx = i>0 ? imgIntegral[i-1][j] : 0;
                ly = j>0 ? imgIntegral[i][j-1] : 0;
                lxy = i>0 && j>0 ? imgIntegral[i-1][j-1] : 0;

                imgIntegral[i][j] = image[i][j] + lx + ly - lxy; 
            }

        return imgIntegral;
    }
}
