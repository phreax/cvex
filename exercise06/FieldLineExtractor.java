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
import java.lang.Math;
import graphic.*;
import exercise06.*;
import java.util.ArrayList;
import java.util.List;


public class FieldLineExtractor {
       
    private static final int thres = 130;

    private static double roll = 0.0;
    private static double pitch = 0.2;
    private static double yaw = 0.0;

    //assumption: height from images always 480px
    // phi = d*f where d = physical pixel size in cm
    private static double phi = (1.3/480) * 3.8;

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

    public static List<Vector2D> transformPoints(List<Vector2D> points) {
        List<Vector2D> transformedpoints = new ArrayList<Vector2D>();

        // Rotation Matrix R = Rx Ry Rz
        //
        double[][] array1 = {{1.,0.,0.}, {1., Math.cos(pitch), Math.sin(pitch)}, {0., -Math.sin(pitch), Math.cos(pitch)}};
        Matrix Rx = new Matrix(array1);
        //double[][] array2 = {{Math.cos(pitch), 0., -Math.sin(pitch)}, {0.,1.,0.}, {Math.sin(pitch), 0., Math.cos(pitch)}};
        //Matrix Ry = new Matrix(array2);
        //double[][] array3 = {{Math.cos(pitch), Math.sin(pitch), 0.}, {-Math.sin(pitch), Math.cos(pitch), 0.}, {0.,0.,1.,}};
        //Matrix Rz = new Matrix(array3);
        // Compute R
        //Matrix R = Rx.times(Ry).times(Rz);
        Matrix R = Rx; 
        System.out.println("Rotation Matrix:");
        R.print(3,5);

        // Setup K
        double[][] array4 = {{phi, 0., 0.}, {0.,phi,0.}, {0.,0.,1.}};
        Matrix K = new Matrix(array4);
        System.out.println("K Matrix:");
        K.print(3,5);

        // Compute t'
        double[][] array5 = {{0},{0},{30}};
        Matrix t = new Matrix(array5); 
        Matrix t_ = R.times(-1.0).times(t);
        System.out.println("t' Vector:");
        t_.print(3,3);

        // Setup H
        Matrix H = R;
        H.set(0,2,t_.get(0,0));
        H.set(1,2,t_.get(1,0));
        H.set(2,2,t_.get(2,0));
        System.out.println("H Matrix:");
        H.print(3,3);

        // transform
        Matrix pxpy1 = new Matrix(3,1);
        pxpy1.set(2,0, 1.); // (p_x'', p_y'', 1).T

        Matrix K_inverse = K.inverse();
        Matrix H_inverse = H.inverse();
        

        for(Vector2D v : points) {
            pxpy1.set(0,0,(v.getX()-320)*phi); // center pixel w/2
            pxpy1.set(1,0,(v.getY()-240)*phi); // center pixel h/2
            Matrix xy1 = H_inverse.times(K_inverse).times(pxpy1);
            xy1 = xy1.times(1/xy1.get(2,0)); // normalize by z coordinte
            //xy1.print(3,3);
            transformedpoints.add(new Vector2D(xy1.get(0,0)+400, xy1.get(1,0)+300));
        }

        return transformedpoints;
    }
}






