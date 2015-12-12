package jiba.msd.analysis

import jiba.msd.model.Track
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FunSuite, FunSpec, BeforeAndAfter, FlatSpec}

import scala.io.Source

/**
  * Created by austin on 11/12/2015.
  *
  * Unit testing pattern for Spark Driver acknowledged to:
  * http://mkuthan.github.io/blog/2015/03/01/spark-unit-testing/
  *
  */
class MusicAnalysisSpec extends FunSuite with BeforeAndAfter {

  val master = "local"
  val appName = "Music Analysis Test Suite"
  var sc: SparkContext = _

  /**
    * Before and after test setup and tear down
    */
  before {
    val conf = new SparkConf().setMaster(master).setAppName(appName)
    sc = new SparkContext(conf)
  }

  after {
    if (sc != null) {
      sc.stop()
    }

    // load our test data
  val lines = Source.fromURL(getClass.getResource("TestTracks.csv")).getLines().toList
  lines.map(l => println(l))
  val rawTrackData = sc.parallelize(lines)

    test("Test Track raw data should have a size = 100") {

      val tracks = rawTrackData.map(l=>Track.createTrack(l))
      assert(tracks.count()===100)
    }
  }
}