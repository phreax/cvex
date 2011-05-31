% Computer Vision: Exercise 06
% Michael Thomas 4282523; Jan Swoboda 4288773

Exercise 1
=========

As representation of our world, a 800x600 grid was chose, in units of 1cm.
The computation of the lookuptable, which maps each point in the grid to a vector of force
vectors, representing the attractive force to each of the 11 model elements is done in
*DistanceTable.java*.
A visualization of the field and the force matrix is provided by *FieldGraphicTest.java*:

    $ java exercise06.FieldGraphicTest

Exercise 2
=========

The task was to extract all white points (points of the field lines) and transform
them according extrinsic camera parameters. As translation vector $t = (0,0,30)$ was used
as the height of the camera is known in advance. Also we use a unit of 1cm, as our
world grid. This is done in *FieldLineExtractor.java*
For rotation, the pitch angle in radians of $\alpha = 0.2$ was used. For the intrinsic
parameter of the camera, the chip size of 13mm in height a focal length of 38mm is used, 
as in the previous exercise. From that, the scale factor $\phi = \delta * f = 1.3/460 *3.8$ is calculated.
We then calculate the rotation matrix and the homography Matrix $H = (r1 r2 -Rt)$. The
mapping from Pixel coordinates to Field coordinates was done according to the formular:

$(x,y,1)^T = f_1(H^{-1}K^{-1}(p''_x, p''_y, 1)^T)$

where f1 is the normalization to the z-Coordinate $f_1(x,y,z) = (x/z,y/z,1)$
Unfortunatly, the result was quite poor. It seemed that almost  all points were mapped
to a narrow horizontal line. Also many of them were outside the grid. We tried
everything, translating coordinates by the grid center, different scaling, orientations
etc. nothing seemed to improve or change the result. 

You can view the extracted points and the transformed points with *FieldLineExtractorTest.java*:

    $ java exercise06.FieldLineExtractorTest

Exercise06
==========

The Expectation Maximization Algorithm is Implemented in *GlobalLocalisation.java*.
It contains computation of the center of mass, total force and total angular moment.
The forces are accumulated and weighted by the distances of to all 11 model elements, then
the mean force for all forces of each point is calculated.
In the M-Step the data points in the pointclouds are rotated and translated according
to the compute force and moments. 
The Code was not tested, as we had problems with the Point extraction and transformation
and we also ran out of time. But anyway, it should work. The last thing
to do, would be to adapt the coefficients for for translation and rotation angle.
$t = \alpha * F$ and $\theta = \beta * M$, where \alpha and \beta a set to 1.0 by now. 








