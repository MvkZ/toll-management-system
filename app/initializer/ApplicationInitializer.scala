package initializer

import com.google.inject.AbstractModule
import play.api.Logger

class ApplicationInitializer extends AbstractModule {

  val logger = Logger(this.getClass)

  override def configure(): Unit = {
    logger.info("Injecting the initializer.. ")
    bind(classOf[EngineInitializer]).asEagerSingleton()
  }

}
