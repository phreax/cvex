% configure the model (system matrices, covariance matrices)
% for the kalman filter
function [P,H,R] = kalmanmodel(delta_t)

P = [1 delta_t; 0 1];  % system matrix
H = [1 0];             % measure matrix
R = 1;                 % measure covariance

