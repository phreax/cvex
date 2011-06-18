/** HoughTransormation.java
 *
 *
 * Implementation of the Hough Transformation
 * Algorithm for Linear Features (lines)
 *
 * @version v0.1 2011-06-17
 * @author  Michael Thomas, Jan Swoboda
 *
 */

package exercise08;
import graphic.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class HoughTransformation {


    static class LinearParam {
        LinearParam(double theta, double dist) {
            this.theta = theta;
            this.dist = dist;
        }

        double theta;
        double dist;
    }

    /** given a binary image, compute linear parameters theta,r
     * for each pixel by sampling over a number of angles
     * the parameter configurations represent the lines through the
     * current pixel.
     *
     * @param  image       -- binary input image
     * @param  numSamples  -- number of angle samples to be taken
     * @return             -- accumulated parameter matrix
     *
     **/
    public static int[][] computeHough(Image image, int numSamples) {


        int w = image.width();
        int h = image.height();
        int maxDist = (int) (2*Math.sqrt(w*w+h*h));

        // accumulator matrix for linear paremter
        // axis 0 represents the angle theta, axis 1 represents
        // distance
        int[][] paramAcc = new int[numSamples][maxDist];

        for(int i=0;i<w;i++) {
            for(int j=0;j<h;j++) {
                int pixel = image.getGray(i,j);
                
                if(pixel > 0) {
                    // sample angles
                    for(int k=0; k<numSamples;k++) {
                        double theta = 2*Math.PI*k/numSamples;

                        // compute distance, given theta
                        // add sqrt(w^2+h^2) to normalize to positive value
                        // dist \in [0,2*sqrt(w^2+h^2)]
                        int dist = (int) (i*Math.cos(theta) + j*Math.sin(theta) + maxDist/2);

                        if(dist>=0 && dist < maxDist) {
                            paramAcc[k][dist] +=1;
                        }
                        else {
                            System.out.println("distance value does not fit into matrix");
                        }
                        
                    }
                }
            }
        }

        return paramAcc;
    }

    /** given a binary image, do hough transformation
     *  and find best matches for parameter configurations, 
     *  based on a simple threshold
     *
     * @param  image       -- binary input image
     * @param  numSamples  -- number of angle samples to be taken
     * @return             -- accumulated parameter matrix
     *
     **/

    public static List<LinearParam> findBestMatches(Image image, int numSamples, int thres) {

        // list of matches
        List<LinearParam> bestMatches = new ArrayList<LinearParam>();

        int[][] paramAcc = computeHough(image,numSamples);
        int maxDist = paramAcc[0].length;

        for(int i=0;i<numSamples;i++) {
            for(int j=0;j<maxDist;j++) {
                if(paramAcc[i][j] > thres) {
                    double theta = 2*Math.PI*i/(double)(numSamples);
                    double dist = j-(maxDist/2);
                    bestMatches.add(new LinearParam(theta,dist));
                }
            }
        }

        return bestMatches;
    }

    /** find hough lines and draw them into an image */
    public static void houghLines(Image imgIn, Image imgOut, int numSamples
                                , int thres, Color color) {

        List<LinearParam> bestMatches = findBestMatches(imgIn,numSamples,thres);

        System.out.println("total number of lines found: " + bestMatches.size() );
            
        int w = imgOut.width();
        int h = imgOut.height();

        // split up found lines into vertical and horizontal
        List<LinearParam> vLines = new ArrayList<LinearParam>();
        List<LinearParam> hLines = new ArrayList<LinearParam>();

        for(LinearParam p : bestMatches) {
            if(p.theta < 0.25*Math.PI || p.theta > Math.PI * 0.75)
                vLines.add(p);
            else
                hLines.add(p);
        }

        System.out.println("vertical: " + vLines.size() );
        System.out.println("horizontal: " + hLines.size() );

        // vertical lines
        for(int i=0;i<w;i++) {
            for(LinearParam p : vLines) {
                double sint = Math.sin(p.theta);
                double cost = Math.cos(p.theta);
                int j = (int) ((-cost/sint)*i + (p.dist/sint));
                if(j>=0 && j<h)
                    imgOut.setPixel(i,j,color);
            }
        }


        // vertical lines
        for(int j=0;j<h;j++) {
            for(LinearParam p : vLines) {
                double sint = Math.sin(p.theta);
                double cost = Math.cos(p.theta);
                int i = (int) ((j-(p.dist/sint))*(-sint/cost));
                if(i>=0 && i<w)
                    imgOut.setPixel(i,j,color);
            }
        }
                               
    }
}

