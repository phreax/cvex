% use gui to get image
% coordinates for the corners

function coord = get_corners(filename)

img = imread(filename);
image(img);
coord = ginput(4); % get corners
