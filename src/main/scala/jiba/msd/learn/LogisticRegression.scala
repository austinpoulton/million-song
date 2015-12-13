package jiba.msd.learn

import org.apache.spark.rdd.RDD

import scala.math.exp
import scala.util.Random
import breeze.linalg.DenseVector


/**
 * Created by austin on 04/12/2015.
 */
class LogisticRegression(val iterations : Int, val regulariser : Double = 0.0, val numFeatures : Int, val alpha : Double = 0.05)  {
  
  private var w : DenseVector[Double]  = DenseVector.rand(numFeatures)
  
  /**
   * sigmoid function
   */
  def sigmoid(x : Double) = 1 / (1+ exp(-x)) 
  
  /**
   * Train the model/fit to the training data
   */
   def fit(train : RDD[LabelledInstance]): Unit = {
    for (i <- 1 to iterations) {
      val gradient = alpha*train.map(x => sigmoid(w.t*x.features)-x.target * x.features -regulariser*w).reduce(_ + _)
      w -= gradient
    }


  }
  
  /**
   * Predict 
   */
   def predict(test : List[LabelledInstance]): LabelledInstance = ???
  
  /** 
   *  returns the weights for the model
   */
   def weights(): Array[Double] = w.toArray
  
}


class LogisticRegressionModel(val threshold : Double) { //extends RegressionModel {
  require(threshold >0.0 && threshold < 1.0)



  
  
}