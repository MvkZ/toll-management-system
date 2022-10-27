package repository

import models.{SlickTablesGeneric, TollBooth, TollCompany, TollPass, TollTicket, Vehicle}
import play.api.Logging
import play.libs.exception.ExceptionUtils
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

object TableRepository {

  def apply(db: Database, profile: JdbcProfile) =
    new TableRepository(db, profile)

}

class TableRepository(db: Database, profile: JdbcProfile) extends Logging {

  protected val table: SlickTablesGeneric = SlickTablesGeneric(profile)

  def getCompanies = {
    Try(Await.result(db.run(table.companyTable.result), Duration.Inf).toList).getOrElse(List())
  }

  def addCompanies(toll: TollCompany) = {
    try {
      val insertQuery = table.companyTable += toll
      Await.result(db.run(insertQuery), Duration.Inf)
    }
    catch {
      case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
    }
  }

  def getBooths = {
    Try(Await.result(db.run(table.boothTable.result), Duration.Inf).toList).getOrElse(List())
  }

  def addBooths(booth: TollBooth) = {
    try {
      val insertQuery = table.boothTable += booth
      Await.result(db.run(insertQuery), Duration.Inf)
    }
    catch {
      case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
    }
  }

  def getPasses = {
    Try(Await.result(db.run(table.passTable.result), Duration.Inf).toList).getOrElse(List())
  }

  def addPasses(pass: TollPass) = {
    try {
      val insertQuery = table.passTable += pass
      Await.result(db.run(insertQuery), Duration.Inf)
    }
    catch {
      case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
    }
  }

  def addVehcicle(newVehicle: Vehicle) = {
    try {
      val insertQuery = table.vehicleTable  += Vehicle(0, newVehicle.regNo, newVehicle.vehicleType)
      Await.result(db.run(insertQuery), Duration.Inf)
    }
    catch {
      case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
    }
  }

  def addTicket(newTicket: TollTicket) = {
    try {
      val insertQuery = table.ticketTable += TollTicket(0, newTicket.ticketDate, newTicket.expiryDate, newTicket.boothId, newTicket.vehicleId, newTicket.passId)
      Await.result(db.run(insertQuery), Duration.Inf)
    }
    catch {
      case exception: Exception => logger.error(ExceptionUtils.getStackTrace(exception))
    }
  }

  def getParticularToll(boothId: Int) = {
    Try(Await.result(db.run(table.boothTable.filter(_.boothId === boothId).result), Duration.Inf).toList).getOrElse(List())
  }


  def getParticularVehicle(vehicleNo: String) = {
    Try(Await.result(db.run(table.vehicleTable.filter(_.regNo like vehicleNo).result), Duration.Inf).toList).getOrElse(List())
  }

  def getParticularPass(passType: String, tollID: Int, vehicleType: String) = {
    Try(Await.result(db.run(table.passTable.filter(_.passType like passType).filter(_.tollId === tollID).filter(_.vehicleType === vehicleType).result), Duration.Inf).toList).getOrElse(List())
  }

  def getParticularPassUsingId(passId: Int) = {
    Try(Await.result(db.run(table.passTable.filter(_.passId === passId).result), Duration.Inf).toList).getOrElse(List())
  }

  def getParticularTicket(vehicleId: Long, boothId: Int) = {
    Try(Await.result(db.run(table.ticketTable.filter(_.vehicleId === vehicleId).filter(_.boothId === boothId).result), Duration.Inf).toList).getOrElse(List())
  }
}
