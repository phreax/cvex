/**
 * YUV.java
 *
 * Input a color image, convert it to YUV colorspace
 * and make UV Histogram
 *
 * @version 0.1 2011-04-21
 * @author Michael Thomas, Jan Swoboda
 */

package uebung01;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import graphic.Color;
import graphic.Image;
import graphic.Painter;
import java.lang.Math;

public class YUV {

    // uv histgram
    private float[][] uvHist;

    // max value of histogram
    private float maxVal = 0;

    // ctor
    public YUV() {
        uvHist = new float[256][256];
    }

    /**
     * convert image to Yuv
     */
    public Image toYuv(Image rgb) {

        Image yuv = new Image(rgb.width(),rgb.height());
        for(int i=0;i<rgb.width();i++)
            for(int j=0;j<rgb.height();j++) {
                Color crgb = rgb.getPixel(i,j);
                Color cyuv = crgb.rgb2yuv();
                Color rgb2 = Color.yuv2rgb(cyuv);
                yuv.setPixel(i,j,cyuv);
                int u,v;
                u = Math.max(cyuv.channels[1],0);
                v = Math.max(cyuv.channels[2],0);
                


                uvHist[u][v] +=1; 
            }
        return yuv;
    }

    /**
     * Normalize Histogram
     */
    public void normalizeHist(Image img) {
        int npixels = img.width() * img.height();
        for(int i=0;i<256;i++)
            for(int j=0;j<256;j++) {

                // normalize by number of pixels
                uvHist[i][j] = (255*uvHist[i][j])/npixels;
    
                // determin max value of histogram (for spreading)
                if(uvHist[i][j] > maxVal) maxVal = uvHist[i][j];

                           

            }
                
    }

    /**
     * create image to visualize histogram
     *      - not much to see, since only a view values appear and the rest
     *        remains black 
     *      - tried to spread to 0-255 doesnt help either
     */
    public Image createHistImage() {
        Image imgHist = new Image(256,256, "gray");

        float spread = 1;

        if(maxVal > 0)
            spread = 255/(maxVal);
        
        System.out.printf("spread = %.2f, maxval = %.2f\n",spread, maxVal);
        for(int i=0;i<256;i++)
            for(int j=0;j<256;j++)
            {

                int val = Math.min(255,(int)(spread*uvHist[i][j]));
                imgHist.setPixel( i,j, (int)val);

                if(val > 0.0)
                    System.out.printf("(%d,%d) = %d\n",i,j,val);
                
            }
        return imgHist;
    }

    
    public static void main(String[] args) {

        String file = "uebung01/wallpaper.jpg";
        if(args.length >= 1) {
            file = args[0];
        }

        YUV converter = new YUV();


        Image imgPlain = new Image(file);
        if(imgPlain == null) {
            System.err.println("failed loading Image");
            return;
        }
        Painter painterPlain = new Painter("Plain " + file,imgPlain);

        Image imgYUV =  converter.toYuv(imgPlain);

        
        Painter painterYUV = new Painter("YUV " + file,imgYUV);
        
        // normalize histogram
        converter.normalizeHist(imgYUV);

        // create histogram image
        Image imgHist = converter.createHistImage();

        Painter painterHist = new Painter("Histogram " + file,imgHist);

    }
}

    






