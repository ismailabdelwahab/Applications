# n_queens_problem
## Story
Also named Bezzel's problem:

In 1848 a chess composer named Max Bezzel published the following problem:

By how many ways can we put 8 queens on a chess board without having any of them attacking each other?

Knowing that a chess board is 8x8.

#### This code is a solution to this problem generalized to n queens on a n*n board
# Compilation
Clone the repository, then cd yourself to the generated directory, then do:

Simply to compile: 
    
     make
     
To clean object files:
    
    make clean
    
To remove executable:
    
    make mrproper
# By default n = 8, How to change n:
Open **lib/eight_queens_puzzle.h** with your favorite text editor

You will find **line 6**:
    
    #define SIZE_BOARD 8
    
Change **8** to any other value, then recompile.

*Exemple*: changing 8 by 10, will tell you by how many ways can you put 10 queens on a 10x10 board, without any attack.

*Warning: starting at 14, the computation starts to be really long*!
# Documentation
All function are commented in the lib/*.h files, above every function you will see comments that will explain you
what is the function doing.

All code can be found in src/*.c files.
    
# Repo's achitecture:
- **lib/**   { C Libraries }
  - eight_queens_puzzle.h  
  - TEST_eight_queens.h 
- **obj/**   { Object files }
  - eight_queens_puzzle.o
  - TEST_eight_queens.o
- **src/**   { C code files }
  - eight_queens_puzzle.c
  - TEST_eight_queens.c
- **makefile** { Used for compililation }
- **n_quees_problem.exe** { executable }

# Usage of code
You can use this code freely, just do not forget to mention this repository

https://github.com/ismailAbdelwahab/n_queens_problem

Reminder: copy-pasting is not always the best solution, try to uderstand what have been done,
before using this code.
