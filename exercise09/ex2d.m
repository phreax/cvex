% ex 2d
% add uniform noise to measurement

function ex2d()

% time grid 
grid = 0:0.1:50;

% some time samples
samples = 0:1:50;

% velocity
v = 0.1;

% position vector
X = arrayfun(@(t) motion(v,t),grid);

% noisy measurements
measures = arrayfun(@(t) motion(v,t),samples) + 2*rand(1,length(samples))-1;
[filtered,S] = kalmanfilter(measures,samples);

stddev = cellfun(@(s) sqrt(det(s)), S);

plot(grid,X,samples,measures,'xg',samples,filtered,'-or',samples,-2*stddev,'-k',samples,2*stddev,'-k');
legend('simulated position','measurements','filtered','+- 2*standard deviation','Location','NorthWest');
xlabel('time');
ylabel('x Position');


