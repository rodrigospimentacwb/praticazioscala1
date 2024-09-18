package br.com.pepper.praticazioscala.http

import br.com.pepper.praticazioscala.Environment.AppEnv
import br.com.pepper.praticazioscala.config.Config
import cats.syntax.all.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio.*
import zio.interop.catz.*


object Server:

  def run(): ZIO[AppEnv & Config, Throwable, Unit] = for {

    config <- Config(_.httpServer)
    swaggerRoutes = ZHttp4sServerInterpreter()
      .from(
        SwaggerInterpreter().fromServerEndpoints[RIO[AppEnv, *]](
          CustomerRoutes.endpoints,
          "Praticazioscala1",
          "0.1.0"
        )
      )
      .toRoutes
    routes = Router(
      "/" -> (CustomerRoutes.routes <+> swaggerRoutes)
    ).orNotFound
    _ <- BlazeServerBuilder[RIO[AppEnv, *]]
      .bindHttp(config.port, config.host)
      .withoutBanner
      .withHttpApp(routes)
      .serve
      .compile
      .drain
  } yield ()
