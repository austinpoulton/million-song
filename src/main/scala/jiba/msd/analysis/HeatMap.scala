package jiba.msd.analysis

import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import jiba.msd.model.Track
import org.apache.spark._
import scala.math._



/*

Call by data_in, data_out, x_field_val, x_bin_val, y_field_val, y_bin_val

1. Read input file
2. Set x and y bin values
3. Build tuple RDDs
4. Build matrix and populate
5. Print matrix and summary values
*/


object HeatMap {
  def main(args: Array[String]) {
    val sc = new SparkContext(new SparkConf().setAppName("HeatMap"))

    // 1. Read input file - +args(0) means the first argument in command prompt
    val msd = sc.textFile("hdfs://moonshot-ha-nameservice" + args(0))

    
    // 2. set x and y bin values
    val x_bin = 15
    val y_bin = 0.1 
    

    // 3. build tuple RDDs
    // x_join = trackID(31) & tempo(27)
    // y_join = trackID(31) & track_hotness(24)
    // The value of 15 below are the binning values
    val x_join = msd.map( x => (x.split("\",\"")(31),((Math.ceil(x.split("\",\"")(27).toFloat/x_bin))*x_bin).toInt)).filter(x => x._2 != 0)
    val y_join_pre = msd.map( y => (y.split("\",\"")(31),y.split("\",\"")(24))).filter(y => y._2 != "nan")
    val y_join = y_join_pre.map(y => (y._1, (BigDecimal(y._2.toFloat)).setScale(1, BigDecimal.RoundingMode.CEILING).toFloat)).filter(y => y._2 > 0.0)
    val valid_x_y = x_join.join(y_join).map(xy => xy._2)
    valid_x_y.persist()    
    
    // Save rdd to HDFS to check if necessary
    //valid_x_y.saveAsTextFile("hdfs://moonshot-ha-nameservice/user/bt302" + args(1))
    
    
    // 4. Build matrix and populate
 
     //Define array(y,x)
     var binplot = Array.ofDim[Int](22,11)
     
     // Write y axis values to col 0 of array
     for (y <- 1 to 21)
       {binplot(y)(0) = y*15;
       }     
     
     // Write x axis values to row 0 of array
     for (x <- 1 to 10)
       {binplot(0)(x) = x;
       }


    //Calculate summary values   
    val valid_x_y_count = valid_x_y.count().toInt
    val tempo_min = valid_x_y.map(x => x._1).min()
    val tempo_max = valid_x_y.map(x => x._1).max()
    val hot_min = valid_x_y.map(x => x._2).min()
    val hot_max = valid_x_y.map(x => x._2).max()   
    
    
    // Loops to populate the array
    for (y <- 1 to 21) {
      for (x <- 1 to 10) {
        binplot(y)(x) = valid_x_y.filter(xy => (xy._1 == y*15)).filter(xy => (xy._2*10.toInt == x)).count().toInt
        }
    }

   
    //5. Print matrix and summary values       
    
    //Print matrix
    println()
    println(binplot.map(_.mkString(", ")).mkString("\n"))
    println()
    
    //Print summary values
    println("Valid_x_y rdd count " + valid_x_y_count)
    println("Hotness range " + hot_min + " to " + hot_max)
    println("Tempo range " + tempo_min + " to " + tempo_max)

  }
}
