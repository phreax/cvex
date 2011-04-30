/**
 * SIFT.java
 *
 * Compute SIFT features from an image
 *
 * @version 0.1  2011-04-28
 * @author Michael Thomas, Jan Swoboda
 */

package exercise02;
import graphic.*;
import java.lang.Math; 
import exercise02.*;

public class SIFT {

    public static void main(String[] args) {

        String file = "exercise02/kermit001.jpg";
        if(args.length >= 1) {
            file = args[0];
        }

        Image image= new Image(file);
        Image gradients = new Image(image.width(),image.height(),"gray");
       
        // compute gradients
        OrientedGradients og = new OrientedGradients();

        og.compute(image);

        // cast gradients into bytes 
        for (int i=0; i<image.width(); i++) 
            for (int j=0; j<image.height(); j++) {
                int g = (int)og.getMagnitude(i,j);
                gradients.setGray(i,j,g);
            }

        Painter p = new Painter("Gradient Image", gradients);

        // find descriptor
        Descriptor desc = new Descriptor(new Point(image.width()/2, image.height()/2),
                0, 100.0);

        desc.compute(og);

        desc.visualize();


        double phi = 2*Math.PI*(13.0/360);

        Descriptor desc2 = new Descriptor(new Point(0.25+image.width()/2.0, 0.128+image.height()/2.0),
                phi, 20.0);

        desc2.compute(og);

        desc2.visualize();

    }
        
}
