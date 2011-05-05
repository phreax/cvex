package graphic;
import graphic.Color;
import graphic.Image;
import graphic.Painter;

public class TestImage {

    
    public static void main(String[] args) {

       Image img = new Image(200,200,"rgb");

       for(int i = 0; i<200;i++)
           for(int j = 0; j<200;j++)
               if(i<100) 
                   img.setPixel(i,j,new Color(55,124,100));
               else
                   img.setPixel(i,j,new Color(100,124,55));

       Painter painter1 = new Painter("RGB", img);
       Image gray = Image.rgb2gray(img);

       Painter painter2 = new Painter("Gray", gray);

    }
}


