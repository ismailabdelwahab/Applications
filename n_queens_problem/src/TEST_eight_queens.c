#include<assert.h>

#include "TEST_eight_queens.h"
#include "eight_queens_puzzle.h"

/* WARNING : THESE  Test were written with SIZE_BOARD equal to 8*/

void TEST_LAUNCH_ALL_TESTS(void){
    if(SIZE_BOARD >= 7)
    {
        TEST_initializeChessBoard();
        TEST_attackOnRow();
        TEST_attackOnDiagonals();
        TEST_isAttacked();
    }
}

/* INIT the array for all tests. */
int chess_board[SIZE_BOARD][SIZE_BOARD];

void TEST_initializeChessBoard(void)
{
    initializeChessBoard(chess_board);
    for (int i = 0; i < SIZE_BOARD; i++)
        for (int j = 0; j < SIZE_BOARD; j++)
            assert(!chess_board[j][i]);
}

void TEST_attackOnRow(void)
{
    /* Setting 1 queen on the board. */
    chess_board[2][3]=1;
    assert(attackOnRow(chess_board,3,3));
    assert(attackOnRow(chess_board,6,3));
}

void TEST_attackOnDiagonals(void)
{
    assert(attackOnDiagonals(chess_board,3,2));
    assert(attackOnDiagonals(chess_board,5,6));
}

void TEST_isAttacked(void)
{
    /* All the previous coordinates must be seen as
            attacked by this function too. */
    assert(isAttacked(chess_board,3,3));
    assert(isAttacked(chess_board,6,3));
    assert(isAttacked(chess_board,3,2));
    assert(isAttacked(chess_board,5,6));
}
