package jiba.msd.analysis

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


/**
 * Created by austin on 30/11/2015.
 */




object BasicAnalysis extends App {

  val sc = new SparkContext(new SparkConf().setAppName("Spark Word Count"))

  val lines = sc.textFile("hdfs://moonshot-ha-nameservice" + args(0))

  //transformations from the original input data
  val words = lines.flatMap(line => line.split("[ .,;:()]+"))
  val pairs = words.map(word => (word,1))
  val counts = pairs.reduceByKey((a,b)=>a+b)

  //collect() is an action, so this value is retrieved to the driver
  val winter = counts.filter(pair=>pair._1.equals("winter")).collect()

  winter.foreach(println)



}
