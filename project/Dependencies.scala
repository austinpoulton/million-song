import sbt._

object Version {
  val mockito    = "1.10.19"
  val scala      = "2.10.5"  //we must use scala 2.10 as this is spark's scala version
  val scalaTest  = "2.2.4"
  val sparkCore  = "1.0.0-cdh5.1.0"
}

object Library {


  val mockitoAll     = "org.mockito"       %  "mockito-all"     % Version.mockito
  val scalaTest      = "org.scalatest"     %% "scalatest"       % Version.scalaTest
  val sparkCore      = "org.apache.spark"  % "spark-core_2.10"  % Version.sparkCore

}

object Dependencies {

  import Library._

  val sparkDeps = Seq(
    sparkCore,
    scalaTest      % "test",
    mockitoAll     % "test"
  )
}
