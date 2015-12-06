package jiba.msd.model

/**
 * Created by austin on 30/11/2015.
 *
 * Case class and companion object that represents track data from the
 * million song database
 *
 */


/**
 *
 * Note: confidence measures are bounded [0..1] on associated algorithmic estimated attributes
 * @param sampleRate
 * @param artist7Id
 * @param artistFamiliarity
 * @param artistHotness
 * @param artistId
 * @param artistLong
 * @param artistLocation
 * @param artistLat
 * @param artistMbid
 * @param artistName
 * @param artistPlaymeId
 * @param audioMd5
 * @param danceability
 * @param duration        song duration in
 * @param endOfFadeIn
 * @param energy          energy from a listener perspective ??
 * @param key             key the song is in
 * @param keyConfidence
 * @param loudness        overall loudness in dB
 * @param mode            major (1) or minor (0) key
 * @param modeConfidence
 * @param unknown         WHAT IS THIS?? All instances seem to have 1???
 * @param release         albumn name
 * @param release7Id
 * @param songHotness     algo est.
 * @param songId
 * @param startOfFadeOut
 * @param tempo           est. bpm
 * @param timeSignature   est. # of beats per bar
 * @param timeSignatureConfidence
 * @param title
 * @param trackId
 * @param track7Id
 * @param year
 */
class Track (val sampleRate : Double,
             val artist7Id  : Int,
             val artistFamiliarity : Double,
             val artistHotness : Double,
             val artistId : String,
             val artistLong : Double,
             val artistLocation : String,
             val artistLat : Double,
             val artistMbid : String,
             val artistName : String,
             val artistPlaymeId : Int,
             val audioMd5 : String,
             val danceability : Double,
             val duration : Double,
             val endOfFadeIn : Double,
             val energy : Double,
             val key : Int,
             val keyConfidence : Double,
             val loudness : Double,
             val mode : Int,
             val modeConfidence : Double,
             val unknown : String,
             val release : String,
             val release7Id : Int,
             val songHotness : Double,
             val songId : String,
             val startOfFadeOut : Double,
             val tempo : Int,
             val timeSignature : Int,
             val timeSignatureConfidence : Double,
             val title : String,
             val trackId : String,
             val track7Id : Int,
             val year : Int
              )
{

  override def toString():String = ???

}


object Track {
  /**
    * @param t Array of strings representing the track meta data attributes
   * @return A instance of Track
   */
  def apply(t :Array[String]):Track = {
    if (t.length != 34) throw new IllegalArgumentException("t corrupted: " + t.length)
    val track = new Track(t(0).toDouble,
      t(1).toInt,
      t(2).toDouble,
      t(3).toDouble,
      t(4),
      t(5).toDouble,
      t(6),
      t(7).toDouble,
      t(8),
      t(9),
      t(10).toInt,
      t(11),
      t(12).toDouble,
      t(13).toDouble,
      t(14).toDouble,
      t(15).toDouble,
      t(16).toInt,
      t(17).toDouble,
      t(18).toDouble,
      t(19).toInt,
      t(20).toDouble,
      t(21),
      t(22),
      t(23).toInt,
      t(24).toDouble,
      t(25),
      t(26).toDouble,
      t(27).toInt,
      t(28).toInt,
      t(29).toDouble,
      t(30),
      t(31),
      t(32).toInt,
      t(33).toInt
    )
    track // return track
  }

  def processTrackLine(t: String) : Track =
  {
    // val components = t.split(",")
    val components = t.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)
    val cleaned = components.map(s => s.replaceAll("\"",""))
    Track(cleaned)
  }
}
