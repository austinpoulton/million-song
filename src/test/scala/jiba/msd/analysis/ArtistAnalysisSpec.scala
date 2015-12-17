package jiba.msd.analysis

import breeze.numerics.pow
import jiba.msd.learn.{Regressor, LinearRegressor}
import jiba.msd.model.{Artist, Track}
import org.apache.spark.SparkContext._

/**
  * Created by austin on 14/12/2015.
  */
class ArtistAnalysisSpec extends BaseDriverSpec("Artist Analysis Spec") with Serializable {



  "The Artist analysis " should "filter tracks by relevant artist features" in {
    val aa = ArtistAnalysis()
    val tracks = rawTestTrackData.map(l => Track.createTrack(l))
    val tracksWithArtistFeatures = tracks.filter(to => to != None && aa.hasQualityArtistFeatures(to.get)).map(to => to.get)
    println("tracks with artist features: "+tracksWithArtistFeatures.count() )
    assert(tracksWithArtistFeatures.count() == 607)
  }

  it should "map tracks to artists by artistId " in {
    val aa = ArtistAnalysis()
    val tracks = rawTestTrackData.map(l => Track.createTrack(l))
    val tracksWithArtistFeatures = tracks.filter(to => to != None && aa.hasQualityArtistFeatures(to.get)).map(to => to.get)
    // artist is an RDD[(Int, Artist)]
    val artists = tracksWithArtistFeatures.map(t => (t.artist7Id, Artist(t))).reduceByKey((v1, v2)=> v1 +v2)
    println("total artits: "+artists.count())

    assert(artists.count() == 592)
  }

  it should "train a linear regressor " in {

    val aa = ArtistAnalysis()
    val tracks = rawTestTrackData.map(l => Track.createTrack(l))
    val tracksWithArtistFeatures = tracks.filter(to => to != None && aa.hasQualityArtistFeatures(to.get)).map(to => to.get)
    val artists = tracksWithArtistFeatures.map(t => (t.artist7Id, Artist(t))).reduceByKey((v1, v2)=> v1 +v2)
    //artists.saveAsTextFile("out/artists")

    // split training and test data 60/40 % on our test 592 artists
    val splits = artists.randomSplit(Array(0.6, 0.4))
    val trArtists = splits(0)
    val teArtists = splits(1)
    println("Training size >>>> "+trArtists.count())
    println("Test size >>>> "+teArtists.count())

    // get an RDD which we can train on RDD[LabelledInstance]
    val training = trArtists.map( t => t._2.labelledInstanceForArtistHotness())
    val testing = teArtists.map(t =>  t._2.labelledInstanceForArtistHotness())

    val regressor = new LinearRegressor()
    val lrModel = regressor.fit(training, training.first().dim)

    println(lrModel)

    val predictions = lrModel.predict(testing)
    val error = Regressor.sse(predictions)
    println("Prediction error: = "+ error)

    // setup the linear regressor trainer
//    val algorithm = new LinearRegressionWithSGD()
//    val model = algorithm run training

  }

}