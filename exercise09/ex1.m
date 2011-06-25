% exercise 1

function ex1()

% time grid 
grid = 0:0.1:10;

% velocity
v = 0.1

% position vector
X = arrayfun(@(t) motion(v,t),grid);
[filtered,S] = kalmanfilter(X,grid);

plot(grid,X,grid,filtered,'xr');
legend('simulated position','filtered position','Location','NorthWest');
xlabel('time');
ylabel('x Position');


