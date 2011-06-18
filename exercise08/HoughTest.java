/**
 * testing Hough Transformation:
 *
 *  find hidden lines in a noisy image
 *
 * @author  Michael Thomas, Jan Swoboda
 * @version 2011-06-18
 *
 */

package exercise08;
import exercise08.*;
import graphic.*;

public class HoughTest {


    public static void main(String[] args) {

        String imagefile = "exercise08/rauschen.png";

        int numSamples = 400;
        int thres = 110;
        
        if(args.length>0)
            numSamples = Integer.valueOf(args[0]);
        
        if(args.length>1)
            thres = Integer.valueOf(args[1]);


        Image image = new Image(imagefile);
       
        Painter painter = new Painter("image", image);

        HoughTransformation.houghLines(image,image,numSamples,thres,new Color(255));

        Painter painter2 = new Painter("hough lines", image);
    }
}



