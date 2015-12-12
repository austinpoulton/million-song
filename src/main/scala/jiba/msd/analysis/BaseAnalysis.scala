package jiba.msd.analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by austin on 10/12/2015.
  * Base Spark Driver for MSD Analysis
  *
  */
abstract class BaseAnalysis(val driverName : String) {

  final val sparkClusterURL = "hdfs://moonshot-ha-nameservice"
  final val hdfsMSDlocation = "/data/millionsong"

  val sparkCtx = new SparkContext(new SparkConf().setAppName(driverName))

  // TODO should be abstract
  def rawTrackData() : RDD[String] = sparkCtx.textFile(sparkClusterURL+hdfsMSDlocation)

  //abstract def run(): Unit

}
