package jiba.msd.analysis

import jiba.msd.model.Track
import jiba.msd.stats.{SumComp, Statistics}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD


/**
  * Music analysis class
  */
class MusicAnalysis extends Statistics with Serializable {

  def tracksWithQualityMusicFeatures(rawTracks : RDD[String]): RDD[Track] = {
    val tracks = rawTracks.map(l => Track.createTrack(l))
    tracks.filter(to => to != None && this.qualityMusicFeatures(to.get)).map(to => to.get)
  }

  def correlation(tracks : RDD[Track], aggFunc : (SumComp,Track)=> SumComp ) : Double = {
    val zeros = SumComp()  // zero sums
    // now the magic happens - pass over tracks adding feature values and accumulating them in SumComp
    val acc  = tracks.aggregate(zeros)(aggFunc, combinerFunc)
    spearmanCorrelation(acc)
  }

  // function variables
  val combinerFunc = (acc1: SumComp, acc2: SumComp) => spearmanCombiner(acc1, acc2)
  val tempoHotnessAggregationFunc = (acc : SumComp,t:  Track) => spearmanAggregator(acc,t.tempo, t.songHotness)

  /**
    * predicate to identify Tracks with quality musical features
    * @param t
    * @return true if the track contains features with sufficient confidence
    */
  def qualityMusicFeatures(t : Track ) : Boolean =
    (t.modeConfidence > 0.6 && t.mode > 0 && t.keyConfidence > 0.6 && t.key > 0 && t.timeSignatureConfidence > 0.6 && t.timeSignature >0)

  def validSongHotness(t: Track) : Boolean = t.songHotness > 0
}

object MusicAnalysis {

  def apply() : MusicAnalysis = new MusicAnalysis
}



object MusicAnalysisDriver extends BaseDriver("Music Analysis Driver")   {

  def main(args: Array[String]): Unit = {
    val ma =  MusicAnalysis()
    // val lines = sparkCtx.textFile(sparkClusterURL+dataFile)
    // marshall the raw csv data into Option[Track] objects. tracks is a RDD[Option[Track]]
    val tracks = rawTrackData.map(l => Track.createTrack(l))
    // filter for Option[Track] = Some and quality musical features.  tracksWithQualityFeatures is a RDD[Track]
    val tracksWithQualityMusicFeatures = tracks.filter(to => to != None && ma.qualityMusicFeatures(to.get) && ma.validSongHotness(to.get)).map(to => to.get)

    // find correlation of tempo and danceability
    val tempoHotnessCorr = ma.correlation(tracksWithQualityMusicFeatures,ma.tempoHotnessAggregationFunc)
    println ("#DancingDads : Spearman correlation, r(tempo, hotness) = " + tempoHotnessCorr)
  }
}