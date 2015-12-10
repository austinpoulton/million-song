package jiba.msd.model

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import scala.io.Source


class LogisticModelSpec {
  
  val sc = new SparkContext(new SparkConf().setAppName("Track logistic regression").setMaster("local"))
  val lines = Source.fromFile("TestTracks.csv").getLines()

  //val data = sc.parallelize(prepareFeaturesWithLabels(prepareFeatures(people)))
  
 
}