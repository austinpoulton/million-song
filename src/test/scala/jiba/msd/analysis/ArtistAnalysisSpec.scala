package jiba.msd.analysis

import breeze.linalg.DenseVector
import breeze.numerics.pow
import jiba.msd.learn.{LinearRegressionModel, Regressor, LinearRegressor}
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

    //artists.map(t => t._2).saveAsTextFile("out/artists")
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
    val training = trArtists.map( t => t._2.familiarityHotnessLabelledInstance())
    val testing = teArtists.map(t =>  t._2.familiarityHotnessLabelledInstance())


    val modelsAndErrors = for (i <- 1 to 10) yield {
      val regressor = new LinearRegressor()
      val lrModel = regressor.fit(training, training.first().dim)
      val predictions = lrModel.predict(testing)
      val error = Regressor.sse(predictions)

      (lrModel.weights(),error)
    }

    modelsAndErrors.foreach(v =>  println("Model weights: "+v._1+" yielded error: " + v._2))
    //y = 0.4028x + 0.1854
    // R² = 0.3618

    val targetModel = LinearRegressionModel(DenseVector(Array(0.1854,0.4028)))
    val trainPredictions =  targetModel.predict(training)
    val testingPrediction = targetModel.predict(testing)

    println("Target model:\nTraining error: "+Regressor.sse(trainPredictions)+"\nTest error: "+Regressor.sse(testingPrediction))


    // setup the linear regressor trainer
//    val algorithm = new LinearRegressionWithSGD()
//    val model = algorithm run training

  }

  "A linear regressor" should "predict " in {


  }

}