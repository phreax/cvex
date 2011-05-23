% scale from pixel coordinates to meters
function coordc = scale_im_coord(coordim) 

imheight = 480;
cheight = 13e-3;

coordc = coordim * (cheight/imheight);

