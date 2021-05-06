#!/bin/bash
########### Colors #####################
RED=`tput bold && tput setaf 1`
GREEN=`tput bold && tput setaf 2`
YELLOW=`tput bold && tput setaf 3`
BLUE=`tput bold && tput setaf 4`
NC=`tput sgr0`
function RED(){ echo -e ${RED}${1}${NC} ;}
function GREEN(){ echo -e ${GREEN}${1}${NC} ;}
function YELLOW(){ echo -e ${YELLOW}${1}${NC} ;}
function BLUE(){ echo -e ${BLUE}${1}${NC} ;}

###### Proper Usage + Check arguments #######
function properUsage(){
	YELLOW "\tProper usage: $0 [dir]"
	BLUE "With \"${NC}dir${BLUE}\" the directory containing the java files to compile.\n
	(Note: if dir is \"${NC}.${BLUE}\" then the whole project will be recompiled. )"
	exit 1
}
############## Script ####################################################
if [ $# -ne 1 ]; then properUsage; fi
######### GLOBAL Vars #################
SRC_DIR=$(pwd)
######## cd to the argument ###########
# Change directory if needed:
if [ $1 != "." ]; then YELLOW "Changing directory to: [$1] ."; fi
if [ ! -d $1 ]; then
	RED "Error: [${NC}$1${RED}] directory does not exists.\n"
	properUsage
else 
	cd $1 
fi
### Find classes to compile ###########
YELLOW "Finding all java classes in this directory ..."
java_files=$(find -name "*.java")
GREEN "Done.\n"

########### Compiling #################
for file in ${java_files}; do 
	BLUE "Compiling: [${NC}${file}${BLUE}]."
	javac -classpath ${SRC_DIR} ${file}
done



