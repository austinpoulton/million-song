package jiba.msd.learn

import breeze.linalg.{ DenseVector}
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

class LinearRegressor(val iterations : Int=100, val regulariser : Double = 0.0, val gradientRate : Double = 0.0001) extends Serializable{

  // adds the bias to a feature vector withBias = [1.0 features]

  def computeError(x : LabelledInstance, w : DenseVector[Double]): DenseVector[Double] =  {
    //println("########DancingDads: w dims:" + w.length + "with features" + withBias(x.features))
    (x.target-(w.t *Regressor.withBias(x.features)))*Regressor.withBias(x.features) - regulariser * w
    //(x.target-(w.t *x.features))*x.features - regulariser * w
  }


  def fit(train: RDD[LabelledInstance], numFeatures : Int): LinearRegressionModel = {
    //train.persist()
    var w : DenseVector[Double]  = DenseVector.rand(numFeatures + 1 ) // adding the bias coefficient as +1
    //var w : DenseVector[Double]  = DenseVector.rand(numFeatures)
    for (i <- 1 to iterations) {
      val gradient = ((gradientRate * 1.0 ) / numFeatures) * train.map(x => computeError(x, w)).reduce(_ + _)
      w = w + gradient
      //println("#DancingDads: Gradient =  " + gradient + ": iteration " + i )
      //println("#DancingDads: w =  " + w  + ": iteration " + i
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

  override def predict(testRow: LabelledInstance): (Double, Double) = (w.t * Regressor.withBias(testRow.features), testRow.target)
    //(w.t * testRow.features, testRow.target)
    //(w.t * Regressor.withBias(testRow.features), testRow.target)
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

