package exercise02;
import exercise02.Descriptor;
import graphic.Point;
import graphic.Rectangle;
import java.lang.Math;
import junit.framework.*;

public class DescriptorTest extends TestCase {

    public void testBoundingBox() {
        Descriptor desc = new Descriptor(new Point(2,2),0,2);
        Rectangle bbox = desc.boundingBox();


        System.out.printf("bbox = %s %s\n", bbox.p1, bbox.p2);
        
        //assertTrue(bbox.p1.equals(new Point(1,1)));
        //assertTrue(bbox.p2 == new Point(3,3));
        
        desc = new Descriptor(new Point(2,2),2*Math.PI,2);
        bbox = desc.boundingBox();
        
        System.out.printf("bbox = %s %s\n", bbox.p1, bbox.p2);

        //assertTrue(bbox.p1 == new Point(1,1));
        //assertTrue(bbox.p2 == new Point(3,3));
    }

    public static void main(String[] args) {

        Descriptor desc = new Descriptor(Point.center(2,2),0.25*Math.PI,3);

        for(int i = 0;i<5;i++) 
            for(int j = 0;j<5;j++) {
                Point slot = desc.slotIndex(i,j);
                if(slot!=null) {
                    System.out.printf("I(%d,%d) -> slot(%d,%d)\n",i,j,slot.getX(),slot.getY());
                }
        }  

        for(int i=0;i<20;i++) {
            double g = Descriptor.gauss(i/20.0,3);

            System.out.printf("g(%d) = %.4f\n",i,g);
        }

        //desc.visualize();
    }
}
