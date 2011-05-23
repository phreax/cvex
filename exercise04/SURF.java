/**
 * SURF.java
 *
 * Compute a SURF descriptors for interessting points 
 *
 * @version 0.1  2011-05-15
 * @author Michael Thomas, Jan Swoboda
 */

package exercise04;
import graphic.*;
import exercise04.SURFDescriptor;
import java.util.ArrayList;
import java.lang.Math;

public class SURF {

    // compute gauss distribution
    static double gauss(double x, double sigma) {
        double g = (1/(Math.sqrt(Math.PI*2*sigma))) *Math.exp( (-(x*x)/2*sigma*sigma));

        return g;
    }
    // compute main orientation of 
    // the descriptor

    static class Vector {

        public double dx;
        public double dy;

        Vector(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }

    }

    private static boolean checkBoundaries(short[][] image, int i, int j) {
        int w = image.length;
        int h = image[0].length;
        return (i>=0&&j>=0&&i<w&&j<h);
    }

    protected static double mainOrientation( int[][] image
                                    , Point keypoint 
                                    , double scale) 
    {

        // radius of circular window:
        int radius = (int)(6*scale);
        int filtersize = (int)(4*scale);
                                          
        Neighborhood neigh = new Neighborhood(keypoint,radius);
        Rectangle window = new Rectangle( keypoint.getX()-(radius/2)
                                        , keypoint.getY()-(radius/2)
                                        , keypoint.getX()+(radius/2)
                                        , keypoint.getY()+(radius/2)
                                        );

        //System.out.printf("dims = %d,%d, window = (%s,%s)\n", image.length,image[0].length,window.p1,window.p2);
        
        
        // computing wavelet responses for the window
        short[][] imgHaarX = Convolution.haar(image,window,filtersize,0); 
        short[][] imgHaarY = Convolution.haar(image,window,filtersize,1); 
       
        double maxOrientation = Double.MIN_VALUE;
        
        // use a sliding circular window 
        // with size 1/3 PI
        // sum up all orientations assigned
        // to a window, and compute max
        Vector[] orientations = new Vector[6];
        for(int i=0;i<6;i++) orientations[i] = new Vector(0,0);
                                    
        double dx, dy; //gradients
        double weight, theta=0;
        int wi;

        // window size
        for(Point p : neigh) {
            
            double dist = Point.distance(keypoint,p);
        
            int i,j; //indice of aperture window
            i=Point.sub(p,neigh.point(0,0)).getX();
            j=Point.sub(p,neigh.point(0,0)).getY();

            //System.out.printf("dist=%.2f, out ofBound = %s\n", dist,checkBoundaries(imgHaarX,i,j)? "yes" : "no" );
            
            // for all points in circular window
            if(dist<=radius && checkBoundaries(imgHaarX,i,j)) {
                dx = imgHaarX[i][j];
                dy = imgHaarY[i][j];

                // weight haar responses by gaussian
                weight = gauss(dist,scale);
                dx *= weight;
                dy *= weight;

                // shift to 0,2PI
                theta = Math.atan2(dy,dx) + Math.PI;

                
                
                // window index
                wi = (int) Math.round(6*theta/(2*Math.PI));

                wi = (wi+6)%6;
//                System.out.printf("dx,dy = %.2f %.2f\n theta = %.2f\nindex = %d\n", dx,dy,theta,wi);

                orientations[wi].dx += dx;
                orientations[wi].dy += dy;
            }
        }

        // find maximum of each window
        for(int i=0;i<6; i++) {
            theta = Math.atan2(orientations[i].dy, orientations[i].dx);
//            System.out.printf("max: dx,dy = %.2f %.2f\n theta = %.2f\nindex = %d\n", orientations[i].dx,orientations[i].dy,theta,i);
            if(Math.abs(theta) > Math.abs(maxOrientation)) maxOrientation = theta;
        }
        
        

        return maxOrientation;
    }

