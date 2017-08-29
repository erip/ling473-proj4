lazy val `project-four` = (project in file("."))
  .settings(
    name := "project4",
    scalaVersion := "2.12.2",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= Dependencies.projectFourDependencies,
    mainClass in Compile := Some("edu.washington.rippeth.ling473.proj4.ProjectFourDriver")
    //logLevel in Compile := Level.Warn
  )
