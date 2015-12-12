package jiba.msd.model

/**
 * Places.scala
 * 
 * Shamelessly copied from earlier version of Track.scala
 * - maps Geonames fields (http://download.geonames.org/export/dump/)
 */


/**
 *
 * @param geonameid
 * @param name
 * @param asciiname
 * @param alternatenames
 * @param latitude  (wgs84)
 * @param longitude (wgs84)
 * @param featureclass
 * @param featurecode
 * @param countrycode
 * @param cc2
 * @param admin1code
 * @param admin2code
 * @param admin3code
 * @param admin4code
 * @param population
 * @param elevation
 * @param dem
 * @param timezone
 * @param modificationdate
 */
class Place (val geonameid : Int,           //  0
             val name :  String,            //  1
             val asciiname : String,        //  2
             val alternatenames : String,   //  3
             val latitude: Double,          //  4
             val longitude : Double,        //  5
             val featureclass : String,     //  6
             val featurecode : String,      //  7
             val countrycode : String,      //  8
             val cc2 : String,              //  9
             val admin1code : String,       // 10
             val admin2code : String,       // 11
             val admin3code : String,       // 12
             val admin4code : String,       // 13
             val population : Int,      // 14
             val elevation: Double,         // 15
             val dem : Int,                 // 16
             val timezone : String,         // 17
             val modificationdate: String)  // 18
{

  override def toString():String =
    "Place:\t"+this.asciiname+"("+this.latitude+","+this.longitude+") - "+this.countrycode
    
}

object Place {
  /**
   *
   * @param p Array of strings representing the place attributes
   * @throws java.lang.IllegalArgumentException - compatibility with Java
   * @throws java.lang.NumberFormatException
   * @return  A instance of Place
   */
  @throws(classOf[IllegalArgumentException])
  @throws(classOf[NumberFormatException])
  def apply(p :Array[String]):Option[Place] = {
    if (p.length != 19) None // throw new IllegalArgumentException("p corrupted: " + p.length)
    else {
      val place = new Place(
        toInt(p(0)).getOrElse(0), // geonameid
        p(1), // name
        p(2), // asciiname
        p(3), // alternatenames
        toDouble(p(4)).getOrElse(0.0), // latitude
        toDouble(p(5)).getOrElse(0.0), // longitude
        p(6), // featureclass
        p(7), // featurecode
        p(8), // ccountrycode
        p(9), // cc2
        p(10), // admin1code
        p(11), // admin2code
        p(12), // admin3code
        p(13), // admin4code
        toInt(p(14)).getOrElse(0), // population
        toDouble(p(15)).getOrElse(0.0), // elevation
        toInt(p(16)).getOrElse(0), // dem
        p(17), // timezone
        p(18)  // modificationdate
      )
      Some(place) // return place
    }
  }

  /**
   * parses an Int from a string safely
   * @param s
   * @return
   */
  def toInt(s: String):Option[Int] = { try {
    Some(s.toInt) } catch {
    case e: NumberFormatException => None }
  }

  /**
   * parses a Double from a string safely
   * @param s
   * @return
   */
  def toDouble(s: String):Option[Double] = { try {
    Some(s.toDouble) } catch {
    case e: NumberFormatException => None }
  }

  /**
   * Parses a line from Geonames allCountries TSV data file into a Place instance
   * @param p
   * @return
   */
  def createPlace(p: String) : Option[Place] =
  {
    val components = p.split( "\\t")

    Place(components)

  }
}
