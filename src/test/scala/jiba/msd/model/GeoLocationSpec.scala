package jiba.msd.model

import org.scalatest.FlatSpec

/**
  * Created by austin on 13/12/2015.
  */
class GeoLocationSpec extends FlatSpec{

  val geoCords1 = GeoLocation(-86.67,36.12)
  val geoCords2 = GeoLocation(-118.40,33.94)


  "A GeoLocation" should "provide the distance when subtracted from another geoLocation" in {

    println("The distance between "+geoCords1 + " and "+geoCords2)
    println("is "+(geoCords1-geoCords2))
    assert(geoCords1 - geoCords2 == 2887.2599506071106)
  }

}