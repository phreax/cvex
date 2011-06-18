/**
 * testing Hough Transformation:
 *
 * find rail tracks 
 *
 * @author  Michael Thomas, Jan Swoboda
 * @version 2011-06-18
 *
 */


package exercise08;
import exercise08.*;
import graphic.*;

public class HoughTest2 {


    public static void main(String[] args) {

        String imagefile = "exercise08/schienen.jpg";
       
        int numSamples = 500;
        int thres = 100;

        if(args.length>0)
            numSamples = Integer.valueOf(args[0]);
  
        if(args.length>1)
            thres = Integer.valueOf(args[1]);
        hres = Integer.valueOf(args[1]);
        Image image = new Image(imagefile);

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
