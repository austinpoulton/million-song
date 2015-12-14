package jiba.msd.learn

import org.apache.spark.rdd.RDD
import scala.math.exp
import breeze.linalg.DenseVector

/**
  * Logistic Regression Classes, Companion objects and traits
  * Created by austin on 04/12/2015.
 */

/**
  * A Binary classification logistic regressor
  * @param iterations number of training iterations
  * @param regulariser L2 regulariser value
  * @param numFeatures The number of features or dimensions of X
  * @param gradientRate the rate of gradeint descent (alpha)
  */
class LogisticRegression(val iterations : Int, val regulariser : Double = 0.0, val numFeatures : Int, val gradientRate : Double = 0.05)  {

  private var w : DenseVector[Double]  = DenseVector.rand(numFeatures)

  /**
   * sigmoid function
   */
  def sigmoid(x : Double) = 1 / (1+ exp(-x))

  /**
   * Train the model/fit to the training data
   */
   def fit(train : RDD[LabelledInstance]): LogisticRegressionModel = {
    for (i <- 1 to iterations) {
      val gradient = gradientRate*train.map(x => sigmoid(w.t*x.features)-x.target * x.features -regulariser*w).reduce(_ + _)
      w -= gradient
    }
    LogisticRegressionModel(w)
  }
}

class LogisticRegressionModel(w : DenseVector[Double], val threshold : Double = 0.5) extends RegressionModel with Serializable {
  // enforce the constraint for user provided threshold
  require(threshold >0.0 && threshold < 1.0)

  /**
    *  returns the weights for the model
    */
  override def weights(): DenseVector[Double] = w

  /**
    * predict a single test instance
    * @param testRow
    * @return
    */

  override def predict(testRow: LabelledInstance): (Double,Double) = if (w.t * testRow.features > threshold) (1.0, testRow.target) else (0.0, testRow.target)
  /** predicitons for a set of instances
    *
    * @param testInstances
    * @return
    */
  override def predict(testInstances: RDD[LabelledInstance]): RDD[(Double, Double)] = testInstances.map(x => predict(x))
}

object LogisticRegressionModel {

  def apply(w: DenseVector[Double]) : LogisticRegressionModel = new LogisticRegressionModel(w)

}