package br.com.pepper.praticazioscala.http

import br.com.pepper.praticazioscala.domain.customer.Customer
import sttp.tapir.ValidationError

object data:

  trait CustomerInfo:
    val firstName: String
    val lastName: String

  final case class CustomerData(id: Long, firstName: String, lastName: String) extends CustomerInfo

  extension (c: Customer)
    def toData: CustomerData = CustomerData(c.id, c.name, c.email)