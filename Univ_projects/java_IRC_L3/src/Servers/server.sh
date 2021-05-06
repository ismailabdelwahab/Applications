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
function properUsage(){
	YELLOW "$0 [Server (name or folder) to Execute]"
	YELLOW "$0 [Server (name or folder) to Execute] [port]"
	exit 1
}
########### Check Arguments ################
if [ $# == 0 ] || [ $# -gt 2 ]; then
  RED "You must give the name of a server to execute."
  properUsage
fi
########### GLOBAL Vars ####################
DEFAULT_PORT=12345
cd .. ; SRC_DIR=$(pwd); cd Servers
########### Launch server ###################
if [ $# -eq 2 ]; then
	DEFAULT_PORT=$2
fi
if [ ! -d $1 ]; then
	RED "Server: [${NC}$1${RED}] Does not exists."
	properUsage
fi
SERVER_NAME=$(basename $1)
java -classpath ${SRC_DIR} Servers.${SERVER_NAME}.${SERVER_NAME} ${DEFAULT_PORT}


