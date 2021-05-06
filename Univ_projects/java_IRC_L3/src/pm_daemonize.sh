#!/bin/sh

if [ $# = 0 ]
then
    echo "Usage : $0 command"
else
    $* > server.log&
    #$* &
    PID=$!
    echo $PID
fi

#This script was given by our professor during the course
# in a previous practicum (TP)
