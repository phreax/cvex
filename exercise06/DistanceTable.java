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
    Vector2D dmatrix[][][];

    public DistanceTable() {

        vmatrix = new Vector2D[gridWidth][gridHeight][11];
        dmatrix = new Vector2D[gridWidth][gridHeight][11];
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
        double t = Vector2D.dot(Vector2Dsub(p,u),r) / Vector2D.dot(r,r);

        // projection is on within the linesegment -> nearest point
        // is the next endpoint
        if(t<0.0) return u;
        if(t>1.0) return v;

        // else return projection x = u + t*r
        return( Vector2D.add(u,Vector2D.mul(r,t)));
    }

    private static Vector2D centerGridPoint(Vector2D p) {

        // translate to origin at center
        Vector2D pc = Vector2D.sub(p,center);
        return pc;
    }

    private argmin(

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

        penaltyleft     = new Vector2D(-FieldModel.HFL+FieldModel.PENALTY_DIST,0);
        penaltyright    = new Vector2D(FieldModel.HFL-FieldModel.PENALTY_DIST,0);


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
            for(int j=0; j< gridWidth; j++) {

                Vector2D current = new Vector2D(i,j);
                Vector2D basepoint;
                double dist;
                // distances to each model element

                // left boundary
                basepoint = nearestPoint(bottomleft,topleft,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;
                
                // left goal
                basepoint = nearestPoint(bottomleft,topleft,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;

                // left penalty
                dist = Vector2D.distance(penaltyleft,current);

                vmatrix[i][j][0] = Vector2D.sub(penaltyleft,current);;
                dmatrix[i][j][0] = dist;

                
                
                // right boundary
                basepoint = nearestPoint(bottomright,topright,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;

                // right goal

                // right penalty
                dist = Vector2D.distance(penaltyright,current);

                vmatrix[i][j][0] = Vector2D.sub(penaltyright,current);;
                dmatrix[i][j][0] = dist;

                // right circle half

                // top boundary
                basepoint = nearestPoint(topleft,topright,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;

                
                // bottom boundary
                basepoint = nearestPoint(bottomleft,bottomright,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;

                // center line

                basepoint = nearestPoint(bottomcenter,topcenter,current);
                dist = Vector2D.distance(basepoint,current);
               
                vmatrix[i][j][0] = Vector2D.sub(basepoint,current);
                dmatrix[i][j][0] = dist;

                // left/circle half circle half
                
                // consider field side
                // left half
                if(i<=gridWidth) {
                    dist = Math.abs(Vector2D.distance(current,center) - FieldModel.CENTER_CIRCLE_RADIUS);


