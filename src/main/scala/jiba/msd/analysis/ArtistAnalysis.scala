package jiba.msd.analysis

import breeze.numerics._
import jiba.msd.analysis.MusicAnalysisDriver._
import jiba.msd.learn.{Regressor, LinearRegressor, LabelledInstance}
import jiba.msd.model.{Artist, Track}
import jiba.msd.stats.{SumComp, Statistics}
import org.apache.spark.SparkContext._

/**
  * Created by austin on 13/12/2015.
  */
class ArtistAnalysis extends Statistics with Serializable {

  def hasQualityArtistFeatures(t : Track ) : Boolean = t.songHotness > 0 && t.artistHotness > 0 && t.year > 1950 && t.artistFamiliarity > 0





}

object ArtistAnalysis extends Serializable {

  def apply(): ArtistAnalysis = new ArtistAnalysis()
}


object ArtistAnalysisDriver extends BaseDriver("Artist Analysis Driver") {

  def main(args: Array[String]): Unit = {
    val aa =  ArtistAnalysis()
    // val lines = sparkCtx.textFile(sparkClusterURL+dataFile)
    // marshall the raw csv data into Option[Track] objects. tracks is a RDD[Option[Track]]

    val tracks = rawTrackData.map(l => Track.createTrack(l))
    // get the artist features
    val tracksWithArtistFeatures = tracks.filter(to => to != None && aa.hasQualityArtistFeatures(to.get)).map(to => to.get)

    // artist is an RDD[(Int, Artist)]
    val allArtists = tracksWithArtistFeatures.map(t => (t.artist7Id, Artist(t))).reduceByKey((v1, v2)=> v1 +v2)
    val artists = allArtists.sample(false,0.01)


    // split training and test data 60/40 % on our test 592 artists
    val splits = artists.randomSplit(Array(0.6, 0.4))
    val trArtists = splits(0)
    val teArtists = splits(1)
    val trArtistCount = trArtists.count()
    val teArtistCount = teArtists.count()

    // get an RDD which we can train on RDD[LabelledInstance]
    val training = trArtists.map( t => t._2.labelledInstanceForArtistHotness())
    val testing = teArtists.map(t =>  t._2.labelledInstanceForArtistHotness())

    val regressor = new LinearRegressor()
    val lrModel = regressor.fit(training, training.first().dim)

    println(lrModel)

    val predictions = lrModel.predict(testing)
    val error = Regressor.sse(predictions)

    println("#DancingDads: Training size >>>> "+trArtistCount)
    println("#DancingDads: Test size >>>> "+teArtistCount)

    println("#DancingDads:  Prediction error = "+ error)

   // val artistSongCounts = tracks.filter(to => to != None).map(t => (t.get.artistName, 1)).reduceByKey((v1, v2) => v1 + v2)




    // filter for Option[Track] = Some and quality musical features.  tracksWithQualityFeatures is a RDD[Track]
   //al tracksWithQualityMusicFeatures = tracks.filter(to => to != None && ma.qualityMusicFeatures(to.get) && ma.validSongHotness(to.get)).map(to => to.get)

    // find correlation of tempo and danceability
   // val tempoHotnessCorr = ma.correlation(tracksWithQualityMusicFeatures,ma.tempoHotnessAggregationFunc)
   // println ("#DancingDads : Spearman correlation, r(tempo, hotness) = " + tempoHotnessCorr)
  }

}