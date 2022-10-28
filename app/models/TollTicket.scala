package models

import java.time.LocalDate

case class TollTicket(ticketId: Int = 0, ticketDate: LocalDate, expiryDate: LocalDate, boothId: Int, vehicleId: Long, passId: Int)
