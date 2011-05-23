% use gui to get image
% coordinates for the corners

function [x y]= get_corners(filename)

img = imread(filename);
image(img);
[x y] = ginput(4); % get corners
