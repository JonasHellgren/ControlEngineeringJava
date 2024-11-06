# One dimensional integrator


$
\begin{align}
x_{k+1} &= x_k + dt u_k \\
y_k &= x_k
\end{align}
$


$
\begin{align}
x_{k+1} &= A x_k + B u_k \\
y_k &= C x_k + D u_k
\end{align}
$



$
A = 1, \quad B = dt, \quad C = 1, \quad D = 0
$


where dt is time step


![plots.png](..%2Fone_dim_integrator%2Fplots.png)