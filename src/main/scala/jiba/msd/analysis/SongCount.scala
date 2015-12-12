package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._


/**
 * Created by austin on 04/12/2015.
 */
object SongCount {

  val moon = "hdfs://moonshot-ha-nameservice"

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Artist song count"))
    val hdcf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(moon), hdcf)
    val username = System.getProperties().get("user.name")
    val outdir = moon + "/user/" + username + "/msd"

    val lines = sc.textFile(moon + args(0))
    val tracks  = lines.map(line => Track.createTrack(line))
    val artistSongCounts = tracks.filter(to => to != None).map(t => (t.get.artistName, 1)).reduceByKey((v1, v2) => v1 + v2)

 
    
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
