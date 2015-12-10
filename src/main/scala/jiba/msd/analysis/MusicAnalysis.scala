package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object MusicAnalysis {

  val moon = "hdfs://moonshot-ha-nameservice"
  
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Musici Analysis"))
    val hdcf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(moon), hdcf)
    val username = System.getProperties().get("user.name")
    val outdir = moon + "/user/" + username + "/msd"

    // val lines = sc.textFile(moon + "/data/millionsong/A.csv")
    val lines = sc.textFile(moon + args(0))
    val tracks  = lines.map(line => Track.createTrack(line))
    val artistSongCounts = tracks.filter(to => to != None).map(t => (t.get.artistName, 1)).reduceByKey((v1, v2) => v1 + v2)
  
    println ("Saving to " + outdir)
    artistSongCounts.saveAsTextFile(outdir)
  }
}