package jiba.msd.analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec}

import scala.io.Source

/**
  * Created by austin on 12/12/2015.
  * Base Driver Unit Test class for MSD analysis
  *
  * Unit testing pattern for Spark Driver inspired by:
  * http://mkuthan.github.io/blog/2015/03/01/spark-unit-testing
  */
abstract class BaseDriverSpec(name : String) extends FlatSpec with BeforeAndAfter {

  val master = "local"
  val appName = name
  var sc: SparkContext = _

  /**
    * Before and after test setup and tear down of fixtures etc
    */
  before {
    println("Setting up local test Spark cluster...")
    val conf = new SparkConf().setMaster(master).setAppName(appName)
    sc = new SparkContext(conf)
    println(sc.appName + " Spark cluster setup complete")

  }

  after {
    if (sc != null) {
      sc.stop()
      println("Stopped Spark cluster.")
    }
  }

  def rawTestTrackData() : RDD[String] = sc.parallelize(Source.fromURL(getClass.getResource("/TestTracks.csv")).getLines().toList)

}
