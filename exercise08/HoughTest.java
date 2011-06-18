
package exercise08;
import exercise08.*;
import graphic.*;

public class HoughTest {


    public static void main(String[] args) {

        Image image = new Image(args[0]);

        int numSamples = 500;
        int thres = 110;

        if(args.length>1)
            numSamples = Integer.valueOf(args[1]);
        
        if(args.length>2)
            thres = Integer.valueOf(args[2]);

        Painter painter = new Painter("image", image);

        HoughTransformation.houghLines(image,image,numSamples,thres,new Color(255));

        Painter painter2 = new Painter("hough lines", image);
    }
}



