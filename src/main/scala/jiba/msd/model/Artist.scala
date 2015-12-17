package jiba.msd.model

import breeze.linalg.DenseVector
import jiba.msd.learn.LabelledInstance

/**
  * Created by austin on 13/12/2015.
  */
class Artist(val id  :  Int,  //artist7id
             val name : String,
             val familiarity : Double,
             val hotness: Double,
             val playMeIdPresent: Int,
             val totalTracks: Int,
             val totalSongHotness: Double,
             val totalYears: Int,
             // TODO set these to none for now
             val location : Option[GeoLocation] = None,
             val songWords : Option[Map[String, Int]] = None
            )
            extends Serializable
{
  // add two Artists together
  def +(that : Artist): Artist = Artist(this.id, this.name, (this.familiarity+that.familiarity/2),
                                        (this.hotness+that.hotness)/2, this.playMeIdPresent, this.totalTracks+that.totalTracks, this.totalSongHotness+that.totalSongHotness, this.totalYears+that.totalYears)

  override def toString() : String = "\t"+id +"\t"+name+"\t"+totalTracks+"\t"+totalSongHotness+"\t"+familiarity+"\t"+playMeIdPresent+"\t"+totalYears+"\t"+hotness


  def avgYear : Int = totalYears / totalTracks

  def avgSongHotness : Double = totalSongHotness / totalTracks

  /**
    * creates a labelled instance with artist features (familiarity, totalSongHotness, totalTrack, avgYear)
    * and a target of aritst hotness
    * @return
    */
  def labelledInstanceForArtistHotness() : LabelledInstance = {
     val features : DenseVector[Double] = new DenseVector(Array(familiarity, totalSongHotness, totalTracks))
     //val features : DenseVector[Double] = new DenseVector(Array(familiarity))
     LabelledInstance(this.hotness, features)
  }
}


object Artist {

  def apply(id : Int, name : String, famailiarity : Double, hotness : Double, playmeIdPresent : Int, totalTracks : Int, totalSongHotness : Double, totalYears : Int):Artist =
        new Artist(id,name,famailiarity,hotness,playmeIdPresent, totalTracks,totalSongHotness,totalYears)

  def apply(t: Track): Artist = Artist(t.artist7Id, t.artistName, t.artistFamiliarity, t.artistHotness, t.playmeIdPresent, 1, t.songHotness, t.year)


}