package jiba.msd.learn

import breeze.linalg.DenseVector
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
  def predict(testFeatures : LabelledInstance): (Double, Double)

  /** predicitons for a set of instances
    *
    * @param testInstances
    * @return
    */
  def predict(testInstances : RDD[LabelledInstance]) : RDD[(Double, Double)]

  /**
    * get the model weights
    * @return
    */
  def weights() : DenseVector[Double]

}


