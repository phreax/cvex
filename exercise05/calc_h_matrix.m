% compute the transformation matrix h
% input:
%   xw,yw -- world coordinates of the corners
%   xc,yc -- chip coordinates of the corners

function H = calc_h_matrix(xw, yw, xc, yc)

H = zeros(8,9);

% compute rows of the H matrix
for i=1:4
    row = i*2 -1;
    H(row,:) =  xc(i) * [0 0 0 0 0 0 xw(i) yw(i) 1];
    H(row,:) =  [xw(i) yw(i) 1 0 0 0 0 0 0] - H(row,:); 
    
    H(row+1,:) =  yc(i) * [0 0 0 0 0 0 xw(i) yw(i) 1];
    H(row+1,:) =  [0 0 0 xw(i) yw(i) 1 0 0 0] - H(row+1,:); 
end



