/*############# DEFINE SECTION #################### */
#define NB_ROW 1
#define NB_COL 6
// Board is like: [. X . . . .] "." is a peg, "X" is a blank space
/****** Initial state ************/
#define first_hole_X 1
#define first_hole_Y 0
#define nb_initial_pegs 5
/******* procedure NEVER's variables *********/
#define p (nbPegs==2)
/*#############  GLOBAL VARIABLES #################### */
mtype = {UNDEF, present, absent};
typedef array { mtype c[NB_COL] };

int i, j;
int nbPegs = nb_initial_pegs;
/* Board usage: r[2].c[1] is the square on the 2nd ROW and 1st Column
	in cartesian coordinates: (x=1, y=2) 
	indexed from the top left corner of the board.*/
array r[NB_ROW];
/*################ Procedures ########################*/
proctype play_game(){
move:
//Choose indexes "i" and "j":
if ::i=0 fi
if ::j=0 ::j=1 ::j=2 ::j=3 ::j=4 ::j=5 fi
if
//Block is not defined in the game:
::( r[i].c[j]==UNDEF ) -> goto move
//Jump up    :
::( i>=2 && r[i].c[j]==present &&
  r[i-1].c[j]==present && r[i-2].c[j]==absent)->
        printf("Go up   : [%d;%d] ↑\n",i,j); goto jump_up;
//Jump right :
::( j<=NB_COL-3 && r[i].c[j]==present &&
  r[i].c[j+1]==present && r[i].c[j+2]==absent)->
        printf("Go right: [%d;%d] →\n",i,j); goto jump_right
//Jump down  :
::( i<=NB_ROW-3 && r[i].c[j]==present &&
  r[i+1].c[j]==present && r[i+2].c[j]==absent)->
        printf("Go down : [%d;%d] ↓\n",i,j);goto jump_down
//Jump left  :
::( j>=2 && r[i].c[j]==present &&
  r[i].c[j-1]==present && r[i].c[j-2]==absent)->
        printf("Go left : [%d;%d] ←\n",i,j);goto jump_left
//No jumps are possible
::(1==1) -> goto move
fi

jump_up: atomic{
	r[i].c[j]=absent; r[i-1].c[j]=absent; r[i-2].c[j]=present; 
	nbPegs-- -> goto move;
};
jump_right: atomic{
 	r[i].c[j]=absent; r[i].c[j+1]=absent; r[i].c[j+2]=present;
	nbPegs-- -> goto move;
};
jump_down: atomic{
	r[i].c[j]=absent; r[i+1].c[j]=absent; r[i+2].c[j]=present;
	nbPegs-- -> goto move;
};
jump_left: atomic{ 
	r[i].c[j]=absent; r[i].c[j-1]=absent; r[i].c[j-2]=present;
	nbPegs-- -> goto move; 
};
end: skip;
}

/*#################### Init ##########################*/
init{
/* ----- Fill the board ----- */
for ( i : 0 .. NB_ROW-1 ) {
	for( j : 0 .. NB_COL-1 ){
		r[i].c[j]=present
	}
};
/* ----- Remove undefined blocks ----- */
/* ----- Add first hole ----- */
r[first_hole_Y].c[first_hole_X] = absent;
/* ----- Launch the game ------ */
printf("GO {direction} : [ROW number;Column number] {arrow direction}\n");
run play_game();
}
/*#################### Never #########################*/
never  {    /* <>p */
T0_init:
	do
	:: atomic { ((p)) -> assert(!((p))) }
	:: (1) -> goto T0_init
	od;
accept_all:
	skip
}
