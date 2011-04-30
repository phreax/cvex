/**
 * Rectangle.java
 *
 * Representation of a rectangle in 2D space 
 *
 * @version 0.1  2011-04-29
 * @author Michael Thomas, Jan Swoboda
 */
package graphic;
import java.lang.Math;

public class Rectangle {

    /**
     * bottom-left and top-right corner
     * defining the rectangle
     **/
    public Point p1,p2;

    public Rectangle(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Rectangle(int x1, int y1, int x2, int y2) {
        this(new Point(x1,y1), new Point(x2,y2));
    }
}

