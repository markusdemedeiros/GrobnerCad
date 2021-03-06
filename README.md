# GrobnerCAD: A Geometric Constraint Generation Engine

## Summary

This project is written in fulfillment of UBC's Computer Science 210 Software Contruction project. It is a graphical frontend, written in Java, which allows the user to edit 2D dimensioned drawings and generates a system of polynomial equations representing the user's model. A demonstration of the functionality can be found [here](https://youtu.be/HW87QbK1Wi4). 


Below you will find the sections of the README required by the CPSC 210 project, although not all of it is relevant in the final build. 


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
- As a user, I want to be able to save and edit my lists of constraints at a later time, or in different text editing programs
- As a user, I want to be able to load systems of geometric constraints I have previously specified for processing. 

## User Stories (not yet implemented)
- As a user, I want to have algebraic and geometric information about my solution space extracted into human readable formats
- As a user, I want to be able to tell the program to analyse my drawing and
  determine if it is over-, under-, or well-constrained. 
- As a a user, I want to see a rendering of my drawing if it is
  well-constrained. 
- As a user, I want to export a well-constrained drawing to human-readable
  formats such as PDF, or machine readable formats such as STEP. 
- As a user, I want to use group theoretic equivalencies to choose heuristically identical solutions in my solution space. 
- As a user, I want to choose from a finite number of structurally well-constrained by geometrically underconstrained solutions graphically.   

## Instructions for Grader
- You can generate the first required event by clicking one of the top two buttons on the left. These add geometric elements to the space!
- You can generate the second required event by clicking any of the buttons below the first two on the left. These add the algebraic elements to the space! 
- You can locate my visual component looking at the big panel in the middle! If you click on an element you added it will select it (and if there is any ambiguity, it will prompt you to choose an element). If you drag starting not on a selected element, it pans the plane. A drag starting on a selected element moves all selected elements and recursively repaints the picture.
- You can save the state of my application by clicking file > save on the menu bar. It will translate that graphical data you just put in with the mouse into a proper algebaric system, and then save it alongside the graphical data to a file.
- You can reload the state of my application by clicking file > open on the menu bar. Now, not all files generated by the CLI app can be loaded- you can only load those which have their graphic data specified in the file. Also- you have to pan the screen to see the loaded in data afterwards, it's a known bug I am working in fixing. 

*To the Graders: I have become aware that some of my more ambitious user stories, especially those pertaining to analysis of the solutions, are not possible using my current solver in Java (the algebra package I am using is not designed for my use case and has signifigant issues in both preformance and accuracy).*

*By the time this course is done I will have the skills in mathematics needed to write my own, more purpose-build Buchberger solving algorithm in (probably) Julia which will interface with the files this program saves. Buchberger's algorithm is invariant under the physical placement of the elements in space, so the new graphics data I am saving is only so this program can read it's own creations. I want this program to be able to read and write to .sys files WITH the graphics specified so I can visually design complex real life test cases, but it shouldn't be able to read a .sys file without graphics info so that I can unleash my new solver on systems with un-drawable algebraic and geometric systems. I hope that explains part of why my save/load is so odd, while some of the weird coupling and cohesion is admittedly poor design which I hope to tackle in the next phase, the idea is motivated by something good :)*

## Phase 4 Task: 2
I will explain the type hierarchy I implement to represent objects in ui.gui.mainwindow.component.

The abstract class Drawable represents an object which can be drawn to the screen. It is designed to handle all of the common functions, like recomputing coordinates, being drawn to a graphics2D object, detecting clicks, etc. 

The classes GraphicalLine and GraphicalPoint extends Drawable, overriding the above listed functions to display lines and points to the screen.

The abstract class PointLabel also extends drawable, it represents constraint labels attached to GraphicalPoints. One special type of pointlabel, the SquarePointLabel, is an abstract class representing point labels that have a square icon. I currently have implemented two concrete types (constraintSetXLabel and ConstraintSetYLabel) which are different types of square Labels. 

Similarly, a Linelabel is an object attached to a line. SquareLineLabel is a special type of line label, and is implemented by three distinct constraints.

Finally I have one more concrete Drawable subtype- the ConstraintCoincidentLabel. It is neither a Linelabel or Pointlabel because it it behaves more like a line (open my program to see what I mean), so I simply have it as a subtype of GraphicalLine. 

All of my constriant types implement DrawableConstraint, which allows me to turn them into their nongraphical (model) counterparts to solve or save to a file. 

## Phase 4: Task 3

There are several places in my code with questionable design decisions, but for now I will focus on what I think is the most critical. 

- Improve Cohesion in coordinate transformations: All of my drawable elements use many different constraint systems which I have explained in a comment. However, this leads to lots of duplicate code and passing around unnessecary variables which is making development hard at this scale. I will abstract the coordinate transformation methods from that class in DrawingEditor, and refactor my Drawable elements to let the new Coordinate types handle all these computations so they do not need to "know" the nuts and bolts of how the editor is handling coordinates and can just be given the peritnent information. This increased robustness would allow me to add more nuanced coordinate operations, like zoooming the screen in and out.

- Improve Cohesion in drawable creations: Right now, I have a signifigant amount of duplicate code in DrawingComponent pertaining to the creation of objects.

- Improve Coupling in Solver: Make solver take a Fullsystem instead of two lists of geometric and algebraic elements. A solver only makes sense in the context of a full system of compatable algebra and geometry, so passing it both components individually could lead to situations which are undefined (like a constraint on a point which is not there). This will make my solver more robust.  
