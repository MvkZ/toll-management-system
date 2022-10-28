package models

import slick.jdbc.JdbcProfile

import java.time.LocalDate

object SlickTablesGeneric {

  def apply(profile: JdbcProfile): SlickTablesGeneric = new SlickTablesGeneric(profile)

}


class SlickTablesGeneric(val profile: JdbcProfile) {
  import profile.api._
  class TollCompanyTable(tag: Tag) extends Table[TollCompany](tag, None, "toll_company") {
    def id = column[Int]("company_id", O.PrimaryKey)
    def companyName = column[String]("company_name")
    def * = (id, companyName) <> (TollCompany.tupled, TollCompany.unapply)
  }

  class TollBoothTable(tag: Tag) extends Table[TollBooth](tag, None, "toll_booth") {
    def boothId = column[Int]("toll_booth_id", O.PrimaryKey)
    def tollId = column[Int]("company_id")
    def boothNo = column[Int]("booth_lane_no")

    override def * = (boothId, tollId, boothNo) <> (TollBooth.tupled, TollBooth.unapply)
  }

  class TollPassTable(tag: Tag) extends Table[TollPass](tag, None, "toll_pass") {
    def passId = column[Int]("pass_id", O.PrimaryKey)
    def vehicleType = column[String]("vehicle_type")
    def tollId = column[Int]("toll_company_id")
    def rate = column[Int]("rate")
    def passDate = column[LocalDate]("pass_date")
    def passType = column[String]("pass_type")

    def * = (passId, vehicleType, tollId, rate, passDate, passType) <> (TollPass.tupled, TollPass.unapply)
  }

  class TollTicketTable(tag: Tag) extends Table[TollTicket](tag, "toll_ticket") {
    def ticketId = column[Int]("ticket_id", O.PrimaryKey, O.AutoInc , O.SqlType("SERIAL"))

    def ticketDate = column[LocalDate]("ticket_date")

    def expiryDate = column[LocalDate]("ticket_expiry_date")

    def boothId = column[Int]("toll_booth_id")

    def vehicleId = column[Long]("vehicle_id")

    def passId = column[Int]("pass_id")

    def * = (ticketId, ticketDate, expiryDate, boothId, vehicleId, passId) <> (TollTicket.tupled, TollTicket.unapply)
  }

  class VehicleTable(tag: Tag) extends Table[Vehicle](tag, "vehicle") {
    def vehicleId = column[Int]("vehicle_id",  O.PrimaryKey, O.AutoInc)

    def regNo = column[String]("registration_no")

    def vehicleType = column[String]("vehicle_type")

    def * = (vehicleId, regNo, vehicleType) <> ((Vehicle.apply _).tupled, Vehicle.unapply _)
  }


  val companyTable = TableQuery[TollCompanyTable]
  val boothTable = TableQuery[TollBoothTable]
  val passTable = TableQuery[TollPassTable]
  val ticketTable = TableQuery[TollTicketTable]
  val vehicleTable = TableQuery[VehicleTable]

}
