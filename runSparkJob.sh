if ["$#" -lt 3]
then
    echo "Usage: spark-submit --class <full qualified driver class" --master yarn target/<spark-dist.jar> <hdfs source folder>"
else
   echo "Starting job"
   spark-submit --class $1 --master yarn target/$2 $3
fi


# spark-submit --class bdp.spark.wordcount.SparkWordCount --master yarn target/wordcount-0.0.1-SNAPSHOT.jar /data/gutenberg