
package exercise08;
import exercise08.*;
import graphic.*;

public class HoughTest2 {


    public static void main(String[] args) {

        Image image = new Image(args[0]);

        int numSamples = 500;
        int thres = 120;

        if(args.length>1)
            numSamples = Integer.valueOf(args[1]);
        
        if(args.length>2)
            thres = Integer.valueOf(args[2]);

        Painter painter = new Painter("image", image);

        // detect edges, using sobel filter
        short[][] magnitude = Convolution.sobelMagnitude(image);
        Image imgMag = Image.linearSpread(magnitude);
        
        Painter painterMag = new Painter("sobel edges", imgMag);

        // binarize image
        Image imgBin = util.threshold(imgMag,90); 

        Painter painterBin = new Painter("binarized edges", imgBin);

        HoughTransformation.houghLines(imgBin,image,numSamples,thres,new Color(0,255,0));

        Painter painter2 = new Painter("hough lines", image);

    }
}
