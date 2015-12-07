package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark._
import org.apache.spark.SparkContext._

//import org.apache.spark.rdd.RDD._

/**
 * Johns first bash, based on Austin's genius
 */
object Correlations {

  val moon = "hdfs://moonshot-ha-nameservice"
  val fileNames : Array[String] =  Array("A.csv", "B.csv", "C.csv"  )
 
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Correlating stuff"))
    val hdcf = new org.apache.hadoop.conf.Configuration()
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new java.net.URI(moon), hdcf)
    val username = System.getProperties().get("user.name")
    val outdir = moon + "/user/" + username + "/msd"

    val csvlines = sc.textFile(moon + args(0))
    val tracks  = csvlines.map(track => Track.createTrack(track))
    //
    // Cannot use 'MLIB' - just a template for now until we can do it the hard way
    //
    val hotnessRDD = tracks.filter(to => to != None).map(t => t.get.songHotness)
    val tempoRDD = tracks.filter(to => to != None).map(t => t.get.tempo)
    
    println ("#DancingDads : Considering " + hotnessRDD.count() + " song hotnesses")
    println ("#DancingDads : Considering " + tempoRDD.count() + " song tempos")
    // val correlation: Double = OurVeryOwnCorrelationFunctionHere.corr(hotnessRDD,tempoRDD,"spearman")
    
    // println ("#DancingDads : HOTNESS vs TEMPO correlation = " + correlation)
    /**
    
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
    blah.saveAsTextFile(outdir)
    */

  }
}
