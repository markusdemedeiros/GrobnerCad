# A Geometric Constraint solver

## Background
**Geometric Constraint Solving** is a class of problems in computational geometery, centrally motivated by it's applications in computer aided design (CAD). The task at hand is to develop algorithms to resolve dimensioned drawings: collections of geometric elements which satisfy a set of constraints pertaining to their placement. One simple formulation of this problem takes place in 2 dimensional real space with geometric elements consisting of points and lines and dimensions consisting of line-line angles, point-point distnaces and point-line distance. Several other more general formulations are of practical use, but are often signifigantly more difficult and are unsolved in general. 

For this simple formulation, there have been several geometric constraint solvers developed not limited to numerical solvers, graph-theoretic solvers, and solvers derived from theorems in algebraic geometry. The latter is the slowest in general (and has at the worst case double exponential complexity) but generates the entire set of solutions as well as several critical pieces of information such as solution dimension that other solvers lack. In addition, it has been shown to be possible to reduce the practical time complexity of these algorithms by exploiting results in group theory. This will be the solver I build. 

## Theory


## Motivation
The applicibility of geometric constraint solving admits my program several classes of users:
- Researchers in computational algebraic geometry using it as a robust framework for examining the open problem of reducing the time complexity of Buchberger's Algorithm in a geometric context.
- Engineers using the complete solution set to examine cases which might cause their faster numerical solvers to fail
- Technicians using it to resolve underconstrained drawings, and rigorously communuicate issues arising from poorly constrained designs. 

On a personal note, I am a student of pure mathematics with interest in pure and applied algebra, I am a member of a engineering team in 3D design, and I am a amateur machinist. This project is the intersection of my most central interests. 

## User Stories (Implemented)
- As a user, I want to be able to add geometric objects to a list of geometric objects. 
- As a user, I want to be able to add geometric constraints to a list of geometric constraints that act on my list of geometric objects. 
- As a user, I want to see the exact space of polynomials which represent my drawing's solution.
- As a user, I want to be able to tell if my drawing is inconsistently constrained. 

## User Stories (not yet implemented)
- As a user, I want to be able to save and edit my lists of constraints at a later time, or in different text editing programs
- As a user, I want to have algebraic and geometric information about my solution space extracted into human readable formats
- As a user, I want to be able to tell the program to analyse my drawing and
  determine if it is over-, under-, or well-constrained. 
- As a a user, I want to see a rendering of my drawing if it is
  well-constrained. 
- As a user, I want to export a well-constrained drawing to human-readable
  formats such as PDF, or machine readable formats such as STEP. 
- As a user, I want to use group theoretic equivalencies to choose heuristically identical solutions in my solution space. 
- As a user, I want to choose from a finite number of structurally well-constrained by geometrically underconstrained solutions graphically.   
 
