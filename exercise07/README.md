% Computer Vision: Exercise 06
% Michael Thomas 4282523; Jan Swoboda 4288773

Exercise 1
=========

The Implementation of the naive Stereo Matching Algorithm is
implemented in *StereoMatch.java*, method *computeDisparity()*. For
dimilarity measure, the sum of squared distances is used, with a max
distance of $maxdist = m^2*300$ where m is the size of the patch.
Values above this distance are classified as not matched.

A Demoprogramm with no further input is provided (maxDisparity = 64, patchsize=5):

    $ java exercise07.StereoMatchingTest


Results
-------

![5x5 Patch](Disparity_5x5.png)
![11x11 Patch](Disparity_11x11.png)

The first image was made with a 5x5 patch, the second with a 11x11 patch.
Using a bigger Patch yields less matching errors, but you see that smaller details are match more unprecise.


Exercise 2
=========

The enhanced algorithm which makes usage of Dynamic Programming to find
a contious disparity match for each epipolar line was in implemented in
*computeDisparityDP()*.
Unfortunatly it is quite computational expensive, so I was not able to
retrieve some results. Maybe there are some possibilities for optimization
othere wise I will not be able to run it on my 2.4GHz Core Duo. 
A demo can be executed with:

    $ java exercise07.StereoMatchingDPTest


