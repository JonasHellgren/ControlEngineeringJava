# Lunar lander


## State space description

$
\dot{x}_1 = x_2; 
\dot{x}_2 = u - g
$


$
\begin{align}
x_{1,k+1} = x_{1,k} + dt x_{2,k};  
x_{2,k+1} = x_{2,k} + dt (u_k - g)
\end{align}
$



$
\begin{align}
x_{k+1} &= A x_k + B u_k \\
y_k &= C x_k + D u_k
\end{align}
$


$
\begin{bmatrix} x_{1,k+1} \\ x_{2,k+1} \end{bmatrix} =
\begin{bmatrix} 1 & dt \\ 0 & 1 \end{bmatrix} \begin{bmatrix} x_{1,k} \\ x_{2,k} \end{bmatrix} +
\begin{bmatrix} 0 \\ dt \end{bmatrix} u_k
$

$
A = \begin{bmatrix} 1 & dt \\ 0 & 1 \end{bmatrix}, \quad
B = \begin{bmatrix} 0 \\ dt \end{bmatrix}, \quad
C = \begin{bmatrix} 1 & 0 \\ 0 & 1 \end{bmatrix}, \quad
D = \begin{bmatrix} 0 \\ 0 \end{bmatrix}
$

