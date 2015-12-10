package jiba.msd.model

import scala.io.Source
import scala.io._

object modelTest {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(128); 
  println("Welcome to the Scala worksheet");$skip(65); 
  
	val stream = getClass.getResourceAsStream("/TestTracks.csv");System.out.println("""stream  : java.io.InputStream = """ + $show(stream ));$skip(73); 
	val lines = scala.io.Source.fromInputStream( stream ).getLines mkString;System.out.println("""lines  : String = """ + $show(lines ))}
  
  
}
