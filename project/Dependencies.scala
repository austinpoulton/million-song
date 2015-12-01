import sbt._

object Version {
  val hadoop     = "2.6.0"
  val mockito    = "1.10.19"
  val scala      = "2.10.5"  //we must use scala 2.10 as this is spark's scala version
  val scalaTest  = "2.2.4"
  val spark      = "1.4.1"
  val sparkCore  = "1.0.0-cdh5.1.0"
}

object Library {

  val hadoopClient   = "org.apache.hadoop" %  "hadoop-client"   % Version.hadoop excludeAll ExclusionRule(organization = "javax.servlet")
  val mockitoAll     = "org.mockito"       %  "mockito-all"     % Version.mockito
  val scalaTest      = "org.scalatest"     %% "scalatest"       % Version.scalaTest
  val sparkStreaming = "org.apache.spark"  % "spark-streaming_2.10" % Version.spark % "provided"
  val sparkSQL       = "org.apache.spark"  % "spark-sql_2.10"       % Version.spark % "provided"
  val sparkCore      = "org.apache.spark"  % "spark-core_2.10"  % Version.sparkCore

}

object Dependencies {

  import Library._

  val sparkDeps = Seq(
    sparkStreaming,
    hadoopClient,
    sparkSQL,
    sparkCore,
    scalaTest      % "test",
    mockitoAll     % "test"
  )
}
