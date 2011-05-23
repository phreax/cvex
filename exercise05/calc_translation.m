%{ 
calculate translation vector from scaled transformation
matrix H
first the rotation matrix R must be retrieved
from H. r1 and r2 are already known (h1 and h2).
The last column can be computed as the orthogonal
vector to r1 and r2:

    r3 = r1 x r2

With the rotation matrix the translation vector
can be obtained easily. Remember that:

    h3 = -Rt

    <=> t = -R^(-1) * h3
%}

function t = calc_translation(H)

R = calc_rotation(H);

t = inv(-R) * H(:,3);

