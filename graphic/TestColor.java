package graphic;
import graphic.Color;;

public class TestColor {

    public static void assertTrue(boolean val) {
        if(!val) {
            System.out.println("Assertion failed");
        }
        else 
            System.out.println("True");
    }


    public void testRGB2YUV() {
        Color color = new Color(10,70,250);

        Color yuv = color.rgb2yuv();
        Color rgb = yuv.yuv2rgb();
        
        System.out.println("color:\n" + color);
        System.out.println("yuv:\n" + yuv);
        System.out.println("rgb:\n" + rgb);

        System.out.println("color == color.rgb2yuv().yuv2rgb()");

        assertTrue(color.equals(rgb));
    }

    public void testIntegerConverstion() {
        Color color = new Color(10,70,250);

        int colori = color.toInt();

        Color color2 = new Color(colori);
    
        System.out.println("color == color.toInt().fromInt()");

        assertTrue(color.equals(color2));

    }
        
         


    public static void main(String[] args) {

        TestColor test = new TestColor();
        test.testRGB2YUV();
        test.testIntegerConverstion();
    }
}