    /**
     * compute bounding box of the rotated descriptor window.
     *
     * used for performance reason, to iterate only over
     * a subset ob the whole image
     */
    public static Rectangle boundingBox(Point center, double theta, double size) {
        // corners of the descriptor window
        Point[] corners = new Point[4];
        corners[0] = new Point(center.x-size/2,center.y+size/2).rotate(theta,center);
        corners[1] = new Point(center.x+size/2,center.y+size/2).rotate(theta,center);;
        corners[2] = new Point(center.x-size/2,center.y-size/2).rotate(theta,center);;
        corners[3] = new Point(center.x+size/2,center.y-size/2).rotate(theta,center);;

        // x,y extrema
        double minX, minY, maxX, maxY;

        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        maxY = Double.MIN_VALUE;

        // find outer boundin points
       for(Point p : corners) {
        
            if(p.x>maxX) maxX = p.x; 
            if(p.x<minX) minX = p.x;
            if(p.y>maxY) maxY = p.y;
            if(p.y<minY) minY = p.y;
        }
 
        minX = Math.round(minX);
        minY = Math.round(minY);
        maxX = Math.round(maxX);
        maxY = Math.round(maxY);

        return new Rectangle((int)minX,(int)minY,(int)maxX,(int)maxY);
    }

    /**
     * compute to wich descriptor subidx a pixel refers
     *
     */
    private static Point subIndex(int x, int y, Point center, double theta, double size) {

        // rotate pixel center by -theta around descriptor center 
        // and look, whether its inside the descriptor subidx
        // (this is equivalent to rotating the descriptor by theta)
        Point p = Point.center(x,y).rotate(-theta,center); 
        
        // subidx indices
        int si,sj;
        si = -1;
        sj = -1;

        // subidx boundaries
        double borderLeft,borderRight;
        double borderBottom,borderTop;

        // scan from left to right
//      System.out.printf("point = (%.2f,%.2f)\n", p.x,p.y);
      //System.out.println("scan from left to right");
        for(int i=0;i<4;i++) {

            borderLeft = center.x + (i-2)*size/4;
            borderRight = borderLeft + (size/4);
           
      //    System.out.printf("\ti = %d, left = %.2f, right = %.2f\n", i, borderLeft, borderRight);
            if(i==0 && (p.x < borderLeft))
                return null; // pixel not in descriptor

            if(i==3 && (p.x >= borderRight))
                return null; // pixel not in descriptor

            if(p.x >= borderLeft && p.x < borderRight) {
                si = i;
                break;
            }
        }

        // scan from bottom to top 
        //
        //System.out.println("scan from bottom to top");
        for(int i=0;i<4;i++) {

            borderBottom = center.y + (i-2)*size/4;
            borderTop = borderBottom + (size/4);
            
      //      System.out.printf("\tj = %d, bottom = %.2f, top = %.2f\n", i, borderBottom, borderTop);
            
            if(i==0 && (p.y < borderBottom))
                return null; // pixel not in descriptor

            if(i==3 && (p.y >= borderTop))
                return null; // pixel not in descriptor

            if(p.y >= borderBottom && p.y < borderTop) {
                sj = i;
                break;
            }
        }

        if(si >= 0 && sj >= 0) 
            return new Point(si,sj);

        // something evil happened
        System.out.printf("evil error in subIndex(%d,%d): could not compute indices\n",x,y);
        return null;
    }

    public static SURFDescriptor computeDescriptor(int[][] image, Point keypoint, double scale) {

        // compute main orientation of descriptor
        double theta = mainOrientation(image, keypoint, scale);

        
        int filtersize =(int)( 2*scale);
        double windowsize =  (20*scale);

        // get a bounding box of the rotated descriptor window
        Rectangle bbox = boundingBox(keypoint,theta,windowsize);

        // compute haar responses
        short[][] haarX = Convolution.haar(image,bbox,filtersize,0);
        short[][] haarY = Convolution.haar(image,bbox,filtersize,1);

        // descriptor fields
        double dx, dy, adx, ady;
        int ii,jj; // indeces of the response image

        // descriptor data
        SURFDescriptor descriptor = new SURFDescriptor(theta,scale);

        for(int i=bbox.p1.getX(); i<bbox.p2.getX();i++)
            for(int j=bbox.p1.getY(); j<bbox.p2.getY();j++) {
           
                // get descriptor subregion index for current pixel
                Point subidx = subIndex(i,j,keypoint,theta,windowsize);
                if(subidx != null) {
                    ii = Point.sub(new Point(i,j),bbox.p1).getX();
                    jj = Point.sub(new Point(i,j),bbox.p1).getY();
                
                    dx = haarX[ii][jj];
                    dy = haarY[ii][jj];
                
                    adx = Math.abs(dx);
                    ady = Math.abs(dy);
           
                    // accumulate descriptor fields
                    descriptor.add(4*subidx.getX()+subidx.getY(), dx,adx,dy,ady);
                    
                    
                }
            }

        return descriptor;
    }
}

