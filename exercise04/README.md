Exercise 04: Harris Corner & SURF Features 
================================

Packages
--------

New in **graphic**:

- `Integral`:    computing integral images
- `Convolution`: added efficient haar wavelet transform, using integral
                 images

      
**exercises04**:

- `HarrisCorner`:  modified Harris Corner Detector, using only Determinant
                   of the hessian as blob response
- `SURF:`       : Computing Surf Features
- `SURFDescriptor: ` representation of a 16x4 surf descriptor vector


Execution
---------

Classes can be executed by simple running the java vm:

- java exercise04.SURFTest.java 


Remarks
-------

Corner Detector works well, but a better local maxima algorithm is needed, 
to only allow one pixel as representative for an edge.
Could be done with the Neubeck Algorithms [1].

Creadits
--------

* Course: Computer Vision 2011
* Authors: Jan Swoboda, Michael Thomas

Links
-----

[1] http://www.vision.ee.ethz.ch/publications/papers/proceedings/eth_biwi_00446.pdf
