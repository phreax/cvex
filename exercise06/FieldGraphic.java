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

public class FieldGraphic {

   // define corners of the field

    Vector2D topleft,topright,bottomleft,bottomright,
             topcenter,bottomcenter,center, gltopouter,
             gltopinner,glbottomouter,glbottominner,
             grtopouter,grtopinner,grbottomouter,grbottominner,
             penaltyleft,penaltyright, circletop, circleleft;

    Image image = new Image(800,600);
    Graphics graphic;

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

        // create Graphic object to manipulate image
        this.graphic = this.image.getImage().createGraphics();
    }

    /**
     * Visualize the soccer field
     *
     * @params: none
     * @return: none
     **/
    public void visualizeField() {
        // the green weedy soccer field
        this.graphic.setColor(Color.green);
        this.graphic.fillRect(0,0,800,600);
        this.graphic.setColor(Color.white);
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
        graphic.drawRect(this.penaltyleft.getX()-1, this.penaltyleft.getY()-1, 3, 3);
        //penalty right
        graphic.drawRect(this.penaltyright.getX()-1, this.penaltyright.getY()-1, 3, 3);
        //circle  (note that java.awt.Graphics sux a bit, therefore a little work arounds)
        int circlewidth = (int)this.circleleft.distance(this.center) * 2; 
        int circleheight = (int)this.circletop.distance(this.center) * 2;
        graphic.drawOval(this.circleleft.getX(), this.circletop.getY(), circlewidth, circleheight);
        //paint the field
        Painter painter = new Painter("Soccer Field", this.image);
    }

    /**
     * Visualize Data Cloud
     *
     * @param: List of Vector2D Objects
     * @return: none
     **/
    public void visualizeCloud(List<Vector2D> cloud) {
        this.graphic.setColor(Color.yellow);
        for (Vector2D c : cloud) {
            graphic.drawRect(c.getX()-1,c.getY()-1,3,3);
        }
        Painter painter = new Painter("Data Cloud on Soccer Field", this.image);
    }

    public void visualizeDistances() {

        DistanceTable dtable = new DistanceTable();
        dtable.createTable();

        int argmin;
        for(int i=0;i<image.width();i+=10)
            for(int j=0;j<image.height();j+=10) {
                argmin = dtable.getArgMin(i,j);
                Vector2D dvector = dtable.vmatrix[i][j][argmin];
                this.graphic.setColor(Color.blue);
                graphic.drawLine(i,j,dvector.getX()+i,dvector.getY()+j);
            }
        Painter painter = new Painter("Distances", this.image);
    }
                




}
