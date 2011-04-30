package exercise02;
import exercise02.HOG;
import java.lang.Math;

import junit.framework.*;

public class HOGTest extends TestCase {

    public HOGTest(String name) {
        super(name);
    }

    public void testContructor() {

        HOG hog = new HOG(8);

        assertTrue(hog.size==0.25*Math.PI);
    }
   
    public void testSet() {
        
        HOG hog = new HOG(8);

        hog.set(Math.PI,10);

        assertTrue(hog.get(3) == 5.0);
        assertTrue(hog.get(4) == 5.0);

        hog.set(Math.PI*1.25,20);

        assertTrue(hog.get(4) == 15.0);
        assertTrue(hog.get(5) == 10.0);

        hog.set(Math.PI*1.125,4.0);

        assertTrue(hog.get(3) == 5.0);
        assertTrue(hog.get(4) == 19.0);
        assertTrue(hog.get(5) == 10.0);
       
        hog.set(0,4.0);
        assertTrue(hog.get(0) == 2.0);
        assertTrue(hog.get(7) == 2.0);

    }

}
