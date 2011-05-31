/**
 * GlobalLocalisation.java 
 *
 * Find the global position of a soccer robot, given a current
 * frame of its camera, the orientation and height of the camera.
 * 
 * The Points of the field lineas are extracted from the image and
 * projected onto the model, using the cameras paremers. 
 * An iterative expectation maximisation algorithm will then be applied
 * to translate and rotate the point cloud to the closest model elements, 
 *
 *
 *
* @version 0.1  2011-05-07
 * @author Michael Thomas, Jan Swoboda
 */

package exercise06;
import Jama.Matrix;
import graphic.*;
import exercise06.*;
import java.util.ArrayList;
import java.util.List;


public class GlobalLocalisation {

    private List<Vector2D> pointcloud;

    private DistanceTable dtable;

    private static final double MAX_ROT   = 0.034; // 2°
    private static final double MAX_TRANS = 10.0;  // cm


    // some heuristic cofficients for the rotation/translation
    private static final double TRANS_C = 1.0;
    private static final double ROT_C = 1.0;

    public GlobalLocalisation() {
        dtable = new DistanceTable();
        dtable.createTable();
    }

    private static Vector2D meanVector(Vector2D[] vectorlist) {

        Vector2D sumVector = new Vector2D(0,0);

        // sum up all vectors
        for(Vector2D v: vectorlist) {
            sumVector = sumVector.add(v);
        }

        // mean value of all vectors
        return sumVector.mul(1.0/vectorlist.length);

    }

    /** compute the mean of a list of vectors
     */
    private static Vector2D meanVector(List<Vector2D> vectorlist) {
        return meanVector((Vector2D[])vectorlist.toArray());
    }
    // mean of a double list
    //
    private static double mean(double[] values) {
        
        double sum = 0.0;
        for(double v : values) sum+=v;
        return sum/values.length;
    }

    /** compute forces for all points int the cloud
     */
    private Vector2D[] computeForces() {

        Vector2D[] forces = new Vector2D[pointcloud.size()];

        int count = 0;
        for(int i=0;i<forces.length; i++) {
           int x = pointcloud.get(i).getX();
           int y = pointcloud.get(i).getY();

           if(x>=0 && x<dtable.gridWidth &&
              y>=0 && y<dtable.gridHeight ) 
           {

               Vector2D[] distvectors = dtable.vmatrix[x][y];
               double[] distances = dtable.dmatrix[x][y];
                

               // compute normalizing factor
               double normf = 0;
               for(int k=0;k<distances.length;k++) {
                   normf += Math.exp(-distances[k]/10.0);
               }

               double weight;
               // compute gauss smoothed force
               Vector2D f = new Vector2D(0,0);
               for(int k=0;k<distances.length;k++) {
                   double dist = distances[k];
                   Vector2D dv   = distvectors[k];

                   weight = Math.exp(-dist/10.0);
                   f = f.add(dv.mul(weight));
               }


               // append to list
               forces[i] = f;
               count++;
           }

        }
        
        if(count < forces.length) {

            System.out.println("Some forces could not be computed!\nThis could mean that some points of the cloud are not inside the grid.");
        }
        return forces;
    }

    /** compute angluar moments around center of mass
     * for each point.
     * 
     * the angular moment is given by:
     * M = r x f
     *
     * where r is the distance vector p-c (c is the center)
     *
     * as we dont have vectors in 2d, we just use scalar
     * representation:
     *
     *  |M| = |r| * |f| * sin(r,f)
     *
     * sin(r,f) can be computed as:
     *
     * sin(r,f) = sin(acos((r*f)/(|r|*|f|))
     *          = sqrt(1-((r*f)/(|r|*|f|))^2)
     *
     * the oriantation is is given by: 
     * sign(r1 * f2 - r2*f1) (z coordinate of the cross product)
     *
     * all the sqrts are very expensive, maybe there is 
     * a better, faster approximation
     *
     */
    private double[] computeAngularMoments( Vector2D[] forces, Vector2D center) {

        double[] moments = new double[forces.length];
        for(int i=0; i<forces.length;i++) {

            Vector2D f = forces[i];
            Vector2D p = pointcloud.get(i);
            
            Vector2D r = p.sub(center);


            double magn = r.norm() * f.norm();
            
            double sinrf = Math.sqrt(1-(Math.pow(Vector2D.dot(f,r)/magn,2.0)));

            // direction (+/-)
            double dir = Math.signum(r.x*f.y - r.x*f.x);

            moments[i] = magn*sinrf*dir;
        }

        return moments;
    }

    /**  
     * transform point cloud given the new center of mass and
     * forces/angular moments
     *
     */
    private void transformPoint() {

        // center of mass
        Vector2D cof = meanVector(pointcloud);
        Vector2D[] forces = computeForces();
        double[] moments = computeAngularMoments(forces,cof);


        // accumulated forces & moments
        Vector2D totalforce = meanVector(forces);
        double totalmoment = mean(moments);

        List<Vector2D> newpointcloud = new ArrayList<Vector2D>();


        double phi = totalmoment * ROT_C;
        Vector2D t = totalforce.mul(TRANS_C);


        // scale to maximum translation/rotation
        if(Math.abs(phi) > MAX_ROT) {
            phi = MAX_ROT * Math.signum(phi);
        }
        
        double tlength = t.norm();
        if(tlength > MAX_TRANS) {
            t = t.mul(MAX_TRANS/tlength);
        }

        for(Vector2D p : pointcloud) {

            // rotate vector by phi and rotate by t
            Vector2D pnew = p.rotate(phi,cof).add(t);

            newpointcloud.add(pnew);
        }

        // reset point cloud to new point cloud
        this.pointcloud = newpointcloud;
    }
}

