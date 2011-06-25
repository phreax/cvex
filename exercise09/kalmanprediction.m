% predict new state and covariance matrix
function [x_pred,sigma_pred] = kalmanfilter(x_old, sigma_old,P)

x_pred = P*x_old;
sigma_pred = P*sigma_old*P'; % +Q

