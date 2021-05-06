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
if (( $# != 1 )); then
  RED "You must give the name of a client to execute."
  properUsage
  exit 1
fi

DEFAULT_PORT=12345
cd ..
SRC_DIR=$(pwd)
cd Clients

case "$1" in
"SimpleClient")
  java -classpath ${SRC_DIR} Clients.SimpleClient.SimpleClient localhost ${DEFAULT_PORT} ;;
"NormalClient")
  java -classpath ${SRC_DIR} Clients.NormalClient.NormalClient ;;
*)
  RED "Invalid client name [${NC}$1${RED}]. Or not implemented yet."
esac
