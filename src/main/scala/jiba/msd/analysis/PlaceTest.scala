package jiba.msd.analysis

import jiba.msd.model.Place
import org.apache.spark._
import org.apache.spark.SparkContext._
//import org.apache.spark.rdd.RDD._


/**
 * Copied from SongCount - just for testing places (allCountries.tsv)
 */
object PlaceTest {

  val moon = "hdfs://moonshot-ha-nameservice/user/"

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Place test"))
    val hdcf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(moon), hdcf)
    val username = System.getProperties().get("user.name")

    val lines = sc.textFile(moon + username + "/msd/allCountries.tsv")
    val places  = lines.map(line => Place.createPlace(line))

    val sample = places.filter(pl => pl != None).
                 map(p => (p.get.asciiname,(p.get.latitude,p.get.longitude),p.get.countrycode)).
                 takeSample(false,20)
 
    sample.foreach(p =>  println(p))
  }
}
