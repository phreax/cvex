% compute lambda scale factor
% for H matrix 
%   where lambda * r1 = h1 
%   and   lambda * r2 = h2
% 
% use following fact:
%   let H = ( r1 r2 -Rt)
%   |r1| = 1 and |r2| = 1
%   thus lambda1 = |h1| and lambda2 = |h2|
%
% use maximum from both lambdas
% and scale H matrix accordingly

function Hs = scale_h_matrix(H)

h1 = H(:,1);
h2 = H(:,2);

lambda = max(norm(h1),norm(h2));

Hs = H * (1/lambda); 
