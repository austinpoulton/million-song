package jiba.msd.model

import scala.io.Source
import scala.io._

object modelTest {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
	val stream = getClass.getResourceAsStream("/TestTracks.csv")
                                                  //> stream  : java.io.InputStream = java.io.BufferedInputStream@1ed4004b
	val lines = scala.io.Source.fromInputStream( stream ).getLines mkString
                                                  //> lines  : Iterator[String] = non-empty iterator
  
  
}