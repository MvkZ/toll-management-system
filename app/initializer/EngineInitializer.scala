package initializer

import core.DB
import play.api.{Configuration, Logging}
import play.libs.exception.ExceptionUtils

import javax.inject.Inject

class EngineInitializer @Inject()(config: Configuration) extends Logging {

  logger.info("Initializing DB.. ")

  try {
    DB.Init()
    logger.info("DB Initialized.. ")
  }
  catch {
    case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
  }

  logger.info("All Initializations Done.. ")
}
