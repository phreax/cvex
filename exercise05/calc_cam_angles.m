% Compute camara angles from rotation matrix
% formulars from http://en.wikipedia.org/wiki/Yaw,_pitch,_and_roll#Tait-Bryan_angles

function angles = calc_cam_angles(R)

pitch = atan2(-R(3,1), sqrt(R(1,1)^2 + R(2,1)^2));
yaw = atan2(R(2,1)/cos(pitch),R(1,1)/cos(pitch));
roll = atan2(R(3,2)/cos(pitch),R(3,3)/cos(pitch));

angles = [pitch yaw roll];



