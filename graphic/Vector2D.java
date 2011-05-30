/**
 * Vector2D.java
 *
 * Representation of a point int 2D space 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */
package graphic;
import java.lang.Math;

public class Vector2D {

    public double x,y;

    public Vector2D(int x, int y) {
        this.x = (double)x;
        this.y = (double)y;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * rotate a vector by angle theta
     **/
    public static Vector2D rotate(Vector2D p, double theta) {
        double x,y;
        x = Math.cos(theta)*p.x - Math.sin(theta)*p.y;
        y = Math.sin(theta)*p.x + Math.cos(theta)*p.y;
        return new Vector2D(x,y);
    }
   
    /**
     * rotate a vector by angle theta, respectivly a center c
     **/
    public static Vector2D rotate(Vector2D p,double theta, Vector2D c) {
        Vector2D r = sub(p,c).rotate(theta);
        return add(r,c);
    }
    
    /**
     * rotate a vector by angle theta
     *
     **/
    public Vector2D rotate(double theta) {
        return rotate(this,theta);
    }

    /**
     * rotate a vector by angle theta, respectivly a center c
     **/
    public Vector2D rotate(double theta, Vector2D c) {
        return rotate(this,theta,c);
    }
    
    public double distance(Point p) {
        return Math.sqrt( ((this.x-p.x)*(this.x-p.x)) + ((this.y-p.y)*(this.y-p.y)));
    }


    public double distance(Vector2D p) {
        return Math.sqrt( ((this.x-p.x)*(this.x-p.x)) + ((this.y-p.y)*(this.y-p.y)));
    }

    public static double distance(Vector2D v1, Vector2D v2) {
        return v1.distance(v2);
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

    public boolean equal(Vector2D p) {
        return (this.x == p.x && this.y == p.y);
    }

    public String toString() {
        String s = "("+x+","+y+")";
        return s;
    }
    // addition: v1 + v2
    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x+v2.x, v1.y+v2.y);
    }
    
    // subtraction: v1 - v2
    public static Vector2D sub(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x-v2.x, v1.y-v2.y);
    }

    // element wise product 
    public static Vector2D mul(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x*v2.x, v1.y*v2.y);
    }

    // addition: this+p 
    public Vector2D add(Vector2D v) {
        return add(this,v); 
    }
    
    // subtraction: this - p 
    public Vector2D sub(Vector2D v) {
        return sub(this,v); 
    }

    // product: this * p
    public Vector2D mul(Vector2D v) {
        return mul(this,v); 
    }

    public double norm() {
        return Math.sqrt((x*x)+(y*y));
    }

    public Vector2D mul(double s) {
        return new Vector2D(x*s,y*s);
    }

    public static Vector2D mul(Vector2D v,double s) {
        return v.mul(s);
    }

    // dot product
    public static double dot(Vector2D v1, Vector2D v2) {
        return v1.x*v2.x + v1.y*v2.y;
    }

}
