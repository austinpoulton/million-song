package jiba.msd.learn

/**
 * Created by austin on 04/12/2015.
 */
trait Model {
  
  def fit(train : List[FeatureSet])
  def predict(test : List[FeatureSet]): FeatureSet
  def weights() : Array[Double]


}
