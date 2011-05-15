/**
 * ConvolutionTest.java
 *
 * 2D convolution of an image unit test
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package graphic;
import graphic.Convolution;
import graphic.Integral;
import graphic.Painter;
import Jama.Matrix;

public class HaarTest {

    public static void main(String args[]) {
        Image image = new Image(args[0]);
        int[][] integralImage = Integral.compute(image);
        short[][] haarX = Convolution.haar(integralImage, 4, 0); // 0 for x-direction
        short[][] haarY = Convolution.haar(integralImage, 4, 1); // 1 for y-direction
        
        double[][] kernel = {{-2,-2,1,1},{-2,-2,1,1},{-2,-2,1,1},{-2,-2,1,1}};

        short[][] gradientX = Convolution.filter(image, new Matrix(kernel));

        Painter painterOriginal = new Painter("orginal", image);
        Painter painterHaarX = new Painter("haar in x-direction", new Image(haarX));
        Painter painterHaarY = new Painter("haar in y-direction", new Image(haarY));
        Painter paintergradientX = new Painter("haarfilter in y-direction", new Image(gradientX));
    }
}
