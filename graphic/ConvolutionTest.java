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

public class ConvolutionTest {

    public short[][] testHaar(int[][] image, int size, int dir) {
        char direction = '';
        if dir = 0
            direction = 'x';
        if dir = 1
            direction = 'y';
        System.out.println("Testing method \"haar()\" with size "+size+
                           " in "+direction+"-direction");
        return haar(image, size, dir);
    }

    public static void main(String args[]) {
        Image image = new Image(args[0]);
        int[][] integralImage = Integral.compute(image);
        short[][] haarX = testHaar(integralImage, 3, 0) // 0 for x-direction
        short[][] haarY = testHaar(integralImage, 3, 1) // 1 for y-direction
        
        Painter painterOriginal = new Painter("orginal", image);
        Painter painterHaarX = new Painter("haar in x-direction", new Image(haarX));
        Painter painterHaarY = new Painter("haar in y-direction", new Image(haarY));
    }
}
