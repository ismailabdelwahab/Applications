#include<stdio.h>
#include<string.h>

/* You can change the size of the board in the file: eight_queens_puzzle.h */
#include "eight_queens_puzzle.h"
#include "TEST_eight_queens.h"

void initializeChessBoard(int board[][SIZE_BOARD])
{
    for (size_t i = 0; i < SIZE_BOARD; i++)
        for (size_t j = 0; j < SIZE_BOARD; j++)
            board[j][i]=0;
}

bool attackOnRow( int board[][SIZE_BOARD], int column, int row)
{
    while(--column >= 0)
        if(board[column][row])
            return true;
    return false;
}

bool attackOnDiagonals( int board[][SIZE_BOARD], int column, int row)
{
    int c = column;
    int r = row;
    //DIAGONAL to the top-left corner.
    while(--c >= 0 && --r >= 0)
        if(board[c][r])
            return true;
    //DIAGONAL to the bottom-left corner
    while (--column >=0 && ++row < SIZE_BOARD)
        if(board[column][row])
            return true;
    return false;
}

bool isAttacked( int board[][SIZE_BOARD], int column, int row )
{
    return  ( attackOnRow(board,column,row) ||
            attackOnDiagonals(board,column,row));
}

void seeChessBoard(int board[][SIZE_BOARD])
{
    printf("Printing the board :\n");
    for (size_t i = 0; i < SIZE_BOARD; i++)
    {
        for (size_t j = 0; j < SIZE_BOARD; j++)
            printf("%d ",board[j][i]);
        printf("\n");
    }
}

int find_all_solutions_n_queens(int board[][SIZE_BOARD],int column, int row, int nb_queens_set)
{
    int counter_of_solution=0;
    if(nb_queens_set == SIZE_BOARD)
        //This mean we found "1" solution. So we return "1"
        return 1;
    while( row < SIZE_BOARD)
    {
        if( !isAttacked(board, column, row) )
        {
        //Add the queen of this spot
            board[column][row]=1;
        /*Add the nb of next solutions found to our counter, 
          With recursiv call, and the parameters are:
          column = column+1 ==> We need to find the next queen, which can only be on the next column,
          row    =     0    ==> because we test all the rows of the next column to place this queen. */
            counter_of_solution +=  find_all_solutions_n_queens(board, column+1, 0, nb_queens_set+1);
        //We backtrack and remove the queen, so we can place it elsewhere on the actual column.
            board[column][row]=0;
        }
        //We increment the row to test the next cell.
        ++row;
    } 
    return counter_of_solution;
}
/************************ Main of program ***************************************/
int main(void)
{
    // If you do not want to launch the test, comment the line bellow.
    TEST_LAUNCH_ALL_TESTS();
    
    printf("\tThis program can solve the Bezzel problem:\n");
    printf("In 1848 he found the following problem:\n  \"By how many ways can we put 8 queens on a chess board");
    printf(" without having any of them attacking each other?\"\n");
    printf("\n\tOf course this problem have been extend to \"n\" queens later.\n\n");
    printf("   By default this program will resolve the 8 queens problem.\n");
    printf(" But you can go and see to code to try a different number of queens.\n\n");
    
    printf("   Initializing chess board ...\n\n");
    int chessBoard[SIZE_BOARD][SIZE_BOARD];
    initializeChessBoard( chessBoard );
    
    //If you want to see the board, uncomment the line below
    //seeChessBoard( chessBoard );
    
    //Solution of the problem.
    printf("The number of possible placement of %d queens are:\n",SIZE_BOARD);
    printf("  Calcultating the solution, please stand by...   NB =");
    printf(" %d\n",find_all_solutions_n_queens(chessBoard,0,0,0) );
}
