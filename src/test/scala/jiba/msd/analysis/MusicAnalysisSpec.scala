package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FunSuite, FunSpec, BeforeAndAfter, FlatSpec}

/**
  * Created by austin on 11/12/2015.
  *
  */
class MusicAnalysisSpec extends BaseDriverSpec("Music Analysis Test Driver") {



  "The music analysis " should "should confirm that there are 2000 tracks in the test data" in {
    assert(rawTestTrackData().count() == 2000)
    }

  it should "parse to Track objects and find those with good music features" in {
    val ma = MusicAnalysis()
    val tracks = rawTestTrackData.map(l => Track.createTrack(l))
    println("total tracks: "+tracks.count())
    val tracksWithQualityMusicFeatures = tracks.filter(to => to != None && ma.qualityMusicFeatures(to.get)).map(to => to.get)
    println("tracks with quality music features: "+tracksWithQualityMusicFeatures.count() )
    assert(tracksWithQualityMusicFeatures.count() == 112)
  }

  it should "find the correlation between tempo and dancability" in {
    val ma = MusicAnalysis()
    val tracks = rawTestTrackData.map(l => Track.createTrack(l))
    val tracksWithQualityMusicFeatures = tracks.filter(to => to != None && ma.qualityMusicFeatures(to.get) && ma.validSongHotness(to.get)).map(to => to.get)
    // assert that only 56 tracks out of 2000 match the criteria
    assert(tracksWithQualityMusicFeatures.count() == 56)


    val spearCorr = ma.correlation(tracksWithQualityMusicFeatures, ma.tempoHotnessAggregationFunc)
    println("Spearman correlation for tempo and danceability:  " + spearCorr )
  }
}