..
organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `lagom-started` = (project in file("."))
  .aggregate(`lagom-started-api`, `lagom-started-impl`, `lagom-started-stream-api`, `lagom-started-stream-impl`, `lagom-all-testing-api`, `lagom-all-testing-impl`)

lazy val `lagom-started-api` = (project in file("lagom-started-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-started-impl` = (project in file("lagom-started-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`lagom-started-api`)

lazy val `lagom-started-stream-api` = (project in file("lagom-started-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-started-stream-impl` = (project in file("lagom-started-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-started-stream-api`, `lagom-started-api`)


lazy val `lagom-all-testing-api` = (project in file("lagom-all-testing-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )


lazy val `lagom-all-testing-impl` = (project in file("lagom-all-testing-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-all-testing-api`, `lagom-started-stream-api`, `lagom-started-api`)

lagomCassandraPort in ThisBuild := 9042

lagomCassandraCleanOnStart in ThisBuild := false

lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")