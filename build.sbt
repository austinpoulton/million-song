name := "million-song-analysis"

version := "1.0"

//scalaVersion := "2.11.5"

resolvers ++= Seq(
    "Hadoop Libs" at "https://repository.cloudera.com/content/repositories/releases/",
    "Cloudera Libs" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
    "Scala libs" at "http://scala-tools.org/repo-releases",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Dependencies.sparkDeps
//libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.0.0-cdh5.1.0"