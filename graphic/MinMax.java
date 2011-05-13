/**
 * MinMax.java
 *
 * Find local minima / maxima
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package graphic;
import Jama.Matrix;
import graphic.*;
import java.lang.IllegalArgumentException;

public class MinMax {

    public static short[][] max(short[][] image, int windowsize) {
 
        int w = image.length;
        int h = image[0].length;

        short[][] imgMax = new short[w][h];

        short max;
        short val;

        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {

                max = Short.MIN_VALUE;
                Neighborhood neigh = new Neighborhood(new Point(i,j),windowsize);

                for(Point p : neigh) {

                    // set value to min for pixels out of bound
                    val = (i>=0 && j>=0 && i<w && j <h) ? image[p.getX()][p.getY()] : Short.MIN_VALUE;
                    if(val>max) max = val;
                }

                imgMax[i][j] = max;

            }
    
        return imgMax;
    }
}




       

        


