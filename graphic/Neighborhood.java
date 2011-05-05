/**
 * Neighborhood.java
 *
 * Representation of a local N x N neihborhood
 * around a given pixel 
 *
 * @author Michael Thomas
 * @version 0.1 - 2011-05-04
 *
 **/

package graphic;
import graphic.Point;
//import java.lang.Iterable;
import java.util.Iterator;
import java.lang.IndexOutOfBoundsException; 

public class Neighborhood implements Iterable<Point> { 

    // neighborhood points
    public Point[] points;

    public Point center;
    // window size
    int size;

   
    // ctor
    public Neighborhood(Point p, int size) {

        // avoid even window sizes
        if(size % 2 == 0)
            size++;
        this.size = size;

        this.center = p;

    }

    // iterator over all points of the neighborhood
    class PointIterator implements Iterator<Point> {
        int counter;
        int max;
        Neighborhood n;
        public PointIterator(Neighborhood n) {
            this.counter = 0;
            this.max = n.getSize()*n.getSize();
            this.n = n;
        }

        public boolean hasNext() {
            return (counter<max);
        }

        public Point next() {
            Point p = n.point(counter);
            counter++;
            return p; 
        }

        public void remove() {}
    }

    public Iterator<Point> iterator() {
        return new PointIterator(this);
    }

    /**
     * return a point from the neighborhood
     *
     * @param index -- 0 = index in [0,size*size[ 
     **/
    public Point point(int index) {
        int i = index/size;
        int j = index%size;
        return point(i,j);
    }

    /**
     * return a point from the neighborhood
     *
     * @param i,j -- 0 = i,j in [0,size[ 
     **/
    public Point point(int i,int j) {

        if(i<0 || i>=size || j<0 || j>=size)
            throw new IndexOutOfBoundsException("Point not in Neighborhood"); 

        int index = i*size + j;
        return new Point(i+center.x-size/2,j+center.y-size/2);
    }
    
    public int getSize() {
        return size;
    }


}
                


