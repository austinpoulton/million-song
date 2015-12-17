package jiba.msd.learn

import breeze.linalg.DenseVector
import org.apache.spark.rdd.RDD

/**
  * Created by austin on 13/12/2015.
  */

/**
  * A Linear Regressor
  * @param iterations number of training iterations for gradient descent optimisation
  * @param regulariser L2 regulariser
  * @param gradientRate the rate for gradient descent  (alpha)
  */
class LinearRegressor(val iterations : Int=100, val regulariser : Double = 0.1, val gradientRate : Double = 0.02) extends Serializable{

  def addBias(x : DenseVector[Double] ): DenseVector[Double] = {
     val features = x.toArray
     val bias = Array(1.0)
     DenseVector(Array.concat(bias,features))
  }



  def fit(train: RDD[LabelledInstance], numFeatures : Int): LinearRegressionModel = {
    var w : DenseVector[Double]  = DenseVector.rand(numFeatures + 1)  // adding the bias coefficient as +1
    for (i <- 1 to iterations) {
      val gradient = gradientRate * (train.map(x => (x.target-(w.t * addBias(x.features))) * x.features - regulariser * w).reduce(_ + _))
      w = w + gradient
    }
    LinearRegressionModel(w)  // return a LinearRegressionModel with trained weights
  }
}

class LinearRegressionModel(val w: DenseVector[Double]) extends RegressionModel with Serializable {

  /**
    *  returns the weights for the model
    */
  override def weights(): DenseVector[Double] = w

  /**
    * predict a single test instance
    * @param testRow
    * @return
    */

  override def predict(testRow: LabelledInstance): (Double, Double) = (w.t * testRow.features, testRow.target)
  /** predicitons for a set of instances
    *
    * @param testInstances
    * @return
    */
  override def predict(testInstances: RDD[LabelledInstance]): RDD[(Double, Double)] = testInstances.map(x => predict(x))


  override def toString(): String = "Linear Regression Model with weights: "+ w

}

object LinearRegressionModel {

  def apply(w : DenseVector[Double]) : LinearRegressionModel = new LinearRegressionModel(w)

}

class PredictionResult(val predicted : Double, val actual : Double) {

}

