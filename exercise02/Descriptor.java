/**
 * Descriptor.java
 *
 * Representation of a 4x4 SIFT descriptor 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */

package exercise02;
import graphic.*;
import exercise02.*;
import java.awt.Graphics;
import java.awt.Color;

public class Descriptor {


    // actual descriptor data as histogram of oriented gradients
    public HOG[][] hogs;

    // descriptor center
    Point center;

    // rotation offset of the descriptor
    double theta;
    double size; // length of descriptor window

    // size of sigma as multiple of
    // descriptor size
    double gauss_size = 1.5;

    public Descriptor(Point center, double theta, double size) {
        this.center = center;
        this.theta = theta;
        this.size = size;

        hogs = new HOG[4][4];

        for(int i=0;i<4;i++) 
            for(int j=0;j<4;j++) 
                hogs[i][j] = new HOG(8);

    }

    /**
     * compute center of Slot i,j
     */

    public Point slotCenter(int i, int j) {
        
        // compute offset
        double dx,dy;
        dx = (i-2) * size/4 + size/8;
        dy = (j-2) * size/4 - size/8;

        return new Point(center.x + dx, center.y +dy).rotate(theta,center);
    }

    /**
     * compute gauss weight
     * for gradients
     *
     * W_g = G(|S_c -  D_c|)
     *
     *   with:
     *      sigma = gauss_size * descriptor_size
     *      S_c : center of the descriptor subregion
     *      D_C : center of the descriptor
     *
     *  @param i,j -- subregion / histogram slot
     **/
    double gaussWeight(int i, int j) {

        Point sc = slotCenter(i,j);

        double dist = Point.distance(sc,center);

        return gauss(dist,gauss_size*size);
    }

    // compute gauss distribution
    static double gauss(double x, double sigma) {
        double g = (1/(Math.sqrt(Math.PI*2*sigma))) *Math.exp( (-(x*x)/2*sigma*sigma));

        return g;
    }

    /**
     * compute bounding box of the descriptor window.
     *
     * used for performance reason, to iterate only over
     * a subset ob the whole image
     */
    public Rectangle boundingBox() {
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
     * compute sift descriptor 
     **/
    public void compute(OrientedGradients og) {

        Rectangle bbox = boundingBox();

        // iterate over all pixels in the bound box and
        // compute distances of each pixel to all descriptor
        // subregion centers
        for(int i=bbox.p1.getX(); i< bbox.p2.getX(); i++)
            for(int j=bbox.p1.getY(); j< bbox.p2.getY(); j++) {

                for(int si=0;si<4;si++)
                    for(int sj=0;sj<4;sj++) {

                        // pixel center, subregion center
                        Point pc, sc;

                        pc = Point.center(i,j);
                        sc = slotCenter(si,sj);

                        // distances to subregion and to descriptor center
                        double distSub, distDesc;

                        distSub = Point.distance(pc,sc);
                        distDesc = Point.distance(pc,center)/(2*size);

                        double wd = 0;
                        double wg = 0;
                        // (1-d) weight by distance to subregion center
                        wd = Math.max(0,1-(distSub/(4*size)));

                        // gauss weight by distance to descriptor center
                        // no computation if wd is zero, save costs :)
                        if(wd > 0)
                            wg = gauss(distDesc,gauss_size*size);

                        

                        // compute local orientation relative to main orientation of the descriptor
                        double delta = Math.abs(og.getOrientation(i,j)-theta);
                        double weightedGradient = wg*wd*og.getMagnitude(i,j);
                        /*if(wd >0) {
                          System.out.printf("gradient : %.3f\n",weightedGradient);
                          System.out.printf("\tdist : %.3f\n",wd);
                          System.out.printf("\tgauss : %.3f\n",wg);
                          System.out.printf("\tddist : %.3f\n",distDesc);
                        }*/
                        // set histogram bin
                        hogs[si][sj].set(delta, wd*wg*og.getMagnitude(i,j));
                    }
            }
    }


    static void drawLine(Graphics g, Point s, Point e) {
        g.drawLine(s.getX(),s.getY(),e.getX(),e.getY());
    }

    static void drawArrow(Graphics g, Point c,  double length, double theta) {
        Point end = new Point(c.x,c.y - length).rotate(theta,c);

        Point arrowTip = new Point(c.x,c.y - 0.75*length).rotate(theta,c);

        drawLine(g,c,end);
        drawLine(g,end,arrowTip.rotate(0.25*Math.PI,end));
        drawLine(g,end,arrowTip.rotate(-0.25*Math.PI,end));
    }

    public void visualize() {

        Image descriptorImage = new Image(600,600);

        int gridSize = 400;
        int offset = 100;

        Graphics graphic = descriptorImage.getImage().createGraphics();

        graphic.setColor(Color.white);
        graphic.fillRect(0,0,600,600);

        graphic.setColor(Color.black);

        // paint grid
        for(int i=0;i<5;i++) {

            Point hStart = new Point(offset,offset+i*(gridSize/4)); 
            Point hEnd = new Point(offset+gridSize,offset+i*(gridSize/4)); 
            Point vStart = new Point(offset+i*(gridSize/4),offset); 
            Point vEnd = new Point(offset+i*(gridSize/4),offset+gridSize); 


            graphic.drawLine(hStart.getX(),hStart.getY(),hEnd.getX(),hEnd.getY());
            graphic.drawLine(vStart.getX(),vStart.getY(),vEnd.getX(),vEnd.getY());
        }
        
        // draw histograms
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {

                Point center = new Point((0.128*gridSize)+i*(0.25*gridSize),(0.128*gridSize)+(j*0.25*gridSize));
                center = Point.add(center,new Point(offset,offset));

                System.out.printf("Histogram at %d, %d\n",i,j);
                HOG histogram = hogs[i][j];
                // for each direction
                for(int bi = 0; bi<8; bi++) {
                    double phi = (bi/8.0) * 2*Math.PI;
                    double magnitude = histogram.get(bi);
                    double len = ((magnitude*(0.9)*gridSize)/8) / histogram.getMax();
                
                    System.out.printf("\t%d: %.3f\n",bi,magnitude);
//                    System.out.printf("\t%d: %.3f\n",bi,histogram.getMax());
                    drawArrow(graphic,center,len,phi);
                }

            }
        }

        Painter painter = new Painter("SIFT Descriptor",descriptorImage);
    }

    /**
     * compute to wich descriptor slot a pixel refers
     *
     * deprecated  -- not needed, can be done much easier
     */
    public Point slotIndex(int x, int y) {

        // rotate pixel center by -theta around descriptor center 
        // and look, whether its inside the descriptor slot
        // (this is equivalent to rotating the descriptor by theta)
        Point p = Point.center(x,y).rotate(-theta,center); 
        
        // slot indices
        int si,sj;
        si = -1;
        sj = -1;

        // slot boundaries
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
        System.out.printf("evil error in slotIndex(%d,%d): could not compute indices\n",x,y);
        return null;
    }


}





