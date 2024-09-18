val scala3Version = "3.1.2-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "praticazioscala1",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-Ykind-projector:underscores"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.0-RC2",
      "dev.zio" %% "zio-interop-cats" % "3.3.0-RC2",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
      "org.http4s" %% "http4s-blaze-server" % "0.23.10",
      "org.http4s" %% "http4s-dsl" % "0.23.10",
      "io.circe" %% "circe-refined" % "0.15.0-M1",
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server" % "1.0.0-M1",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.0.0-M1",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.0.0-M1",
      "mysql" % "mysql-connector-java" % "8.0.33",

      "com.github.pureconfig" %% "pureconfig-core" % "0.17.1",
      "dev.zio" %% "zio-logging-slf4j" % "2.0.0-RC5",
      "ch.qos.logback" % "logback-classic" % "1.2.11"
    ) ,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )