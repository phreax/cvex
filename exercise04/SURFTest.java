/**
 * SURFTest.java
 *
 * Testing 
 *
 * @version 0.1  2011-05-15
 * @author Michael Thomas, Jan Swoboda
 */

package exercise04;
import graphic.*;
import exercise04.SURFDescriptor;
import java.util.ArrayList;
import java.lang.Math;


public class SURFTest  {

    public static void test1(Image image) {

        int[][] imgInt = Integral.compute(image);

        SURFDescriptor desc = SURF.computeDescriptor(imgInt, new Point(100,100), 1);
        System.out.printf("theta = %.2f\n", desc.theta);

        for(int i=0;i<16;i++) {
            double[] v = desc.get(i);
            System.out.printf("dx,adx,dy,ady = %.2f %.2f %.2f %.2f\n", v[0],v[1],v[2],v[3]);

        }
        System.out.println();
    }

    public static void printDescriptor(SURFDescriptor desc) {

        for(int i=0;i<16;i++) {
            double[] v = desc.get(i);
            System.out.printf("dx,adx,dy,ady = %.2f %.2f %.2f %.2f\n", v[0],v[1],v[2],v[3]);
        }
    }

    public static void test2(Image image) {

        HarrisCorner hc = new HarrisCorner();

        hc.findCorners(image,9);

        Painter painterMaxima = new Painter("Blob Maxima",new Image(hc.blobmaxima));
       
        int[][] imgInt = Integral.compute(image);

        // look for maxima in hessian blob response
        // matrix, if > 0, us it as corner
        // and compute descriptor
        for(int i=0;i<image.width();i++)
            for(int j=0;j<image.height();j++) 
                if(hc.blobmaxima[i][j] > 0) { 
                    image.setPixel(i,j,new Color(255,0,0)); 
                    System.out.printf("Computing surf descriptor at: %d,%d:\n\n",i,j);
                    SURFDescriptor desc = SURF.computeDescriptor(imgInt, new Point(i,j), 2);
                    printDescriptor(desc);
                }
                    
        Painter painterOriginal = new Painter("original",image);
    }

    public static void main (String [] args)
    {

        Image image = new Image("exercise04/test_corner.jpg");
        if(args.length>0) 
            image = new Image(args[0]);
        test2(image);

    }
}



