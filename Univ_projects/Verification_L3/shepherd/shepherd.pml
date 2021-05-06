#define b (positions[0])
#define m (positions[1])
#define c (positions[2])
#define l (positions[3])
#define test (check_here)

/******************** VARIABLES ***********************************/
/* Position of characters:
  position[0] ==> Shepherd
  ________[1] ==> Sheep
  ________[2] ==> Cabbage
  ________[3] ==> Wolf
*/
bool positions[4];

/* Flag used to verify if we need to 
check the current state of the system in our LTL formula */
bool check_here=0;

/* Variables used by the model of our system */
mtype = {aller, retour};
chan canal = [0] of {mtype};


/******************** PROCEDURES ***********************************/
proctype Shepherd(){ //Shepherd
left:  if
       :: atomic{
              printf("The {{shepherd}} if going [RIGHT].\n")
              canal!aller;  
              positions[0]=1; check_here=1; goto right
          }
       :: atomic{
               printf("The {{shepherd}} if going [RIGHT] but he is [ALONE].\n")
               positions[0]=1; check_here=1; goto right;
          }
       fi;
right: if
       :: atomic{
              printf("The {{shepherd}} if going [LEFT].\n")
              canal!retour;  
              positions[0]=0; check_here=1;  goto left;
          }
       :: atomic{
               printf("The {{shepherd}} if going [LEFT] but he is [ALONE].\n")
               positions[0]=0; check_here=1; goto left;
          }
       fi;
}

proctype Sheep(){
left : atomic{
       canal?aller; check_here=0; positions[1]=1;
       printf("The [sheep] is coming with him to the [RIGHT].\n");
       goto right;}
right: atomic{
       canal?retour; check_here=0; positions[1]=0; 
       printf("The [sheep] is coming with him to the [LEFT].\n");
       goto left;}
}

proctype Cabbage(){
left : atomic{
       canal?aller; check_here=0; positions[2]=1; 
       printf("He takes the [cabbage] with him.\n");
       goto right;}
right: atomic{
       canal?retour; check_here=0; positions[2]=0; 
       printf("He takes the [cabbage] with him.\n");
       goto left;}
}

proctype Wolf(){
left : atomic{ 
       canal?aller; check_here=0; positions[3]=1; 
       printf("The [wolf] followed him to the [RIGHT].\n");
       goto right;}
right: atomic{
       canal?retour; check_here=0; positions[3]=0;  
       printf("The [wolf] will follow him to the [LEFT].\n");
       goto left;}
}

init{  atomic{run Shepherd(); run Cabbage(); run Wolf(); run Sheep()}  }

never  {    /* (((test &&((!l && !m) || (!m && !c)))->!b) &&(((test && ((l && m)||(m && c)))->b)) ) U (l && m && c && b) */
T0_init:
       do
       :: atomic { ((l && m && c && b)) -> assert(!((l && m && c && b))) }
       :: (((! ((b)) && ! ((test && ((l && m)||(m && c))))) || (((! ((test && ((l && m)||(m && c)))) && ! ((test &&((!l && !m) || (!m && !c))))) || (! ((test &&((!l && !m) || (!m && !c)))) && (b)))))) -> goto T0_init
       od;
accept_all:
       skip
}
