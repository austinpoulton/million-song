#!/bin/bash
me=$(basename $0)
default_jar=million-song-analysis_2.10-1.0.jar
default_hdfs=/data/millionsong
if [ $# -eq 0 ]
then
    cat <<!
Usage: $me <driver class> <spark-dist.jar|.> <hdfs source folder|blank>
e.g.

./${me} jiba.msd.analysis.BasicAnalysis million-song-analysis_2.10-1.0.jar /data/millionsong

or:

./${me} jiba.msd.analysis.BasicAnalysis

(defaults to jiba.msd.analysis.BasicAnalysis million-song-analysis_2.10-1.0.jar /data/millionsong)

or:

./${me} jiba.msd.analysis.BasicAnalysis . /data/millionsong/A.csv

(defaults to jiba.msd.analysis.BasicAnalysis million-song-analysis_2.10-1.0.jar /data/millionsong/A.csv)

!
else
   case $# in
      1) jar=$default_jar ; hdfs=$default_hdfs ;;
      2) jar=$2 ; hdfs=$default_hdfs ;;
      3) jar=$2 ; hdfs=$3  ;;
      *) echo "bad number of parameters" ;;
   esac
   if [ "$jar" = "." ];then jar=$default_jar;fi
   echo "Starting job"
   spark-submit --class $1 --master yarn target/scala-2.10/$jar $hdfs
fi
