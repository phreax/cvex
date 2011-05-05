package graphic;
import java.lang.Math;
import junit.framework.*;
import graphic.Point;
import graphic.Neighborhood;

public class NeighborhoodTest extends TestCase {

    public NeighborhoodTest(String name) {
        super(name);
    }

    public void testCtor() {

        Neighborhood neigh = new Neighborhood(new Point(10,10), 3);

        for( Point p : neigh  ) {
            System.out.println(p);
        }
        assertTrue(neigh.point(0,0).x == 9);
        assertTrue(neigh.point(0,0).y == 9);
        
        assertTrue(neigh.point(2,2).x == 11);
        assertTrue(neigh.point(2,2).y == 11);
    }

}
