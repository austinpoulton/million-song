package jiba.msd.analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by austin on 10/12/2015.
  * Base Spark Driver for MSD Analysis
  *
  */
abstract class BaseDriver(val driverName : String) {
  // Moonshot cluster name service
  final val sparkClusterURL = "hdfs://moonshot-ha-nameservice"
  // Spark context for the driver that implements this base driver
  val sparkCtx = new SparkContext(new SparkConf().setAppName(driverName))

  // Hadoop FS location for million song db
  final val hdfsMSDlocation = "/data/millionsong"

  // Hadoop configuration
  final val hdcf = new org.apache.hadoop.conf.Configuration()
  // handle to the hadoop file system for moonshot cluster
  final val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(sparkClusterURL), hdcf)


  // the lazy evaluated RDD for the msd track data
  def rawTrackData() : RDD[String] = sparkCtx.textFile(sparkClusterURL+hdfsMSDlocation)
  // get any other RDD given its HDFS location
  def rawData(sourceHDFSloc : String) : RDD[String] = sparkCtx.textFile(sparkClusterURL+sourceHDFSloc)
}