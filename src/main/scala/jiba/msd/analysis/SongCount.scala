package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD._


/**
 * Created by austin on 04/12/2015.
 */
object SongCount {

  val fileNames : Array[String] =  Array("A.csv", "B.csv", "C.csv"  )
  //val fileSizes : Map[String,Long]

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Artist song count"))

    val lines = sc.textFile("hdfs://moonshot-ha-nameservice/data/millionsong/A.csv")
    val tracks  = lines.map(line => Track.processTrackLine(line))
    val aristSongCounts = tracks.map(t => (t.artistName, 1)).reduceByKey((v1, v2) => v1 + v2)
    aristSongCounts.saveAsTextFile("hdfs://moonshot-ha-nameservice/user/agp30/")
  }
}