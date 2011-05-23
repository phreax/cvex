%{

Camara calibration

    - get image corners of a din A4 paper
      in the order: topleft, topright, bottomleft, bottomright
    - world coordinates of the corners are:
      (0,0), (0.21,0), (0,0.297), (0.21,0.297)
    - transform image coordinates to chip cordinates (in meters)
      using scale factor: 13e-3/480 (chipheight/imgheight)
    - assemble the eight equations as matrix
    - solve equations using singular value decomposition
        
            M*h = 0 <=> h = ker(M)

            assuming the last coloumn of the singular 
            value matrix is zero, the last coloumn of
            V* yields ker(M) ( the vector mapped onto zero)
    - compute rotation matrix and translation matrix
      from transformation matrix
    . compute orientation angles of the camara and position

%}

function H = main(filename)

% get corners coordinates from user
disp('please mark corners in the order: topleft,topright,bottomleft,bottomright');

[cornersx cornersy] = get_corners(filename);

% scale to meters (chip coordinates)
disp('corners in meters are:');
xc = scale_im_coord(cornersx)
yc = scale_im_coord(cornersy)

% world coordinates of corners
disp('world coordinates are:');
xw = [0.0; 0.21; 0.0; 0.21]
yw = [0.0; 0.0; 0.297; 0.297]

% assemble equations
disp('assemble equations');
M = calc_eq_matrix(xw,yw,xc,yc);

% solve equation
disp('recover transformation matrix:');
H = solve_eq(M)

% calculate camara position
disp('camara position is:');
t = calc_translation(H)

% calculate camara orientation
R = calc_rotation(H);
disp('camara orientation (pitch, yaw, roll):');

angles = calc_cam_angles(R)

disp('completed');




     
