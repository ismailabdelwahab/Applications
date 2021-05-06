#!/usr/bin/env python3
import sys
import os
import subprocess
############# Check Args #########################
def checkArgumentLine():
    if( len(sys.argv) != 2 ):
        print("    Error: One file expected as argument.")
        with open("src/PROPER_USAGE.txt", 'r') as f:
            for line in f.read().splitlines():
                print(line)
        sys.exit()

############### Manage Data #####################
def getNB_COL( board_lines ):
    return max( [len(line) for line in board_lines] )
def getNB_ROW( board_lines ):
    return len( board_lines )

def extractBoardFromFile( filepath ):
    """ Return all the line of "filepath", 
    (except the one beginning with "#"), as an array."""
    with open( filepath ) as f:
        board_text_file = f.read().splitlines()
    # Return a version with comments REMOVED:
    return [line for line in board_text_file if line[0]!='#']

def copyFileToOpenedFile( source_file_path, opened_file ):
    """ Copy the content of source_file_path into opened_file """
    with open( source_file_path, 'r' ) as f:
        opened_file.writelines( line for line in f.readlines() )

############### Manage PML file ##################
def pml_addHeader( pml_file, NB_COL, NB_ROW, first_hole, NB_PEGS):
    header = "/*############# DEFINE SECTION #################### */\n\
#define NB_COL {}\n\
#define NB_ROW {}\n\
/****** Initial state ************/\n\
#define first_hole_X {}\n\
#define first_hole_Y {}\n\
#define nb_initial_pegs {}\n\
/******* procedure NEVER's variables *********/\n\
#define p (nbPegs==1)\n".\
format(NB_COL, NB_ROW, first_hole[1], first_hole[0], NB_PEGS)
    pml_file.write(header)
    copyFileToOpenedFile("src/global_variables.txt", pml_file)

def pml_addGameCode(pml_file, NB_ROW, NB_COL):
    copyFileToOpenedFile("src/procedure/procedure_header.txt",pml_file)
    # Add i indexe possibilities:
    i_range = "if "
    for i in range(NB_ROW):
        i_range += "::i={} ".format(i)
    i_range += "fi\n"
    pml_file.write(i_range)
    # Add j indexe possibilities:
    j_range = "if "
    for j in range(NB_COL):
        j_range += "::j={} ".format(j)
    j_range += "fi\n"
    pml_file.write(j_range)
    copyFileToOpenedFile("src/procedure/procedure_footer.txt",pml_file)

def pml_addInit( pml_file, UNDEF ):
    copyFileToOpenedFile("src/init/init_header.txt", pml_file)
    #Adding UNDEF squares on the board:
    for coord in UNDEF:
        pml_file.write("r[{}].c[{}]=UNDEF; ".format(coord[0],coord[1]) )
    copyFileToOpenedFile("src/init/init_footer.txt", pml_file)

def pml_addNever( pml_file ):
    copyFileToOpenedFile("src/never/never_procedure.txt", pml_file)
################ Main ###########################
def main():
    ### VARIABLES ####
    NB_PEGS, NB_ROW, NB_COL, i, j = 0, 0, 0, 0, 0
    first_hole = (0,0)
    UNDEF = []
    
    ### Script ####
    board_lines = extractBoardFromFile( sys.argv[1] )
    for line in board_lines:
        j=0
        for char in line:
            if( char == 'o' ):
                NB_PEGS+=1
            if( char == 'X' ):
                 first_hole = (i,j)
            if( char == ' ' ):
                UNDEF.append( (i,j) )
            j += 1
        i += 1 
    NB_ROW = getNB_ROW( board_lines )
    NB_COL = getNB_COL( board_lines )
    
    pml_file = open("peg_solitaire.pml", "w+")
    pml_addHeader( pml_file ,NB_COL, NB_ROW, first_hole, NB_PEGS )
    pml_addGameCode( pml_file, NB_ROW, NB_COL )
    pml_addInit( pml_file, UNDEF )
    pml_addNever( pml_file )
    pml_file.close()
    print(" [#] PML file was created successfully.\n\
    Please check [peg_solitaire.pml] to view the new generated code.\n\
 [#] Use compile.sh to compile and execute this project.\n\
    ./compile.sh [-v]")

if __name__ == '__main__':
    checkArgumentLine()
    main()
