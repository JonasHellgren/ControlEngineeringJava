# Model predictive control explained - a practical approach

## Discrete state space

In the context of control systems, discrete state space refers to a mathematical model used to represent systems with states that evolve at discrete intervals, typically described by time steps rather than continuous evolution. This model is commonly applied in digital control and computer-based simulations, where systems update at discrete moments.

In a discrete state-space representation, the model is characterized by a set of linear equations that describe how the system transitions from one state to the next. The standard form for a discrete-time linear state-space system is:


![discrete_state_space.png](..%2Fmpc_method%2Fdiscrete_state_space.png)


## MPC briefly

Model Predictive Control (MPC) is an advanced control strategy that optimizes the control actions of a system by predicting its future behavior over a specific time horizon. It is widely used in systems where dynamic constraints are crucial, such as in process control, robotics, and automotive applications. The basic idea behind MPC is to use a model of the system to predict its future states and make optimal control decisions based on these predictions.

Key Concepts of MPC:
Prediction Model: MPC relies on a mathematical model (usually a discrete state-space model) of the system to predict its future behavior over a certain horizon. This model takes into account current state and input conditions to estimate future outputs.

Finite Time Horizon: MPC considers a limited time horizon, called the prediction horizon, over which it optimizes control actions. Although the control action is optimized over this horizon, only the first control action is applied. At the next time step, the entire optimization is repeated (known as a receding horizon approach).

Optimization: MPC optimizes an objective function, typically aiming to minimize some combination of tracking error (deviation from desired states) and control effort (to avoid excessive input use). This optimization is subject to constraints that the system must satisfy, such as limits on state values, control inputs, or output performance.

Constraints Handling: MPC is particularly valued for its ability to handle constraints directly in the optimization process. Constraints could include physical limits on control actions (like throttle range in a vehicle), safe operational boundaries (like maximum temperature), or even environmental constraints.


#  MPC QP formulation
![HandF.png](..%2Fmpc_method%2FHandF.png)

where:

u: The vector of control actions over the prediction horizon.

H: The Hessian matrix, which is symmetric and positive semi-definite. It encodes information about the quadratic costs on the control inputs and system states.

f: The gradient vector, which represents the linear part of the cost function and typically arises from the setpoints or references that the system should track.


![HandFDefinitions.png](..%2Fmpc_method%2FHandFDefinitions.png)


![img.png](img.png)



## Matrix and vector sizes

h: Prediction horizon (number of time steps over which future predictions and optimizations are made).

n: Number of states (dimensionality of the state vector

x, describing the internal system behavior).

p: Number of outputs (dimensionality of the output vector

y, representing the measurable or tracked system performance indicators).

![matrix_sizes.png](..%2Fmpc_method%2Fmatrix_sizes.png)