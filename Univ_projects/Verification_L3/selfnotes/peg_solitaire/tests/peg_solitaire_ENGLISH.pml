/*############# DEFINE SECTION #################### */
#define NB_COL 7
#define NB_ROW 7

#define first_hole_X 3
#define first_hole_Y 3
//#define nb_initial_pegs 36
#define nb_initial_pegs 32

#define p (nbPegs==3)
/*#############  GLOBAL VARIABLES #################### */
mtype = {UNDEF, present, absent};
typedef array { mtype c[NB_ROW] };

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
if ::i=0 ::i=1 ::i=2 ::i=3 ::i=4 ::i=5 ::i=6 fi
if ::j=0 ::j=1 ::j=2 ::j=3 ::j=4 ::j=5 ::j=6 fi
if
//Block is not defined in the game:
::( r[i].c[j]==UNDEF ) -> goto move
//Jump up    :
::( i>=2 && r[i].c[j]==present && 
  r[i-1].c[j]==present && r[i-2].c[j]==absent)-> 
	printf("Go up   : [%d;%d] ↑\n",i,j); goto jump_up;
//Jump right :
::( j<=NB_ROW-3 && r[i].c[j]==present && 
  r[i].c[j+1]==present && r[i].c[j+2]==absent)->
	printf("Go right: [%d;%d] →\n",i,j); goto jump_right
//Jump down  :
::( i<=NB_COL-3 && r[i].c[j]==present &&
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
/* TODO: check how to automate this */
r[0].c[0]=UNDEF; r[0].c[1]=UNDEF; r[1].c[0]=UNDEF;
r[6].c[0]=UNDEF; r[5].c[0]=UNDEF; r[6].c[1]=UNDEF;
r[0].c[6]=UNDEF; r[0].c[5]=UNDEF; r[1].c[6]=UNDEF;
r[6].c[6]=UNDEF; r[5].c[6]=UNDEF; r[6].c[5]=UNDEF;
	/* transfer to english board */
r[1].c[1]=UNDEF; r[1].c[5]=UNDEF; 
r[5].c[1]=UNDEF; r[5].c[5]=UNDEF;
/* ----- Add first hole ----- */
r[first_hole_X].c[first_hole_Y] = absent;

/* ----- Launch the game ------ */
printf("Begin the game: -------------------\n");
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
