% update predection given a measurement z

function [x,sigma] = kalmanfilter(x_pred, sigma_pred,H,z,R)

% Kalman Matrix, also called gain
gain = sigma_pred*H'*inv(H*sigma_pred*H'+R);

% update
x = x_pred + gain*(z-H*x_pred);
sigma = (eye(length(H)) - gain*H)*sigma_pred;
