package br.com.pepper.praticazioscala.http

import br.com.pepper.praticazioscala.Environment.CustomerEnv
import br.com.pepper.praticazioscala.domain.customer.CustomerService
import br.com.pepper.praticazioscala.http.ErrorInfo
import br.com.pepper.praticazioscala.http.ErrorInfo.*
import br.com.pepper.praticazioscala.http.data.*

import io.circe.generic.auto.*
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir.EndpointOutput.OneOf
import sttp.tapir.PublicEndpoint
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.http4s.ztapir.*
import sttp.tapir.ztapir.*
import zio.*

object CustomerRoutes:

  val httpErrors: OneOf[ErrorInfo, ErrorInfo] = oneOf[ErrorInfo](
    oneOfVariant(StatusCode.InternalServerError, jsonBody[InternalServerError]),
    oneOfVariant(StatusCode.BadRequest, jsonBody[BadRequest]),
    oneOfVariant(StatusCode.NotFound, jsonBody[NotFound])
  )

  val getCustomer: PublicEndpoint[Long, ErrorInfo, CustomerData, Any] =
    endpoint.get
      .in("customers" / path[Long]("id"))
      .out(jsonBody[CustomerData])
      .errorOut(httpErrors)

  // endpoints

  def getCustomerEndpoint: ZServerEndpoint[CustomerEnv, Any] =
    getCustomer.zServerLogic { (id: Long) =>
      CustomerService(_.get(id)).mapBoth(e => NotFound(e.getMessage), _.toData)
    }

  // routes
  val endpoints: List[ZServerEndpoint[CustomerEnv, Any]] = List(
    getCustomerEndpoint,
  )

  val routes: HttpRoutes[RIO[CustomerEnv, *]] =
    ZHttp4sServerInterpreter()
      .from(
        endpoints
      )
      .toRoutes