import sbt._

object Dependencies {

  val logback             = "ch.qos.logback"               %  "logback-classic"     % "1.2.3"
  val awsLambda           = "com.amazonaws"                %  "aws-java-sdk-lambda" % "1.11.592"
  val enumeratum          = "com.beachape"                 %% "enumeratum"          % "1.5.13"
  val codec               = "commons-codec"                % "commons-codec"        % "1.11"
  val passay              = "org.passay"                   % "passay"               % "1.4.0"
  val guice               = "com.google.inject"            % "guice"                % "4.2.2"
  val guiceAssistedInject = "com.google.inject.extensions" % "guice-assistedinject" % "4.2.2"
  val scalatest           = "org.scalatest"                %% "scalatest"           % "3.0.8"

  sealed trait BaseProject {
    lazy val dependencies = compileDependencies ++ testDependencies

    val compileDependencies: Seq[ModuleID]
    val testDependencies: Seq[ModuleID]

    def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % Compile) map (_.exclude("commons-logging", "commons-logging"))
    def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % Test)
  }

  object Infrastructure extends BaseProject {
    val compileDependencies = compile(codec)
    val testDependencies = test(scalatest)
  }

  object Entities extends BaseProject {
    val compileDependencies = compile(enumeratum, passay)
    val testDependencies = test(scalatest)
  }

  object UseCases extends BaseProject {
    val compileDependencies = compile(guice, guiceAssistedInject)
    val testDependencies = test(scalatest)
  }

  object Adapters extends BaseProject {
    val compileDependencies = compile(guice, guiceAssistedInject, awsLambda)
    val testDependencies = test(scalatest)
  }

}
