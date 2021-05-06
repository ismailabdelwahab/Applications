#!/bin/bash
########### Colors #############################
RED=`tput bold && tput setaf 1`
GREEN=`tput bold && tput setaf 2`
YELLOW=`tput bold && tput setaf 3`
BLUE=`tput bold && tput setaf 4`
NC=`tput sgr0`
function RED(){ echo -e ${RED}${1}${NC} ;}
function GREEN(){ echo -e ${GREEN}${1}${NC} ;}
function YELLOW(){ echo -e ${YELLOW}${1}${NC} ;}
function BLUE(){ echo -e ${BLUE}${1}${NC} ;}
################################################
YELLOW "Searching for all file as: *.class"
class_files=$(find -name "*.class")

for file in ${class_files}; do
	BLUE "Removing: [${NC}${file}${BLUE}]."
	rm ${file}
done

GREEN "All \".class\" files have been deleted."
