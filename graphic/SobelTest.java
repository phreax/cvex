package graphic;
import graphic.*;

public class SobelTest {

    public static void main (String [] args)
    {
        Image original = new Image(args[0]);

        short[][] sobelx = Convolution.sobel(original,0);
        short[][] sobely = Convolution.sobel(original,1);

        Painter painterOriginal = new Painter("original",original);
        Painter painterSobelX = new Painter("sobel in x", new Image(sobelx));
        Painter painterSobelY = new Painter("sobel in y", new Image(sobely));
    }
}

