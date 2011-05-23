%{ 
compute rotation matrix
r1 and r2 are already known (h1 and h2).
The last column can be computed as the orthogonal
vector to r1 and r2:

    r3 = r1 x r2
%}

function R = calc_rotation(H)
r3 = cross(H(:,1),H(:,2));

R = [H(:,1), H(:,2), r3];
