package jiba.msd.learn

import breeze.linalg.{DenseVector}

/**
  * Label
  * @param target the target or predicted value
  * @param features the attributes or features
  */
class  LabelledInstance(val target: Double, val features: DenseVector[Double]) extends Serializable {

   def dim = features.length
}

/**
 * companion object
 */

object LabelledInstance extends Serializable{

   def apply(target: Double, features : DenseVector[Double]):LabelledInstance = new LabelledInstance(target, features)

}