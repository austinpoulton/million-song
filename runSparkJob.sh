#!/bin/bash
if [ $# -lt 3 ]
then
    cat <<!
Usage: spark-submit --class <full qualified driver class" --master yarn target/<spark-dist.jar> <hdfs source folder>
e.g.

spark-submit --class jiba.msd.analysis.BasicAnalysis --master yarn million-song-analysis_2.10-1.0.jar /data/millionsong

!
else
   echo "Starting job"
   spark-submit --class $1 --master yarn target/scala-2.10/$2 $3
fi
