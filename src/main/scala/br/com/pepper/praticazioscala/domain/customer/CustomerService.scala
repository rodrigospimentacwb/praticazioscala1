package br.com.pepper.praticazioscala.domain.customer

import br.com.pepper.praticazioscala.domain.errors.DomainError
import zio.*

trait CustomerService:
  def get(id: Long): RIO[CustomerRepository, Customer]

object CustomerService extends zio.Accessible[CustomerService]

case class CustomerServiceLive(repo: CustomerRepository) extends CustomerService:
  override def get(id: Long): RIO[CustomerRepository, Customer] =
    repo
      .getById(id)
      .flatMap(maybeCustomer =>
        ZIO.fromOption(maybeCustomer).mapError(_ => DomainError.CustomerNotFound(id))
      )

object CustomerServiceLive:
  val layer: URLayer[CustomerRepository, CustomerService] =
    (CustomerServiceLive(_)).toLayer[CustomerService]