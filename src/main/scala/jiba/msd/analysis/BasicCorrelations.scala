package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._
import scala.math._

//import org.apache.spark.rdd.RDD._

/**
 * Johns first bash, based on Austin's genius
 */
object BasicCorrelations {

  val moon = "hdfs://moonshot-ha-nameservice"
 
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Correlating MillionSong hotness with tempo"))
 
    // ingest the HDFS set
    
    val csvlines = sc.textFile(moon + args(0))
    
    // map to track features
    
    val tracks  = csvlines.map(track => Track.createTrack(track))
    val total_tracks = tracks.count()
    println ("#DancingDads : mapped features of " + total_tracks + " tracks from " + moon + args(0))
    
    // filter non-zero song hotness and tempos

    val id_hot_pairs = tracks.filter(to => to != None).map(t => (t.get.track7Id,t.get.songHotness)).filter(hp => hp._2 != 0)
    println ("#DancingDads : " + id_hot_pairs.count() + " songs have a non-zero hotness")
    val id_tempo_pairs = tracks.filter(to => to != None).map(t => (t.get.track7Id,t.get.tempo)).filter(tp => tp._2 != 0)
    println ("#DancingDads : " + id_tempo_pairs.count() + " songs have a non-zero tempo")
    
    // join by track7ID key to get tuples of all non-zero hotness and tempo (id,(hotness,tempo))
    
    val valid_tracks = id_hot_pairs.join(id_tempo_pairs)
    val n = valid_tracks.groupByKey().count()
    println ("#DancingDads : resulting join on track7Id key gives " + n + " to play with")
    
    /**
    Clunky Spearman correlation
    - let's call song hotness "x" and tempo "y"
    */
    
    // get tuples of (X,Y,X*Y,X^2,Y^2)
    //                                     X        Y         X*Y                   X^2             Y^2      
    val xy = valid_tracks.map(vt => (vt._2._1,vt._2._2,vt._2._1*vt._2._2,pow(vt._2._1,2),pow(vt._2._2,2)))
    
    // keep in memory while computing several different values
    xy.persist()
    val sum_x = xy.map(s => s._1).sum()
    val sum_y = xy.map(s => s._2).sum()
    val sum_xy = xy.map(s => s._3).sum()
    val sum_xsqrd = xy.map(s => s._4).sum()
    val sum_ysqrd = xy.map(s => s._5).sum()
    xy.unpersist()
    
    val numerator = (n*sum_xy) - ((sum_x)*(sum_y))
    val denominator = sqrt((n*sum_xsqrd) - pow(sum_x,2)) *
                      sqrt((n*sum_ysqrd) - pow(sum_y,2))
    val corr = numerator / denominator
    
    println ("#DancingDads : Spearman correlation of Song Hotness to Temp is " + corr)
    if (corr >= 0.5 ) println ("#DancingDads : ...which implies a reasonable, positive correlation")
    else if (corr <= -0.5) println ("#DancingDads : ...which implies a reasonable, negative correlation")
    else if ((abs(corr) < 0.5) & (abs(corr) >= 0.1)) println ("#DancingDads : ...which implies a weak correlation")
    else println ("#DancingDads : ...which implies almost no correlation [but that does not mean independence]")
    
  }
}
