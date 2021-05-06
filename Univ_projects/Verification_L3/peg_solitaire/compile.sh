#!/bin/bash
PML_FILE='peg_solitaire.pml'
TRAIL_FILE="${PML_FILE}.trail"

function showProperUsage(){
	#Print error message:
	if [ $# -eq 1 ]; then echo -e " Error: $1."; fi
	#Proper usage:
	echo -e "\tProper usage: $0 [-v]"
	echo -e " No arguments : spin will use [     -t -B      ] parameter"
	echo -e "   With [-v]  : spin will use [ -p -g -l -t -B ] parameters"
	exit 1
}
function checkArguments(){
	if [ $# -gt 1 ]; then showProperUsage "Too much arguments given"; fi
	if [ $# -eq 1 ]; then
		case "$1" in
		'-v') continue ;;
		'-h' | '-help') showProperUsage ;;
		*) showProperUsage "Argument given is not valid" ;;
		esac
	fi
}
function delete_old_trail(){
	if [ -f ${TRAIL_FILE} ]; then
		echo " ==== Deleting old trail ===="
		rm ${TRAIL_FILE}
	fi
}
function generate_and_exec_c_files(){
	echo " ==== Generating C files ===="
	spin -a ${PML_FILE}
	echo " ==== Compiling pan.c ===="
	gcc -DPC -DBITSTATE -DBCOMP -DNOBOUNDCHECK -DNOCOMP -DNOFAIR -DNOSTUTTER -DSAFETY -DNIBIS -o pan pan.c
	echo " ==== Finding shortests counter-example ===="
	./pan -i
}
function print_trail(){
	echo -e "\n/========= Printing trail ============\\"
	if [ $# -eq 0 ]; then
		spin -t -B ${PML_FILE}
	else 
		spin -p -g -l -t -B ${PML_FILE}
	fi
	echo -e "\\============ End of trail ============/\n"
}
function delete_pan_files(){
	files_to_rm=( pan pan.* *.tcl)
	echo -e "  #===# Removing pan related files..."
	for file in "${files_to_rm[@]}"; do
		rm -f ${file}
	done
}
#****************** Script **********************************#
checkArguments $*
delete_old_trail
generate_and_exec_c_files
print_trail $*
delete_pan_files
