package jiba.msd.learn

import breeze.linalg.DenseVector

class Feature(val x : Double, val name : String = "x")

class FeatureSet(val target: Feature, val features: Seq[Feature]) {
    
   def featureValues: DenseVector[Double] = new DenseVector(features.toArray.map(f => f.x)) 
   def targetValue: Double = target.x 
   def dim = features.length
}

/**
 * companion object
 */

object FeatureSet {
  
  
  
  
}