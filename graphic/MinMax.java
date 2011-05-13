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

        int ii,jj;
        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {

                max = Short.MIN_VALUE;
                Neighborhood neigh = new Neighborhood(new Point(i,j),windowsize);

                for(Point p : neigh) {
                    ii = p.getX();
                    jj = p.getY();
                    // set value to min for pixels out of bound
                    val = (ii>=0 && jj>=0 && ii<w && jj <h) ? image[ii][jj] : Short.MIN_VALUE;
                    if(val>max) max = val;
                }

                imgMax[i][j] = max;

            }
    
        return imgMax;
    }
    
    // awfull hack to avoid generics
    // (generics can only handel classes .. useless, i'd love to do this
    // in c++)
    public static int[][] max(int[][] image, int windowsize) {
 
        int w = image.length;
        int h = image[0].length;

        int[][] imgMax = new int[w][h];

        int max;
        int val;

        int ii,jj;
        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {

                max = Short.MIN_VALUE;
                Neighborhood neigh = new Neighborhood(new Point(i,j),windowsize);

                for(Point p : neigh) {
                    ii = p.getX();
                    jj = p.getY();
                    // set value to min for pixels out of bound
                    val = (ii>=0 && jj>=0 && ii<w && jj <h) ? image[ii][jj] : Short.MIN_VALUE;
                    if(val>max) max = val;
                }

                imgMax[i][j] = max;

            }
    
        return imgMax;
    }

    // awfull hack to avoid generics 
    public static short[][] max(Image image, int windowsize) {
 
        int w = image.width();
        int h = image.height();

        short[][] imgMax = new short[w][h];

        short max;
        short val;
        int ii,jj;

        for(int i=0;i<w;i++)
            for(int j=0;j<h;j++) {

                max = Short.MIN_VALUE;
                Neighborhood neigh = new Neighborhood(new Point(i,j),windowsize);

                for(Point p : neigh) {
                    ii = p.getX();
                    jj = p.getY();

                    // set value to min for pixels out of bound
                    val = (ii>=0 && jj>=0 && ii<w && jj <h) ? (short)image.getGray(ii,jj) : Short.MIN_VALUE;
                    if(val>max) max = val;
                }

                imgMax[i][j] = max;

            }
    
        return imgMax;
    }
}




       

        


