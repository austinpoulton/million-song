package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._
// import org.apache.spark.rdd.RDD._


/**
 * Created by austin on 04/12/2015.
 */
object SongCount {

  val moon = "hdfs://moonshot-ha-nameservice"
  val fileNames : Array[String] =  Array("A.csv", "B.csv", "C.csv"  )
  //val fileSizes : Map[String,Long]

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Artist song count"))
    val hdcf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(moon), hdcf)
    val username = System.getProperties().get("user.name")
    val outdir = moon + "/user/" + username + "/msd"

    val lines = sc.textFile(moon + "/data/millionsong/A.csv")
    val tracks  = lines.map(line => Track.processTrackLine(line))
    val artistSongCounts = tracks.map(t => (t.artistName, 1)).reduceByKey((v1, v2) => v1 + v2)

    //
    // Zap the output directory first if it exists
    //
    try {
       hdfs.delete(new org.apache.hadoop.fs.Path(outdir), true)
        }
    catch {
       case _ : Throwable => { }
          }
    //
    // Write out the result to user hdfs
    //
    println ("Saving to " + outdir)
    artistSongCounts.saveAsTextFile(outdir)
  }
}
