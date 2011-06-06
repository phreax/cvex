/**
 * StereoMatching.java
 *
 * Find corresponding Points in a pair of
 * stereo images, and computing the 
 * of each corresponding pair of points
 *
 * Two implemantions are provided:
 *
 *      - naive matching
 *      - enhanced matching, using dynamic programming
 *
 * @author Michael Thomas, Jan Swoboda
 * @version 0.1, 2011-06-04
 *
 */

package exercise07;
import graphic.*;
import java.lang.Math.*;

public class StereoMatching {

    /** compute the sum of squared distances 
     *  of to image patches of a given size and position
     *
     *  note: image boundaries will be padded with -1
     *
     *  @param image1 -- image1
     *  @param image2 -- image2
     *  @param p1     -- patch center resp. image1
     *  @param p2     -- patch center resp. image2
     *  @param size   -- patch size
     **/

    public static int ssd(Image image1, Image image2, Point p1, Point p2, int size) {

        Neighborhood patch1 = new Neighborhood(p1, size);
        Neighborhood patch2 = new Neighborhood(p2, size);

        int sum = 0;

        for(int i=0;i<size*size;i++) {
            int val1 =-1;
            int val2 =-1;

            if(image1.checkBoundaries(patch1.point(i)))
                val1 = image1.getGray(patch1.point(i));
             
            if(image2.checkBoundaries(patch2.point(i)))
                val2 = image2.getGray(patch2.point(i));
   
            int diff = val1 -val2;
            sum += diff*diff;
        }

        return sum;
    }


    /** Compute similarites (ssd) of an image patch in one image, 
     * with all patches in a horizontal line segment of a given
     * length in the second image
     *
     *  @param image1 -- image1
     *  @param image2 -- image2
     *  @param start  -- point to start
     *  @param length -- length of the line segment
     *  @param size   -- patch size
     *
     *  @return array of size 'length', containing disimilarity
     *          sim(i) = ssd(image1(start), image2(start-i))
     *          
     **/
    public static int[] ssdLineSegment(Image image1, Image image2, Point start, int length, int size) {

        int[] simvector = new int[length];

        for(int i=0;i<length;i++) {
            simvector[i] = ssd(image1,image2,start,new Point(start.getX()-i,start.getY()), size);
            //System.out.printf("(img1 %s - img2 %s)² = %d\n", start, new Point(start.getX()-i,start.getY()),simvector[i]);
        }
        return simvector;
    }

    public static int argmin(int[] array) {

        int minidx = 0;

        for(int i=0;i<array.length;i++) {
            if(array[i] < array[minidx]) minidx = i;
        }
        return minidx;
    }

    private static String arrayToString(int[] array) {
        String s = "";
        for(int i=0;i<array.length;i++) {
            s = s+ " " +array[i];
        }
        return s;
    }

    
    /** compute simple disparity image, using
     * a naive algorithm.
     *
     * Given a pair of coincide stereo images imgLeft and imgRight (i.e. epipolar lines
     * have the same y-offset both images) the disparity of each point is recovered by scanning
     * each line with an image page, computing the similarities of the patch in imgLeft with
     * all points in imgRight, the maximum similarity is considered as match of the respective
     * Points
     *
     * @return disparity image
     **/
    public static int[][] computeDisparity(Image imgLeft, Image imgRight, int maxDisparity, int patchsize) {
        
        int w = imgLeft.width();
        int h = imgLeft.height();

        int[][] disparityMatrix = new int[w][h];

        int MAX_DIST = patchsize*patchsize*300;

        int progress = 0;
        for(int j=0;j<h;j++) { 
            for(int i=0;i<w;i++) {
                int[] simvector = ssdLineSegment(imgLeft,imgRight,new Point(i,j),maxDisparity,patchsize);
                int disparity = argmin(simvector);

                //System.out.printf("similarities = %s\n",arrayToString(simvector));
                //System.out.printf("disparity(%d,%d) = %d\n",i,j,disparity);
//                System.out.printf("disparity(%d,%d) = \n\t%d\n",i,j,simvector[disparity]);
//
                    
                if(simvector[disparity] > MAX_DIST)
                    disparityMatrix[i][j] = -1;
                else
                    disparityMatrix[i][j] = disparity;

            }
            if(progress<(int)((j/(float)h)*100)) {
                progress = (int)((j/(float)h)*100);
                System.out.println(progress+"%" );
            }
                
        }
        System.out.println("100%");

        return disparityMatrix;
    }

