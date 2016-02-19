name := "Battleshio"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "3.0.1" % "test",
  "org.specs2" %% "specs2-junit" % "3.0.1" % "test",
  "org.apache.commons" % "commons-math3" % "3.6",
  "org.specs2" % "specs2-mock_2.11" % "3.7",
  "commons-io" % "commons-io" % "2.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

fork in run := true