package br.com.pepper.praticazioscala.domain

import scala.util.control.NoStackTrace
import io.circe.generic.auto.*

object errors:

  sealed trait Error extends Throwable with NoStackTrace

  enum DomainError(msg: String) extends Throwable(msg) with Error:
    case CustomerNotFound(customerId: Long) extends DomainError(s"Customer with id ${customerId} was not found!")
    case UnknownError() extends DomainError(s"Unkown Error!")
