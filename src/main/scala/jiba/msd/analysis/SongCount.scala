package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._


/**
 * Created by austin on 04/12/2015.
 */
object SongCount extends BaseDriver("Song Count Driver") {


  def main(args: Array[String]): Unit = {

    val username = System.getProperties().get("user.name")
    val outdir = this.sparkClusterURL + "/user/" + username + "/msd"

    val lines = this.rawTrackData

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
