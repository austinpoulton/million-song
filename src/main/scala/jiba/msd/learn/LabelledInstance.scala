package jiba.msd.learn

import breeze.linalg.Vector

/**
  * Label
  * @param target the target or predicted value
  * @param features the attributes or features
  */
class  LabelledInstance(val target: Double, val features: Vector[Double]) {

   def dim = features.length
}

/**
 * companion object
 */

object LabelledInstance {
  
  
  
  
}