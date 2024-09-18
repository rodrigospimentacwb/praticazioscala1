package br.com.pepper.praticazioscala

import br.com.pepper.praticazioscala.config.{Config, ConfigLive}
import br.com.pepper.praticazioscala.db.{DBTransactor, DBTransactorLive}
import br.com.pepper.praticazioscala.domain.customer.{CustomerRepository, CustomerService}
import zio.{Clock, ZEnv, ZLayer}

object Environment:

  type CustomerEnv = CustomerService & CustomerRepository & Clock

  type AppEnv = CustomerEnv
