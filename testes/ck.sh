#!/bin/bash
echo "Change branch to $MYCOMMIT"
git checkout $MYCOMMIT
echo "Analysing commit $MYCOMMIT to version $MYVERSION" 
java -jar ck-0.6.4-jar-with-dependencies.jar $MYDIR 
mkdir results
mv *.csv results
myresults=results
newmyresults="$MYVERSION$myresults" 
mv results $newmyresults
git checkout master
echo "Analysis complete!"