/**
 * DistanceTable.java
 *
 * Create a lookup table for minimal distances
 * from each point in an image to the nearest
 * model element
 *
* @version 0.1  2011-05-07
 * @author Michael Thomas, Jan Swoboda
 */

package exercise06;

import graphic.*;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import Jama.Matrix;


public class DistanceTable {


    // grid dimensions of our world
    // in units of 1cm
    static final int gridWidth = 800;
    static final int gridHeight = 600;

    static final int centerX = gridWidth/2;
    static final int centerY = gridHeight/2;

    // table with vectors to the nearest
    // point of each model element
    Vector2D vmatrix[][][];

    //  distances to each model 
    //  element (to save costs, pre compute
    //  all distances befor)
    double dmatrix[][][];

    public DistanceTable() {

        vmatrix = new Vector2D[gridWidth][gridHeight][11];
        dmatrix = new double[gridWidth][gridHeight][11];
    }

    /* compute the nearest point on a line
     * segment uv to the point p
     */
    private static Vector2D nearestPoint(Vector2D u, Vector2D v, Vector2D p) {

        // line uv, parametrized as u + t * (v-u)
        // the closest point is the orthogonal projection
        // of vector up on the line uv

        // normalized length of the projection vector
        Vector2D r = Vector2D.sub(v,u); // direction vector)
        double t = Vector2D.dot(Vector2D.sub(p,u),r) / Vector2D.dot(r,r);

        // projection is on within the linesegment -> nearest point
        // is the next endpoint
        if(t<0.0) return u;
        if(t>1.0) return v;

        // else return projection x = u + t*r
        return( Vector2D.add(u,Vector2D.mul(r,t)));
    }

    /*private static Vector2D centerGridPoint(Vector2D p) {

        // translate to origin at center
        Vector2D pc = Vector2D.sub(p,center);
        return pc;
    }
    */

    // return index of nearest model element
    // for a given point
    public int getArgMin(int i, int j) {
        double distances[] = dmatrix[i][j];

        int argmin = 0;
        for(int k=0;k<11;k++) {
            if(distances[k] < distances[argmin]) {
                argmin = k;
            }
        }
        return argmin;
    }
            

    public void createTable() {

        // define corners of the field

        Vector2D topleft,topright,bottomleft,bottomright,
                 topcenter,bottomcenter,center, gltopouter,
                 gltopinner,glbottomouter,glbottominner,
                 grtopouter,grtopinner,grbottomouter,grbottominner,
                 penaltyleft,penaltyright;

        center = new Vector2D(centerX,centerY);

        topleft         = new Vector2D(-FieldModel.HFL,FieldModel.HFW);
        bottomleft      = new Vector2D(topleft.x,-topleft.y);

        topright        = new Vector2D(-topleft.x,topleft.y);
        bottomright     = new Vector2D(-topleft.x,-topleft.y);

        topcenter       = new Vector2D(0,FieldModel.HFW);
        bottomcenter    = new Vector2D(0,-FieldModel.HFW);

        gltopouter      = new Vector2D(-FieldModel.HFL,FieldModel.HGW); 
        gltopinner      = new Vector2D(gltopouter.x+FieldModel.GOAL_AREA_DEPTH,gltopouter.y); 

        glbottomouter   = new Vector2D(-FieldModel.HFL,-FieldModel.HGW); 
        glbottominner   = new Vector2D(glbottomouter.x+FieldModel.GOAL_AREA_DEPTH,glbottomouter.y); 

        grtopouter      = new Vector2D(FieldModel.HFL,FieldModel.HGW); 
        grtopinner      = new Vector2D(grtopouter.x-FieldModel.GOAL_AREA_DEPTH,grtopouter.y); 

        grbottomouter   = new Vector2D(FieldModel.HFL,-FieldModel.HGW); 
        grbottominner   = new Vector2D(grbottomouter.x-FieldModel.GOAL_AREA_DEPTH,grbottomouter.y); 

        penaltyleft     = new Vector2D(FieldModel.PENALTY_DIST,0);
        penaltyright    = new Vector2D(-FieldModel.PENALTY_DIST,0);


        // scale to cm and translate by grid origin
        topleft         =   topleft.mul(100).add(center); 
        bottomleft      =   bottomleft.mul(100).add(center);   
        topright        =   topright.mul(100).add(center);     
        bottomright     =   bottomright.mul(100).add(center);
        topcenter       =   topcenter.mul(100).add(center);    
        bottomcenter    =   bottomcenter.mul(100).add(center); 
        gltopouter      =   gltopouter.mul(100).add(center);   
        gltopinner      =   gltopinner.mul(100).add(center);   
        glbottomouter   =   glbottomouter.mul(100).add(center);
        glbottominner   =   glbottominner.mul(100).add(center);
        grtopouter      =   grtopouter.mul(100).add(center);   
        grtopinner      =   grtopinner.mul(100).add(center);   
        grbottomouter   =   grbottomouter.mul(100).add(center);
        grbottominner   =   grbottominner.mul(100).add(center);

        penaltyleft     = penaltyleft.mul(100).add(center);
        penaltyright    = penaltyright.mul(100).add(center);


        // compute distances and distance vector table
        for(int i=0; i< gridWidth; i++) 
            for(int j=0; j< gridHeight; j++) {

                Vector2D current = new Vector2D(i,j);
                Vector2D basepoint;
                // distances to each model element

                // left boundary
                basepoint = nearestPoint(bottomleft,topleft,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = Vector2D.distance(basepoint,current);
                
                // left penalty
                vmatrix[i][j][1] = Vector2D.sub(penaltyleft,current);;
                dmatrix[i][j][1] = Vector2D.distance(penaltyleft,current);

                
                // right boundary
                basepoint = nearestPoint(bottomright,topright,current);
               
                vmatrix[i][j][2] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][2] = Vector2D.distance(basepoint,current);

                // right penalty
                vmatrix[i][j][3] = Vector2D.sub(penaltyright,current);;
                dmatrix[i][j][3] = Vector2D.distance(penaltyright,current);

                // top boundary
                basepoint = nearestPoint(topleft,topright,current);
               
                vmatrix[i][j][4] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][4] = Vector2D.distance(basepoint,current);
                
                // bottom boundary
                basepoint = nearestPoint(bottomleft,bottomright,current);
               
                vmatrix[i][j][5] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][5] = Vector2D.distance(basepoint,current);

