% use gui to get image
% coordinates for the corners

function coord = imcoord(filename)

image = imread(filename);
imshow(image);
coord = ginput(4); % get corners
