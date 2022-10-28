package controllers

import core.TicketHelpers.checkForVehicle
import core.{DB, TicketHelpers}
import models.{PassType, TollTicket, Vehicle, VehicleType}
import net.liftweb.json._
import play.api.Configuration
import play.api.libs.json
import play.api.libs.json.Format.GenericFormat
import play.api.mvc._
import play.libs.exception.ExceptionUtils
import slick.util.Logging

import java.time
import java.time.LocalDate
import java.util.Scanner
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class TicketController @Inject()(config: Configuration, val controllerComponents: ControllerComponents)(implicit exec: ExecutionContext)
  extends BaseController with Logging {

  val scanner = new Scanner(System.in)
  implicit val formats = DefaultFormats
  logger.trace("Ticket API Initialized... ")

  def purchaseTicket(): Action[json.JsValue] = Action.async((parse.json)) { request =>
    logger.trace("purchasing the ticket... ")
    val jsonBody = request.body
    Future {
      try {
        if (request.hasBody) {
          val vehicleNo = (jsonBody \ "regNo").as[String]
          val boothId = (jsonBody \ "boothId").as[Int]
          val tollId = Try(DB.repo.getParticularToll(boothId).head.tollId).getOrElse(-1)
          val status = checkForVehicle(vehicleNo)
          var response = ""
          status._1 match {
            case "NOT FOUND" =>
              print("Welcome new User! \nSelect the Type of Vehicle: \n1. 2 Wheeler\n2. 4 Wheeler\n")
              var input = scanner.nextInt()
              val vehicleType = if(input == 1) VehicleType.TWO_WHEELER else VehicleType.FOUR_WHEELER
              val insertVehicle = Vehicle(0, regNo = vehicleNo, vehicleType = vehicleType)
              DB.repo.addVehcicle(insertVehicle)
              print("Select the Pass Type: \n1. Single\n2. Return\n3. Weekly\n")
              input = scanner.nextInt()
              val passType = if(input == 1) PassType.SINGLE else if(input == 2) PassType.RETURN else PassType.WEEKLY
              val vehicleId = DB.repo.getParticularVehicle(vehicleNo).head.vehicleId
              val passRate = DB.repo.getParticularPass(passType, tollId, vehicleType).head.rate
              val passId = DB.repo.getParticularPass(passType, tollId, vehicleType).head.passId
              val purchaseDate = LocalDate.now()
              val expiryDate = if(passType == PassType.SINGLE) time.LocalDate.now()
                                else if(passType == PassType.RETURN) time.LocalDate.now().plusDays(1)
                                else time.LocalDate.now().plusDays(7)
              val newTicket = TollTicket(ticketDate = purchaseDate, expiryDate = expiryDate, boothId = boothId, vehicleId = vehicleId, passId = passId)
              DB.repo.addTicket(newTicket)
              response = "Thanks for Purchasing the ticket! Total Cost => " + passRate + "\n" +
                "Thanks for your visit! You can pass through.. "
            case "FOUND" =>
              val currentDate = LocalDate.now()
              val vehicleId = status._2
              val decision = TicketHelpers.checkIfValid(status._2, currentDate, boothId)
              decision match {
                case "EXPIRED" =>
                  println("Ticket's Expired. get the new Ticket!.. \n")
                  val vehicleType = status._3
                  print("Select the Pass Type: \n1. Single\n2. Return\n3. Weekly\n")
                  val input = scanner.nextInt()
                  val passType = if (input == 1) PassType.SINGLE else if (input == 2) PassType.RETURN else PassType.WEEKLY
                  val passRate = DB.repo.getParticularPass(passType, tollId, vehicleType).head.rate
                  val passId = DB.repo.getParticularPass(passType, tollId, vehicleType).head.passId
                  val purchaseDate = LocalDate.now()
                  val expiryDate = if (passType == PassType.SINGLE) time.LocalDate.now()
                  else if (passType == PassType.RETURN) time.LocalDate.now().plusDays(1)
                  else time.LocalDate.now().plusDays(7)
                  val newTicket = TollTicket(ticketDate = purchaseDate, expiryDate = expiryDate, boothId = boothId, vehicleId = vehicleId, passId = passId)
                  DB.repo.addTicket(newTicket)
                  response = "Thanks for Purchasing the ticket! Total Cost => " + passRate + "\n" +
                    "Thanks for your visit! You can pass through.. "
                case _ =>
                  response = "Ticket is still valid. Thanks for your visit! You can pass through.. "
              }
          }
          Ok(response)
        } else {
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
