package jiba.msd.learn

import org.apache.spark.rdd.RDD

/**
 * Created by austin on 04/12/2015.
 */
trait RegressionModel {

  /**
    * predict a single test instance
    * @param testFeatures
    * @return
    */
  def predict(testFeatures : Vector[Double]): Double

  /** predicitons for a set of instances
    *
    * @param testInstances
    * @return
    */
  def predict(testInstances : RDD[Vector[Double]]) : RDD[Double]


  /**
    * get the model weights
    * @return
    */
  def weights() : Vector[Double]
}
