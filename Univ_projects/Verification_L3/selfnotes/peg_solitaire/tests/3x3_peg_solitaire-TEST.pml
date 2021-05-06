/*############# DEFINE SECTION #################### */
#define NB_COL 3
#define NB_ROW 3

#define first_pos_X 3
#define first_pos_Y 3

#define p (nbPons==1)
/*#############  GLOBAL VARIABLES #################### */
mtype = {UNDEF, present, absent};
typedef array { mtype c[NB_ROW] };

int i, j;
int nbPons = 3;
/* Board: r[2].c[1] is the square of coords:(2,1) */
array r[NB_COL];

/*################ Procedures ########################*/
proctype play_game(){
move:
i=0; j=0;
for( i : 0 .. NB_COL-1 ){
for( j : 0 .. NB_ROW-1 ){
	if
	::( r[i].c[j]==UNDEF )-> 
		printf("This block is undef:i=%d, j=%d\n",i,j);skip;
	::( i>=2 && r[i].c[j]==present &&
	  r[i-1].c[j]==present && r[i-2].c[j]==absent)-> //Jump up
		printf("We will jump up!! i=%d\n",i); goto jump_up;

	:: ( j<=NB_ROW-3 && r[i].c[j]==present && 
	r[i].c[j+1]==present && r[i].c[j+2]==absent)-> //Jump right
		printf("We will jump right!! i=%d\n",i); goto jump_right
	
	:: ( i<=NB_COL-3 && r[i].c[j]==present &&
	 r[i+1].c[j]==present && r[i+2].c[j]==absent)-> //Jump down
		printf("We will jump down!! i=%d\n",i);goto jump_down

	:: ( j>=2 && r[i].c[j]==present && 
	r[i].c[j-1]==present && r[i].c[j-2]==absent)-> //Jump left
		printf("We will jump left!! i=%d\n",i);goto jump_left

       ::(1==1)-> printf("We are in else: [%d, %d]\n",i,j);
       fi
}}
goto move
jump_up: atomic{
	printf("HELLO UP, i=%d\n", i);
	r[i].c[j]=absent; r[i-1].c[j]=absent; r[i-2].c[j]=present; 
	nbPons-- -> goto move;
};
jump_right: atomic{
	printf("Starting to jump RITGH\n");
 	r[i].c[j]=absent; r[i].c[j+1]=absent; r[i].c[j+2]=present;
	nbPons-- -> goto move;
};
jump_down: atomic{
	r[i].c[j]=absent; r[i+1].c[j]=absent; r[i+2].c[j]=present;
	nbPons-- -> goto move;
};
jump_left: atomic{ 
	r[i].c[j]=absent; r[i].c[j-1]=absent; r[i].c[j-2]=present;
	nbPons-- -> goto move; 
};
}

/*#################### Init ##########################*/
init{
/* ----- Fill the board ----- */
for ( i : 0 .. NB_COL-1 ) {
	for( j : 0 .. NB_ROW-1 ){
		r[i].c[j]=absent
	}
};
/* ----- Remove undefined blocks ----- */
/* TODO: check how to automate this */
/*
r[0].c[0]=UNDEF; r[0].c[1]=UNDEF; r[1].c[0]=UNDEF;
r[6].c[0]=UNDEF; r[5].c[0]=UNDEF; r[6].c[1]=UNDEF;
r[0].c[6]=UNDEF; r[0].c[5]=UNDEF; r[1].c[6]=UNDEF;
r[6].c[6]=UNDEF; r[5].c[6]=UNDEF; r[6].c[5]=UNDEF;
*/
/* ----- Add first hole ----- */
r[0].c[1] = present;
r[1].c[1] = present;
r[2].c[2] = present;
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
