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

public class FieldGraphic {

   // define corners of the field

    Vector2D topleft,topright,bottomleft,bottomright,
             topcenter,bottomcenter,center, gltopouter,
             gltopinner,glbottomouter,glbottominner,
             grtopouter,grtopinner,grbottomouter,grbottominner,
             penaltyleft,penaltyright, circletop, circleleft;

    public FieldGraphic () {
        this.center = new Vector2D(400,300);

        this.topleft         = new Vector2D(-FieldModel.HFL,FieldModel.HFW);
        this.bottomleft      = new Vector2D(topleft.x,-topleft.y);
        
        this.topright        = new Vector2D(-topleft.x,topleft.y);
        this.bottomright     = new Vector2D(-topleft.x,-topleft.y);
        
        this.topcenter       = new Vector2D(0,FieldModel.HFW);
        this.bottomcenter    = new Vector2D(0,-FieldModel.HFW);

        this.gltopouter      = new Vector2D(-FieldModel.HFL,FieldModel.HGW);
        this.gltopinner      = new Vector2D(gltopouter.x+FieldModel.GOAL_AREA_DEPTH,gltopouter.y);

        this.glbottomouter   = new Vector2D(-FieldModel.HFL,-FieldModel.HGW);
        this.glbottominner   = new Vector2D(glbottomouter.x+FieldModel.GOAL_AREA_DEPTH,glbottomouter.y);

        this.grtopouter      = new Vector2D(FieldModel.HFL,FieldModel.HGW);
        this.grtopinner      = new Vector2D(grtopouter.x-FieldModel.GOAL_AREA_DEPTH,grtopouter.y);

        this.grbottomouter   = new Vector2D(FieldModel.HFL,-FieldModel.HGW);
        this.grbottominner   = new Vector2D(grbottomouter.x-FieldModel.GOAL_AREA_DEPTH,grbottomouter.y);

        this.penaltyleft     = new Vector2D(-FieldModel.HFL+FieldModel.PENALTY_DIST,0);
        this.penaltyright    = new Vector2D(FieldModel.HFL-FieldModel.PENALTY_DIST,0);

        this.circletop       = new Vector2D(0,-FieldModel.CENTRE_CIRCLE_RADIUS);
        this.circleleft     = new Vector2D(-FieldModel.CENTRE_CIRCLE_RADIUS,0);

        // scale to cm and translate by grid origin
        this.topleft         =   topleft.mul(100).add(center); 
        this.bottomleft      =   bottomleft.mul(100).add(center);   
        this.topright        =   topright.mul(100).add(center);     
        this.bottomright     =   bottomright.mul(100).add(center);
        this.topcenter       =   topcenter.mul(100).add(center);    
        this.bottomcenter    =   bottomcenter.mul(100).add(center); 
        this.gltopouter      =   gltopouter.mul(100).add(center);   
        this.gltopinner      =   gltopinner.mul(100).add(center);   
        this.glbottomouter   =   glbottomouter.mul(100).add(center);
        this.glbottominner   =   glbottominner.mul(100).add(center);
        this.grtopouter      =   grtopouter.mul(100).add(center);   
        this.grtopinner      =   grtopinner.mul(100).add(center);   
        this.grbottomouter   =   grbottomouter.mul(100).add(center);
        this.grbottominner   =   grbottominner.mul(100).add(center);
        this.penaltyleft     = penaltyleft.mul(100).add(center);
        this.penaltyright    = penaltyright.mul(100).add(center);
        this.circletop       = circletop.mul(100).add(center);
        this.circleleft      = circleleft.mul(100).add(center);
    }

    public void visualize() {
        
        Image fieldImage = new Image(800,600);
        Graphics graphic = fieldImage.getImage().createGraphics();

        // the green weedy soccer field
        graphic.setColor(Color.green);
        graphic.fillRect(0,0,800,600);

        graphic.setColor(Color.white);
        
        //topleft - bottomleft
        graphic.drawLine(this.topleft.getX(), this.topleft.getY(), this.bottomleft.getX(), this.bottomleft.getY());
        //topleft - topright
        graphic.drawLine(this.topleft.getX(), this.topleft.getY(), this.topright.getX(), this.topright.getY());
        //topright - bottomright
        graphic.drawLine(this.topright.getX(), this.topright.getY(), this.bottomright.getX(), this.bottomright.getY());
        //bottomleft - bottomright
        graphic.drawLine(this.bottomleft.getX(), this.bottomleft.getY(), this.bottomright.getX(), this.bottomright.getY());
        //topcenter - bottomcenter
        graphic.drawLine(this.topcenter.getX(), this.topcenter.getY(), this.bottomcenter.getX(), this.bottomcenter.getY());
        //goal left
        graphic.drawLine(this.gltopinner.getX(), this.gltopinner.getY(), this.gltopouter.getX(), this.gltopouter.getY());
        graphic.drawLine(this.glbottominner.getX(), this.glbottominner.getY(), this.glbottomouter.getX(), this.glbottomouter.getY());
        graphic.drawLine(this.gltopinner.getX(), this.gltopinner.getY(), this.glbottominner.getX(), this.glbottominner.getY());
        //goal right
        graphic.drawLine(this.grtopinner.getX(), this.grtopinner.getY(), this.grtopouter.getX(), this.grtopouter.getY());
        graphic.drawLine(this.grbottominner.getX(), this.grbottominner.getY(), this.grbottomouter.getX(), this.grbottomouter.getY());
        graphic.drawLine(this.grtopinner.getX(), this.grtopinner.getY(), this.grbottominner.getX(), this.grbottominner.getY());
        //penalty left
        graphic.drawRect(this.penaltyleft.getX(), this.penaltyleft.getY(), 1, 1);
        //penalty right
        graphic.drawRect(this.penaltyright.getX(), this.penaltyright.getY(), 1, 1);
        //circle
        System.out.println(this.circletop.getX() + "  " + this.circletop.getY());
        System.out.println(this.circleleft.getX() + "  " + this.circleleft.getY());
        int circlewidth = (int)this.circleleft.distance(this.center) * 2;
        int circleheight = (int)this.circletop.distance(this.center) * 2;
        graphic.drawOval(this.circleleft.getX(), this.circletop.getY(), circlewidth, circleheight);

        Painter painter = new Painter("Soccer Field", fieldImage);
    }
        
}
