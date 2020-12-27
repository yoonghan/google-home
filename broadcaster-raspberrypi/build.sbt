import sbt.Keys.libraryDependencies

enablePlugins(PackPlugin)


val dottyVersion = "0.27.0-RC1"

lazy val root = project
  .in(file("."))
  .settings(
      name := "device broadcaster",
      version := "0.1.0",

      scalaVersion := dottyVersion,

      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",

      libraryDependencies += "com.pi4j" % "pi4j-core" % "1.2",
      libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
      libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.6.0",
      libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.3",
      libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % "2.11.3",
      libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.13.3",
      libraryDependencies += "com.typesafe.akka" % "akka-actor_2.13" % "2.6.10",
      libraryDependencies += "com.typesafe.akka" % "akka-http_2.13" % "10.2.1",
      libraryDependencies += "com.typesafe.akka" % "akka-actor-typed_2.13" % "2.6.10",
      libraryDependencies += "com.typesafe.akka" % "akka-stream_2.13" % "2.6.10",
      libraryDependencies += "com.typesafe" % "config" % "1.4.0"
  )
