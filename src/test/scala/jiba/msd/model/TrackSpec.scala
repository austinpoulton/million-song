package jiba.msd.model


import org.scalatest.FlatSpec

/**
 * Created by austin on 06/12/2015.
 */
class TrackSpec extends FlatSpec {

  // some test data track lines based on MSD csv data - all data fields are double quote enclosed
  val goodTrackLine1 = "\"22050\",\"2080\",\"0.56871913296871324\",\"0.42031820452241458\",\"ARMLRNE1187FB59A6A\",\"nan\",\"\",\"nan\",\"d5bc8537-5bc5-4e37-915a-008c88436092\",\"Jimmie Rodgers\",\"15970\",\"5a87a74a6a34b377970cc9714e4ad710\",\"0.0\",\"194.95138\",\"0.154\",\"0.0\",\"8\",\"0.67400000000000004\",\"-15.749000000000001\",\"1\",\"0.56999999999999995\",\"1\",\"Recordings 1927 - 1933 Disc D\",\"182831\",\"0.26070917124210047\",\"SOUFKSE12A8AE46DCD\",\"187.93100000000001\",\"116.98099999999999\",\"5\",\"0.53900000000000003\",\"Ninety Nine Years Blues\",\"1974848\",\"TRAEXRD128F4233868\",\"0\""
  val goodTrackLine = "\"22050\",\"94666\",\"0.73502415423161616\",\"0.0\",\"ARSZP6T1187B989F0A\",\"41.504710000000003\",\"Cleveland, OH\",\"-81.690740000000005\",\"44ef0b02-86ff-4d9c-a62e-006a6adc2291\",\"Ringworm\",\"-1\",\"27a9dbc5e03e2c0bcf737cec508b0b73\",\"0.0\",\"124.39465\",\"0.23699999999999999\",\"0.0\",\"6\",\"0.001\",\"-5.6539999999999999\",\"0\",\"0.217\",\"1\",\"The Promise\",\"470033\",\"0.59175416059578445\",\"SOLEAOD12AB0181BE8\",\"113.273\",\"88.356999999999999\",\"4\",\"0.0\",\"Likeness Of Vanity\",\"5225499\",\"TRAEXTN128F9341B37\",\"0\""
  val goodTrackWithNaNs = "\"22050\",\"356\",\"0.63560611741993489\",\"0.37177722840510008\",\"AR7V0DI1187FB524DE\",\"nan\",\"\",\"nan\",\"a278331c-1b22-4ebd-b93f-4408bd96f9b8\",\"Frankee\",\"640\",\"991eb5bb45f4f3a11a822a2ecc916106\",\"0.0\",\"149.99465000000001\",\"0.223\",\"0.0\",\"5\",\"0.0\",\"-9.0350000000000001\",\"1\",\"0.0\",\"1\",\"The Good_ The Bad_ The Ugly\",\"559\",\"0.26586104921065007\",\"SOLPZLH12A6D4F9D12\",\"147.18000000000001\",\"110.768\",\"4\",\"1.0\",\"Gotta Man\",\"4611\",\"TRAEXTM128E07811E1\",\"2004\""
  val tooFewFields = "\"82123\",\"0.57008553026482756\",\"0.40954304386178553\",\"ARZTIB31187B98D8B5\",\"nan\",\"\",\"nan\",\"ed091eb0-c1c6-4af1-94d9-4c727c0e6382\",\"F-Minus\",\"-1\",\"720a65b016f78253f5a432d210e30517\",\"0.0\",\"39.52281\",\"0.0\",\"0.0\",\"9\",\"0.025000000000000001\",\"-5.3390000000000004\",\"0\",\"0.214\",\"1\",\"F-Minus\",\"205357\",\"0.43300507727458548\",\"SODRFQO12A8C138906\",\"36.113\",\"99.516999999999996\",\"1\",\"0.0\",\"Better To Die\",\"2225303\",\"TRAEEYU128F42621F4\",\"1999\""
  val tooManyFields = "\"14400\",\"82123\",\"crap field\",\"0.57008553026482756\",\"0.40954304386178553\",\"ARZTIB31187B98D8B5\",\"nan\",\"\",\"nan\",\"ed091eb0-c1c6-4af1-94d9-4c727c0e6382\",\"F-Minus\",\"-1\",\"720a65b016f78253f5a432d210e30517\",\"0.0\",\"39.52281\",\"0.0\",\"0.0\",\"9\",\"0.025000000000000001\",\"-5.3390000000000004\",\"0\",\"0.214\",\"1\",\"F-Minus\",\"205357\",\"0.43300507727458548\",\"SODRFQO12A8C138906\",\"36.113\",\"99.516999999999996\",\"1\",\"0.0\",\"Better To Die\",\"2225303\",\"TRAEEYU128F42621F4\",\"1999\""
  val corruptData = "\",,\"\"***'\\'\"%%123, @#!@#,     , o ,\"\"\'"

  "A Track" should "contain the artist name and song title created from a well formed data" in {

        val track = Track.createTrack(goodTrackLine)
        assert(track.get.artistName.equals("Ringworm"))
        assert(track.get.title.equals("Likeness Of Vanity"))
  }

  it should "contain 0.0 for the Longtitude and Latitude if created from a data containing NaNs (the artist location is unknown)" in {

    val track = Track.createTrack(goodTrackWithNaNs)
    assert(track.get.artistLong == 0.0)
    assert(track.get.artistLat == 0.0)
  }

  it should "be a None if created from data with too many or too few fields" in {
    val track1 = Track.createTrack(tooFewFields)
    assert(track1 == None)
    val track2 = Track.createTrack(tooManyFields)
    assert(track2 == None)
  }

  it should "be a None if the data is total corrupted" in {
    val track = Track.createTrack(corruptData)
    assert(track == None)
  }
}