    public static int[][] computeDisparityDP(Image imgLeft, Image imgRight, int maxDisparity, int patchsize, int occlusion) {
        int w = imgLeft.width();
        int h = imgLeft.height();

        int[][] disparityMatrix = new int[w][h];
        int progress=0;
        
        for(int j=0;j<h;j++) { 
            int[] matchedDisparity = findBestMatchDP(imgLeft,imgRight,maxDisparity,patchsize,occlusion,j);
                
            for(int i=0;i<w;i++) {
                disparityMatrix[i][j] = matchedDisparity[i];
            }

            if(progress<(int)((j/(float)h)*100)) {
                progress = (int)((j/(float)h)*100);
                System.out.println(progress+"%" );
            }

        }

        return disparityMatrix;
    }



    /** compute disparity space image
     *  from given row 
     */
    private static int[][] computeDSI(Image imgLeft, Image imgRight, int maxDisparity, int patchsize, int row) {

        int w = imgLeft.width();
        int h = imgLeft.height();

        // disparity space image
        int[][] dsi = new int[w][maxDisparity];

        for(int i=0;i<w;i++) {
            dsi[i] = ssdLineSegment(imgLeft,imgRight,new Point(i,row),maxDisparity,patchsize);
        }
        
        return dsi;
    }

    /** dynamic programming algorithm to find the 
     * best matching path in the disparity space image
     */
    private static int[] findBestMatchDP(Image imgLeft, Image imgRight, int maxDisparity, int patchsize, int occlusion, int row) {

        int[][] dsi = computeDSI(imgLeft, imgRight, maxDisparity, patchsize,row);

        int w = imgLeft.width();
        int h = imgRight.width();

        int[][] costs = new int[w][h];
        int[][] path  = new int[w][h];
        
        // initialize cost matrix
        for(int i=0;i<w;i++) costs[i][0] = i*occlusion;
        for(int i=0;i<h;i++) costs[0][i] = i*occlusion;

        int[] minima = new int[3];

        for(int i=1;i<w;i++)
            for(int j=1;j<h;j++) {
                // three different ways to go, choose
                // the one with the smallest costs
                int similarity = Integer.MAX_VALUE;

                // get value of dsi, from simulated
                // w x w matrix
                // is there another way doing it, to reduce complexity
                // to O(w * maxDisparity) ?
                if(i>=j && (i-j) <maxDisparity) 
                    similarity = dsi[j][i-j];
                
                minima[0] = costs[i-1][j-1] + similarity;
                minima[1] = costs[i-1][j] + occlusion;
                minima[2] = costs[i][j-1] + occlusion;

                int minidx = argmin(minima);

                costs[i][j] = minima[minidx];

                // record choose path for reconstruction
                path[i][j] = minidx+1;
            }

        // reconstruction of the path

        int[] matchedDisparity = new int[w];

        int p = w-1;
        int q = h-1;

        while(p!=0 && q!=0) {
            switch(path[p][q]) {
                case 1:
                    // matched
                    matchedDisparity[p] = p-q;
                            
                    p--;q--; 
                    break;
                case 2:
                    // occluded from left
                    matchedDisparity[p] = -1;
                    p--; 
                case 3:
                    // occluded from right
                    matchedDisparity[p] = -1;
                    q--; 
            }
        }

        return matchedDisparity;
    }

}

