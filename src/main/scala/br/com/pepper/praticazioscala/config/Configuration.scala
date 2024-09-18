package br.com.pepper.praticazioscala.config

import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

case class Configuration(httpServer: HttpServerConfig, dbConfig: MysqlConfig) derives ConfigReader