package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._
import scala.math._
// import org.apache.spark.rdd.RDD._

/**
 * Can we find a grouping of geolocations?
 */
object LoudLocations {

  val moon = "hdfs://moonshot-ha-nameservice"
 
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Cluster locations"))
 
    // ingest the HDFS set
    
    val csvlines = sc.textFile(moon + args(0))
    val raw_count = csvlines.count()
    
    // map to track features
    
    val tracks  = csvlines.map(track => Track.createTrack(track)).
                 filter(to => to != None & to.get.artistLat != 0 & to.get.artistLong != 0 &
                              abs(to.get.artistLat) < 90 & abs(to.get.artistLong) <= 180)
      
    val total_tracks = tracks.count()
    println ("#DancingDads : mapped features of " + total_tracks +
             " out of " + raw_count + " tracks from " + moon + args(0))
    println ("#DancingDads : (discarded those with bad features or null lat/long)")
    
    // get value for top 5 loudness
    
    val t = tracks.map(tr => (tr.get.loudness,null)).distinct.sortByKey(false).take(5)
    println("#DancingDads : Top five loudnesses (referenced from -60db) ->")
    t.foreach(l => println("#DancingDads : " + l._1))
    val threshold = t(4)._1
            
    // create an RDD of truncated lat/long tuples plus track Id and artist name

    val tracks2 = tracks.filter(t => t.get.loudness >= threshold)
                                      
    println("#DancingDads : " + tracks2.count() + " tracks louder than " + threshold)
    
    //
    // Write list of Loudnesses and coordinate pairs back out to file
    //
    val username = System.getProperties().get("user.name")
    val result = tracks2.map(v => (v.get.loudness,v.get.artistLong,v.get.artistLat)).
                         saveAsTextFile(moon + "/user/" + username + "/loudplaces/")
    println ("Results written back to user HDFS /user/" + username + "/loudplaces")
        
  }
}
