package br.com.pepper.praticazioscala.db

import br.com.pepper.praticazioscala.config.{Config, MysqlConfig}
import doobie.hikari.*
import doobie.util.transactor.Transactor
import zio.interop.catz.*
import zio.interop.catz.implicits.*
import zio.*

import scala.concurrent.ExecutionContext

trait DBTransactor:
  val trx: UIO[Transactor[Task]]

object DBTransactor extends zio.Accessible[DBTransactor]

case class DBTransactorLive(trx: UIO[Transactor[Task]]) extends DBTransactor

object DBTransactorLive:
  private def makeTransactor(config: MysqlConfig, ec: ExecutionContext): TaskManaged[Transactor[Task]] =
    HikariTransactor
      .newHikariTransactor[Task](
        config.className,
        config.url,
        config.user,
        config.password,
        ec
      )
      .toManagedZIO

  val managed: ZManaged[Config, Throwable, Transactor[Task]] = (
      for {
        dbConfig <- Config(_.dbConfig).toManaged
        ce <- ZIO.descriptor
          .map(_.executor.asExecutionContext)
          .toManaged
        xa <- makeTransactor(dbConfig, ce)
      } yield xa
    )
  
  val layer: RLayer[Config, DBTransactor] = ZLayer.fromManaged(
    managed.map(t => DBTransactorLive(UIO(t)))
  )