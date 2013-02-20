import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "GerritBot"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "commons-httpclient" % "commons-httpclient" % "3.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here
    )

}
