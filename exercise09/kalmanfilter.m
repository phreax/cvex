% compute estimation
% given a measurement vector

function [X,S] = kalman(measure,samples)

% initial estimates 
x     = [measure(1);0];
sigma = eye(2); 

% initial zpdate


X(1) = measure(1);
S{1} = sigma;
% covariance of measure noise
for i=2:length(samples)
    delta_t = samples(i)-samples(i-1);
    [P,H,R] = kalmanmodel(delta_t);

    [x_pred, sigma_pred] = kalmanprediction(x,sigma,P);
    
    [x,sigma] = kalmanupdate(x_pred,sigma_pred,H,measure(i),R);
    if(i<5)
        str = ['Covariance matrix of kalman update: ',num2str(i-1)];
        disp(str);
        sigma
    end

    S{i} = sigma;

    X(i) = x(1);
end
    
