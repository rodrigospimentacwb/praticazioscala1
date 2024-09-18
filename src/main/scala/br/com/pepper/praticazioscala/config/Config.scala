package br.com.pepper.praticazioscala.config

import pureconfig.*
import pureconfig.generic.derivation.default.*
import zio.*

trait Config:
  val dbConfig: UIO[MysqlConfig]
  val httpServer: UIO[HttpServerConfig]

object Config extends zio.Accessible[Config]

case class ConfigLive(dbConfig: UIO[MysqlConfig], httpServer: UIO[HttpServerConfig]) extends Config

object  ConfigLive:

  private val basePath = "praticazioscala1"
  private val source = ConfigSource.default.at(basePath)

  def layer: ULayer[Config] = ZLayer.fromZIO(
    ZIO
      .attempt(source.loadOrThrow[Configuration])
      .map(c => ConfigLive(UIO(c.dbConfig), UIO(c.httpServer)))
      .orDie
  )