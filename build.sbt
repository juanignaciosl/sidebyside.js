enablePlugins(ScalaJSPlugin)

name := "Side by Side"

scalaVersion := "2.11.8"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0" withSources()
libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.0" withSources()

skip in packageJSDependencies := false
jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"