package br.com.pepper.praticazioscala.domain.customer

import br.com.pepper.praticazioscala.db.{DBTransactor, DBTransactorLive}
import doobie.Transactor
import doobie.implicits.*
import zio.*
import zio.interop.catz.*

trait CustomerRepository {
  def getById(id: Long): Task[Option[Customer]]
}

object CustomerRepository extends zio.Accessible[CustomerRepository]

case class CustomerRepositoryLive(trx: Transactor[Task])
  extends CustomerRepository:

  override def getById(id: Long): Task[Option[Customer]] =
    sql"""SELECT * FROM CUSTOMERS WHERE ID = $id """
      .query[Customer]
      .option
      .transact(trx)

object CustomerRepositoryLive:
  val layer: URLayer[DBTransactor, CustomerRepository] = ZLayer.fromZIO(
    DBTransactor(_.trx).map(CustomerRepositoryLive(_))
  )