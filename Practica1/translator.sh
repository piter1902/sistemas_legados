#!/bin/bash

# Fichero encontrado en: https://github.com/SureSRM/SistemasLegados/blob/master/P1/translator.sh

if [ $# -ne 1 ];
then
    echo "Usage: ./translator.sh input 1> output"
    exit
fi

#cat $1  | sed ':a;N;s/)[ ]*\n[ ]*\"/) \"/;ba' \
#        | sed 's/DISPLAY[ ]*([ ]*\([0-9]*\)[ ]* \([0-9]*\)[ ]*)[ ]*\([^\.\n]*\)/DISPLAY \3 AT LINE \1 COL \2/' \
#        | sed 's/ACCEPT[ ]*([ ]*\([0-9]*\)[ ]* \([0-9]*\)[ ]*)[ ]*\([^\.\n]*\)/ACCEPT \3 AT LINE \1 COL \2/' \
#        | sed -e '/.\{72\}/s/\([^A]*\)\(AT LINE.*\)/\1\n             \2/'


cat $1  | sed 's/DISPLAY[ ]*([ ]*\([0-9]*\)[ ]* \([0-9]*\)[ ]*)[ ]*\([^\.\n]*\)/DISPLAY \3 AT LINE \1 COL \2/' \
        | sed 's/ACCEPT[ ]*([ ]*\([0-9]*\)[ ]* \([0-9]*\)[ ]*)[ ]*\([^\.\n]*\)/ACCEPT \3 AT LINE \1 COL \2/'
exit
