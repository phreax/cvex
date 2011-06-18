// some image utilities

package graphic;
import graphic.*;

public class util {

    /** do threshold binarization 
     */
    public static Image threshold(Image image, int thres) {

        int w = image.width();
        int h = image.height();

        Image imgBinary = new Image(w,h,"gray");

        for(int i=0;i<w;i++) 
            for(int j=0;j<h;j++) {
                if(image.getGray(i,j) > thres) 
                    imgBinary.setGray(i,j,255);
                else
                    imgBinary.setGray(i,j,0);
                
            }

        return imgBinary;
    }
}



