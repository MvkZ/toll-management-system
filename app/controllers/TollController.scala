package controllers

import core.DB
import models.{TollBooth, TollCompany, TollPass}
import play.api.Configuration
import play.api.libs.json
import play.api.libs.json.Format.GenericFormat
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import play.libs.exception.ExceptionUtils
import slick.util.Logging

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TollController @Inject()(config: Configuration, val controllerComponents: ControllerComponents)(implicit exec: ExecutionContext)
  extends BaseController with Logging {

  implicit val formats = DefaultFormats
  logger.trace("Toll API Initialized... ")

  def getTolls(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val tollsInfo = DB.repo.getCompanies
      Ok(write(tollsInfo))
    }(exec)
  }

  def getBooths(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val boothsInfo = DB.repo.getBooths
      Ok(write(boothsInfo))
    }(exec)
  }

  def getPasses(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val passesInfo = DB.repo.getPasses
      Ok(write(passesInfo))
    }(exec)
  }

  def addToll(): Action[json.JsValue] = Action.async((parse.json)) { request =>
    logger.trace("adding toll... ")
    val jsonBody = request.body
    Future {
      try {
        if(request.hasBody) {
          val tollId = (jsonBody \ "id").as[Int]
          val tollName = (jsonBody \ "name").as[String]
          val insertToll = TollCompany(tollId, tollName)
          println(insertToll)
          DB.repo.addCompanies(insertToll)
          logger.trace("successfully Inserted")
          Ok("Successfully Inserted into Table.. ")
        }
        else {
          logger.trace("please give input.. ")
          BadRequest
        }
      }
      catch {
        case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
        InternalServerError
      }
    }(exec)
  }

  def addBooth(): Action[json.JsValue] = Action.async((parse.json)) { request =>
    logger.trace("adding toll... ")
    val jsonBody = request.body
    Future {
      try {
        if (request.hasBody) {
          val boothId = (jsonBody \ "id").as[Int]
          val tollId = (jsonBody \ "tollId").as[Int]
          val boothNo = (jsonBody \ "boothNo").as[Int]
          val insertBooth = TollBooth(boothId, tollId, boothNo)
          DB.repo.addBooths(insertBooth)
          logger.trace("successfully Inserted")
          Ok("Successfully Inserted into Table.. ")
        }
        else {
          logger.trace("please give input.. ")
          BadRequest
        }
      }
      catch {
        case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
          InternalServerError
      }
    }(exec)
  }

  def addPass(): Action[json.JsValue] = Action.async((parse.json)) { request =>
    logger.trace("adding toll... ")
    val jsonBody = request.body
    Future {
      try {
        if (request.hasBody) {
          val passId = (jsonBody \ "id").as[Int]
          val vehicleType = (jsonBody \ "vehicleType").as[String]
          val tollId = (jsonBody \ "tollId").as[Int]
          val rate = (jsonBody \ "rate").as[Int]
          val passType = (jsonBody \ "passType").as[String]
          val insertPass = TollPass(passId, vehicleType,tollId,rate, LocalDate.now, passType)
          DB.repo.addPasses(insertPass)
          logger.trace("successfully Inserted")
          Ok("Successfully Inserted into Table.. ")
        }
        else {
          logger.trace("please give input.. ")
          BadRequest
        }
      }
      catch {
        case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
          InternalServerError
      }
    }(exec)
  }


}
