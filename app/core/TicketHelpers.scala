package core

import models.{PassType, TollPass, TollTicket}

import java.time.LocalDate
import scala.util.Try

object TicketHelpers {

  def checkForVehicle(regNo: String): (String, Long, String) = {
    val checkIfVehicleFound = DB.repo.getParticularVehicle(regNo)
    if(checkIfVehicleFound.nonEmpty) return ("FOUND", checkIfVehicleFound.head.vehicleId, checkIfVehicleFound.head.vehicleType)
    ("NOT FOUND", -1, "")
  }

  def checkIfValid(vehicleId: Long, currDate: LocalDate, boothId: Int): String = {
    val checkIfTicketFound: TollTicket = Try(DB.repo.getParticularTicket(vehicleId, boothId).toSeq.sortBy(x => (x.expiryDate isAfter x.expiryDate)).last).getOrElse(TollTicket(ticketId = -1, ticketDate = LocalDate.MIN, expiryDate = LocalDate.MIN, boothId = -1, vehicleId = -1, passId = -1))
    val expiryDate = checkIfTicketFound.expiryDate
    val passId = checkIfTicketFound.passId
    val passDetails: TollPass = Try(DB.repo.getParticularPassUsingId(passId).head).getOrElse(TollPass(passId = -1, vehicleType = "", tollId = -1, rate = 0, passDate = LocalDate.MIN, passType = "NOTHING"))
    val passType = passDetails.passType
    passType match {
      case PassType.SINGLE => return "EXPIRED"
      case PassType.RETURN => {
        if(currDate.compareTo(expiryDate) > 1) return "EXPIRED"
        else return "NOT EXPIRED"
      }
      case PassType.WEEKLY => {
        if (currDate.compareTo(expiryDate) > 7) return "EXPIRED"
        else return "NOT EXPIRED"
      }
      case _ =>
        return "EXPIRED"
    }
    "EXPIRED"
  }
}
