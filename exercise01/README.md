Exercise 01: YUV and Demosaicing
================================


Packages
--------

- graphic
    
    > Some classes for manipulating images i wrote for convenience

- uebung01

     > the actual code for the exercice

Execution
---------

Main Classes are:
    
- uebung01.YUV
- uebung01.Demosaicing

Classes can be executed by simple running the java vm:

- java uebung01.YUV
- java uebung01.Demosaicing


Remarks
-------

For some strange reason i couldnt stop getting bizare artefacts in both, converting
to YUV and doing Bayer pattern. Seems like java BufferedImages have strange representation
for RGB values and TYPE_BYTE_GRAY also. The RGB values seem to be in a range from
-128..128 some how, I could not figure out what was going on. Somtimes the tend bigger than 255
also, so I cut them. For the YUV conversion I added 128 to the UV channels, in order to get a positive
value to fit in the histogram array.
I need to check out javas image representation more carefully but i did not have time for that.
Concerning the YUV Histogram, it wasent possible to get anything to see because there only appear a
few distinct values in the image I tried, so most of the image is black.

Creadits
--------

* Course: Computer Vision 2011
* Authors: Michael Thomas, Jan Swoboda

