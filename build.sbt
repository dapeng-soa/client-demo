name := "myClientDemo"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "com.today" %% "hello-demo-api" % "0.1-SNAPSHOT"
)