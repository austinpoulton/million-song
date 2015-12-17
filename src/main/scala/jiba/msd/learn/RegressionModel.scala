package jiba.msd.learn

import breeze.linalg.DenseVector
import breeze.numerics._
import org.apache.spark.rdd.RDD

/**
 * Created by austin on 04/12/2015.
 */

object Regressor {


  def withBias(x : DenseVector[Double] ): DenseVector[Double] = {
    val features = x.toArray
    val bias = Array(1.0)
    DenseVector(Array.concat(bias,features))
  }

  def sse(predictionsAndActuals : RDD[Tuple2[Double, Double]]): Double = predictionsAndActuals.map(res => pow((res._1-res._2),2)).reduce(_+_)

  def mse(predictionsAndActuals : RDD[Tuple2[Double, Double]]) : Double = sse(predictionsAndActuals)/predictionsAndActuals.count()


}


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


