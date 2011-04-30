/**
 * HOG.java
 *
 * Histogram of oriented gradients 
 *
 * @version 0.1  2011-04-28
 * @author Michael Thomas, Jan Swoboda
 */

package exercise02;
import java.lang.Math;

public class HOG {

    // histogram
    double[] histogram;

    // size of each bin as part of 2*pi (360°)
    public double size;

    /**
     * @param nbins -- number of bins (one for each orientation)
     */
    public HOG(int nbins) {
        histogram = new double[nbins];
        this.size = 2*Math.PI/nbins;
    }

    // default with 8 bins
    public HOG() {
        this(8);
    }

    /**
     * @param phi     -- orientation of gradien
     * @param gradien -- actual gradient
     */
    public void set(double phi, double gradient) {

        // avoid large values than 360°
        if(phi >= 2*Math.PI) 
            phi = 0;

        // index of bin
        int i;
        double d; // distance
        i = (int) Math.floor(phi/size);

        // distant to the center of current bin
        d = phi/size - (i+0.5);
         
        // adjacent bin
        int j = 0; 
       
        int length = histogram.length;
        if(d<= 0) 
            j = ((i-1)%length + length)%length; // java modolu is crappy
        else 
            j = (i+1)%length;

        double w0,w1;

        // compute weights 

        w1 = Math.abs(d); // weight for adjacent bin
        w0 = 1-w1;

        histogram[i] += w0*gradient;
        histogram[j] += w1*gradient;
    }


    public double getMax() {
        double max = 0.0;
        for(int i=0;i<8;i++) {
            if(histogram[i] > max) max = histogram[i];
        }
        return max;
    }

    public double get(int bin) {
        return histogram[bin];
    }
}
        
        






