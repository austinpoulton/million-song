package jiba.msd.analysis

import jiba.msd.model.Track
import jiba.msd.stats.{SumComp, Statistics}
import org.apache.spark.rdd.RDD


/**
  * Music analysis class
  */
class MusicAnalysis extends Statistics with Serializable {

  def correlation(tracks : RDD[Track], aggFunc : (SumComp,Track)=> SumComp ) : Double = {
    val zeros = SumComp()  // zero sums
    // now the magic happens - pass over tracks adding feature values and accumulating them in SumComp
    val acc  = tracks.aggregate(zeros)(aggFunc, combinerFunc)
    spearmanCorrelation(acc)
  }

  // function variables
  val combinerFunc = (acc1: SumComp, acc2: SumComp) => spearmanCombiner(acc1, acc2)
  val tempoHotnessAggregationFunc = (acc : SumComp,t:  Track) => spearmanAggregator(acc,t.tempo, t.songHotness)
  val yearHotnessAggregationFunc = (acc : SumComp,t:  Track) => spearmanAggregator(acc,t.year, t.songHotness)
  val familiarityHotnessAggregationFunc = (acc : SumComp,t:  Track) => spearmanAggregator(acc,t.artistFamiliarity, t.songHotness)
 

  /**
    * predicate to identify Tracks with quality musical features
    * @param t
    * @return true if the track contains features with sufficient confidence
    */
  def qualityMusicFeatures(t : Track ) : Boolean =
    (t.modeConfidence > 0.6 && t.mode > 0 && t.keyConfidence > 0.6 && t.key > 0 && t.timeSignatureConfidence > 0.6 && t.timeSignature >0)

  def goodYearAndHotness(t : Track ) : Boolean =
    (t.year > 0 && t.songHotness > 0)
    
   def goodFamiliarityAndHotness(t : Track ) : Boolean =
    (t.artistFamiliarity > 0 && t.songHotness > 0)
    
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
    
    val tracks = if (args(0).isEmpty()) {rawTrackData.map(l => Track.createTrack(l))} else {rawData(args(0)).map(l => Track.createTrack(l))}
    val time1 = System.currentTimeMillis
    
    // filter for Option[Track] = Some and quality musical features.  tracksWithQualityFeatures is a RDD[Track]
    val tracksWithQualityMusicFeatures = tracks.filter(to => to != None && ma.qualityMusicFeatures(to.get) && ma.validSongHotness(to.get)).map(to => to.get)
     
    // find correlation of tempo and hotness
    val tempoHotnessCorr = ma.correlation(tracksWithQualityMusicFeatures,ma.tempoHotnessAggregationFunc)
    val time_taken = System.currentTimeMillis - time1
    println ("#DancingDads : Spearman correlation, r(tempo, hotness) = " + tempoHotnessCorr + " in " + time_taken + " milliseconds")
    // find correlation between year and song hotness
    val tracksWithGoodYearAndHotness = tracks.filter(to => to != None && ma.goodYearAndHotness(to.get)).map(to => to.get)
    val yearHotnessCorr = ma.correlation(tracksWithGoodYearAndHotness,ma.yearHotnessAggregationFunc)
    println ("#DancingDads : Spearman correlation, r(year, hotness) = " + yearHotnessCorr)
    
    // find correlation between artist familiarity and song hotness
    val tracksWithGoodFamiliarityAndHotness = tracks.filter(to => to != None && ma.goodFamiliarityAndHotness(to.get)).map(to => to.get)
    val familiarityHotnessCorr = ma.correlation(tracksWithGoodFamiliarityAndHotness,ma.familiarityHotnessAggregationFunc)
    println ("#DancingDads : Spearman correlation, r(familiarity, hotness) = " + familiarityHotnessCorr)
    
  }
}