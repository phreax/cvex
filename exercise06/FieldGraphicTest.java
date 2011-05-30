/**
 * FieldGraphic
 *
 * Visualize the soccer field and the data from the data cloud
 * generated out of robot's view
 *
 * @version 0.1  2011-05-29
 * @author Michael Thomas, Jan Swoboda
 */

package exercise06;
import graphic.*;
import exercise06.*;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

public class FieldGraphicTest {
    public static void main(String args[]) {
        FieldGraphic field = new FieldGraphic();
        field.visualizeField();
        field.visualizeDistances();
    }
}
