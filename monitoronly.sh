#!/bin/sh

DIRNAME=`dirname $0`
CP=$DIRNAME/KitchenScr.jar

# start uniCenta oPOS
java -cp $CP uk.chromis.kitchenscr.KitchenScr monitor
