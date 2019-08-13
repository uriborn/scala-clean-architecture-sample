
val baseName = "sample"

lazy val root = (project in file("."))
  .settings(name := baseName)
  .settings(Settings.basicSettings: _*)
  .aggregate(
    entities,
    useCases,
    adapters
  )

lazy val sharedDDDBase = (project in file("modules/shared/ddd_base"))
  .settings(name := s"$baseName-shared-ddd-base")
  .settings(Settings.basicSettings: _*)

lazy val sharedUtil = (project in file("modules/shared/util"))
  .settings(name := s"$baseName-shared-util")
  .settings(Settings.basicSettings: _*)

lazy val infrastructure = (project in file("modules/infrastructure"))
  .settings(name := s"$baseName-infrastructure")
  .settings(Settings.basicSettings: _*)
  .settings(libraryDependencies ++= Dependencies.Infrastructure.dependencies)

lazy val entities = (project in file("modules/entities"))
  .settings(name := s"$baseName-entities")
  .settings(Settings.basicSettings: _*)
  .settings(libraryDependencies ++= Dependencies.Entities.dependencies)
  .dependsOn(sharedDDDBase, sharedUtil, infrastructure)

lazy val useCases = (project in file("modules/usecases"))
  .settings(name := s"$baseName-usecases")
  .settings(Settings.basicSettings: _*)
  .settings(libraryDependencies ++= Dependencies.UseCases.dependencies)
  .dependsOn(sharedDDDBase, entities)

lazy val adapters = (project in file("modules/adapters"))
  .settings(name := s"$baseName-adapters")
  .settings(Settings.basicSettings: _*)
  .settings(libraryDependencies ++= Dependencies.Adapters.dependencies)
  .dependsOn(useCases)
