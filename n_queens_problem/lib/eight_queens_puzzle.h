#ifndef __8_QUEENS_PUZZLE__
#define __8_QUEENS_PUZZLE__

/* Change the value here to try the problem with an other size of board,
Default size is 8x8 but you can try with any number, starting from 1 */
#define SIZE_BOARD 8
/* NOTE:  -2 and 3, are sizes where the problem have 0 solutions.
          -Over 13, the computation starts beeing slow. */

#include<stdbool.h> //Needed by the functions to return booleans.


/*  Initialize all cells of a variable  {board[][]} at 0. */
void initializeChessBoard(int board[][SIZE_BOARD]);

/*  -Check if there is a queen on the same row.
        (check only on the left of {board[column][row]} as
        our algorithm will fill the board from top left to bottom right)
    -Return  1 if there is another queen on the same row.
             0 if there is NO QUEEN ATTACKING   */
bool attackOnRow( int board[][SIZE_BOARD], int column, int row);

/* NOTE : We do not have to check if there is a queen on the current column,
            because their placement depends on the column we are on.

    Meaning that if we add a queen, we automaticaly move to the next column. */

/*  Check if there is a queen of both diagonal of the cell we are on.
  ->toward the left only:   X 0 0 0 0 
                            0 X 0 0 0        # : is my cell ( <=> board[column][row] )
                            0 0 X 0 0        X : are all the cells checked by the function.
                            0 0 0 # 0 
                            0 0 X 0 0   */
bool attackOnDiagonals( int board[][SIZE_BOARD], int column, int row);

/*    regroup all the two functions above to know if
    { board[column][row] } is attacked by an already existing queen. */
bool isAttacked( int board[][SIZE_BOARD], int column, int row );

/* Print the board as a 2 dimentional array. */
void seeChessBoard(int board[][SIZE_BOARD]);

/* Find the number of positions where we have {SIZE_BOARD} queens on the board (by default 8). */
int find_all_solutions_n_queens(int board[][SIZE_BOARD],int column, int row, int nb_queens_set);


#endif