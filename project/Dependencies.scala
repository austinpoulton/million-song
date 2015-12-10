import sbt._

object Version {
  val mockito    = "1.10.19"
  val scala      = "2.10.5"  //we must use scala 2.10 as this is spark's scala version
  val scalaTest  = "2.2.4"
  val sparkCore  = "1.0.0-cdh5.1.0"
  val breeze    = "0.11.2"
}

object Library {


  val mockitoAll     = "org.mockito"       %  "mockito-all"     % Version.mockito
  val scalaTest      = "org.scalatest"     %% "scalatest"       % Version.scalaTest
  val sparkCore      = "org.apache.spark"  % "spark-core_2.10"  % Version.sparkCore
  val breezeCore     =  "org.scalanlp" %% "breeze" % Version.breeze
  val breezeNative   =  "org.scalanlp" %% "breeze-natives" % Version.breeze
  val breezeViz      =  "org.scalanlp" %% "breeze-viz" %  Version.breeze
}

object Dependencies {

  import Library._

  val projectDeps = Seq(
    sparkCore,
    breezeCore,
    breezeNative,
    breezeViz,
    scalaTest      % "test",
    mockitoAll     % "test"
  )
}
