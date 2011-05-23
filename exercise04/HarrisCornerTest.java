/**
 * HarrisCornerTest.java
 * 
 * unit tests for harris corner functions
 *
 * @version 0.1  2011-05-13
 * @author Michael Thomas, Jan Swoboda
 */
package exercise04;
import  graphic.*;
import  exercise04.*;
import  Jama.Matrix;

public class HarrisCornerTest {

    public static void testGradientKernel() {

        Matrix k9xx = HarrisCorner.gradientKernel(0,9);
        System.out.println("9x9 Kernel in XX:");
        k9xx.print(1,0);
        
        Matrix k9xy = HarrisCorner.gradientKernel(2,9);
        System.out.println("9x9 Kernel in XY:");
        k9xy.print(1,0);

        Matrix k9yy = HarrisCorner.gradientKernel(1,9);
        System.out.println("9x9 Kernel in YY:");
        k9yy.print(1,0);

        Matrix k15xx = HarrisCorner.gradientKernel(0,15);
        System.out.println("15x15 Kernel in XX:");
        k15xx.print(1,0);
        
        Matrix k15xy = HarrisCorner.gradientKernel(2,15);
        System.out.println("15x15 Kernel in XY:");
        k15xy.print(1,0);

        Matrix k15yy = HarrisCorner.gradientKernel(1,15);
        System.out.println("15x15 Kernel in YY:");
        k15yy.print(1,0);

    }

    public static void testBlobMaxima(Image image) {

        HarrisCorner hc = new HarrisCorner();

        hc.findCorners(image,9);

        for(int i=0;i<image.width();i++)
            for(int j=0;j<image.height();j++) 
                if(hc.blobmaxima[i][j] > 0) 
                    image.setPixel(i,j,new Color(255,0,0)); 
        Painter painterOriginal = new Painter("original",image);
    }

    public static void main (String [] args)
    {
        Image original = new Image(args[0]);
        
        testGradientKernel();
        testBlobMaxima(original);
    }
}
            
