package jiba.msd.model


import scala.math._
/**
  * Created by austin on 13/12/2015.
  */
class GeoLocation(val lon : Double, val lat : Double, val name : String="not resolved") extends  Serializable {
  // ensure to restrict values: Latitudes range from -90 to 90.
  //                            Longitudes range from -180 to 180.
  require(lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180)

  /**
    * Convenience for calculaing the distance between two points on earth. Usage:
    * val geoLoc1 = GeoLocation(44.54187, -120.51484)
    * val geoLoc2 = GeoLocation(31.9630499999999, 10.78142)
    * val dist = geoLoc1 - geoLoc2 provides the haversine distance
    * @param that
    * @return
    */
  def -(that: GeoLocation): Double = GeoLocation.haversineDistance(this.lat, this.lon, that.lat, that.lon)

  override def toString(): String = "Longitude:\t"+lon+"\tLatitude:\t"+lat+"\tplace name:\t"+name
}

object GeoLocation extends Serializable {

  final val earthsRadius =  6372.8  //radius in km

  def haversineDistance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double): Double = {
    val rLat=(lat2 - lat1).toRadians
    val rLon=(lon2 - lon1).toRadians
    val a = pow(sin(rLat/2),2) + pow(sin(rLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    earthsRadius * c
  }

  def apply(lon: Double, lat: Double): GeoLocation = new GeoLocation(lon, lat)
  def apply(lon: Double, lat: Double, name: String): GeoLocation = new GeoLocation(lon, lat, name)
}
