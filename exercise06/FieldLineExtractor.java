/**
 * FieldLineExtractor.java
 *
 * extract points of field lines
 *
* @version 0.1  2011-05-07
 * @author Michael Thomas, Jan Swoboda
 */

package exercise06;
import Jama.Matrix;
import graphic.*;
import exercise06.*;
import java.util.ArrayList;
import java.util.List;


public class FieldLineExtractor {
       
    private static final int thres = 130;

    public static List<Vector2D> extractPoints(Image image) {

        List<Vector2D> pointcloud = new ArrayList<Vector2D>();
        int w = image.width();
        int h = image.height();


        for(int i=0; i<w;i++) 
            for(int j=0;j<h;j++) {

                Color c = image.getPixel(i,j);
                int r = c.channels[0];
                int g = c.channels[1];
                int b = c.channels[2];

                if(r > thres && g > thres && b> thres && j > 150) {
                    pointcloud.add( new Vector2D(i,j)); 
                }
            }

        return pointcloud;
    }
}






