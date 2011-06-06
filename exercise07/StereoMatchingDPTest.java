/** 
 * Visualization of disparity Image
 *
 * @author Michal Thomas, Jan Swboda
 * @version 0.1 2011-06-04
 **/

package exercise07;
import exercise07.*;
import graphic.*;

import java.lang.Math.*;

    
public class StereoMatchingDPTest {

    /**
     * do some linear spreading 
     * to visualize disparity
     */
    private static Image disparityToImage(int[][] disparityMatrix, int maxDisparity) { 
        int w = disparityMatrix.length;
        int h = disparityMatrix[0].length;

        float spread = (float)255/(float)maxDisparity;

        Image imgDisparity = new Image(w,h,"gray");

        for(int i=0; i<w;i++) 
            for(int j=0;j<h;j++) {

                int value = disparityMatrix[i][j];

                if(value<0)
                    imgDisparity.setGray(i,j,0);
                else
                    imgDisparity.setGray(i,j,(int) (value*spread));
            }
        return imgDisparity;
    }

    public static void main(String [] args) {
        

        Image imgLeft = new Image("exercise07/cones/im2.png");
        Image imgRight = new Image("exercise07/cones/im6.png");

        if(args.length >= 2)
        {

            imgLeft = new Image(args[0]);
            imgRight = new Image(args[1]);
        }

        int maxdisp = 64;
        int patchsize = 5;
        int occlusion = 5000; // occlusion constant

        if(args.length >= 3)
            occlusion = Integer.parseInt(args[2]);
        if(args.length >= 4)
            patchsize = Integer.parseInt(args[3]);
        if(args.length >= 5)
            maxdisp = Integer.parseInt(args[4]);


        int[][] disparity = StereoMatching.computeDisparityDP(imgLeft, imgRight,
                                                            maxdisp,patchsize,occlusion);

        Image imgDisparity = disparityToImage(disparity, maxdisp);

        Painter painter = new Painter("Disparity", imgDisparity);
    }
}


                


