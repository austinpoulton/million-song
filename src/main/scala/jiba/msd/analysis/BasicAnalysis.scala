package jiba.msd.analysis

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark._
//import org.apache.spark.rdd.RDD._


/**
 * Created by austin on 30/11/2015.
 */


object BasicAnalysis {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext(new SparkConf().setAppName("Spark Word Count"))

    val lines = sc.textFile("hdfs://moonshot-ha-nameservice" + args(0))
    val words = lines.flatMap(line => line.split("[ .,;:()]+"))
    val pairs = words.map(word => (word,1))
    val counts = pairs.reduceByKey((a,b)=>a+b)

    //collect() is an action, so this value is retrieved to the driver
    val winter = counts.filter(pair=>pair._1.equals("winter")).collect()

    winter.foreach(println)

  }
}