                // center line
                basepoint = nearestPoint(bottomcenter,topcenter,current);
               
                vmatrix[i][j][6] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][6] = Vector2D.distance(basepoint,current);

                // left/circle half circle half
                
                // consider field side
                // left half
                double radius = FieldModel.CENTRE_CIRCLE_RADIUS*100;
                if(i<=centerX) {

                    // computing distance to left circle

                    // distance from point to center
                    double dc = Math.abs(Vector2D.distance(current,center));

                    // closest point on the circle is given by
                    // (center-current) * (|1-r/dc|)
                    // length of the vector is |dc -r| 

                    //if current point is inside the circle, flip vector 
                    vmatrix[i][j][7] = Vector2D.sub(center,current).mul((1-(radius/dc)));
                    dmatrix[i][j][7] = Math.abs(dc-radius);


                    // computing distance to right circle
                    // closest point is basicly the intersection of 
                    // the circle with the centre line, eather top, or bottom

                    // closer to top ?
                    //
                    if(j>=centerY) {
                        basepoint = new Vector2D(centerX,centerY+radius);
                    }
                    else
                        basepoint = new Vector2D(centerX,centerY-radius);

                    vmatrix[i][j][8] = Vector2D.sub(basepoint,current);
                    dmatrix[i][j][8] =  Vector2D.distance(basepoint,current);
                }
                // point is in the right half
                else {
                    
                    // closer to top ?
                    //
                    if(j>=centerY) {
                        basepoint = new Vector2D(centerX,centerY+radius);
                    }
                    else
                        basepoint = new Vector2D(centerX,centerY-radius);

                    vmatrix[i][j][7] = Vector2D.sub(basepoint,current);
                    dmatrix[i][j][7] =  Vector2D.distance(basepoint,current);
                    // computing distance to left circle

                    // distance from point to center
                    double dc = Math.abs(Vector2D.distance(current,center));

                    // closest point on the circle is given by
                    // (center-current) * (|1-r/dc|)
                    // length of the vector is |dc -r| 

                    vmatrix[i][j][8] = Vector2D.sub(center,current).mul(1-(radius/dc));
                    dmatrix[i][j][8] = Math.abs(dc-radius);

                    // computing distance to right circle
                    // closest point is basicly the intersection of 
                    // the circle with the centre line, eather top, or bottom
                }

                // compute goals
                Vector2D goalBasePoints[] = new Vector2D[3];
                int argmin = 0;
                double dist;
                double minDist = 1000000; 

                // left goal
                //
                // compute distances to each goal line segment
                goalBasePoints[0] = nearestPoint(gltopinner,gltopouter,current);
                goalBasePoints[1] = nearestPoint(glbottominner,glbottomouter,current);
                goalBasePoints[2] = nearestPoint(glbottominner,gltopinner,current);

                
                for(int k=0;k<3;k++) {
                    dist = Vector2D.distance(goalBasePoints[k],current);
                    if(dist<minDist) {
                        minDist = dist;
                        argmin=k;
                    }
                }

                vmatrix[i][j][9] = Vector2D.sub(goalBasePoints[argmin],current);
                dmatrix[i][j][9] = minDist;


                // doing the same for right goal


                goalBasePoints[0] = nearestPoint(grtopinner,grtopouter,current);
                goalBasePoints[1] = nearestPoint(grbottominner,grbottomouter,current);
                goalBasePoints[2] = nearestPoint(grbottominner,grtopinner,current);

                minDist = 1000000; 
                argmin=0;
                for(int k=0;k<3;k++) {
                    dist = Vector2D.distance(goalBasePoints[k],current);
                    if(dist<minDist) {
                        minDist = dist;
                        argmin=k;
                    }
                }

                vmatrix[i][j][10] = Vector2D.sub(goalBasePoints[argmin],current);
                dmatrix[i][j][10] = minDist;


            }
    }

}
