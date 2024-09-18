import zio.*
import br.com.pepper.praticazioscala.Environment.AppEnv
import br.com.pepper.praticazioscala.config.{Config, ConfigLive}
import br.com.pepper.praticazioscala.db.DBTransactorLive
import br.com.pepper.praticazioscala.domain.customer.{CustomerRepositoryLive, CustomerServiceLive}
import br.com.pepper.praticazioscala.http.Server

object Main extends ZIOApp:

  override type Environment = AppEnv & Config & ZEnv

  override val tag: EnvironmentTag[Environment] = EnvironmentTag[Environment]

  override def layer: ZLayer[ZIOAppArgs, Throwable, Environment] =
    ZLayer.make[AppEnv & Config & ZEnv](
      ZEnv.live,
      ConfigLive.layer,
      DBTransactorLive.layer,
      CustomerRepositoryLive.layer,
      CustomerServiceLive.layer
    )

  override def run: ZIO[Environment & ZEnv & ZIOAppArgs, Any, ExitCode] =
    Server
      .run()
      .tapError(err => ZIO.logError(err.getMessage))
      .exitCode