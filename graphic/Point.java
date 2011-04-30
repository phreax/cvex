/**
 * Point.java
 *
 * Representation of a point int 2D space 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */
package graphic;
import java.lang.Math;

public class Point {

    public double x,y;

    public Point(int x, int y) {
        this.x = (double)x;
        this.y = (double)y;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * input pixel coordinates and return a new point
     * as center of the pixel
     **/
    public static Point center(int x, int y) {
        return new Point(x+0.5,y+0.5);
    }

    /**
     * rotate a point by angle theta
     **/
    public static Point rotate(Point p, double theta) {
        double x,y;
        x = Math.cos(theta)*p.x - Math.sin(theta)*p.y;
        y = Math.sin(theta)*p.x + Math.cos(theta)*p.y;
        return new Point(x,y);
    }
   
    /**
     * rotate a point by angle theta, respectivly a center c
     **/
    public static Point rotate(Point p,double theta, Point c) {
        Point r = sub(p,c).rotate(theta);
        return add(r,c);
    }
    
    /**
     * rotate a point by angle theta
     *
     **/
    public Point rotate(double theta) {
        return rotate(this,theta);
    }

    /**
     * rotate a point by angle theta, respectivly a center c
     **/
    public Point rotate(double theta, Point c) {
        return rotate(this,theta,c);
    }
    
    public double distance(Point p) {
        return Math.sqrt( ((this.x-p.x)*(this.x-p.x)) + ((this.y-p.y)*(this.y-p.y)));
    }

    public static double distance(Point p1, Point p2) {
        return p1.distance(p2);
    }

    /**
     * get coordinates rounded to next integer
     **/
    public int getX() {
        return (int)Math.round(x);
    }

    public int getY() {
        return (int)Math.round(y);
    }

    public boolean equal(Point p) {
        return (this.x == p.x && this.y == p.y);
    }

    public String toString() {
        String s = "("+x+","+y+")";
        return s;
    }
    // addition: p1 + p2
    public static Point add(Point p1, Point p2) {
        return new Point(p1.x+p2.x, p1.y+p2.y);
    }
    
    // subtraction: p1 - p2
    public static Point sub(Point p1, Point p2) {
        return new Point(p1.x-p2.x, p1.y-p2.y);
    }

}
