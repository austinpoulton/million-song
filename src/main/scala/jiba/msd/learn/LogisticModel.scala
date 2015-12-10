package jiba.msd.learn

import scala.math.exp
import scala.util.Random
import breeze.linalg.DenseVector


/**
 * Created by austin on 04/12/2015.
 */
class LogisticModel(val iterations : Int, val regulariser : Double = 0.0, val numFeatures : Int, val alpha : Double = 0.05) extends Model {
  
  private var w : DenseVector[Double]  = DenseVector.rand(numFeatures)
  
  /**
   * sigmoid function
   */
  def sigmoid(x : Double) = 1 / (1+ exp(-x)) 
  
  /**
   * Train the model/fit to the training data
   */
  override def fit(train : List[FeatureSet]): Unit = {
    for (i <- 1 to iterations) {
      val gradient = alpha*train.map(x => sigmoid(w.t*x.featureValues)-x.targetValue*x.featureValues-regulariser*w).reduce(_ + _) 
      w -= gradient  
    }
  }
  
  /**
   * Predict 
   */
  override def predict(test : List[FeatureSet]): FeatureSet = ???
  
  /** 
   *  returns the weights for the model
   */
  override def weights(): Array[Double] = w.toArray
  
  /**
   * generates random weights
   */
  def generateRandomWeights(dim : Int): DenseVector[Double] = {
     val rgen = new Random()
     val array = new Array[Double](dim)  
     new DenseVector(array.map(_ => rgen.nextDouble()))
  }
  
  
}
