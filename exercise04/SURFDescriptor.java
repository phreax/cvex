/**
 * SURFDescriptor.java
 *
 * Representation of a SURF descriptor vector 
 *
 * @version 0.1  2011-05-15
 * @author Michael Thomas, Jan Swoboda
 */

package exercise04;

public class SURFDescriptor {

    // main orientation
    public double theta;

    // scale at which it was generated 
    public double scale;

    // actual descriptor vector
    public double data[];

    public SURFDescriptor(double theta, double scale) {
        this.theta = theta;
        this.scale = scale;

        data = new double[64];
    }

    /** set descriptor
     * field at a position refering to 
     * a subwindow i in [0..15]
     **/
    public void add(int pos, double dx, double adx, double dy, double ady) {
        int index = pos*4;

        data[index] += dx;
        data[index+1] += adx;
        data[index+2] += dy;
        data[index+3] += ady;
    }

    public double[] get(int pos) {
       
        double[] v = new double[4];
        for(int i=0;i<4;i++) {
            v[i] = data[pos*4 + i];
        }

        return v;
    }
}
        




