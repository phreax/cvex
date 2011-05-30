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

public class FieldLineExtractorTest {


    public static void main(String args[]) {
        Image image = new Image(args[0]);

        List<Vector2D> pointcloud = FieldLineExtractor.extractPoints(image);
        List<Vector2D> transformedcloud = FieldLineExtractor.transformPoints(pointcloud);
        visualize(pointcloud);
        visualize(transformedcloud);
    }

    public static void visualize(List<Vector2D> pointcloud) {

        Image image = new Image(800,600);

        for(Vector2D v: pointcloud) {
            if(image.checkBoundaries(v.getX(), v.getY())) {
                image.setGray(v.getX(),v.getY(),255);
            }
        }

        Painter painter = new Painter("the cloud", image);

    }
}


        


