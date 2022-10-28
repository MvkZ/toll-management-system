name := """toll-management-system"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.3.3",
      "org.postgresql" % "postgresql" % "42.3.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
      "com.github.tminglei" %% "slick-pg" % "0.20.3",
      "com.github.tminglei" %% "slick-pg_play-json" % "0.20.3",
      "net.liftweb" %% "lift-json" % "3.3.0"
    )
  )

scalaVersion := "2.12.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

Universal / javaOptions ++= Seq(
  "-Dpidfile.path=/dev/null"
)