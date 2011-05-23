%{
    - solve equations using singular value decomposition
        
            M*h = 0 <=> h = ker(M)

            assuming the last coloumn of the singular 
            value matrix is zero, the last coloumn of
            V* yields ker(M) ( the vector mapped onto zero)
    - compute rotation matrix and translation matrix
%}

function H = solve_eq(M) 

% do single value decomposition

[U D V] = svd(M);

% last coloumn of V is our solution
h = V(:,end);

% reshapre solution into a 3x3 matrix

H = reshape(h,3,3)';
