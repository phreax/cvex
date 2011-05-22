% compute the equation matrix
% input:
%   xw,yw -- world coordinates of the corners
%   xc,yc -- chip coordinates of the corners

function M = calc_eq_matrix(xw, yw, xc, yc)

M = zeros(8,9);

% compute rows of the M matrix
for i=1:4
    row = i*2 -1;
    M(row,:) =  xc() * [0 0 0 0 0 0 xw(i) yw(i) 1];
    M(row,:) =  [xw(i) yw(i) 1 0 0 0 0 0 0] - M(row,:); 
    
    M(row+1,:) =  yc(i) * [0 0 0 0 0 0 xw(i) yw(i)1];
    M(row+1,:) =  [0 0 0 xw(i) yw(i) 1 0 0 0] - M(row+1,:); 
end



